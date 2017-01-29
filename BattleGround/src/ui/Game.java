package ui;

import java.util.ArrayList;

import game.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Game {
	Scene gameScene;
	SceneManager sManager;
	Block[][] map;
	ArrayList<Block> spawners = new ArrayList<Block>();
	GridPane root;
	MapReader mapR;
	double screenX, screenY;
	int scaleX, scaleY;
	Player player;
	ArrayList<Laser> lasers = new ArrayList<Laser>();
	Droid b1, b2, bx, droideka, ig100, grievous;
	ArrayList<Droid> droids = new ArrayList<Droid>();

	public Game(double sX, double sY, Character c, SceneManager s) {
		sManager = s;
		screenX = sX;
		screenY = sY;
		mapR = new MapReader();
		map = mapR.readFile("map");
		spawners = mapR.getSpawners();
		scaleX = (int) (screenX / mapR.getMapX());
		scaleY = (int) (screenY / mapR.getMapY());
		player = new Player(1, 1, scaleX, scaleY, c);
		initComponents();
	}

	public void initComponents() {
		root = new GridPane();
		gameScene = new Scene(root, screenX, screenY);
		Canvas canvas = new Canvas();
		canvas.setWidth(screenX);
		canvas.setHeight(screenY);
		GraphicsContext g = canvas.getGraphicsContext2D();

		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent k) {
				if (k.getCode() == KeyCode.A) {
					player.setMoveX(-1);
				} else if (k.getCode() == KeyCode.D) {
					player.setMoveX(1);
				}

				if (k.getCode() == KeyCode.W) {
					player.setMoveY(-1);
				} else if (k.getCode() == KeyCode.S) {
					player.setMoveY(1);
				}

			}
		});

		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent k) {
				if (k.getCode() == KeyCode.A) {
					player.setMoveX(0);
				} else if (k.getCode() == KeyCode.D) {
					player.setMoveX(0);
				}

				if (k.getCode() == KeyCode.W) {
					player.setMoveY(0);
				} else if (k.getCode() == KeyCode.S) {
					player.setMoveY(0);
				}
			}
		});

		gameScene.setOnMousePressed(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				if(event.isPrimaryButtonDown()){
					lasers.add(player.fire(event.getX(), event.getY(), scaleX, scaleY));
				}
			}
		});

		gameScene.setOnMouseDragged(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				if(event.isPrimaryButtonDown()){
					lasers.add(player.fire(event.getX(), event.getY(), scaleX, scaleY));
				}
			}
		});

		Timeline t = new Timeline();
		t.setCycleCount(Timeline.INDEFINITE);
		t.getKeyFrames().add(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				drawMap(g);
				drawPlayer(g);
				for(Laser laser: lasers){
					laser.draw(g);
					laser.move();
					if(collidableCheck(laser.getX(), laser.getY())){
						laser.setMarked();
					}
				}
				for(Droid droid: droids){
					droid.draw(g);
				}
			}
		}));
		t.play();

		Timeline moveT = new Timeline();
		moveT.setCycleCount(Timeline.INDEFINITE);
		moveT.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000/player.getCharacter().getSpeed()), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						player.move(map);

					}
				}));
		moveT.play();

		System.out.println(screenX + "/" + screenY);

		root.add(canvas, 0, 0);
	}

	public boolean collidableCheck(double x, double y){
		if(x < screenX && y < screenY && x > 0 && y > 0){
			return map[(int) (x/scaleX)][(int) (y/scaleY)].isCollidable();
		} else {
			return true;
		}
	}

	public void drawMap(GraphicsContext g) {
		g.clearRect(0, 0, screenX, screenY);
		for (int y = 0; y < mapR.getMapY(); y++) {
			for (int x = 0; x < mapR.getMapX(); x++) {
				map[x][y].draw(g, scaleX, scaleY);
			}
		}
	}

	public void drawPlayer(GraphicsContext g) {
		player.draw(g, scaleX, scaleY);
	}

	public Scene getScene() {
		return gameScene;
	}

	public void initEnemyTypes(){
		b1 = new Droid("b1", screenX, screenX);
		b2 = new Droid("b2", screenX, screenX);
		bx = new Droid("bx", screenX, screenX);
		droideka = new Droid("droideka", screenX, screenX);
		ig100 = new Droid("ig100", screenX, screenX);
		grievous = new Droid("grievous", screenX, screenX);
	}

	public void spawnRandomDroid(){
		Block spawn = spawners.get((int)Math.random()*spawners.size());
		double rand = Math.random()*1250;
		Droid droid;

		if(rand > 5){
			droid = grievous;
		} else if(rand > 35){
			droid = ig100;
		} else if(rand > 100){
			droid = droideka;
		} else if(rand > 225){
			droid = bx;
		} else if(rand > 600){
			droid = b2;
		} else {
			droid = b1;
		}

		droid.setX(spawn.getX());
		droid.setY(spawn.getY());
		droids.add(droid);
	}
}
