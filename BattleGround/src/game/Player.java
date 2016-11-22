package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.Character;

public class Player extends Entity {
	double posX, posY;
	Character character;

	public Player(double x, double y, Character c) {
		posX = x;
		posY = y;
		character = c;
	}

	public void draw(GraphicsContext g, double sizeX, double sizeY) {
		g.setFill(Color.DODGERBLUE);
		g.fillOval(posX * sizeX, posY * sizeY, sizeX, sizeY);
	}

	public void move(double x, double y) {
		posX += x;
		posY += y;
	}

	public double getX() {
		return posX;
	}

	public double getY() {
		return posY;
	}
}
