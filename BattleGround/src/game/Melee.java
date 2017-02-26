package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class Melee implements Weapon{

	double damage, range, posX, posY, targetX, targetY;

	public Melee(double d, double r, double x, double y, double tX, double tY){
		damage = d;
		range = r;
		posX = x;
		posY = y;


	}

	/* (non-Javadoc)
	 * @see game.Weapon#doDamage(game.Entity)
	 */
	@Override
	public void doDamage(Entity e) {
		e.takeDamage(damage);
	}

	/* (non-Javadoc)
	 * @see game.Weapon#checkCollision(double, double, double, double, double, double, double, double)
	 */
	@Override
	public boolean checkCollision(double x1, double y1, double sX1, double sY1, double x2, double y2, double sX2,
			double sY2) {
		Rectangle2D meleeHitbox = new Rectangle2D(x1, y1, Math.abs(sX1), Math.abs(sY1));
		Rectangle2D enemyHitBox = new Rectangle2D(x2, y2, Math.abs(sX2), Math.abs(sY2));
		if (enemyHitBox.intersects(meleeHitbox)) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(GraphicsContext g, double x, double y, double h, double w) {
		// TODO Auto-generated method stub

	}

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
