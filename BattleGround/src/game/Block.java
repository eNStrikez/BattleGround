package game;

import java.sql.Blob;
import java.sql.SQLException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Block {
	int posX;
	int posY;
	boolean collidable;;
	double h = 0;
	double g = 0;
	Block precursor;
	String type;
	int moveCost;
	Image image;

	public Block(int mC, boolean c, Blob i) {
		moveCost = mC;
		collidable = c;

		try {
			image = new Image(i.getBinaryStream());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Block(Block b, int x, int y){
		posX = x;
		posY = y;
		collidable = b.isCollidable();
		image = b.getImage();
		moveCost = b.getCost();
	}

	public boolean isCollidable() {
		return collidable;
	}

	public int getX() {
		return posX;
	}

	public int getY() {
		return posY;
	}

	public void setX(int x) {
		posX = x;
	}

	public void setY(int y) {
		posY = y;
	}

	public void setG(double a) {
		g = a;
	}

	public void setH(double a) {
		h = a;
	}

	public Image getImage(){
		return image;
	}

	public double getF() {
		return h + g;
	}

	public double getG() {
		return g;
	}

	public void setPrecursor(Block p) {
		precursor = p;
	}

	public Block getPrecursor() {
		return precursor;
	}

	public Rectangle getBounds(double x, double y, double sizeX, double sizeY) {
		return new Rectangle(x * sizeX, y * sizeY, sizeX, sizeY);
	}

	public void draw(GraphicsContext g, double x, double y, double sizeX, double sizeY) {
		g.drawImage(image, x, y, sizeX, sizeY);
	}

	public int getCost() {
		return moveCost;
	}

}
