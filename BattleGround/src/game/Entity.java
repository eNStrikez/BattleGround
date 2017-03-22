package game;

public class Entity {
	double posX; // The x coordinate of the entity
	double posY; // The y coordinate of the entity
	double sizeX; // The width of the entity
	double sizeY; // The height of the entity
	double health; // The health of the entity
	double maxHealth; // The maximum health of the entity
	boolean alive; // Whether the entity's health is more than zero

	public Entity() {
		alive = true;
	}

	/**
	 * Sets the entity's x coordinate
	 *
	 * @param x
	 */
	public void setX(double x) {
		posX = x;
	}

	/**
	 * Sets the entity's y coordinate
	 *
	 * @param y
	 */
	public void setY(double y) {
		posY = y;
	}

	/**
	 * Sets the entity's width
	 *
	 * @param sx
	 */
	public void setSX(double sx) {
		sizeX = sx;
	}

	/**
	 * Sets the entity's height
	 *
	 * @param sy
	 */
	public void setSY(double sy) {
		sizeY = sy;
	}

	/**
	 * Sets the entity's health and max health
	 *
	 * @param h
	 */
	public void setHealth(double h) {
		health = h;
		maxHealth = h;
	}

	/**
	 * Returns the entity's x coordinate
	 *
	 * @return
	 */
	public double getX() {
		return posX;
	}

	/**
	 * Returns the entity's y coordinate
	 *
	 * @return
	 */
	public double getY() {
		return posY;
	}

	/**
	 * Returns the entity's farthest right corner
	 *
	 * @return
	 */
	public double getFarthestX() {
		return posX + sizeX;
	}

	/**
	 * Returns the entity's farthest down corner
	 *
	 * @return
	 */
	public double getFarthestY() {
		return posY + sizeY;
	}

	/**
	 * Returns whether the entity is alive
	 *
	 * @return
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Deals damage to the player by reducing health If health becomes 0 or
	 * less, the entity is set to no longer be alive
	 *
	 * @param d
	 */
	public void takeDamage(double d) {
		health -= d;
		if (health <= 0) {
			alive = false;
		}
	}
}