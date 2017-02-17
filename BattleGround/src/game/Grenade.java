package game;

import javafx.scene.image.Image;

public class Grenade implements Weapon{

	/* (non-Javadoc)
	 * @see game.Weapon#doDamage(game.Entity)
	 */
	@Override
	public void doDamage(Entity e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see game.Weapon#getImage()
	 */
	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#checkCollision(double, double, double, double, double, double, double, double)
	 */
	@Override
	public boolean checkCollision(double x1, double y1, double sX1, double sY1, double x2, double y2, double sX2,
			double sY2) {
		// TODO Auto-generated method stub
		return false;
	}



}
