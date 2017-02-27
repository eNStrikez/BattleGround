package game;

import java.util.ArrayList;
import java.util.Iterator;

import ai.PathFinder;
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

/**
 * @author neilp
 *
 */
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
	ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	Droid b1, b2, bx, droideka, ig100, grievous;
	ArrayList<Droid> droids = new ArrayList<Droid>();
	Round round;
	public final static double FRAME_RATE = 100;
	public final static double ZOOM = 5;
	public final static boolean DEBUG = false;
	int droidsLeft = 0;
	boolean firing = false;
	boolean meleeing = false;
	double firingX, firingY;
	double offX, offY;
	int score;
	PathFinder pF;

	/**
	 * @param sX
	 * @param sY
	 * @param c
	 * @param s
	 * @param diff
	 */
	public Game(double sX, double sY, Character c, SceneManager s, String diff) {
		score = 0;
		sManager = s;
		screenX = sX;
		screenY = sY;
		mapR = new MapReader();
		map = mapR.readFile("map2");
		spawners = mapR.getSpawners();
		scaleX = (int) (ZOOM * (screenY / mapR.getMapY()));
		scaleY = (int) (ZOOM * (screenY / mapR.getMapY()));
		player = new Player(1, 1, scaleX, scaleY, c);
		pF = new PathFinder(map, mapR.getMapX(), mapR.getMapY());
		initComponents();
		initEnemyTypes();
		round = new Round(diff);
		droidsLeft = round.calculateDroids();
	}

	/**
	 * Generates GUI Starts graphics and movement timers
	 */
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
				
				if(k.getCode() == KeyCode.V) {
					meleeing = true;
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
				
				if(k.getCode() == KeyCode.V) {
					meleeing = false;
				}
			}
		});

		gameScene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()) {
					firing = true;
				}
			}
		});
		
		gameScene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				firingX = event.getX();
				firingY = event.getY();
				
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
		t.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / FRAME_RATE), new EventHandler<ActionEvent>() {
			int count = 0;

			@Override
			public void handle(ActionEvent event) {
				if (player.isAlive()) {
					drawMap(g);
					Iterator<Weapon> weaponIt = weapons.iterator();
					while (weaponIt.hasNext()) {
						Weapon weapon = weaponIt.next();
						weapon.draw(g, transformXtoS(weapon.getX()), transformYtoS(weapon.getY()),
								transformXtoS(weapon.getX() + weapon.getW()), transformYtoS(weapon.getY() + weapon.getH()));
						weapon.move();
						if (collidableCheck(weapon.getX(), weapon.getY())) {
							weapon.setMarked();
						}
						if (weapon.isMarked()) {
							weaponIt.remove();
						}
						if (weapon.checkCollision(transformXtoS(weapon.getX()), transformYtoS(weapon.getY()),
								scaleX * weapon.getW(), scaleY * weapon.getH(), transformXtoS(player.getX()),
								transformYtoS(player.getY()), scaleX, scaleY) && !weapon.isMarked() && !weapon.isPlayer()) {
							weapon.doDamage(player);
							weapon.setMarked();
							if (!player.isAlive()) {
								sManager.selectMenu();
							}
						}
					}

					Iterator<Droid> droidIt = droids.iterator();
					while (droidIt.hasNext()) {
						Droid droid = droidIt.next();
						droid.draw(g, transformXtoS(droid.getX()), transformYtoS(droid.getY()));
						if(count % (int) (droid.getSpeed()) == 0){
							if (droid.range * droid.range > distance(player.getX(), player.getY(), droid.getX(),
									droid.getY())) {
								droid.setFiring(true);
							} else {
								droid.setFiring(false);
								droid.moveThroughPath();
							}
						}
						if (count % (int) (droid.getRoF()) == 0 && droid.isFiring()) {
							weapons.add(droid.fire(player.getX(), player.getY()));
						}
						for (Weapon weapon : weapons) {
							if (weapon.checkCollision(transformXtoS(weapon.getX()), transformYtoS(weapon.getY()),
									scaleX * weapon.getW(), scaleY * weapon.getH(), transformXtoS(droid.getX()),
									transformYtoS(droid.getY()), scaleX, scaleY) && !weapon.isMarked() && weapon.isPlayer()) {
								weapon.doDamage(droid);
								incrementScore((int) weapon.getDamage());
								weapon.setMarked();
								if (!droid.isAlive()) {
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

					if (count % (int) (player.getRoF()) == 0 && firing) {
						weapons.add(player.fire(transformStoX(firingX), transformStoY(firingY)));
						player.heat();
					} else {
						player.cool();
					}



					count++;
				}
				drawPlayer(g);
			}
		}));
		t.play();

		Timeline moveT = new Timeline();
		moveT.setCycleCount(Timeline.INDEFINITE);
		moveT.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000 / player.getCharacter().getSpeed()), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						player.move(map, transformXtoS(mapR.getMapX()), transformYtoS(mapR.getMapY()));
						offX = player.getX() - ((mapR.getMapX() / ZOOM) / 2);
						if (offX < 0) {
							offX = 0;
						} else if (offX + 2 * (player.getX() - offX) > mapR.mapX) {
							offX = mapR.mapX - 2 * (player.getX() - offX);
						}

						offY = player.getY() - ((mapR.getMapY() / ZOOM) / 2);

						if (offY < 0) {
							offY = 0;
						} else if (offY + 2 * (player.getY() - offY) > mapR.mapY) {
							offY = mapR.mapY - 2 * (player.getY() - offY);
						}
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

	/**
	 * Checks whether the inputed coordinates intersect with a collidable block
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean collidableCheck(double x, double y) {
		if (x < mapR.getMapX() && y < mapR.getMapY() && x > 0 && y > 0) {
			return map[(int) x][(int) y].isCollidable();
		} else {
			return true;
		}
	}

	/**
	 * Add to the score proportional to the difficulty and round the player is
	 * on
	 *
	 * @param s
	 */
	public void incrementScore(int s) {
		score += s * round.getDifficulty() * round.getRound();
	}

	/**
	 * Draws each block in the map as well as any HUD elements
	 *
	 * @param g
	 */
	public void drawMap(GraphicsContext g) {
		g.clearRect(0, 0, screenX, screenY);

		for (int y = 0; y < mapR.getMapY(); y++) {
			for (int x = 0; x < mapR.getMapX(); x++) {
				map[x][y].draw(g, transformXtoS(x), transformYtoS(y), scaleX, scaleY);
			}
		}

		g.setFill(Color.CRIMSON);
		g.fillText("Round " + round.getRound(), 10, 10);
		g.fillText("Droids left: " + droids.size(), 10, 25);
		g.fillText("Score: " + score, 10, 40);
		if (DEBUG) {
			g.fillText("Offset: (" + offX + "," + offY + ")", 10, 55);
			g.fillText("Player Location: (" + player.getX() + "," + player.getY() + ")", 10, 70);
			g.fillText("Player Velocity: (" + player.getMoveX() + "," + player.getMoveY() + ")", 10, 85);
			g.fillText("Zoom: " + ZOOM, 10, 100);
			g.fillText("Scale: (" + scaleX + "," + scaleY + ")", 10, 115);
		}
	}

	/**
	 * Draws the player
	 *
	 * @param g
	 */
	public void drawPlayer(GraphicsContext g) {
		player.draw(g, transformXtoS(player.getX()), transformYtoS(player.getY()), scaleX, scaleY, firingX, firingY);
	}

	/**
	 * Returns the instance of the scene
	 *
	 * @return
	 */
	public Scene getScene() {
		return gameScene;
	}

	/**
	 * Initialises each type of droid
	 */
	public void initEnemyTypes() {
		b1 = new Droid("b1", scaleX, scaleY, pF);
		b2 = new Droid("b2", scaleX, scaleY, pF);
		bx = new Droid("bx", scaleX, scaleY, pF);
		droideka = new Droid("droideka", scaleX, scaleY, pF);
		ig100 = new Droid("ig100", scaleX, scaleY, pF);
		grievous = new Droid("grievous", scaleX, scaleY, pF);
	}

	/**
	 * Transforms a logical x value into a pixel x value
	 *
	 * @param x
	 * @return
	 */
	public double transformXtoS(double x) {
		return scaleX * (x - offX);
	}

	/**
	 * Transforms a logical y value into a pixel y value
	 *
	 * @param y
	 * @return
	 */
	public double transformYtoS(double y) {
		return scaleY * (y - offY);
	}

	/**
	 * Transforms a pixel x value into a logical x value
	 *
	 * @param x
	 * @return
	 */
	public double transformStoX(double x) {
		return x / scaleX + offX;
	}

	/**
	 * Transforms a pixel y value into a logical y value
	 *
	 * @param y
	 * @return
	 */
	public double transformStoY(double y) {
		return y / scaleY + offY;
	}

	/**
	 * Calculate the square of the distance between two coordinates
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double distance(double x1, double y1, double x2, double y2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}

	/**
	 * Uses a random number generator to spawn a random droid at a random
	 * spawner. The probability of each type of droid is weighted
	 */
	public void spawnRandomDroid() {
		if (spawners.size() > 0 && droidsLeft > 0) {
			int spawnerIndex = (int) (Math.random() * spawners.size());
			Block spawn = spawners.get(spawnerIndex);
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
			if (spawnerIndex + 1 >= spawners.size()) {
				spawnerIndex = 0;
			}
			Block target = spawners.get(spawnerIndex + 1);
			// System.out.println((map[spawn.getX()][spawn.getY()] + " To " +
			// map[target.getX()][target.getY()]));
			droid.setPatrolling(map[spawn.getX()][spawn.getY()], map[target.getX()][target.getY()]);
			droids.add(droid);
			droidsLeft--;
		}
	}
}
