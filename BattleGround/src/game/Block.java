package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {
	int posX;
	int posY;
	boolean collidable = false;
	double h = 0;
	double g = 0;
	Block precursor;
	char type;
	int moveCost;

	public Block(int x, int y, char t){
		posX = x;
		posY = y;
		type = t;

		if(t == 'g'){
			moveCost = 1;
		} else if (t == 'w'){
			moveCost = 3;
		} else if (t == 'm'){
			collidable = true;
		}
	}

	public boolean isCollidable(){
		return collidable;
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

	public Rectangle getBounds(int scaleX, int scaleY) {
		return new Rectangle(posX* scaleX, posY*scaleY, scaleX, scaleY);
	}

	public void draw(GraphicsContext g, int sizeX, int sizeY) {
		if(type == 'g'){
			g.setFill(Color.FORESTGREEN);
		} else if (type == 'w'){
			g.setFill(Color.AQUA);
		} else if (type == 'm'){
			g.setFill(Color.GRAY);
		} else if (type == 's'){
			g.setFill(Color.WHITESMOKE);
		}

		g.fillRect(posX*sizeX, posY*sizeY, sizeX, sizeY);

	}

	public int getCost(){
		return moveCost;
	}


}
