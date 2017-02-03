package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Laser implements Weapon {
	double posX, posY, sizeX, sizeY, damage, speedX, speedY, height, width;
	double speed = 10;
	double size;
	int red, blue, green;
	boolean marked = false;

	public Laser(double a, double x, double y, double d, double tX, double tY, int r, int g, int b) {
		posX = x;
		posY = y;

		double accuracy = a;
		damage = d;
		size = d;

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

		width = size * angleCos;
		height = size * angleSin;
		red = r;
		green = g;
		blue = b;
	}

	@Override
	public void doDamage(Entity e) {
		e.takeDamage(damage);
	}

	@Override
	public boolean checkCollision(Entity e) {
		if (e.getBounds().intersects(new Rectangle2D(posX, posY, Math.abs(width), Math.abs(height)))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Image getImage() {

		return null;
	}

	public void move() {
		posX += speedX;
		posY += speedY;
	}

	public void draw(GraphicsContext g) {
		g.setStroke(Color.rgb(red, green, blue));
		g.setLineWidth(5);
		g.strokeLine(posX, posY, posX + height, posY + width);
	}

	public double getX(){
		return posX;
	}

	public double getY(){
		return posY;
	}

	public void setMarked(){
		marked = true;
	}

	public boolean isMarked(){
		return marked;
	}

}
