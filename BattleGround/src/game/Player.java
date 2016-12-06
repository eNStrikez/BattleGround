package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.Character;

public class Player extends Entity {
	double posX, posY;
	Character character;
	int moveX, moveY;

	public Player(double x, double y, Character c) {
		posX = x;
		posY = y;
		character = c;
	}

	public void draw(GraphicsContext g, double sizeX, double sizeY) {
		g.setFill(Color.DODGERBLUE);
		g.fillOval(posX * sizeX, posY * sizeY, sizeX, sizeY);
	}

	public void move() {
		posX += moveX*character.getSpeed()/100;
		posY += moveY*character.getSpeed()/100;
	}

	public double getX() {
		return posX;
	}

	public double getY() {
		return posY;
	}
	
	public void setMoveX(int mX){
		moveX = mX;
	}
	
	public void setMoveY(int mY){
		moveY = mY;
	}
	
	public Character getCharacter(){
		return character;
	}
	
	public void fire(){
		
	}
}
