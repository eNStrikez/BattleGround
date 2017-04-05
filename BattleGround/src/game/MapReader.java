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
import ui.Main;

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
	 * Returns the map as a 2-dimensional block array and saves a list of the
	 * spawner locations
	 *
	 * @param name
	 * @return
	 */
	public Block[][] readFile(String name) {
		// Creates a hash map used to map the blocks' names to each type of block
		Map<String, Block> blockTypes = new HashMap<String, Block>();
		try {
			// Connects to the database
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.IP + ":3306/battleground", "root",
					"root");
			Statement stmt = con.createStatement();
			// Selects all of the blocks
			ResultSet rs = stmt.executeQuery("select * from blocks");
			while (rs.next()) {
				// Adds each block to the hash map, using the name as an index
				blockTypes.put(rs.getString(2), new Block(rs.getInt(3), rs.getBoolean(5), rs.getBlob(4)));
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(1);
		}

		Block[][] blockArray;
		int lineNo = 0;
		// Finds the file in the same directory as the executable for the code with the name map.txt
		File file = new File(name + ".txt");

		try {
			// Creates a reader to read the file
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line;
			// Loops through the file until it finds an empty line
			while ((line = br.readLine()) != null) {
				if (lineNo == 0) {
					// Sets the map's width as the length of the line
					mapX = line.length();
				}
				lineNo++;
			}
			// Sets the map's height as the number of lines in the file
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
			// Loops through each character
			while ((line = br.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					// Sets the block in the map array based on the corresponding character in the text file
					blockArray[i][lineNo] = new Block(blockTypes.get(String.valueOf(line.charAt(i))), i, lineNo);
					// Sets the x and y positions of the block
					blockArray[i][lineNo].setX(i);
					blockArray[i][lineNo].setY(lineNo);
					// If the block is a spawner, it is added to the list of spawners
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
