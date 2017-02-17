package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ui.Character;

public class MapReader {
	int mapX, mapY;
	ArrayList<Block> spawners = new ArrayList<Block>();

	/**
	 * Returns the width of the map
	 *
	 * @return
	 */
	public int getMapX() {
		return mapX;
	}

	/**
	 * Returns the height of the map
	 *
	 * @return
	 */
	public int getMapY() {
		return mapY;
	}

	/**
	 * Returns a list of all the spawners on the map
	 *
	 * @return
	 */
	public ArrayList<Block> getSpawners() {
		return spawners;
	}

	/**
	 * Returns the map as a 2-dimensional block array and saves a list of the spawner locations
	 *
	 * @param name
	 * @return
	 */
	public Block[][] readFile(String name) {
		Map<String, Block> blockTypes = new HashMap<String, Block>();
		try {
			System.out.println("Loading...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.18:3306/battleground", "root", "root");
			System.out.println("Connected.");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from blocks");
			while (rs.next()) {
				blockTypes.put(rs.getString(2),
						new Block(rs.getInt(3), rs.getBoolean(4), rs.getBlob(5)));

			}
			con.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		Block[][] blockArray;
		int lineNo = 0;
		File file = new File(name + ".txt");

		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);

			String line;

			while ((line = br.readLine()) != null) {
				if (lineNo == 0) {
					mapX = line.length();
				}
				lineNo++;
			}
			mapY = lineNo;
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		blockArray = new Block[mapX][mapY];
		System.out.println(mapX + "," + mapY);

		lineNo = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);

			String line;

			while ((line = br.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					blockArray[i][lineNo] = new Block(blockTypes.get(String.valueOf(line.charAt(i))), i, lineNo);
					blockArray[i][lineNo].setX(i);
					blockArray[i][lineNo].setY(lineNo);

					if (line.charAt(i) == 's') {
						spawners.add(new Block(blockTypes.get(String.valueOf(line.charAt(i))), i, lineNo));
					}
				}
				lineNo++;
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return blockArray;
	}
}
