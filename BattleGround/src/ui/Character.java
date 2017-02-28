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
	public Character(String n, double h, double sp, double a, double sk, String w, String g, String m, String r, Blob i1, Blob i2) {
		name = n;
		try {
			image = new Image(i1.getBinaryStream());
			if(i2 != null){
				gImage  = new Image(i2.getBinaryStream());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		aspectRatio = image.getWidth()/image.getHeight();
		health = h;
		speed = sp;
		accuracy = a;
		skill = sk;
		weaponName = w;
		grenadeName = g;
		meleeName = m;
		rank = r;
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
	public void initWeapon(double damage, double roF, double overheat, double magSize, int r, int g, int b){
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
	public void initMelee(double damage, double range){
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
		g.clearRect(0, 0, sX, sY);
		g.setFill(Color.AQUAMARINE);
		g.fillRect(0, 0, sX, sY);
		g.drawImage(image, (sX - sY*aspectRatio)/2, 0, sY*aspectRatio, sY);
	}

	/**
	 * Return the name of the player
	 *
	 * @return
	 */
	public String getName(){
		return name;
	}

	/**
	 * Return the speed of the player
	 *
	 * @return
	 */
	public double getSpeed(){
		return speed;
	}

	/**
	 * Returns the health of the player
	 *
	 * @return
	 */
	public double getHealth(){
		return health;
	}

	/**
	 * Returns the overheat time of the player's weapon
	 *
	 * @return
	 */
	public double getOverheat(){
		return weaponOverheat;
	}

	/**
	 * Returns the rate of fire in rounds per minute of the player's weapon
	 *
	 * @return
	 */
	public double getRoF(){
		return weaponRoF;
	}

	/**
	 * Returns the accuracy of the player
	 *
	 * @return
	 */
	public double getAccuracy(){
		return accuracy;
	}

	/**
	 * Returns the weapon name
	 *
	 * @return
	 */
	public String getWeaponName(){
		return weaponName;
	}

	/**
	 * Returns the melee name
	 *
	 * @return
	 */
	public String getMeleeName(){
		return meleeName;
	}

	/**
	 * Returns the melee damage
	 *
	 * @return
	 */
	public double getMeleeDamage(){
		return meleeDamage;
	}

	/**
	 * Returns the melee range
	 *
	 * @return
	 */
	public double getMeleeRange(){
		return meleeRange;
	}

	/**
	 * Returns the weapon damage
	 *
	 * @return
	 */
	public double getWeaponDamage(){
		return weaponDamage;
	}

	/**
	 * Returns the in-game image of the character
	 *
	 * @return
	 */
	public Image getImage(){
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
		g.clearRect(0, 0, sX, sY);
		g.setFill(new Color(0.8, 0.8, 0.8, 1));

		// name
		g.setFont(new Font("Consolas", 30));
		if (!name.equals("Default")) {
			g.fillText(rank + " " + name.toUpperCase(), sX / 8, sY / 8);
		} else {
			g.fillText("CT-" + (int) (Math.random() * 9999), sX / 8, sY / 8, sX);
		}
		g.setFont(new Font("Consolas", 16));

		// health
		g.fillText("Health", sX / 8, sY / 4 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4, sX / 2, sY / 32);

		// speed
		g.fillText("Speed", sX / 8, sY / 4 + sY / 16 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4 + sY / 16, sX / 2, sY / 32);

		// accuracy
		g.fillText("Accuracy", sX / 8, sY / 4 + sY / 8 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4 + sY / 8, sX / 2, sY / 32);

		// skill
		g.fillText("Skill", sX / 8, sY / 4 + 3 * sY / 16 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4 + 3 * sY / 16, sX / 2, sY / 32);
		g.fillText("Weapon", sX / 8, sY / 4 + 4 * sY / 16 + sY / 32);
		g.fillText("Grenade", sX / 8, sY / 4 + 5 * sY / 16 + sY / 32);
		g.fillText("Melee", sX / 8, sY / 4 + 6 * sY / 16 + sY / 32);

		g.setFill(Color.MEDIUMAQUAMARINE);
		g.fillRect(sX / 3, sY / 4, (health / 250) * sX / 2, (sY / 32));
		g.fillRect(sX / 3, sY / 4 + sY / 16, (speed / 40) * sX / 2, (sY / 32));
		g.fillRect(sX / 3, sY / 4 + sY / 8, accuracy * sX / 2, (sY / 32));
		g.fillRect(sX / 3, sY / 4 + 3 * sY / 16, (skill / 5) * sX / 2, (sY / 32));
		g.fillText(weaponName, sX / 3, sY / 4 + 4 * sY / 16 + sY / 32);
		g.fillText(grenadeName, sX / 3, sY / 4 + 5 * sY / 16 + sY / 32);
		g.fillText(meleeName, sX / 3, sY / 4 + 6 * sY / 16 + sY / 32);
	}

	/**
	 * Return the weapons colour as an array of [red, green, blue]
	 *
	 * @return
	 */
	public int[] getRGB(){
		return new int[]{
			red, green, blue
		};
	}

}