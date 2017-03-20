package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public class Selection {
	double screenX, screenY;
	ArrayList<Character> clones = new ArrayList<Character>();
	ArrayList<Modifier> modifiers = new ArrayList<Modifier>();
	int index = 0;
	int menuOption = 0;
	GraphicsContext statsG, playerG;
	Timer drawTimer;
	SceneManager sManager;
	Character selectedClone;
	String value;
	Scene scene;

	public Selection(double sX, double sY) {
		// Saves the dimensions of the window
		screenX = sX;
		screenY = sY;
		// Initialises the character selection screen
		initCharacterSelect();

	}

	/**
	 * Set the scene manager of the class
	 *
	 * @param s
	 */
	public void setSceneManager(SceneManager s) {
		sManager = s;
	}

	/**
	 * Performs an SQL query to return the clones, ordered by the total
	 * percentage of stats they have out of the maximum stats possible
	 */
	public void addClones() {
		// Sets the player score default as zero
		int playerScore = 0;
		try {
			// Connects to the database server
			System.out.println("Loading Clones...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			// Retrieves the player's highest score
			ResultSet rs = stmt.executeQuery("select max(score) from scores where userID = \"" + Menu.USER_ID + "\";");
			while (rs.next()) {
				playerScore = rs.getInt(1);
			}
			// Retrieves the clones in order of the cumalative percentages of the character's stats
			rs = stmt.executeQuery("select * from clones order by (health/250 + speed/10 + accuracy + skill/5)");
			while (rs.next()) {
				clones.add(new Character(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4),
						rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getBlob(10), rs.getBlob(11), rs.getInt(12), playerScore));
			}
			con.close();

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
	}

	/**
	 * Performs an SQL query to return all the modifiers linked to the selected
	 * character
	 *
	 * @param name
	 */
	public void addModifiers(String name) {
		// Adds the 'No Modifier' mod for all characters by default
		modifiers.add(new Modifier("No Modifier", "", 0));
		try {
			// Connects to the database server
			System.out.println("Loading Mods...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			// Retrieves all of the selected character's perks
			ResultSet rs = stmt.executeQuery(
					"select * from clone_perks join perks on perks.name = clone_perks.perk where clone = \"" + name
							+ "\";");
			while (rs.next()) {
				modifiers.add(new Modifier(rs.getString(3), rs.getString(4), rs.getDouble(5)));
			}

			con.close();

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
	}

	/**
	 * Sets up the character selection screen
	 */
	public void initCharacterSelect() {
		// Loads the clones from the server
		addClones();
		// Creates the buttons and canvases for the character selection
		Button back = new Button("Back");
		Button left = new Button("<-");
		Button right = new Button("->");
		Button start = new Button("Start");
		Canvas stats = new Canvas(screenX * 0.3, screenY * 0.8);
		Canvas player = new Canvas(screenX * 0.6, screenY * 0.8);
		// Adds the combo box for selecting the difficulty
		ObservableList<String> options = FXCollections.observableArrayList("Youngling", "Padawan", "Jedi Knight",
				"Jedi Master", "Sith Lord", "Emperor");
		ComboBox<String> difficultyBox = new ComboBox<String>(options);
		difficultyBox.getSelectionModel().selectFirst();
		// Retrieves the graphcis context for each canvas
		statsG = stats.getGraphicsContext2D();
		playerG = player.getGraphicsContext2D();
		// Creates the layout
		GridPane root = new GridPane();
		root.setHgap(20);
		root.setVgap(40);
		// Sets the left button to decrement the index of clones
		left.setOnAction(e -> {
			chooseLeft(clones);
			// Disable the start button if the player has not unlocked the currently selected clone
			start.setDisable(!clones.get(index).accessible());
			// Draws the player image
			drawPlayer(playerG);
			// Draws the player's stats
			drawStats(statsG);
		});
		// Sets the right button to increment the index of clones
		right.setOnAction(e -> {
			chooseRight(clones);
			// Disable the start button if the player has not unlocked the currently selected clone
			start.setDisable(!clones.get(index).accessible());
			// Draws the player image
			drawPlayer(playerG);
			// Draws the player's stats
			drawStats(statsG);
		});
		// Sets the start button to save the difficulty and character chosen and creates and shows the mod selection screen
		start.setOnAction(e -> {
			value = difficultyBox.getValue();
			selectedClone = clones.get(index);
			modifierSelect();
		});
		// Sets the back button to return to the main menu
		back.setOnAction(e -> chooseBack());
		// Initialises the scene and adds components to it
		scene = new Scene(root, screenX, screenY);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		root.add(stats, 0, 0, 2, 4);
		root.add(player, 2, 0, 4, 4);
		root.add(back, 0, 4, 2, 1);
		root.add(left, 2, 4, 1, 1);
		root.add(start, 3, 4, 1, 1);
		root.add(right, 4, 4, 1, 1);
		root.add(difficultyBox, 2, 5, 3, 1);
		root.setId("selection");
		// Draws the initial player and stats
		drawPlayer(playerG);
		drawStats(statsG);
	}

	/**
	 * Sets up the modification selection screen
	 */
	public void modifierSelect() {
		// Loads the modifiers from the server
		addModifiers(selectedClone.getName());
		// Creates the buttons and canvas for the mod selection
		Button back = new Button("Back");
		Button left = new Button("<-");
		Button right = new Button("->");
		Button start = new Button("Start");
		Canvas stats = new Canvas(screenX * 0.3, screenY * 0.8);
		// Retrieves the graphcis context for the canvas
		statsG = stats.getGraphicsContext2D();
		// Creates the layout
		GridPane root = new GridPane();
		root.setHgap(20);
		root.setVgap(40);
		// Sets the left button to decrement the index of modifiers
		left.setOnAction(e -> {
			chooseLeft(modifiers);
			// Draws the mod stats
			drawModifierStats(statsG, selectedClone);
		});
		// Sets the right button to increment the index of modifiers
		right.setOnAction(e -> {
			chooseRight(modifiers);
			// Draws the mod stats
			drawModifierStats(statsG, selectedClone);
		});
		// Sets the start button to launch the game
		start.setOnAction(e -> chooseStart());
		// Sets the back button to return to the main menu
		back.setOnAction(e -> chooseBack());
		// Initialises the scene and adds the components
		scene = new Scene(root, screenX, screenY);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		root.add(stats, 0, 0, 2, 4);
		root.add(back, 0, 4, 2, 1);
		root.add(left, 2, 4, 1, 1);
		root.add(start, 3, 4, 1, 1);
		root.add(right, 4, 4, 1, 1);
		root.setId("selection");
		// Sets the scene to be shown
		sManager.selectSelection(scene);
		// Resets the index
		index = 0;
		// Draws the initial mod stats
		drawModifierStats(statsG, selectedClone);
	}

	/**
	 * Decrement the index
	 */
	public void chooseLeft(ArrayList<?> a) {
		if (index > 0) {
			// Decrements the index if the value is higher than 0
			index--;
		} else {
			// Sets the index as the maximum value
			index = a.size() - 1;
		}
	}

	/**
	 * Increment the index
	 */
	public void chooseRight(ArrayList<?> a) {
		if (index < a.size() - 1) {
			// Increments the index if the value is less than the maxiumum
			index++;
		} else {
			// Sets the index as the minimum value
			index = 0;
		}
	}

	/**
	 * Start the game with the selected clone and initialises the clone's
	 * weapons
	 */
	public void chooseStart() {
		try {
			// Connects to the database server
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			// Retrieves the weapon stats for the selected clone
			ResultSet rs = stmt
					.executeQuery("select * from weapons where name = \"" + selectedClone.getWeaponName() + "\";");
			while (rs.next())
				selectedClone.initWeapon(rs.getInt(2), rs.getInt(3), rs.getInt(5), rs.getInt(4), rs.getInt(6),
						rs.getInt(7), rs.getInt(8));
			// Retrieves the melee stats for the selected clone
			rs = stmt.executeQuery("select * from melees where name = \"" + selectedClone.getMeleeName() + "\";");
			while (rs.next())
				selectedClone.initMelee(rs.getInt(2), rs.getInt(3));
			con.close();

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		// Creates a new instance of the game
		sManager.newGame(screenX, screenY, selectedClone, value, modifiers.get(index));
	}

	/**
	 * Go back to the menu screen
	 */
	public void chooseBack() {
		// Sets the scene as the menu
		sManager.selectMenu();
	}

	/**
	 * Return the scene
	 */
	public Scene getScene() {
		// Returns the current scene
		return scene;
	}

	/**
	 * Draws the stat bars and information for the clone specified by the index
	 *
	 * @param g
	 */
	public void drawStats(GraphicsContext g) {
		// Draws the stats of the selected clone
		clones.get(index).drawStats(g, screenX * 0.3, screenY * 0.8);
	}

	/**
	 * Draws the stat bars for the modifiers
	 *
	 * @param g
	 */
	public void drawModifierStats(GraphicsContext g, Character c) {
		// Draws the stats of the selected modifier
		modifiers.get(index).drawStats(g, screenX * 0.3, screenY * 0.8, c);
	}

	/**
	 * Draws the image of the clone specified by the index
	 *
	 * @param g
	 */
	public void drawPlayer(GraphicsContext g) {
		// Draws the image of the selected clone
		clones.get(index).drawPlayer(g, screenX * 0.6, screenY * 0.8);
	}

}
