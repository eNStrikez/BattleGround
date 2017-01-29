package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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

	public Selection(double sX, double sY) {
		addClones();
		initCharacterSelect();
		screenX = sX;
		screenY = sY;
	}



	public void setSceneManager( SceneManager s){
		sManager = s;

	}

	public void addClones() {

		try {
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/battleground", "root", "root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from clones order by (health/250 + speed/40 + accuracy + skill/5)");
			while (rs.next()) {
				clones.add(new Character(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4),
						rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getBlob(10)));
			}
			con.close();

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
	}
	public void initCharacterSelect() {
		back = new Button("Back");
		left = new Button("<-");
		right = new Button("->");
		start = new Button("Start");
		stats = new Canvas(400, 600);
		player = new Canvas(600, 600);

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
		drawPlayer(playerG);
		drawStats(statsG);
	}

	public void chooseLeft() {
		if (index > 0) {
			index--;
		} else {
			index = clones.size() - 1;
		}
	}

	public void chooseRight() {
		if (index < clones.size() - 1) {
			index++;
		} else {
			index = 0;
		}
	}

	public void chooseStart() {
		try {
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/battleground", "root", "root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from weapons where name = \""+ clones.get(index).getWeaponName() + "\";");
			while(rs.next())
			clones.get(index).initWeapon(rs.getInt(2), rs.getInt(3), rs.getInt(5), rs.getInt(4), rs.getInt(6), rs.getInt(7), rs.getInt(8));
			con.close();

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		sManager.newGame(screenX, screenY, clones.get(index));
	}

	public void chooseBack() {
		sManager.selectMenu();
	}

	public Scene getScene() {
		return scene;
	}

	public void drawStats(GraphicsContext g) {
		clones.get(index).drawStats(g, 400, 600);
	}

	public void drawPlayer(GraphicsContext g) {
		clones.get(index).drawPlayer(g, 600, 600);
	}

}
