package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Laser implements Weapon {
	double posX, posY, damage, speedX, speedY, height, width;
	double speed = 1;
	double size;
	int red, blue, green;
	boolean marked = false;
	boolean player;

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
	public Laser(double a, double x, double y, double d, double tX, double tY, int r, int g, int b, double skill) {
		posX = x;
		posY = y;

		double accuracy = a;
		damage = d;
		size = 2;

		double angle = Math.atan2((tX - x) , (tY - y));
		double rand = (Math.random() * accuracy - accuracy / 2);
		double angleSin = Math.sin(angle - Math.toRadians(rand));
		double angleCos = Math.cos(angle - Math.toRadians(rand));

		speedX = speed * angleSin;
		speedY = speed * angleCos;
		width = size * angleSin;
		height = size * angleCos;
		red = r;
		green = g;
		blue = b;
		if(Math.random()*100 < skill){
			damage = damage*10*skill;
			red = 255;
			green = 226;
			blue = 140;
		}

		player = true;
	}

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

		double angle = Math.atan2((tX - x) , (tY - y));
		double rand = (Math.random() * accuracy - accuracy / 2);
		double angleSin = Math.sin(angle - Math.toRadians(rand));
		double angleCos = Math.cos(angle - Math.toRadians(rand));

		speedX = speed * angleSin;
		speedY = speed * angleCos;
		width = size * angleSin;
		height = size * angleCos;
		red = r;
		green = g;
		blue = b;
		player = false;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#doDamage(game.Entity)
	 */
	@Override
	public void doDamage(Entity e) {
		e.takeDamage(damage);
	}

	/* (non-Javadoc)
	 * @see game.Weapon#getDamage()
	 */
	@Override
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
	 * @see game.Weapon#move()
	 */
	@Override
	public void move() {
		posX += speedX;
		posY += speedY;
	}


	/* (non-Javadoc)
	 * @see game.Weapon#draw(javafx.scene.canvas.GraphicsContext, double, double, double, double)
	 */
	@Override
	public void draw(GraphicsContext g, double x, double y, double h, double w) {
		g.setStroke(Color.rgb(red, green, blue));
		g.setLineWidth(5);
		g.strokeLine(x, y, h, w);
	}

	/* (non-Javadoc)
	 * @see game.Weapon#getX()
	 */
	@Override
	public double getX() {
		return posX;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#getY()
	 */
	@Override
	public double getY() {
		return posY;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#getH()
	 */
	@Override
	public double getH() {
		return height;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#getW()
	 */
	@Override
	public double getW() {
		return width;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#setMarked()
	 */
	@Override
	public void setMarked() {
		marked = true;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#isMarked()
	 */
	@Override
	public boolean isMarked() {
		return marked;
	}

	/* (non-Javadoc)
	 * @see game.Weapon#isPlayer()
	 */
	@Override
	public boolean isPlayer() {
		return player;
	}

}
