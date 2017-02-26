package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.Random;

import game.Game;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class Menu extends Stage {
	public static Integer USER_ID;
	Scene scene, optionsScene, statsScene, gameScene;
	Dialog<Pair<String, String>> login;
	Selection selection;
	GridPane root;
	Button start, stats, quit, options;
	SceneManager sManager;

	public Menu() {
		sManager = new SceneManager();
		sManager.setMenu(this);
		sManager.setMenuScene(this.getScene());

		showLoginPopup();

		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		setWidth(bounds.getWidth());
		setHeight(bounds.getHeight());

		root = new GridPane();
		root.setVgap(50);
		root.setHgap(10);
		root.setId("root");
		scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		start = new Button("START");
		stats = new Button("STATS");
		quit = new Button("QUIT");
		options = new Button("OPTIONS");

		start.setAlignment(Pos.CENTER);
		stats.setAlignment(Pos.CENTER);
		quit.setAlignment(Pos.CENTER);
		options.setAlignment(Pos.CENTER);

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

		});

		stats.setOnAction(e -> {

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
				System.out.println("Loading...");
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
						"root");
				System.out.println("Connected.");
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
					if (option.get() == ButtonType.OK) {
						stmt.executeUpdate("insert into user (userID, username, password) values(null, '"
								+ usernamePassword.getKey() + "', '"
								+ encrypt(usernamePassword.getKey(), usernamePassword.getValue()) + "');");
						rs = stmt.executeQuery(
								"select userID from user where username = '" + usernamePassword.getKey() + "';");
						rs.next();
					} else {
						System.exit(1);
					}
				}

				USER_ID = rs.getInt(1);
				System.out.println(USER_ID);

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
			seed += (int) username.charAt(i);
		}
		rand.setSeed(Integer.parseInt(seed));
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
}