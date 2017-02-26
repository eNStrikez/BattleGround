package game;

import javafx.scene.canvas.GraphicsContext;

public interface Weapon {

	/**
	 * Do damage to entity
	 *
	 * @param e
	 */
	public void doDamage(Entity e);

	/**
	 * Checks whether the weapon has collided with an entity
	 *
	 * @param e
	 * @param x
	 * @param y
	 * @param sX
	 * @param sY
	 * @return
	 */
	public boolean checkCollision(double x1, double y1, double sX1, double sY1, double x2, double y2, double sX2,
			double sY2);

	/**
	 * Draw the weapon on the screen
	 *
	 * @param g
	 * @param x
	 * @param y
	 * @param h
	 * @param w
	 */
	public void draw(GraphicsContext g, double x, double y, double h, double w);

	/**
	 * Moves the weapon along its path
	 */
	public void move();

	/**
	 * Returns the weapon's x coordinate
	 *
	 * @return
	 */
	public double getX();

	/**
	 * Returns the weapon's y coordinate
	 *
	 * @return
	 */
	public double getY();

	/**
	 * Set the weapon as marked for removal
	 */
	public void setMarked();

	/**
	 * Returns whether the weapon is marked or not
	 *
	 * @return
	 */
	public boolean isMarked();

	/**
	 * Returns whether the weapon is marked or not
	 *
	 * @return
	 */
	public boolean isPlayer();

	/**
	 * Returns the weapon's logical height
	 *
	 * @return
	 */
	public double getH();
	/**
	 * Returns the weapon's logical width
	 *
	 * @return
	 */
	public double getW();

	/**
	 * Returns the weapon's damage value
	 *
	 * @return
	 */
	public double getDamage();

}
