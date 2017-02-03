package game;

import java.util.ArrayList;
import java.util.Iterator;
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
import ui.*;
import ui.Character;

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
	Round round;
	public final static double FRAME_RATE = 100;
	int droidsLeft = 0;
	boolean firing = false;
	double firingX, firingY;

	public Game(double sX, double sY, Character c, SceneManager s, String diff) {
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
		initEnemyTypes();
		round = new Round(diff);
		droidsLeft = round.calculateDroids();
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

		gameScene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()) {
					firing = true;
					firingX = event.getX();
					firingY = event.getY();
				}
			}
		});
		
		gameScene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!event.isPrimaryButtonDown()) {
					firing = false;
				}
			}
		});


		gameScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()) {
					firing = true;
					firingX = event.getX();
					firingY = event.getY();
					
				}
			}
		});
		
		gameScene.setOnMouseDragReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!event.isPrimaryButtonDown()) {
					firing = false;
				}
			}
		});

		
		Timeline t = new Timeline();
		t.setCycleCount(Timeline.INDEFINITE);
		t.getKeyFrames().add(new KeyFrame(Duration.millis(1000/FRAME_RATE), new EventHandler<ActionEvent>() {
			int count = 0;
			
			@Override
			public void handle(ActionEvent event) {
				if (player.isAlive()) {
					drawMap(g);
					Iterator<Laser> laserIt = lasers.iterator();
					while (laserIt.hasNext()) {
						Laser laser = laserIt.next();
						laser.draw(g);
						laser.move();
						if (collidableCheck(laser.getX(), laser.getY())) {
							laser.setMarked();
						}
						if (laser.isMarked()) {
							laserIt.remove();
						}
					}

					Iterator<Droid> droidIt = droids.iterator();
					while (droidIt.hasNext()) {
						Droid droid = droidIt.next();
						droid.draw(g);
						for (Laser laser : lasers) {
							if (laser.checkCollision(droid) && !laser.isMarked()) {
								laser.setMarked();
								laser.doDamage(droid);
								if(!droid.isAlive()){
									droidIt.remove();
									break;
								}
							}
						}
					}

					if (droids.size() == 0 && droidsLeft == 0) {
						round.increaseRound();
						droidsLeft = round.calculateDroids();
					}

					if(count%(int)(player.getRoF()) == 0 && firing){
						lasers.add(player.fire(firingX, firingY, scaleX, scaleY));
					}
					
					count++;
				}
				drawPlayer(g);
			}
		}));
		t.play();

		Timeline droidT = new Timeline();
		droidT.setCycleCount(Timeline.INDEFINITE);
		droidT.getKeyFrames().add(
				new KeyFrame(Duration.millis(250), new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						Iterator<Droid> droidIt = droids.iterator();
						while (droidIt.hasNext()) {
							Droid droid = droidIt.next();
							droid.move(map, mapR.getMapX(), mapR.getMapY());
						}

					}

				}));
		droidT.play();

		Timeline moveT = new Timeline();
		moveT.setCycleCount(Timeline.INDEFINITE);
		moveT.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000 / player.getCharacter().getSpeed()), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						player.move(map, mapR.getMapX(), mapR.getMapY());
					}
				}));
		moveT.play();

		Timeline spawnT = new Timeline();
		spawnT.setCycleCount(Timeline.INDEFINITE);
		spawnT.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				spawnRandomDroid();
			}
		}));
		spawnT.play();

		System.out.println(screenX + "/" + screenY);

		root.add(canvas, 0, 0);
	}

	public boolean collidableCheck(double x, double y) {
		if (x / scaleX < mapR.getMapX() && y / scaleY < mapR.getMapY() && x > 0 && y > 0) {
			return map[(int) (x / scaleX)][(int) (y / scaleY)].isCollidable();
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

		g.setFill(Color.CRIMSON);
		g.fillText("Round " + round.getRound(), 10, 10);
		g.fillText("Droids left: " + droids.size(), 10, 25);
	}

	public void drawPlayer(GraphicsContext g) {
		player.draw(g, scaleX, scaleY);
	}

	public Scene getScene() {
		return gameScene;
	}

	public void initEnemyTypes() {
		b1 = new Droid("b1", scaleX, scaleY);
		b2 = new Droid("b2", scaleX, scaleY);
		bx = new Droid("bx", scaleX, scaleY);
		droideka = new Droid("droideka", scaleX, scaleY);
		ig100 = new Droid("ig100", scaleX, scaleY);
		grievous = new Droid("grievous", scaleX, scaleY);
	}

	public void spawnRandomDroid() {
		if (spawners.size() > 0 && droidsLeft > 0) {
			Block spawn = spawners.get((int) (Math.random() * spawners.size()));
			double rand = Math.random() * 1250;
			Droid droid;

			if (rand < 10) {
				droid = new Droid(grievous);
			} else if (rand < 45) {
				droid = new Droid(ig100);
			} else if (rand < 100) {
				droid = new Droid(droideka);
			} else if (rand < 225) {
				droid = new Droid(bx);
			} else if (rand < 600) {
				droid = new Droid(b2);
			} else {
				droid = new Droid(b1);
			}

			droid.setX(spawn.getX());
			droid.setY(spawn.getY());
			droids.add(droid);
			droidsLeft--;
		}
	}
}
