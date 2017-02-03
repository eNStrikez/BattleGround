package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.Character;

public class Player extends Entity {
	Character character;
	int moveX, moveY;
	double roF;
	double overheatMax;
	double overheatCurrent;

	public Player(double x, double y, double sx, double sy, Character c) {
		setX(x);
		setY(y);
		setSX(sx);
		setSY(sy);
		setHealth(c.getHealth());
		character = c;
		overheatMax = c.getOverheat();
		roF = c.getRoF();
	}

	public void draw(GraphicsContext g, int sizeX, int sizeY) {
		g.setFill(Color.DODGERBLUE);
		g.fillOval(posX * sizeX, posY * sizeY, sizeX, sizeY);
	}

	public void move(Block[][] map, int mapX, int mapY) {
		if(posX + moveX < mapX && posY + moveY < mapY && posX + moveX >= 0 && posY + moveY >= 0)
		if(!map[(int) (posX + moveX)][(int) (posY + moveY)].isCollidable()){
			posX += moveX;
			posY += moveY;
		}
	}

	public double getX() {
		return posX;
	}

	public double getY() {
		return posY;
	}

	public void setMoveX(int mX) {
		moveX = mX;
	}

	public void setMoveY(int mY) {
		moveY = mY;
	}

	public Character getCharacter() {
		return character;
	}

	public Laser fire(double d, double e, int sizeX, int sizeY) {
		return new Laser(character.getAccuracy(), posX * sizeX, posY * sizeY, character.getWeaponDamage(), d, e,
				character.getRGB()[0], character.getRGB()[1], character.getRGB()[2]);
	}
	
	public void cool(){
		if(overheatCurrent > 0){
			overheatCurrent--;
		}
	}
	
	public void heat(){
		if(overheatCurrent < overheatMax){
			overheatCurrent++;
		} 
	}
	
	public double getRoF(){
		return (60/roF) * Game.FRAME_RATE;
	}
}
