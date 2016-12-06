package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Laser implements Weapon {
	double posX, posY, sizeX, sizeY, damage;

	public Laser(double x, double y, double sX, double sY, double damage){
		posX = x;
		posY = y;
		sizeX = sX;
		sizeY = sY;
	}

	@Override
	public void doDamage(Entity e) {
		e.takeDamage(damage);
	}

	@Override
	public boolean checkCollision(Entity e) {
		if(e.getBounds().intersects(new Rectangle2D(posX,posY,sizeX,sizeY))){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Image getImage() {
		
		return null;
	}

	public void move(){

	}

}
