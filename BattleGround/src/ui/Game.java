package ui;

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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Game {
	Scene gameScene;
	SceneManager sManager;
	Block map[][];
	GridPane root;
	MapReader mapR;
	double screenX, screenY;
	int scaleX, scaleY;
	Player player;

	public Game(double sX, double sY, Character c, SceneManager s) {
		sManager = s;
		screenX = sX;
		screenY = sY;
		player = new Player(0, 0, c);
		mapR = new MapReader();
		map = mapR.readFile("map");
		scaleX = (int) (screenX / mapR.getMapX());
		scaleY = (int) (screenY / mapR.getMapY());
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
					System.out.println();
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
		
		Timeline t = new Timeline();
		t.setCycleCount(Timeline.INDEFINITE);
		t.getKeyFrames().add(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				drawMap(g);
				drawPlayer(g);
			}
		}));
		t.play();

		Timeline moveT = new Timeline();
		moveT.setCycleCount(Timeline.INDEFINITE);
		moveT.getKeyFrames().add(
				new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						player.move();
					}
				}));
		moveT.play();

		System.out.println(screenX + "/" + screenY);
		root.add(canvas, 0, 0);
	}

	public void drawMap(GraphicsContext g) {
		g.clearRect(0, 0, screenX, screenY);
		for (int y = 0; y < mapR.getMapY(); y++) {
			for (int x = 0; x < mapR.getMapX(); x++) {
				map[x][y].draw(g, (int)(x) * scaleX, (int)(y) * scaleY, scaleX, scaleY);
			}
		}
	}

	public void drawPlayer(GraphicsContext g) {
		player.draw(g, scaleX, scaleY);
	}

	public Scene getScene() {
		return gameScene;
	}
}
