package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Melee implements Weapon {

	double posX, posY, damage, speedX, speedY, height, width, range, maxRangeX, maxRangeY;
	double rangeX = 0;
	double rangeY = 0;
	double speed = 0.1;
	double size;
	boolean marked = false;
	boolean player;

	public Melee(double d, double r, double x, double y, double tX, double tY, boolean isP) {
		posX = x;
		posY = y;
		damage = d;
		size = 2;

		double angle = Math.atan2((tX - x) , (tY - y));
		double angleSin = Math.sin(angle);
		double angleCos = Math.cos(angle);

		speedX = speed * angleSin;
		speedY = speed * angleCos;
		maxRangeX = range * angleSin;
		maxRangeY = range * angleCos;

		width = size * angleSin;
		height = size * angleCos;
		player = isP;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#doDamage(game.Entity)
	 */
	@Override
	public void doDamage(Entity e) {
		e.takeDamage(damage);
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
		Rectangle2D meleeHitbox = new Rectangle2D(x1, y1, Math.abs(sX1), Math.abs(sY1));
		Rectangle2D enemyHitBox = new Rectangle2D(x2, y2, Math.abs(sX2), Math.abs(sY2));
		if (enemyHitBox.intersects(meleeHitbox)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#draw(javafx.scene.canvas.GraphicsContext, double,
	 * double, double, double)
	 */
	@Override
	public void draw(GraphicsContext g, double x, double y, double h, double w) {
		g.setStroke(Color.GREY);
		g.setLineWidth(5);
		g.strokeLine(x, y, h, w);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see game.Weapon#move()
	 */
	@Override
	public void move() {
		if (rangeX < maxRangeX && rangeY < maxRangeY) {
			rangeX += speedX;
			rangeY += speedY;
			posX += speedX;
			posY += speedY;
		} else {
			setMarked();
		}
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
	 * @see game.Weapon#getDamage()
	 */
	@Override
	public double getDamage() {
		return damage;
	}

}
