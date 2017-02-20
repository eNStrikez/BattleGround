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
import javafx.scene.layout.GridPane;

public class Selection {
	Scene scene;
	GridPane root;
	Canvas stats, player;
	Button back, left, right, start;
	double screenX, screenY;
	ArrayList<Character> clones = new ArrayList<Character>();
	int index = 0;
	int menuOption = 0;
	GraphicsContext statsG, playerG;
	Timer drawTimer;
	SceneManager sManager;
	final char sMarks = '"';
	ObservableList<String> options;
	ComboBox<String> difficultyBox;

	public Selection(double sX, double sY) {
		screenX = sX;
		screenY = sY;
		addClones();
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

		try {
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.18:3306/battleground", "root", "root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select * from clones order by (health/250 + speed/40 + accuracy + skill/5)");
			while (rs.next()) {
				clones.add(new Character(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4),
						rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getBlob(10), rs.getBlob(11)));
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
		back = new Button("Back");
		left = new Button("<-");
		right = new Button("->");
		start = new Button("Start");
		stats = new Canvas(screenX * 0.3, screenY * 0.8);
		player = new Canvas(screenX * 0.6, screenY * 0.8);
		options = FXCollections.observableArrayList("Youngling", "Padawan", "Jedi Knight", "Jedi Master", "Sith Lord",
				"Emperor");
		difficultyBox = new ComboBox<String>(options);
		difficultyBox.getSelectionModel().selectFirst();

		statsG = stats.getGraphicsContext2D();
		playerG = player.getGraphicsContext2D();
		root = new GridPane();
		root.setHgap(20);
		root.setVgap(40);
		left.setOnAction(e -> {
			chooseLeft();
			drawPlayer(playerG);
			drawStats(statsG);
		});
		right.setOnAction(e -> {
			chooseRight();
			drawPlayer(playerG);
			drawStats(statsG);
		});
		start.setOnAction(e -> chooseStart());
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
		drawPlayer(playerG);
		drawStats(statsG);
	}

	/**
	 * Decrement the index
	 */
	public void chooseLeft() {
		if (index > 0) {
			index--;
		} else {
			index = clones.size() - 1;
		}
	}

	/**
	 * Increment the index
	 */
	public void chooseRight() {
		if (index < clones.size() - 1) {
			index++;
		} else {
			index = 0;
		}
	}

	/**
	 * Start the game with the selected clone and initialises the clone's weapons
	 */
	public void chooseStart() {
		try {
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.18:3306/battleground", "root", "root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select * from weapons where name = \"" + clones.get(index).getWeaponName() + "\";");
			while (rs.next())
				clones.get(index).initWeapon(rs.getInt(2), rs.getInt(3), rs.getInt(5), rs.getInt(4), rs.getInt(6),
						rs.getInt(7), rs.getInt(8));
			rs = stmt.executeQuery("select * from melees where name = \"" + clones.get(index).getMeleeName() + "\";");
			while (rs.next())
				clones.get(index).initMelee(rs.getInt(2), rs.getInt(3));
			con.close();

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		sManager.newGame(screenX, screenY, clones.get(index), difficultyBox.getValue());
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

	/**
	 * Draws the image of the clone specified by the index
	 *
	 * @param g
	 */
	public void drawPlayer(GraphicsContext g) {
		clones.get(index).drawPlayer(g, screenX * 0.6, screenY * 0.8);
	}

}
