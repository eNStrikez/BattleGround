package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import game.Game;
import game.Sortable;
import game.Sorter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class Menu extends Stage {
	public static Integer USER_ID;
	Scene scene, optionsScene, statsScene, gameScene;
	Dialog<Pair<String, String>> login;
	Selection selection;
	SceneManager sManager;
	boolean scoreAsc = true;
	boolean roundAsc = true;

	public Menu() {
		sManager = new SceneManager();
		sManager.setMenu(this);
		sManager.setMenuScene(this.getScene());

		showLoginPopup();

		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		setWidth(bounds.getWidth());
		setHeight(bounds.getHeight());

		GridPane root = new GridPane();
		root.setVgap(50);
		root.setHgap(10);
		root.setId("root");
		scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Button start = new Button("START");
		Button stats = new Button("STATS");
		Button quit = new Button("QUIT");
		Button options = new Button("OPTIONS");

		start.setOnAction(e -> {
			selection = new Selection(bounds.getWidth(), bounds.getHeight());
			sManager.setSelection(selection);
			selection.setSceneManager(sManager);
			setScene(selection.getScene());
		});

		quit.setOnAction(e -> {
			close();
		});

		options.setOnAction(e -> {
			setScene(optionsScene);
		});

		stats.setOnAction(e -> {
			initStatsScene(bounds.getWidth(), bounds.getHeight());
			setScene(statsScene);
		});

		root.add(start, 0, 0);
		root.add(stats, 0, 1);
		root.add(options, 0, 2);
		root.add(quit, 0, 3);
		setScene(scene);
	}

	/**
	 * Create a new game scene and sets it as the current scene
	 *
	 * @param sX
	 * @param sY
	 * @param c
	 * @param diff
	 */
	public void createGame(double sX, double sY, Character c, String diff) {
		if (!USER_ID.equals(null)) {
			gameScene = new Game(sX, sY, c, sManager, diff).getScene();
			setScene(gameScene);
		} else {
			System.exit(1);
		}
	}

	/**
	 * Creates and shows a login popup for the player to login or create an
	 * account
	 */
	public void showLoginPopup() {
		login = new Dialog<>();
		login.setTitle("Login");
		login.setHeaderText(
				"Enter your username and password.\nIf you are a new user, type in your desired login details and these will be saved.");
		login.initStyle(StageStyle.UTILITY);

		ButtonType loginButton = new ButtonType("Login", ButtonData.OK_DONE);
		login.getDialogPane().getButtonTypes().addAll(loginButton, ButtonType.CANCEL);

		TextField usernameText = new TextField();
		usernameText.setPromptText("Username");

		PasswordField passwordText = new PasswordField();
		passwordText.setPromptText("Password");

		GridPane root = new GridPane();
		root.add(new Label("Username"), 0, 0);
		root.add(usernameText, 1, 0);
		root.add(new Label("Password"), 0, 1);
		root.add(passwordText, 1, 1);

		login.getDialogPane().setContent(root);

		login.setResultConverter(dialogButton -> {
			if (dialogButton == loginButton) {
				return new Pair<>(usernameText.getText(), passwordText.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = login.showAndWait();

		result.ifPresent(usernamePassword -> {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
						"root");
				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(
						"select userID from user where username = '" + usernamePassword.getKey() + "' and password = '"
								+ encrypt(usernamePassword.getKey(), usernamePassword.getValue()) + "';");

				if (!rs.next()) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirm login details");
					alert.setHeaderText("Account with username " + usernamePassword.getKey() + " will be created.");
					alert.setContentText("Confirm that these will be the login credentials.");
					Optional<ButtonType> option = alert.showAndWait();
					if (option.get() == ButtonType.OK && usernamePassword.getKey().matches("^[a-zA-Z]{1}\\w{4,11}$") && usernamePassword.getValue().matches("^[a-zA-Z]{1}\\w{4,11}$")) {
						stmt.executeUpdate("insert into user (userID, username, password) values(null, '"
								+ usernamePassword.getKey() + "', '"
								+ encrypt(usernamePassword.getKey(), usernamePassword.getValue()) + "');");
						rs = stmt.executeQuery(
								"select userID from user where username = '" + usernamePassword.getKey() + "';");
						rs.next();
					} else {
						throw new Exception("Invalid input");
					}
				}

				USER_ID = rs.getInt(1);

				con.close();

			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(
						"An error occurred during account credential verification or creation.\nPlease try again with valid credentials.");
				alert.setContentText("Error: " + e);
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get() == ButtonType.OK) {
					System.exit(1);
				}
			}
		});
	}

	/**
	 * Performs vernam cipher encryption based on username and password. The key
	 * is generated from a random generator with the seed of the binary form of
	 * the username.
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	public String encrypt(String username, String password) {
		if (username.length() < password.length()) {
			username = String.format("%" + password.length() + "s", username).replace(' ', '.');
		} else if (username.length() > password.length()) {
			password = String.format("%" + username.length() + "s", password).replace(' ', '.');
		}

		Random rand = new Random();
		byte[] key = new byte[username.length()];
		byte[] pass = password.getBytes();
		String newPass = "";
		String seed = "";

		for (int i = 0; i < username.length(); i++) {
			seed += (int) username.charAt(i) - 48;
		}
		rand.setSeed(Long.parseLong(seed));
		rand.nextBytes(key);

		for (int i = 0; i < key.length; i++) {
			newPass += key[i] ^ pass[i];
		}
		return newPass;
	}

	/**
	 * Sets the current scene as the menu scene
	 */
	public void setMenu() {
		setScene(scene);
	}

	public void initStatsScene(double sX, double sY) {

		GridPane root = new GridPane();
		Sorter sorter = new Sorter();
		ArrayList<Sortable> list = new ArrayList<Sortable>();
		root.setVgap(50);
		root.setHgap(10);
		root.setId("root");
		statsScene = new Scene(root, sX, sY);
		statsScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		TableView<Sortable> table = new TableView<Sortable>();
		try {
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root", "root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select username, score, date, round, difficulty, clone from scores join user on scores.userID = user.userID;");
			while(rs.next()){
				list.add(new Score(rs.getString(1), rs.getInt(2), rs.getTimestamp(3).toString(), rs.getInt(4), rs.getString(5), rs.getString(6)));
			}
			table.setItems(FXCollections.observableArrayList(list));
			ResultSetMetaData rsmd = rs.getMetaData();

			for(int c = 1; c < rsmd.getColumnCount(); c++){
				TableColumn col = new TableColumn(rsmd.getColumnName(c));
				col.setCellValueFactory(new PropertyValueFactory<Score, String>(rsmd.getColumnName(c)));
				col.setSortable(false);
				table.getColumns().add(col);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}



		Button back = new Button("BACK");
		Button score = new Button("SORT BY SCORE");
		Button round = new Button("SORT BY ROUND");

		back.setOnAction(e -> {
			setMenu();
		});

		score.setOnAction(e -> {
			for(Sortable s: list){
				s.setValue("score");
			}
			table.setItems(FXCollections.observableArrayList(sorter.sort(list, scoreAsc)));
			scoreAsc = !scoreAsc;
			if(scoreAsc){
				score.setText("SORT BY SCORE (ASCENDING)");
			} else {
				score.setText("SORT BY SCORE (DESCENDING)");
			}
		});

		round.setOnAction(e -> {
			for(Sortable s: list){
				s.setValue("round");
			}
			table.setItems(FXCollections.observableArrayList(sorter.sort(list, roundAsc)));
			roundAsc = !roundAsc;
			if(roundAsc){
				round.setText("SORT BY ROUND (ASCENDING)");
			} else {
				round.setText("SORT BY ROUND (DESCENDING)");
			}
		});

		root.add(back, 0, 0);
		root.add(score, 0, 1);
		root.add(round, 0, 2);
		root.add(table, 1, 0);
	}
}

