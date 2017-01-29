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

	public void draw(GraphicsContext g){
		g.setFill(Color.ORANGERED);
		g.fillOval(posX * sizeX, posY * sizeY, sizeX, sizeY);
		//g.drawImage(image, posX, posY, sizeX, sizeY);
	}

	public void loadDroid(String name){
		try {
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/battleground", "root", "root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from droids where droids.name = \"" + name + "\"");
			while (rs.next()) {
				name = rs.getString(1);
				setHealth(rs.getDouble(2));
				speed = rs.getDouble(3);
				accuracy = rs.getDouble(4);
				skill = rs.getDouble(5);
				weaponName = rs.getString(6);
				image = new Image(rs.getBlob(7).getBinaryStream());
				meleeName = rs.getString(8);

			}

			rs = stmt.executeQuery("select * from weapons where name = (select weapon from droids where name = \"" + name + "\");");
			while (rs.next()) {
				weaponDamage = rs.getDouble(2);
				weaponRoF = rs.getDouble(3);
				weaponMagSize = rs.getDouble(4);
				weaponOverheat = rs.getDouble(5);
				red = rs.getInt(6);
				blue = rs.getInt(7);
				green = rs.getInt(8);
			}

			rs = stmt.executeQuery("select * from melees where name = (select weapon from droids where name = \"" + name + "\");");
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

}
