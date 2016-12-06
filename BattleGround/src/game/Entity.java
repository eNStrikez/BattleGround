package game;

import javafx.geometry.Rectangle2D;

public class Entity {
	double posX, posY, sizeX, sizeY, health;

	public Rectangle2D getBounds() {
		return new Rectangle2D((int) posX, (int) posY, (int)sizeX, (int)sizeY);
	}
	
	public void takeDamage(double d){
		health -= d;
		if(health < 0){
			health = 0;
		}
	}
}
