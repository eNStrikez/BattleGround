package game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

import ai.PathFinder;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import ui.Main;

public class Droid extends Entity implements Sortable {

	Image image; // The image of the droid
	double speed; // The number of times per second the droid can move
	double accuracy; // The relative width of the cone of the droid's weapon
						// spread
	double weaponDamage; // The damage the droid's weapon deals
	double weaponRoF; // The number of rounds per second the the droid can fire
	double weaponOverheat; // Future Enhancement
	double weaponMagSize; // Future Enhancement
	double meleeDamage; // The damage of the droid's melee
	double meleeRange; // The melee range of the droid
	double range; // The aggression range of the droid, i.e. how far away it can
					// detect the player
	double rarity; // The relative probabilty of the droid spawning
	int red, blue, green; // The colour of the droid's laser
	Block start, end; // The start and end blocks for the droid's pathfinding
						// algorithm
	LinkedList<Block> path; // The current path that the droid moves along
	PathFinder pathFinder; // The pathfinding class
	boolean firing = false; // Whether the droid is firing at the player
	private double sortValue; // The sorting value that is used
	ImageView iV; // The object used to rotate the image

	/**
	 * @param name
	 * @param sX
	 * @param sY
	 */
	public Droid(ResultSet rs, double sX, double sY, PathFinder p) {
		loadDroid(rs);
		setSX(sX);
		setSY(sY);
		pathFinder = p;
	}

	/**
	 * @param d
	 */
	public Droid(Droid d) {
		image = d.image;
		speed = d.speed;
		accuracy = d.accuracy;
		weaponDamage = d.weaponDamage;
		weaponRoF = d.weaponRoF;
		weaponOverheat = d.weaponOverheat;
		weaponMagSize = d.weaponMagSize;
		meleeDamage = d.meleeDamage;
		meleeRange = d.meleeRange;
		red = d.red;
		blue = d.blue;
		green = d.green;
		sizeX = d.sizeX;
		sizeY = d.sizeY;
		pathFinder = d.pathFinder;
		setHealth(d.health);
		range = d.range;
		rarity = d.rarity;
		iV = new ImageView(image);
	}

	/**
	 * Draws the droid and its health bar
	 *
	 * @param g
	 * @param x
	 * @param y
	 */
	public void draw(GraphicsContext g, double x, double y) {
		// Draws the droid's health bar once it has recieved some damage
		if (health < maxHealth) {
			g.setFill(Color.CRIMSON);
			g.fillRect(x, y - sizeY / 4, sizeX, sizeY / 4);
			g.setFill(Color.LIMEGREEN);
			g.fillRect(x, y - sizeY / 4, sizeX * health / maxHealth, sizeY / 4);
		}
		// Draws the unrotated image of the droid
		g.drawImage(image, x, y, sizeX, sizeY);

	}

	/**
	 * Draws the droid facing the player and its health bar
	 *
	 * @param g
	 * @param x
	 * @param y
	 */
	public void draw(GraphicsContext g, double x, double y, double targetX, double targetY) {
		// Draws the droid's health bar once it has recieved some damage
		if (health < maxHealth) {
			g.setFill(Color.CRIMSON);
			g.fillRect(x, y - sizeY / 4, sizeX, sizeY / 4);
			g.setFill(Color.LIMEGREEN);
			g.fillRect(x, y - sizeY / 4, sizeX * health / maxHealth, sizeY / 4);
		}
		// Calculates the angle between the droid and its target
		double angle = 0;
		angle = -Math.atan2(targetX - x, targetY - y);
		// Rotates the image by the angle
		iV.setRotate(Math.toDegrees(angle));
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		// Rescales the image to account for the rotation
		double length = Math.abs(sizeY * Math.sin(angle)) + Math.abs(sizeX * Math.cos(angle));
		double height = Math.abs(sizeY * Math.cos(angle)) + Math.abs(sizeX * Math.sin(angle));
		// Draws the rotated image
		g.drawImage(iV.snapshot(params, null), x, y, length, height);
	}

	/**
	 * Performs an SQL query to retrieve and save the droid's stats, weapon and
	 * melee
	 *
	 * @param name
	 */
	public void loadDroid(ResultSet rs) {
		try {
			setHealth(rs.getDouble(2));
			speed = rs.getDouble(3);
			accuracy = rs.getDouble(4);
			if (rs.getBlob(7) != null) {
				image = new Image(rs.getBlob(7).getBinaryStream());
			}
			range = rs.getDouble(9);
			rarity = rs.getDouble(10);
			weaponDamage = rs.getDouble(12);
			weaponRoF = rs.getDouble(13);
			weaponMagSize = rs.getDouble(14);
			weaponOverheat = rs.getDouble(15);
			red = rs.getInt(16);
			blue = rs.getInt(17);
			green = rs.getInt(18);
			meleeDamage = rs.getDouble(20);
			meleeRange = rs.getDouble(21);
		} catch (Exception e) {

		}
	}

	/**
	 * Sets the droid to patrol a certain route between two points
	 *
	 * @param s
	 * @param e
	 */
	public void setPatrolling(Block[][] map, ArrayList<Block> spawners) {
		// Sets the path for the droid to patrol
		start = map[(int) getX()][(int) getY()];
		end = spawners.get((int) (Math.random()*spawners.size()));
		path = pathFinder.findPath(start, end);
	}

	/**
	 * Makes the droid move to the next block in the path. If the path has been
	 * traversed, it reverses the path and continues
	 */
	public void moveThroughPath(Block[][] map, ArrayList<Block> spawners) {
		// If the droid has a path to move through, it locates itself to the
		// next block on the path, else it will reverse its path
		if (path.size() > 0) {
			Block next = path.pop();
			posX = next.getX();
			posY = next.getY();
		} else {
			setPatrolling(map, spawners);
		}
	}

	/**
	 * Set whether the droid is firing or not
	 *
	 * @param f
	 */
	public void setFiring(boolean f) {
		firing = f;
	}

	/**
	 * Set whether the droid is firing or not
	 *
	 * @param f
	 */
	public boolean isFiring() {
		return firing;
	}

	/**
	 * Fires a laser from the droids position to the target location
	 *
	 * @param tX
	 * @param tY
	 * @return
	 */
	public Laser fire(double tX, double tY, double x, double y) {
		return new Laser(accuracy, x, y, weaponDamage, tX, tY, red, green, blue);
	}

	/**
	 * Returns the number of times per second that a laser can be generated
	 *
	 * @return
	 */
	public double getRoF() {
		return (60 / weaponRoF) * Game.FRAME_RATE;
	}

	/**
	 * Returns the droid's speed value
	 *
	 * @return
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Returns the droid's rarity value
	 *
	 * @return
	 */
	public double getRarity() {
		return rarity;
	}

	@Override
	public double getValue() {
		return sortValue;
	}

	@Override
	public void setValue(String value) {
		if (value == "rarity") {
			sortValue = rarity;
		}
	}
}
