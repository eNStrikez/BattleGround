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
		screenX = sX;
		screenY = sY;

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
		int playerScore = 0;
		try {
			System.out.println("Loading Clones...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select max(score) from scores where userID = \"" + Menu.USER_ID + "\";");
			while (rs.next()) {
				playerScore = rs.getInt(1);
			}
			rs = stmt.executeQuery("select * from clones order by (health/250 + speed/40 + accuracy + skill/5)");
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

	public void addModifiers(String name) {
		try {
			System.out.println("Loading Mods...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();

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
		addClones();
		Button back = new Button("Back");
		Button left = new Button("<-");
		Button right = new Button("->");
		Button start = new Button("Start");
		Canvas stats = new Canvas(screenX * 0.3, screenY * 0.8);
		Canvas player = new Canvas(screenX * 0.6, screenY * 0.8);
		ObservableList<String> options = FXCollections.observableArrayList("Youngling", "Padawan", "Jedi Knight",
				"Jedi Master", "Sith Lord", "Emperor");
		ComboBox<String> difficultyBox = new ComboBox<String>(options);
		difficultyBox.getSelectionModel().selectFirst();

		statsG = stats.getGraphicsContext2D();
		playerG = player.getGraphicsContext2D();
		GridPane root = new GridPane();
		root.setHgap(20);
		root.setVgap(40);
		left.setOnAction(e -> {
			chooseLeft(clones);
			start.setDisable(!clones.get(index).accessible());
			drawPlayer(playerG);
			drawStats(statsG);
		});
		right.setOnAction(e -> {
			chooseRight(clones);
			start.setDisable(!clones.get(index).accessible());
			drawPlayer(playerG);
			drawStats(statsG);
		});
		start.setOnAction(e -> {
			value = difficultyBox.getValue();
			selectedClone = clones.get(index);
			modifierSelect();
		});
		back.setOnAction(e -> chooseBack());
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
		drawPlayer(playerG);
		drawStats(statsG);
	}

	public void modifierSelect() {
		addModifiers(selectedClone.getName());
		Button back = new Button("Back");
		Button left = new Button("<-");
		Button right = new Button("->");
		Button start = new Button("Start");
		Canvas stats = new Canvas(screenX * 0.3, screenY * 0.8);
		statsG = stats.getGraphicsContext2D();
		GridPane root = new GridPane();
		root.setHgap(20);
		root.setVgap(40);
		left.setOnAction(e -> {
			chooseLeft(modifiers);
			drawModifierStats(statsG);
		});
		right.setOnAction(e -> {
			chooseRight(modifiers);
			drawModifierStats(statsG);
		});
		start.setOnAction(e -> chooseStart());
		back.setOnAction(e -> chooseBack());
		scene = new Scene(root, screenX, screenY);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		root.add(stats, 0, 0, 2, 4);
		root.add(back, 0, 4, 2, 1);
		root.add(left, 2, 4, 1, 1);
		root.add(start, 3, 4, 1, 1);
		root.add(right, 4, 4, 1, 1);
		root.setId("selection");
		sManager.selectSelection(scene);
		index = 0;
		drawModifierStats(statsG);
	}

	/**
	 * Decrement the index
	 */
	public void chooseLeft(ArrayList<?> a) {
		if (index > 0) {
			index--;
		} else {
			index = a.size() - 1;
		}
	}

	/**
	 * Increment the index
	 */
	public void chooseRight(ArrayList<?> a) {
		if (index < a.size() - 1) {
			index++;
		} else {
			index = 0;
		}
	}

	/**
	 * Start the game with the selected clone and initialises the clone's
	 * weapons
	 */
	public void chooseStart() {
		try {
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select * from weapons where name = \"" + selectedClone.getWeaponName() + "\";");
			while (rs.next())
				selectedClone.initWeapon(rs.getInt(2), rs.getInt(3), rs.getInt(5), rs.getInt(4), rs.getInt(6),
						rs.getInt(7), rs.getInt(8));
			rs = stmt.executeQuery("select * from melees where name = \"" + selectedClone.getMeleeName() + "\";");
			while (rs.next())
				selectedClone.initMelee(rs.getInt(2), rs.getInt(3));
			con.close();

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		sManager.newGame(screenX, screenY, selectedClone, value, modifiers.get(index));
	}

	/**
	 * Go back to the menu screen
	 */
	public void chooseBack() {
		sManager.selectMenu();
	}

	/**
	 * Return the scene
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * Draws the stat bars and information for the clone specified by the index
	 *
	 * @param g
	 */
	public void drawStats(GraphicsContext g) {
		clones.get(index).drawStats(g, screenX * 0.3, screenY * 0.8);
	}

	public void drawModifierStats(GraphicsContext g) {
		modifiers.get(index).drawStats(g, screenX * 0.3, screenY * 0.8);
	}

	/**
	 * Draws the image of the clone specified by the index
	 *
	 * @param g
	 */
	public void drawPlayer(GraphicsContext g) {
		clones.get(index).drawPlayer(g, screenX * 0.6, screenY * 0.8);
	}

}
