package application;

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
	GraphicsContext statsG, playerG;
	Timer drawTimer;


	public Selection(double sX, double sY) {
		initComponents();
		addClones();
		screenX = sX;
		screenY = sY;
	}

	// TO BE REPLACED WITH DATABASE QUERY AND RETURN
	public void addClones() {
		clones.add(new Character("Default", 100, 10, 0.8, 1, "DC-15s", "EMP", "Punch", "CT"));
		clones.add(new Character("212", 100, 8, 0.9, 1, "DC-15s", "EMP", "Punch", "Air Trooper"));
		clones.add(new Character("Shock", 80, 8, 0.95, 1.2, "DC-15a", "EMP", "Bayonet", "CT"));
		clones.add(new Character("Appo", 150, 15, 0.7, 1.2, "DC-15s", "Thermal Detonator", "Bayonet", "Sergeant"));
		clones.add(new Character("Cody", 200, 10, 0.8, 1.5, "DC-15s", "Thermal Detonator", "Stun Baton", "Commander"));
		clones.add(new Character("Deviss", 80, 25, 0.99, 1.5, "DC-15a", "EMP", "Gut Knife", "Commander"));
		clones.add(new Character("Rex", 180, 17, 0.7, 1.8, "Dual DC-17", "EMP", "Punch", "Captain"));
		clones.add(new Character("Fox", 150, 19, 0.8, 1.4, "Dual DC-17", "Thermal Detonator", "Riot Baton", "Commander"));
		clones.add(new Character("Wolffe", 110, 12, 0.75 , 4.0, "Dual DC-17", "Thermal Imploder", "Vibroblade", "Commander"));
		clones.add(new Character("Sniper", 70, 25, 0.8 , 2.0, "DLT-19X", "Flashbang", "Gut Knife", "ARF"));
	}

	public void initComponents() {
		addClones();
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
		left.setOnAction(e -> {chooseLeft(); drawPlayer(playerG); drawStats(statsG);});
		right.setOnAction(e -> {chooseRight(); drawPlayer(playerG); drawStats(statsG);});
		start.setOnAction(e -> chooseStart());
		scene = new Scene(root, screenX, screenY);
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

	public void chooseStart(){

	}

	public Scene getScene() {
		return scene;
	}

	public void drawStats(GraphicsContext g){
		clones.get(index).drawStats(g, 400, 600);
	}

	public void drawPlayer(GraphicsContext g){
		clones.get(index).draw(g, 600, 600);
	}

}
