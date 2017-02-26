package game;

import javafx.scene.canvas.GraphicsContext;
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
	 * @see game.Weapon#checkCollision(double, double, double, double, double, double, double, double)
	 */
	@Override
	public boolean checkCollision(double x1, double y1, double sX1, double sY1, double x2, double y2, double sX2,
			double sY2) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#draw(javafx.scene.canvas.GraphicsContext, double, double, double, double)
	 */
	@Override
	public void draw(GraphicsContext g, double x, double y, double h, double w) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see game.Weapon#move()
	 */
	@Override
	public void move() {
		// TODO Auto-generated method stub

	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMarked() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMarked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlayer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getH() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getW() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}



}
