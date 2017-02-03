package game;

import javafx.geometry.Rectangle2D;

public class Entity {
	double posX, posY, sizeX, sizeY, health;
	boolean alive;

	public Entity(){
		alive = true;
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

	public boolean isAlive() {
		return alive;
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D((int) posX*sizeX, (int) posY*sizeY, (int)sizeX, (int)sizeY);
	}

	public void takeDamage(double d){
		health -= d;
		if(health < 0){
			alive = false;
		}
	}
}