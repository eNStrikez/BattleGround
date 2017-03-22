package ui;

import java.sql.Blob;
import java.sql.SQLException;

import game.Laser;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Character {
	String name;
	Image image;
	Image gImage;
	double aspectRatio;
	double health;
	double speed;
	double accuracy;
	double skill;
	double weaponDamage;
	double weaponRoF;
	double weaponOverheat;
	double weaponMagSize;
	double meleeDamage;
	double meleeRange;
	int unlock;
	int playerScore;
	int red, blue, green;
	String weaponName;
	String grenadeName;
	String meleeName;
	String rank;

	/**
	 * @param n
	 * @param h
	 * @param sp
	 * @param a
	 * @param sk
	 * @param w
	 * @param g
	 * @param m
	 * @param r
	 * @param i
	 */
	public Character(String n, double h, double sp, double a, double sk, String w, String g, String m, String r,
			Blob i1, Blob i2, int u, int pS) {
		name = n;
		try {
			// If the image inputs exist, they are loaded from the
			// blob (Binary Large OBject) in the database
			if (i1 != null)
				image = new Image(i1.getBinaryStream());
			if (i2 != null)
				gImage = new Image(i2.getBinaryStream());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Sets the aspect ratio of the image so the rescaling reduce quality
		aspectRatio = image.getWidth() / image.getHeight();
		health = h;
		setSpeed(sp);
		setAccuracy(a);
		setSkill(sk);
		weaponName = w;
		grenadeName = g;
		meleeName = m;
		rank = r;
		unlock = u;
		playerScore = pS;
	}

	/**
	 * Initialises the weapons stats of the character
	 *
	 * @param damage
	 * @param roF
	 * @param overheat
	 * @param magSize
	 * @param r
	 * @param g
	 * @param b
	 */
	public void initWeapon(double damage, double roF, double overheat, double magSize, int r, int g, int b) {
		weaponDamage = damage;
		weaponRoF = roF;
		weaponOverheat = overheat;
		weaponMagSize = magSize;
		red = r;
		green = g;
		blue = b;
	}

	/**
	 * Initialises the melee stats of the character
	 *
	 * @param damage
	 * @param range
	 */
	public void initMelee(double damage, double range) {
		meleeDamage = damage;
		meleeRange = range;
	}

	/**
	 * Draws the player's image
	 *
	 * @param g
	 * @param sX
	 * @param sY
	 */
	public void drawPlayer(GraphicsContext g, double sX, double sY) {
		// Clears the previous drawing(s)
		g.clearRect(0, 0, sX, sY);
		// Draws a background for the image
		g.setFill(Color.AQUAMARINE);
		g.fillRect(0, 0, sX, sY);
		// If the player has a high enough score to unlock the selected
		// character, draws the player image, else a grey rectangle is painted
		// over it to show the player does not have access
		if (playerScore >= unlock) {
			g.drawImage(image, (sX - sY * aspectRatio) / 2, 0, sY * aspectRatio, sY);
		} else {
			g.setFill(Color.GRAY);
			g.fillRect((sX - sY * aspectRatio) / 2, 0, sY * aspectRatio, sY);
		}
	}

	/**
	 * Return the name of the player
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the speed of the player
	 *
	 * @return
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Return the skill of the player
	 *
	 * @return
	 */
	public double getSkill() {
		return skill;
	}

	/**
	 * Returns the health of the player
	 *
	 * @return
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * Returns the overheat time of the player's weapon
	 *
	 * @return
	 */
	public double getOverheat() {
		return weaponOverheat;
	}

	/**
	 * Returns the rate of fire in rounds per minute of the player's weapon
	 *
	 * @return
	 */
	public double getRoF() {
		return weaponRoF;
	}

	/**
	 * Returns the accuracy of the player
	 *
	 * @return
	 */
	public double getAccuracy() {
		return accuracy;
	}

	/**
	 * Returns the weapon name
	 *
	 * @return
	 */
	public String getWeaponName() {
		return weaponName;
	}

	/**
	 * Returns the melee name
	 *
	 * @return
	 */
	public String getMeleeName() {
		return meleeName;
	}

	/**
	 * Returns the melee damage
	 *
	 * @return
	 */
	public double getMeleeDamage() {
		return meleeDamage;
	}

	/**
	 * Returns the melee range
	 *
	 * @return
	 */
	public double getMeleeRange() {
		return meleeRange;
	}

	/**
	 * Returns the weapon damage
	 *
	 * @return
	 */
	public double getWeaponDamage() {
		return weaponDamage;
	}

	/**
	 * Returns the in-game image of the character
	 *
	 * @return
	 */
	public Image getImage() {
		return gImage;
	}

	/**
	 * Draws the player's stat bars
	 *
	 * @param g
	 * @param sX
	 * @param sY
	 */
	public void drawStats(GraphicsContext g, double sX, double sY) {
		// Clears the previous drawing(s)
		g.clearRect(0, 0, sX, sY);
		g.setFill(new Color(0.8, 0.8, 0.8, 1));

		// Draws the player name
		g.setFont(new Font("Consolas", 20));
		// If the player has unlocked the selected character, the name is drawn,
		// otherwise the required score is shown with the player's current
		// highest score
		if (playerScore >= unlock) {
			if (!name.equals("Default")) {
				// Draws the name for the character as the rank followed by the
				// name in capitals
				g.fillText(rank + " " + name.toUpperCase(), sX / 8, sY / 8);
			} else {
				// Draws the default character's name as "CT" followed by a
				// random number up to 9999
				g.fillText("CT-" + (int) (Math.random() * 9999), sX / 8, sY / 8, sX);
			}
		} else {
			g.fillText("REQUIRES CLEARANCE LEVEL: " + unlock, sX / 8, sY / 8);
			g.fillText("CURRENT CLEARANCE LEVEL: " + playerScore, sX / 8, sY / 8 + sY / 16);
		}
		g.setFont(new Font("Consolas", 16));

		// Draws the labels and bars for the stats
		g.fillText("Health", sX / 8, sY / 4 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4, sX / 2, sY / 32);
		g.fillText("Speed", sX / 8, sY / 4 + sY / 16 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4 + sY / 16, sX / 2, sY / 32);
		g.fillText("Accuracy", sX / 8, sY / 4 + sY / 8 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4 + sY / 8, sX / 2, sY / 32);
		g.fillText("Skill", sX / 8, sY / 4 + 3 * sY / 16 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4 + 3 * sY / 16, sX / 2, sY / 32);
		// Draws the labels for the weapons
		g.fillText("Weapon", sX / 8, sY / 4 + 4 * sY / 16 + sY / 32);
		g.fillText("Grenade", sX / 8, sY / 4 + 5 * sY / 16 + sY / 32);
		g.fillText("Melee", sX / 8, sY / 4 + 6 * sY / 16 + sY / 32);
		// If the player has unlocked the character, fills in their stats bars
		// and weapons, otherwise it leaves the stat bars blank and censors the
		// weapons
		if (playerScore >= unlock) {
			g.setFill(Color.MEDIUMAQUAMARINE);
			g.fillRect(sX / 3, sY / 4, (health / 250) * sX / 2, (sY / 32));
			g.fillRect(sX / 3, sY / 4 + sY / 16, (getSpeed() / 10) * sX / 2, (sY / 32));
			g.fillRect(sX / 3, sY / 4 + sY / 8, getAccuracy() * sX / 2, (sY / 32));
			g.fillRect(sX / 3, sY / 4 + 3 * sY / 16, (getSkill() / 5) * sX / 2, (sY / 32));
			g.fillText(weaponName, sX / 3, sY / 4 + 4 * sY / 16 + sY / 32);
			g.fillText(grenadeName, sX / 3, sY / 4 + 5 * sY / 16 + sY / 32);
			g.fillText(meleeName, sX / 3, sY / 4 + 6 * sY / 16 + sY / 32);
		} else {
			g.fillText("CLASSIFIED", sX / 3, sY / 4 + 4 * sY / 16 + sY / 32);
			g.fillText("CLASSIFIED", sX / 3, sY / 4 + 5 * sY / 16 + sY / 32);
			g.fillText("CLASSIFIED", sX / 3, sY / 4 + 6 * sY / 16 + sY / 32);
		}
	}

	/**
	 * Return the weapons colour as an array of [red, green, blue]
	 *
	 * @return
	 */
	public int[] getRGB() {
		return new int[] { red, green, blue };
	}

	/**
	 * Returns whether the player's highest score is greater than the required
	 * unlock score
	 *
	 * @return
	 */
	public boolean accessible() {
		if (playerScore >= unlock) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the player's speed
	 *
	 * @param s
	 */
	public void setSpeed(double s) {
		speed = s;
	}

	/**
	 * Sets the player's accuracy
	 *
	 * @param a
	 */
	public void setAccuracy(double a) {
		accuracy = a;
	}

	/**
	 * Sets the player's skill
	 *
	 * @param s
	 */
	public void setSkill(double s) {
		skill = s;
	}

}