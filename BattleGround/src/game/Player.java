package game;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import ui.Character;
import ui.Modifier;

public class Player extends Entity {
	Character character;
	double moveX, moveY;
	double roF;
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
		// Calculates the angle between the player and the cursor
		double angle = -Math.atan2(mX - x, mY - y);
		// Rotates the image according to the angle
		iV.setRotate(Math.toDegrees(angle));
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		// Resizes the image to maintain the image aspect ratio
		double length = Math.abs(sizeY * Math.sin(angle)) + Math.abs(sizeX * Math.cos(angle));
		double height = Math.abs(sizeY * Math.cos(angle)) + Math.abs(sizeX * Math.sin(angle));
		// Draws the player's image
		g.drawImage(iV.snapshot(params, null), x, y, length, height);
		// Draws the player's health bar above the player
		g.setFill(Color.CRIMSON);
		g.fillRect(x, y - sizeY / 4, sizeX, sizeY / 4);
		g.setFill(Color.LIMEGREEN);
		g.fillRect(x, y - sizeY / 4, sizeX * health / maxHealth, sizeY / 4);
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
		// Checks that the player is on the map
		if (posX + moveX < mapX - 2 && posY + moveY < mapY - 2 && posX + moveX >= 0 && posY + moveY >= 0)
			// Checks whether the block the player is attempting to move into is collidable
			if (!map[(int) (posX + moveX)][(int) (posY + moveY)].isCollidable()) {
				// Moves the player according to the player velocity
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
	public Laser fire(double tX, double tY, double x, double y) {
		return new Laser(character.getAccuracy(), x, y, character.getWeaponDamage(), tX, tY, character.getRGB()[0],
				character.getRGB()[1], character.getRGB()[2], character.getSkill());
	}

	/**
	 * Creates a melee at the player's current position aimed at the mouse's
	 * position
	 *
	 * @param tX
	 * @param tY
	 * @param x
	 * @param y
	 * @param sX
	 * @param sY
	 * @return
	 */
	public Melee melee(double tX, double tY, double x, double y) {
		return new Melee(character.getMeleeDamage(), character.getMeleeRange(), x, y, tX, tY, true);
	}

	/**
	 * Changes the player's stats corresponding to their chosen modifier
	 *
	 * @param mod
	 */
	public void modifyStat(Modifier mod) {
		// Gets the stat that the modifier affects
		String stat = mod.getStat();
		// Gets the multiplier of the modifier
		double multiplier = mod.getMultiplier();
		// Alters the stats of the player depending on the modifer chosen
		if (stat.equals("Health"))
			setHealth(health * multiplier);
		else if (stat.equals("Speed"))
			character.setSpeed(character.getSpeed() * multiplier);
		else if (stat.equals("Accuracy"))
			character.setAccuracy(character.getAccuracy() * multiplier);
		else if (stat.equals("Skill"))
			character.setSkill(character.getSkill() * multiplier);
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
