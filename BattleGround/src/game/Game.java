package game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
	ArrayList<Droid> droids = new ArrayList<Droid>();
	ArrayList<Droid> droidTypes = new ArrayList<Droid>();
	Round round;
	public final static double FRAME_RATE = 100;
	public final static double ZOOM = 2;
	public final static boolean DEBUG = false;
	int droidsLeft = 0;
	boolean firing = false;
	boolean meleeing = false;
	double firingX, firingY;
	double offX, offY;
	int score;
	PathFinder pF;
	double probability = 0;
	Sorter sorter = new Sorter();

	/**
	 * @param sX
	 * @param sY
	 * @param c
	 * @param s
	 * @param diff
	 */
	public Game(double sX, double sY, Character c, SceneManager s, String diff, Modifier mod) {
		score = 0;
		sManager = s;
		screenX = sX;
		screenY = sY;
		mapR = new MapReader();
		// Creates the map from a file
		map = mapR.readFile("map");
		// Sets the spawners on the map
		spawners = mapR.getSpawners();
		// Sets the size of each block in pixels
		scaleX = (int) (ZOOM * (screenY / mapR.getMapY()));
		scaleY = (int) (ZOOM * (screenY / mapR.getMapY()));
		// Initialises the player
		player = new Player(1, 1, scaleX, scaleY, c);
		// Creates the path finder based on the map
		pF = new PathFinder(map, mapR.getMapX(), mapR.getMapY());
		initComponents();
		initEnemyTypes();
		round = new Round(diff);
		droidsLeft = round.calculateDroids();
		player.modifyStat(mod);
	}

	/**
	 * Generates GUI Starts graphics and movement timers
	 */
	public void initComponents() {
		// Creates the layout for the window
		root = new GridPane();
		// Creates the scene for the game
		gameScene = new Scene(root, screenX, screenY);
		// Creates the canvas for the game to be drawn on
		Canvas canvas = new Canvas();
		canvas.setWidth(screenX);
		canvas.setHeight(screenY);
		GraphicsContext g = canvas.getGraphicsContext2D();
		// Creates the event handler for key presses
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent k) {
				// Moves the player horizontally depending on which key is
				// pressed
				if (k.getCode() == KeyCode.A) {
					player.setMoveX(-1);
				} else if (k.getCode() == KeyCode.D) {
					player.setMoveX(1);
				}
				// Moves the player vertically depending on which key is pressed
				if (k.getCode() == KeyCode.W) {
					player.setMoveY(-1);
				} else if (k.getCode() == KeyCode.S) {
					player.setMoveY(1);
				}

				// Sets the player as meleeing if the v key is pressed
				if (k.getCode() == KeyCode.V) {
					meleeing = true;
				}

			}
		});
		// Creates the event handler for key releases
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent k) {
				// Stops the player moving horizontally
				if (k.getCode() == KeyCode.A) {
					player.setMoveX(0);
				} else if (k.getCode() == KeyCode.D) {
					player.setMoveX(0);
				}
				// Stops the player moving vertically
				if (k.getCode() == KeyCode.W) {
					player.setMoveY(0);
				} else if (k.getCode() == KeyCode.S) {
					player.setMoveY(0);
				}
				// Stops the player meleeing
				if (k.getCode() == KeyCode.V) {
					meleeing = false;
				}
			}
		});

		// Creates the event handler for mouse presses
		gameScene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Sets the player as firing if the left button is clicked
				if (event.isPrimaryButtonDown()) {
					firing = true;
				}
			}
		});
		// Creates the event handler for mouse movement
		gameScene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Sets the player's target as the mouse location
				firingX = event.getX();
				firingY = event.getY();

			}
		});
		// Creates the event handler for mouse releasing
		gameScene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Stops the player firing when the left button is released
				if (!event.isPrimaryButtonDown()) {
					firing = false;
				}
			}
		});
		// Creates the event handler for mouse dragging
		gameScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Sets the player as firing and update the target location to
				// the mouse location
				if (event.isPrimaryButtonDown()) {
					firing = true;
					firingX = event.getX();
					firingY = event.getY();
				}
			}
		});
		// Creates the event handler for mouse drag releasing
		gameScene.setOnMouseDragReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Stops the player firing
				if (!event.isPrimaryButtonDown()) {
					firing = false;
				}
			}
		});

		// Creates the global timer for the game, running at 100 FPS default
		Timeline t = new Timeline();
		t.setCycleCount(Timeline.INDEFINITE);
		t.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / FRAME_RATE), new EventHandler<ActionEvent>() {
			// Initialises the count for the timer, used to keep track of the
			// time for events that happen less frequently than the frame rate
			int count = 0;

			@Override
			public void handle(ActionEvent event) {
				// The timer only functions while the player is alive
				if (player.isAlive()) {
					// Draws the map
					drawMap(g);
					// Creates an iterator to safely iterate through the weapons
					// array
					Iterator<Weapon> weaponIt = weapons.iterator();
					while (weaponIt.hasNext()) {
						Weapon weapon = weaponIt.next();
						// Draws the weapon on the screen
						weapon.draw(g, transformXtoS(weapon.getX()), transformYtoS(weapon.getY()),
								transformXtoS(weapon.getX() + weapon.getW()),
								transformYtoS(weapon.getY() + weapon.getH()));
						// Moves the weapon
						weapon.move();
						// Checks for collisions between the weapon and any
						// collidable blocks
						if (collidableCheck(weapon.getX(), weapon.getY())) {
							// Sets the weapon as marked for removal
							weapon.setMarked();
						}
						// If the weapon is marked for removal, it is safely
						// removed by the weapon iterator
						if (weapon.isMarked()) {
							weaponIt.remove();
						}
						// Checks for a collision between the weapon and the
						// player
						if (weapon.checkCollision(transformXtoS(weapon.getX()), transformYtoS(weapon.getY()),
								scaleX * weapon.getW(), scaleY * weapon.getH(), transformXtoS(player.getX()),
								transformYtoS(player.getY()), scaleX, scaleY) && !weapon.isMarked()
								&& !weapon.isPlayer()) {
							// Does damage to the player
							weapon.doDamage(player);
							// Marks the weapon for removal
							weapon.setMarked();
							// If the player is killed by the weapon, the score
							// is saved and the user is returned to the main
							// menu
							if (!player.isAlive()) {
								saveScore();
								sManager.selectMenu();
							}
						}
					}

					// Creates an iterator to safely iterate through the droids
					// array
					Iterator<Droid> droidIt = droids.iterator();
					// Iterates through the list of droids
					while (droidIt.hasNext()) {
						Droid droid = droidIt.next();
						// Removes droids with no speed to avoid division by 0
						if (droid.getSpeed() == 0) {
							droidIt.remove();
						} else {
							// If the droid is firing, draws the rotated
							// variation of the droid, otherwise draws the
							// regular version
							if (droid.isFiring()) {
								droid.draw(g, transformXtoS(droid.getX()), transformYtoS(droid.getY()),
										transformXtoS(player.getX()), transformYtoS(player.getY()));
							} else {
								droid.draw(g, transformXtoS(droid.getX()), transformYtoS(droid.getY()));
							}
							// Checks if the droid can move in the current frame
							if (count % (int) (Main.FRAME_RATE / droid.getSpeed()) == 0) {
								// If the droid is in range of the player, it
								// will stand still and start firing at the
								// player, else it will not fire and move
								// through its designated path
								if (droid.range * droid.range > distance(player.getX(), player.getY(), droid.getX(),
										droid.getY())) {
									droid.setFiring(true);
								} else {
									droid.setFiring(false);
									droid.moveThroughPath(map, spawners);
								}
							}
							// If the droid can fire this frame and it is in a
							// firing state, it will create a fire a laser
							// towards the player
							if (count % (int) (droid.getRoF()) == 0 && droid.isFiring()) {
								weapons.add(droid.fire(player.getX() + 0.5, player.getY() + 0.5, droid.getX() + 0.5,
										droid.getY() + 0.5));
							}
							// Iterates through each weapon currently existing
							for (Weapon weapon : weapons) {
								// Checks if the current weapon is intersecting
								// the current droid
								if (weapon.checkCollision(transformXtoS(weapon.getX()), transformYtoS(weapon.getY()),
										scaleX * weapon.getW(), scaleY * weapon.getH(), transformXtoS(droid.getX()),
										transformYtoS(droid.getY()), scaleX, scaleY) && !weapon.isMarked()
										&& weapon.isPlayer()) {
									// Applies damage to the player
									weapon.doDamage(droid);
									// Adds to the score of the player
									incrementScore((int) weapon.getDamage());
									// Marks the weapon for removal
									weapon.setMarked();
									// If the droid has its health reduced to 0,
									// it is removed
									if (!droid.isAlive()) {
										droidIt.remove();
										break;
									}
								}
							}
						}
					}
					// If all the droids in the current round have been
					// destroyed, the player advances to the next round
					if (droids.size() == 0 && droidsLeft == 0) {
						round.increaseRound();
						droidsLeft = round.calculateDroids();
					}
					// If the player can fire in the current frame and is in a
					// state of firing, the player fires a laser towards the
					// mouse location
					if (count % (int) (player.getRoF()) == 0 && firing) {
						weapons.add(player.fire(transformStoX(firingX), transformStoY(firingY), player.getX() + 0.5,
								player.getY() + 0.5));
					}
					// If the player is in a state of meleeing, an instance of
					// melee is created towards the mouse location
					if (meleeing) {
						weapons.add(player.melee(transformStoX(firingX), transformStoY(firingY), player.getX() + 0.5,
								player.getY() + 0 / 5, transformStoX(scaleX), transformStoY(scaleY)));
					}
					// If the player can move on the current frame, their
					// position is adjusted and the offset of the map is changed
					// correspondingly
					if (count % (int) (Main.FRAME_RATE / player.getCharacter().getSpeed()) == 0) {
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

					if(count % 1000 == 0){
						// Spawns a droid on the map
						spawnRandomDroid();
					}

					// Increments the count
					count++;
				}
				// Draws the player on the screen
				drawPlayer(g);
			}
		}));
		// Starts the timer
		t.play();
		// Adds the canvas to the layout
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
		// Checks if the input coordinates are on the map
		if (x < mapR.getMapX() && y < mapR.getMapY() && x > 0 && y > 0) {
			// Returns whether the block chosen is collidable
			return map[(int) x][(int) y].isCollidable();
		} else {
			// Returns true, as it should be impossible for anything to exist
			// off of the map
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
		// Increments the score by the damage multiplied
		score += s * round.getDifficulty() * round.getRound();
	}

	/**
	 * Draws each block in the map as well as any HUD elements
	 *
	 * @param g
	 */
	public void drawMap(GraphicsContext g) {
		// Clears the previous drawing(s)
		g.clearRect(0, 0, screenX, screenY);
		// Loops through each block in the map
		for (int y = 0; y < mapR.getMapY(); y++) {
			for (int x = 0; x < mapR.getMapX(); x++) {
				// Draws the block at the transformed coordinates
				map[x][y].draw(g, transformXtoS(x), transformYtoS(y), scaleX, scaleY);
			}
		}
		// Draws text to show information relating to the current round
		g.setFill(Color.CRIMSON);
		g.fillText("Round " + round.getRound(), 10, 10);
		g.fillText("Droids left: " + droids.size(), 10, 25);
		g.fillText("Score: " + score, 10, 40);
		// If the debugging mode is on, more information is shown for testing
		// purposes
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
		// Draws the player at the transformed coordinates, rotated towards the
		// current cursor location
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
		try {
			// Connects to the database on the server
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			Statement stmt = con.createStatement();
			// Retrieves data for each type of droid, along with its weapon and
			// melee
			ResultSet rs = stmt.executeQuery(
					"select * from droids join weapons on droids.weapon = weapons.name join melees on droids.melee = melees.name;");
			while (rs.next()) {
				// Creates a template droid for each type
				Droid droid = new Droid(rs, scaleX, scaleY, pF);
				// Sets the droid's sorting value as its rarity
				droid.setValue("rarity");
				// Adds the droid template to the list
				droidTypes.add(droid);
				// Accumulates the rarity of each droid
				probability += rs.getDouble(10);
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		// Sorts the droids in descending order of rarity, as the rarity is used
		// in the spawnRandomDroid function for comparison
		droidTypes = sorter.breakDown(droidTypes, false);

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
		// Checks whether any more droids can spawn in the round
		if (spawners.size() > 0 && droidsLeft > 0) {
			// Chooses a random spawner from the list of spawners
			int spawnerIndex = (int) (Math.random() * spawners.size());
			Block spawn = spawners.get(spawnerIndex);
			// Chooses a random type of droid
			double rand = Math.random() * probability;
			// Creates a default droid from the template
			Droid droid = new Droid(droidTypes.get(0));
			// Iterates the types of droid to find the droid with the lowest
			// rarity above the random value
			for (Droid d : droidTypes) {
				// If the droid's rarity is higher than the random value, it is
				// overwritten by the rarer type of droid
				if (rand <= d.getRarity()) {
					droid = new Droid(d);
				}
			}
			// Sets the droid's position as the spawner's position
			droid.setX(spawn.getX());
			droid.setY(spawn.getY());
			// Sets the droid patrolling between spawners
			droid.setPatrolling(map, spawners);

			// Adds the droid to the list of currently active droids
			droids.add(droid);
			// Decrements the number of droids left to spawn for the round
			droidsLeft--;
		}
	}

	/**
	 * Performs a SQL update to add the users score to the score database
	 */
	public void saveScore() {
		try {
			// Connects to the database on the server
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			// Updates the scores table in the database with the player's score
			// from the round
			stmt.executeUpdate("insert into scores (userID, score, round, difficulty, clone) values (" + Menu.USER_ID
					+ "," + score + "," + round.getRound() + ",\"" + round.getDifficultyS() + "\",\""
					+ player.getCharacter().getName() + "\");");
		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
	}
}