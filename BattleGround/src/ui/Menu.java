package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import game.Game;
import game.Sortable;
import game.Sorter;
import javafx.collections.FXCollections;
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
		// Creates the scene manager
		sManager = new SceneManager();
		sManager.setMenu(this);
		sManager.setMenuScene(this.getScene());
		// Displays the login popup
		showLoginPopup();
		// Makes the window the size of the screen
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		setWidth(bounds.getWidth());
		setHeight(bounds.getHeight());
		// Creates the grid pane layout to arrange the GUI components in a grid
		// format instead of specifing coordinates
		GridPane root = new GridPane();
		// Sets the gap between components
		root.setVgap(50);
		root.setHgap(10);
		// Sets the CSS ID used for the layout
		root.setId("root");
		// Creates the scene suing the specified layout manager
		scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
		// Sets the scene to use the application.css file for CSS
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		// Creates buttons
		Button start = new Button("START");
		Button stats = new Button("STATS");
		Button quit = new Button("QUIT");
		Button options = new Button("OPTIONS");

		// Sets the start button to create and switch to a new instance of the
		// selection screen upon press
		start.setOnAction(e -> {
			selection = new Selection(bounds.getWidth(), bounds.getHeight());
			sManager.setSelection(selection);
			selection.setSceneManager(sManager);
			setScene(selection.getScene());
		});
		// Sets the quit button to exit upon press
		quit.setOnAction(e -> {
			close();
		});
		// Sets the option button to show the option scene
		options.setOnAction(e -> {
			setScene(optionsScene);
		});
		// Sets the stats button to create and show the stats scene
		stats.setOnAction(e -> {
			initStatsScene(bounds.getWidth(), bounds.getHeight());
			setScene(statsScene);
		});
		
		options.setDisable(true);
		
		// Adds the buttons to the layout
		root.add(start, 0, 0);
		root.add(stats, 0, 1);
		root.add(options, 0, 2);
		root.add(quit, 0, 3);
		// Shows the scene
		setScene(scene);
	}

	/**
	 * Create a new game scene and sets it as the current scene
	 *
	 * @param sX
	 * @param sY
	 * @param c
	 * @param diff
	 * @param mod
	 */
	public void createGame(double sX, double sY, Character c, String diff, Modifier mod) {
		// Checks whether the user has a correct ID
		if (!USER_ID.equals(null)) {
			// Creates a new instance of the game
			gameScene = new Game(sX, sY, c, sManager, diff, mod).getScene();
			// Shows the game scene
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
		// Creates a new login dialogue instance
		login = new Dialog<>();
		// Sets the title, header and style of the dialogue
		login.setTitle("Login");
		login.setHeaderText(
				"Enter your username and password.\nIf you are a new user, type in your desired login details and these will be saved.");
		login.initStyle(StageStyle.UTILITY);
		// Creates the login button and cancel button
		ButtonType loginButton = new ButtonType("Login", ButtonData.OK_DONE);
		login.getDialogPane().getButtonTypes().addAll(loginButton, ButtonType.CANCEL);
		// Creates a text field for the username
		TextField usernameText = new TextField();
		usernameText.setPromptText("Username");
		// Creates a password field for the password
		PasswordField passwordText = new PasswordField();
		passwordText.setPromptText("Password");
		// Adds the username and password to the layout for the dialogue
		GridPane root = new GridPane();
		root.add(new Label("Username"), 0, 0);
		root.add(usernameText, 1, 0);
		root.add(new Label("Password"), 0, 1);
		root.add(passwordText, 1, 1);
		login.getDialogPane().setContent(root);
		// Sets the return information for the button
		login.setResultConverter(dialogButton -> {
			if (dialogButton == loginButton) {
				return new Pair<>(usernameText.getText(), passwordText.getText());
			}
			return null;
		});
		// Sets the dialogue to wait for data entry
		Optional<Pair<String, String>> result = login.showAndWait();
		// Sets the result of a button press
		result.ifPresent(usernamePassword -> {
			try {
				// Connects to the MySQL 5.7 server using the IP specified on
				// port 3306 and schema battleground
				Class.forName("com.mysql.jdbc.Driver");

				Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
						"root");
				Statement stmt = con.createStatement();
				// Returns the user ID for the data matching the input
				ResultSet rs = stmt.executeQuery(
						"select userID from user where username = '" + usernamePassword.getKey() + "' and password = '"
								+ encrypt(usernamePassword.getKey(), usernamePassword.getValue()) + "';");
				// Creates an alert informing the user that their credentials
				// are invalid and asks them to create an account if the
				// database returns no matching ID
				if (!rs.next()) {
					// Creates the alert
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirm login details");
					alert.setHeaderText("Account with username " + usernamePassword.getKey() + " will be created.");
					alert.setContentText("Confirm that these will be the login credentials.");
					Optional<ButtonType> option = alert.showAndWait();
					// Sets the button to create a new account with the input
					// credentials if the button is pressed and the inputs are
					// between 3 and 12 alphabet or numeric characters long
					// (specified by a regex statement)
					if (option.get() == ButtonType.OK && usernamePassword.getKey().matches("^[a-zA-Z]{1}\\w{2,11}$")
							&& usernamePassword.getValue().matches("^[a-zA-Z]{1}\\w{2,11}$")) {
						// Updates the database with the new account details
						stmt.executeUpdate("insert into user (userID, username, password) values(null, '"
								+ usernamePassword.getKey() + "', '"
								+ encrypt(usernamePassword.getKey(), usernamePassword.getValue()) + "');");
						// Retrieves the user ID for the new account
						rs = stmt.executeQuery(
								"select userID from user where username = '" + usernamePassword.getKey() + "';");
						rs.next();
					} else {
						// Throws an exception if the input does not match the
						// regex statement
						throw new Exception("Invalid input");
					}
				}
				// Saves the user ID
				USER_ID = rs.getInt(1);
				// The connection to the server is closed
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
		// Makes the username and password the same length by appending characters to the shorter one
		if (username.length() < password.length()) {
			username = String.format("%" + password.length() + "s", username).replace(' ', '.');
		} else if (username.length() > password.length()) {
			password = String.format("%" + username.length() + "s", password).replace(' ', '.');
		}

		Random rand = new Random();
		// Creates an array of bytes for storing the key
		byte[] key = new byte[username.length()];
		// Converts the password to bytes and stores it
		byte[] pass = password.getBytes();
		String newPass = "";
		String seed = "";
		// Generates a random seed by coverting the username into a series of bytes
		for (int i = 0; i < username.length(); i++) {
			seed += (int) username.charAt(i) - 48;
		}
		// Sets the seed of the random number generator
		rand.setSeed(Long.parseLong(seed));
		// Creates a random set of bytes and stores them to be used as the key for the cipher
		rand.nextBytes(key);

		// Carries out a logical XOR between each byte of the key and password and appends it to the result string
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

	/**
	 * Sets up the Stats scene. It creates buttons and a table of high scores.
	 *
	 * @param sX
	 * @param sY
	 */
	public void initStatsScene(double sX, double sY) {
		// Creates the layout for the stats scene
		GridPane root = new GridPane();
		Sorter sorter = new Sorter();
		ArrayList<Sortable> list = new ArrayList<Sortable>();
		root.setVgap(50);
		root.setHgap(10);
		root.setId("root");
		statsScene = new Scene(root, sX, sY);
		statsScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		// Creates a new table
		TableView<Sortable> table = new TableView<Sortable>();
		try {
			// Connects to the database server
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			// Retrieves a set of data to be displayed in the high score table from the database
			ResultSet rs = stmt.executeQuery(
					"select username, score, date, round, difficulty, clone from scores join user on scores.userID = user.userID;");
			while (rs.next()) {
				list.add(new Score(rs.getString(1), rs.getInt(2), rs.getTimestamp(3).toString(), rs.getInt(4),
						rs.getString(5), rs.getString(6)));
			}
			table.setItems(FXCollections.observableArrayList(list));
			ResultSetMetaData rsmd = rs.getMetaData();
			// Sets the columns for the table based on the meta data of the SQL query
			for (int c = 1; c < rsmd.getColumnCount(); c++) {
				TableColumn col = new TableColumn(rsmd.getColumnName(c));
				col.setCellValueFactory(new PropertyValueFactory<Score, String>(rsmd.getColumnName(c)));
				col.setSortable(false);
				table.getColumns().add(col);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		// Creates the buttons for the scene
		Button back = new Button("BACK");
		Button score = new Button("SORT BY SCORE");
		Button round = new Button("SORT BY ROUND");
		// Sets the back button the return to the main menu on click
		back.setOnAction(e -> {
			setMenu();
		});
		// Sets the score button to order the table by score, alternating between ascending and descending
		score.setOnAction(e -> {
			for (Sortable s : list) {
				// Sets each item to be sorted by score
				s.setValue("score");
			}
			table.setItems(FXCollections.observableArrayList(sorter.breakDown(list, scoreAsc)));
			// Changes the button text to alternate between ascending and descending
			scoreAsc = !scoreAsc;
			if (scoreAsc) {
				score.setText("SORT BY SCORE (ASCENDING)");
			} else {
				score.setText("SORT BY SCORE (DESCENDING)");
			}
		});
		// Sets the round button to order the table by round, alternating between ascending and descending
		round.setOnAction(e -> {
			for (Sortable s : list) {
				// Sets each item to be sorted by round
				s.setValue("round");
			}
			table.setItems(FXCollections.observableArrayList(sorter.breakDown(list, roundAsc)));
			// Changes the button text to alternate between ascending and descending
			roundAsc = !roundAsc;
			if (roundAsc) {
				round.setText("SORT BY ROUND (ASCENDING)");
			} else {
				round.setText("SORT BY ROUND (DESCENDING)");
			}
		});
		// Adds the components to the window
		root.add(back, 0, 0);
		root.add(score, 0, 1);
		root.add(round, 0, 2);
		root.add(table, 1, 0);
	}
}
