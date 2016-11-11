package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Block {
	int posX;
	int posY;
	boolean collidable = false;
	double h = 0;
	double g = 0;
	Block precursor;
	char type;
	int moveCost;
	boolean isMarked = false;
	boolean isScanned = false;

	public Block(int x, int y, char t){
		posX = x;
		posY = y;
		type = t;

		if(t == 'g'){
			moveCost = 1;
		} else if (t == 'w'){
			moveCost = 3;
		} else if (t == 'm'){
			moveCost = 100;
		}
	}

	public int getX(){
		return posX;
	}

	public int getY(){
		return posY;
	}

	public void setG(double a){
		g = a;
	}

	public void setH(double a){
		h = a;
	}

	public double getF(){
		return h + g;
	}

	public double getG(){
		return g;
	}

	public void setPrecursor(Block p){
		precursor = p;
	}

	public Block getPrecursor(){
		return precursor;
	}

	public void draw(GraphicsContext g, int posX, int posY, int sizeX, int sizeY) {
		if(type == 'g'){
			g.setFill(Color.FORESTGREEN);
		} else if (type == 'w'){
			g.setFill(Color.AQUA);
		} else if (type == 'm'){
			g.setFill(Color.GRAY);
		}
		if(isMarked){
			g.setFill(Color.GOLDENROD);
		} else if(isScanned){
			g.setFill(Color.AZURE);
		}

		g.fillRect(posX, posY, sizeX, sizeY);

	}

	public int getCost(){
		return moveCost;
	}

	public void setMarked(boolean isM){
		isMarked = isM;
	}
	
	public void setScanned(boolean isS){
		isScanned = isS;
	}


}