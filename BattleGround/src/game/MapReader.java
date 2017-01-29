package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapReader {
	int mapX, mapY;
	ArrayList<Block> spawners = new ArrayList<Block>();

	public int getMapX(){
		return mapX;
	}

	public int getMapY(){
		return mapY;
	}

	public ArrayList<Block> getSpawners(){
		return spawners;
	}

	public Block[][] readFile(String name) {
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

		lineNo = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);

			String line;

			while ((line = br.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					blockArray[i][lineNo] = new Block(i, lineNo, line.charAt(i));
					if(blockArray[i][lineNo].type == 's'){
						spawners.add(blockArray[i][lineNo]);
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
