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

	public Character(String n, double h, double sp, double a, double sk, String w, String g, String m, String r, Blob i) {
		name = n;
		try {
			image = new Image(i.getBinaryStream());
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

	public void initWeapon(double damage, double roF, double overheat, double magSize, int r, int g, int b){
		weaponDamage = damage;
		weaponRoF = roF;
		weaponOverheat = overheat;
		weaponMagSize = magSize;
		red = r;
		green = g;
		blue = b;
	}

	public void initMelee(double damage, double range){
		meleeDamage = damage;
		meleeRange = range;
	}

	public void drawPlayer(GraphicsContext g, int sX, int sY) {
		g.clearRect(0, 0, sX, sY);
		g.setFill(Color.AQUAMARINE);
		g.fillRect(0, 0, sX, sY);
		g.drawImage(image, (sX - sY*aspectRatio)/2, 0, sY*aspectRatio, sY);
	}

	public double getSpeed(){
		return speed;
	}

	public double getHealth(){
		return health;
	}
	
	public double getOverheat(){
		return weaponOverheat;
	}
	
	public double getRoF(){
		return weaponRoF;
	}

	public double getAccuracy(){
		return speed;
	}

	public String getWeaponName(){
		return weaponName;
	}

	public String getMeleeName(){
		return meleeName;
	}

	public double getWeaponDamage(){
		return weaponDamage;
	}

	public void drawStats(GraphicsContext g, int sX, int sY) {
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

	public int[] getRGB(){
		return new int[]{
			red, green, blue
		};
	}

}