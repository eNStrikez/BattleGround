package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Laser implements Weapon {
	double posX, posY, sizeX, sizeY, damage;

	public Laser(double x, double y, double sX, double sY){
		posX = x;
		posY = y;
		sizeX = sX;
		sizeY = sY;
	}
	
	@Override
	public void doDamage(Entity e) {
		
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
		// TODO Auto-generated method stub
		return null;
	}
	
	public void move(){
		
	}

}
