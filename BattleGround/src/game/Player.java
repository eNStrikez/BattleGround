package game;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import ui.Character;

public class Player extends Entity {
	Character character;
	double moveX, moveY;
	double roF;
	double overheatMax;
	double overheatCurrent;
	ImageView iV;
	Image i;

	/**
	 * @param x
	 * @param y
	 * @param sx
	 * @param sy
	 * @param c
	 */
	public Player(double x, double y, double sx, double sy, Character c) {
		setX(x);
		setY(y);
		setSX(sx);
		setSY(sy);
		setHealth(c.getHealth());
		character = c;
		overheatMax = c.getOverheat();
		roF = c.getRoF();
		iV = new ImageView(c.getImage());
	}

	/**
	 * Draws the player on the screen
	 *
	 * @param g
	 * @param x
	 * @param y
	 * @param sizeX
	 * @param sizeY
	 */
	public void draw(GraphicsContext g, double x, double y, double sizeX, double sizeY, double mX, double mY) {
		double angle = 0;

		angle = -Math.atan2(mX - x, mY - y);


		iV.setRotate(Math.toDegrees(angle));

		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		double length = Math.abs(sizeY*Math.sin(angle)) + Math.abs(sizeX*Math.cos(angle));
		double height = Math.abs(sizeY*Math.cos(angle)) + Math.abs(sizeX*Math.sin(angle));
		//System.out.println("Player logical scale: " + sizeX + "/" + sizeY);
		//System.out.println("Player image scale: " + length + "/" + height);
		g.setFill(Color.YELLOW);
		//g.fillRect(x, y, length, height);
		g.drawImage(iV.snapshot(params, null), x, y, length, height);
		g.fillText("" + Math.toDegrees(angle), x, y + height * 1.5);
	}

	/**
	 * Attempts to move the player into a neighbouring block, assuming that it
	 * is not collidable or off the map
	 *
	 * @param map
	 * @param mapX
	 * @param mapY
	 */
	public void move(Block[][] map, double mapX, double mapY) {
		if (posX + moveX < mapX - 1 && posY + moveY < mapY - 1 && posX + moveX >= 0 && posY + moveY >= 0)
			if (!map[(int) (posX + moveX)][(int) (posY + moveY)].isCollidable()) {
				posX += moveX;
				posY += moveY;
			}
	}

	/**
	 * Sets the player's horizontal velocity
	 *
	 * @param mX
	 */
	public void setMoveX(double mX) {
		moveX = mX;
	}

	/**
	 * Sets the player's vertical velocity
	 *
	 * @param mY
	 */
	public void setMoveY(double mY) {
		moveY = mY;
	}

	/**
	 * Returns the player's horizontal velocity
	 *
	 * @return
	 */
	public double getMoveX() {
		return moveX;
	}

	/**
	 * Returns the player's vertical velocity
	 *
	 * @return
	 */
	public double getMoveY() {
		return moveY;
	}

	/**
	 * Returns the player's character
	 *
	 * @return
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * Creates a laser at the player's current position aimed at the mouse's
	 * position
	 *
	 * @param tX
	 * @param tY
	 * @param sizeX
	 * @param sizeY
	 * @return
	 */
	public Laser fire(double tX, double tY) {
		return new Laser(character.getAccuracy(), posX, posY, character.getWeaponDamage(), tX, tY,
				character.getRGB()[0], character.getRGB()[1], character.getRGB()[2], true);
	}

	public Melee melee(double tX, double tY){
		return new Melee(character.getMeleeDamage(), character.getMeleeRange(), posX, posY, tX, tY);
	}

	/**
	 * Reduces the heat of the weapon
	 */
	public void cool() {
		if (overheatCurrent > 0) {
			overheatCurrent -= 60;
		}
	}

	/**
	 * Increases the heat of the weapon
	 */
	public void heat() {
		if (overheatCurrent < overheatMax) {
			overheatCurrent += overheatMax / getRoF();
		}
	}

	/**
	 * Returns the number of times per second that a laser can be generated
	 *
	 * @return
	 */
	public double getRoF() {
		return (60 / roF) * Game.FRAME_RATE;
	}
}
