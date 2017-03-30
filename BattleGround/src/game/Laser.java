package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Laser implements Weapon {
	double posX;
	double posY;
	double damage;
	double speedX;
	double speedY;
	double height;
	double width;
	double speed = 1;
	double size;
	int red, blue, green;
	boolean marked = false;
	boolean player;

	/**
	 * Constructor for laser with skill input
	 *
	 * @param a
	 * @param x
	 * @param y
	 * @param d
	 * @param tX
	 * @param tY
	 * @param r
	 * @param g
	 * @param b
	 */
	public Laser(double a, double x, double y, double d, double tX, double tY, int r, int g, int b, double skill) {
		// Sets the initial position
		posX = x;
		posY = y;
		// Sets the accuracy, damage and size of the laser
		double accuracy = a;
		damage = d;
		size = 2;
		// Calculates the angle between the initial laser position and the
		// target
		double angle = Math.atan2((tX - x), (tY - y));
		// Calculates the random deviation of the laser
		double rand = (Math.random() * accuracy - accuracy / 2);
		double angleSin = Math.sin(angle - Math.toRadians(rand));
		double angleCos = Math.cos(angle - Math.toRadians(rand));
		// Sets the horizontal and vertical components of the laser's speed
		speedX = speed * angleSin;
		speedY = speed * angleCos;
		// Sets the width and height of the rectangle which bounds the laser
		width = size * angleSin;
		height = size * angleCos;
		// Sets the lasers colour
		red = r;
		green = g;
		blue = b;
		// If the random value is lower than the skill chance, the laser's
		// damage is increased and the colour is set as golden
		if (Math.random() * 100 < skill) {
			damage = damage * 10 * skill;
			red = 255;
			green = 226;
			blue = 140;
		}
		// Sets the laser as owned by the player
		player = true;
	}

	/**
	 * Constructor for laser without skill input
	 *
	 * @param a
	 * @param x
	 * @param y
	 * @param d
	 * @param tX
	 * @param tY
	 * @param r
	 * @param g
	 * @param b
	 */
	public Laser(double a, double x, double y, double d, double tX, double tY, int r, int g, int b) {
		// Sets the initial position
		posX = x;
		posY = y;
		// Sets the accuracy, damage and size of the laser
		double accuracy = a;
		damage = d;
		size = 2;
		// Calculates the angle between the initial laser position and the
		// target
		double angle = Math.atan2((tX - x), (tY - y));
		// Calculates the random deviation of the laser
		double rand = (Math.random() * accuracy - accuracy / 2);
		double angleSin = Math.sin(angle - Math.toRadians(rand));
		double angleCos = Math.cos(angle - Math.toRadians(rand));
		// Sets the horizontal and vertical components of the laser's speed
		speedX = speed * angleSin;
		speedY = speed * angleCos;
		// Sets the width and height of the rectangle which bounds the laser
		width = size * angleSin;
		height = size * angleCos;
		// Sets the lasers colour
		red = r;
		green = g;
		blue = b;
		// Sets the laser as not owned by the player
		player = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#doDamage(game.Entity)
	 */
	@Override
	public void doDamage(Entity e) {
		// Causes the entity to take damage
		e.takeDamage(damage);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#getDamage()
	 */
	@Override
	public double getDamage() {
		return damage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#checkCollision(double, double, double, double, double,
	 * double, double, double)
	 */
	@Override
	public boolean checkCollision(double x1, double y1, double sX1, double sY1, double x2, double y2, double sX2,
			double sY2) {
		// Creates a hitbox around the laser and enemy and checks for
		// intersection
		Rectangle2D laserHitbox = new Rectangle2D(x1, y1, Math.abs(sX1), Math.abs(sY1));
		Rectangle2D enemyHitBox = new Rectangle2D(x2, y2, Math.abs(sX2), Math.abs(sY2));
		// If the hitboxes intersect eachother, true is returned, else false is
		// returned
		if (enemyHitBox.intersects(laserHitbox)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#move()
	 */
	@Override
	public void move() {
		// Increments the laser's position by the
		posX += speedX;
		posY += speedY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#draw(javafx.scene.canvas.GraphicsContext, double,
	 * double, double, double)
	 */
	@Override
	public void draw(GraphicsContext g, double x, double y, double h, double w) {
		// Draws the laser as a line of width 5 along the diagonal of a
		// rectangle specified by the input
		g.setStroke(Color.rgb(red, green, blue));
		g.setLineWidth(5);
		g.strokeLine(x, y, h, w);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#getX()
	 */
	@Override
	public double getX() {
		return posX;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#getY()
	 */
	@Override
	public double getY() {
		return posY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#getH()
	 */
	@Override
	public double getH() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#getW()
	 */
	@Override
	public double getW() {
		return width;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#setMarked()
	 */
	@Override
	public void setMarked() {
		marked = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#isMarked()
	 */
	@Override
	public boolean isMarked() {
		return marked;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#isPlayer()
	 */
	@Override
	public boolean isPlayer() {
		return player;
	}

}
