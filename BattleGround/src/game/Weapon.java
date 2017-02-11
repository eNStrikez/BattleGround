package game;

import javafx.scene.image.Image;

public interface Weapon {
	/**
	 * Do damage to entity
	 *
	 * @param e
	 */
	public void doDamage(Entity e);

	/**
	 * Returns the image of the weapon
	 *
	 * @return
	 */
	public Image getImage();

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
	public boolean checkCollision(double x1, double y1, double sX1, double sY1, double x2, double y2, double sX2, double sY2);
}
