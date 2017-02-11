package game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Droid extends Entity {

	Image image;
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
	String meleeName;

	public Droid(String name, double sX, double sY) {
		loadDroid(name);
		setSX(sX);
		setSY(sY);
	}

	public Droid(Droid d) {
		image = d.image;
		speed = d.speed;
		accuracy = d.accuracy;
		skill = d.skill;
		weaponDamage = d.weaponDamage;
		weaponRoF = d.weaponRoF;
		weaponOverheat = d.weaponOverheat;
		weaponMagSize = d.weaponMagSize;
		meleeDamage = d.meleeDamage;
		meleeRange = d.meleeRange;
		red = d.red;
		blue = d.blue;
		green = d.green;
		weaponName = d.weaponName;
		meleeName = d.meleeName;
		sizeX = d.sizeX;
		sizeY = d.sizeY;
		setHealth(d.health);
	}

	public void draw(GraphicsContext g, double x, double y) {
		if(health < maxHealth){
			g.setFill(Color.CRIMSON);
			g.fillRect(x, y - sizeY/4, sizeX, sizeY/4);
			g.setFill(Color.LIMEGREEN);
			g.fillRect(x, y - sizeY/4, sizeX * health/maxHealth, sizeY/4);
		}
		g.setFill(Color.ORANGERED);
		g.fillOval(x, y, sizeX, sizeY);
		// g.drawImage(image, posX, posY, sizeX, sizeY);
	}

	public void loadDroid(String name) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/battleground", "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from droids where droids.name = \"" + name + "\"");
			while (rs.next()) {
				name = rs.getString(1);
				setHealth(rs.getDouble(2));
				speed = rs.getDouble(3);
				accuracy = rs.getDouble(4);
				skill = rs.getDouble(5);
				weaponName = rs.getString(6);
				// image = new Image(rs.getBlob(7).getBinaryStream());
				meleeName = rs.getString(8);

			}

			rs = stmt.executeQuery(
					"select * from weapons where name = (select weapon from droids where name = \"" + name + "\");");
			while (rs.next()) {
				weaponDamage = rs.getDouble(2);
				weaponRoF = rs.getDouble(3);
				weaponMagSize = rs.getDouble(4);
				weaponOverheat = rs.getDouble(5);
				red = rs.getInt(6);
				blue = rs.getInt(7);
				green = rs.getInt(8);
			}

			rs = stmt.executeQuery(
					"select * from melees where name = (select weapon from droids where name = \"" + name + "\");");
			while (rs.next()) {
				meleeDamage = rs.getDouble(2);
				meleeRange = rs.getDouble(3);
			}
			con.close();

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
	}

	public void move(Block[][] map, double mapX, double mapY) {
		int rand = (int) (Math.random() * 4);
		switch (rand) {
		case 0:
			if (posY - 1 >= 0)
				if (!map[(int) posX][(int) (posY - 1)].collidable)
					posY -= 1;
			break;
		case 1:
			if (posY < mapY + 2)
				if (!map[(int) posX][(int) (posY + 1)].collidable)
					posY += 1;
			break;
		case 2:
			if (posX - 1 >= 0)
				if (!map[(int) (posX - 1)][(int) posY].collidable)
					posX -= 1;
			break;
		case 3:
			if (posX < mapX + 2)
				if (!map[(int) (posX + 1)][(int) posY].collidable)
					posX += 1;
			break;
		}
	}
}
