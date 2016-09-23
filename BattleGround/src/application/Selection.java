package application;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Selection {
	Scene scene;
	GridPane root;
	Canvas stats, player;
	Button back, left, right, start;
	double screenX, screenY;
	ArrayList<Character> clones = new ArrayList<Character>();
	int index = 1;
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
		clones.add(new Character("Default", 100, 10, 0.8, 1, "DC-15", "EMP", "Punch"));
		clones.add(new Character("212", 100, 10, 0.8, 1, "DC-15", "EMP", "Punch"));
		clones.add(new Character("Shock", 80, 8, 0.95, 1.2, "DC-17", "EMP", "Bayonet"));
		clones.add(new Character("Appo", 150, 15, 0.7, 1.2, "DC-15", "Thermal Detonator", "Bayonet"));
		clones.add(new Character("Cody", 200, 10, 0.8, 1.5, "DC-15", "Thermal Detonator", "Stun Baton"));
		clones.add(new Character("Deviss", 150, 25, 0.99, 1.5, "DC-17", "EMP", "Gut Knife"));
	}

	public void initComponents() {
		addClones();
		drawTimer = new Timer();
		back = new Button("Back");
		left = new Button("<-");
		right = new Button("->");
		start = new Button("Start");
		stats = new Canvas(200, 600);
		player = new Canvas(600, 600);
				
		statsG = stats.getGraphicsContext2D();
		playerG = player.getGraphicsContext2D();
		root = new GridPane();
		root.setHgap(20);
		root.setVgap(40);
		
		drawTimer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				drawPlayer(playerG);
				drawStats(statsG);
			}
		}, 100, 2000);
		

		left.setOnAction(e -> chooseLeft());
		right.setOnAction(e -> chooseRight());
		start.setOnAction(e -> chooseStart());
		scene = new Scene(root, screenX, screenY);
		root.add(stats, 0, 0, 2, 4);
		root.add(player, 2, 0, 4, 4);
		root.add(back, 0, 4, 2, 1);
		root.add(left, 2, 4, 1, 1);
		root.add(start, 3, 4, 1, 1);
		root.add(right, 4, 4, 1, 1);
	}

	public void chooseLeft() {
		if (index > 0) {
			index--;
		} else {
			index = clones.size();
		}
	}

	public void chooseRight() {
		if (index < clones.size()) {
			index++;
		} else {
			index = 0;
		}
	}
	
	public void chooseStart(){
		System.out.println(clones.get(index).name);
	}

	public Scene getScene() {
		return scene;
	}

	public void drawStats(GraphicsContext g){
		
	}

	public void drawPlayer(GraphicsContext g){
		g.setFill(Color.BLUE);
		g.fillRect(0, 0, 600, 600);
		clones.get(index).draw(g, 600, 600);
	}

}
