package game;

import javafx.geometry.Rectangle2D;

public class Entity {
	double posX, posY, sizeX, sizeY, health;

	public Entity(){

	}

	public void setX(double x){
		posX = x;
	}

	public void setY(double y){
		posY = y;
	}

	public void setSX(double sx){
		sizeX = sx;
	}

	public void setSY(double sy){
		sizeY = sy;
	}

	public void setHealth(double h){
		health = h;
	}

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
