package game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import ai.PathFinder;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ui.Main;

public class Droid extends Entity implements Sortable {

	Image image;
	double speed;
	double accuracy;
	double skill;
	double weaponDamage;
	double weaponRoF;
	double weaponOverheat;
	double weaponMagSize;
	double meleeDamage;
	double meleeRange;
	double range;
	double rarity;
	int red, blue, green;
	String weaponName;
	String meleeName;
	Block start, end;
	LinkedList<Block> path;
	PathFinder pathFinder;
	boolean firing = false;
	private double sortValue;

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
		skill = d.skill;
		weaponDamage = d.weaponDamage;
		weaponRoF = d.weaponRoF;
		weaponOverheat = d.weaponOverheat;
		weaponMagSize = d.weaponMagSize;
		meleeDamage = d.meleeDamage;
		meleeRange = d.meleeRange;
		red = d.red;
		blue = d.blue;
		green = d.green;
		weaponName = d.weaponName;
		meleeName = d.meleeName;
		sizeX = d.sizeX;
		sizeY = d.sizeY;
		pathFinder = d.pathFinder;
		setHealth(d.health);
		range = d.range;
		rarity = d.rarity;
	}

	/**
	 * Draws the droid and its health bar
	 *
	 * @param g
	 * @param x
	 * @param y
	 */
	public void draw(GraphicsContext g, double x, double y) {
		if (health < maxHealth) {
			g.setFill(Color.CRIMSON);
			g.fillRect(x, y - sizeY / 4, sizeX, sizeY / 4);
			g.setFill(Color.LIMEGREEN);
			g.fillRect(x, y - sizeY / 4, sizeX * health / maxHealth, sizeY / 4);
		}
		g.setFill(Color.ORANGERED);
		g.fillOval(x, y, sizeX, sizeY);
		g.drawImage(image, x, y, sizeX, sizeY);
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
			skill = rs.getDouble(5);
			weaponName = rs.getString(6);
			if (rs.getBlob(7) != null){
				image = new Image(rs.getBlob(7).getBinaryStream());
			}
			meleeName = rs.getString(8);
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
	 * Moves the droid randomly to an available block
	 *
	 * @param map
	 * @param mapX
	 * @param mapY
	 */
	public void move(Block[][] map, double mapX, double mapY) {
		int rand = (int) (Math.random() * 4);
		switch (rand) {
		case 0:
			if (posY - 1 >= 0)
				if (!map[(int) posX][(int) (posY - 1)].collidable)
					posY -= 1;
			break;
		case 1:
			if (posY < mapY + 2)
				if (!map[(int) posX][(int) (posY + 1)].collidable)
					posY += 1;
			break;
		case 2:
			if (posX - 1 >= 0)
				if (!map[(int) (posX - 1)][(int) posY].collidable)
					posX -= 1;
			break;
		case 3:
			if (posX < mapX + 2)
				if (!map[(int) (posX + 1)][(int) posY].collidable)
					posX += 1;
			break;
		}
	}

	/**
	 * Sets the droid to patrol a certain route between two points
	 *
	 * @param s
	 * @param e
	 */
	public void setPatrolling(Block s, Block e) {
		start = s;
		end = e;
		path = pathFinder.findPath(start, end);
	}

	/**
	 * Makes the droid move to the next block in the path. If the path has been
	 * traversed, it reverses the path and continues
	 */
	public void moveThroughPath() {
		if (path.size() > 0) {
			Block next = path.pop();
			posX = next.getX();
			posY = next.getY();
		} else {
			setPatrolling(end, start);
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
	public Laser fire(double tX, double tY) {
		return new Laser(accuracy, posX, posY, weaponDamage, tX, tY, red, green, blue, false);
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
