package game;

import javafx.geometry.Rectangle2D;

public class Entity {
	double posX, posY, sizeX, sizeY, health, maxHealth;
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
		maxHealth = h;
	}

	public double getX() {
		return posX;
	}

	public double getY() {
		return posY;
	}

	public double getSizeX() {
		return posX + sizeX;
	}

	public double getSizeY() {
		return posY + sizeY;
	}


	public boolean isAlive() {
		return alive;
	}

	public void takeDamage(double d){
		health -= d;
		if(health < 0){
			alive = false;
		}
	}
}