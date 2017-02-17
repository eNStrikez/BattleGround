package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Laser implements Weapon {
	double posX, posY, damage, speedX, speedY, height, width;
	double speed = 1;
	double size;
	int red, blue, green;
	boolean marked = false;

	/**
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
		posX = x;
		posY = y;

		double accuracy = a;
		damage = d;
		size = 2;

		double angle = Math.atan((tX - x) / (tY - y));
		double rand = (Math.random() * accuracy - accuracy / 2);
		double angleSin = Math.sin(angle - Math.toRadians(rand));
		double angleCos = Math.cos(angle - Math.toRadians(rand));

		speedX = speed * angleSin;
		speedY = speed * angleCos;

		if (tY < y) {
			speedX *= -1;
			speedY *= -1;
		}

		width = size * angleSin;
		height = size * angleCos;
		red = r;
		green = g;
		blue = b;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#doDamage(game.Entity)
	 */
	@Override
	public void doDamage(Entity e) {
		e.takeDamage(damage);
	}
	
	/**
	 * Returns the damage value of the laser
	 * @return
	 */
	public double getDamage(){
		return damage;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#checkCollision(double, double, double, double, double, double, double, double)
	 */
	@Override
	public boolean checkCollision(double x1, double y1, double sX1, double sY1, double x2, double y2, double sX2, double sY2) {
		Rectangle2D laserHitbox = new Rectangle2D(x1, y1, Math.abs(sX1), Math.abs(sY1));
		Rectangle2D enemyHitBox = new Rectangle2D(x2, y2, Math.abs(sX2), Math.abs(sY2));
		if (enemyHitBox.intersects(laserHitbox)) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#getImage()
	 */
	@Override
	public Image getImage() {

		return null;
	}

	/**
	 * Change the lasers position depending on its velocity
	 */
	public void move() {
		posX += speedX;
		posY += speedY;
	}

	/**
	 * Draw the laser on the screen
	 *
	 * @param g
	 * @param x
	 * @param y
	 * @param h
	 * @param w
	 */
	public void draw(GraphicsContext g, double x, double y, double h, double w) {
		g.setStroke(Color.rgb(red, green, blue));
		g.setLineWidth(5);
		g.strokeLine(x, y, h, w);
		//g.fillRect(x, y, Math.abs(h - x), w - y);
	}

	/**
	 * Returns the laser's x coordinate
	 *
	 * @return
	 */
	public double getX() {
		return posX;
	}

	/**
	 * Returns the laser's y coordinate
	 *
	 * @return
	 */
	public double getY() {
		return posY;
	}

	/**
	 * Returns the laser's logical height
	 *
	 * @return
	 */
	public double getH() {
		return height;
	}

	/**
	 * Returns the laser's logical width
	 *
	 * @return
	 */
	public double getW() {
		return width;
	}

	/**
	 * Set the laser as marked for removal
	 */
	public void setMarked() {
		marked = true;
	}

	/**
	 * Returns whether the laser is marked or not
	 *
	 * @return
	 */
	public boolean isMarked() {
		return marked;
	}

}
