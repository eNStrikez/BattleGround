package game;

import java.sql.Blob;
import java.sql.SQLException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Block{
	int posX;
	int posY;
	boolean collidable;;
	double h = 0;
	double g = 0;
	Block precursor;
	String type;
	int moveCost;
	Image image;

	/**
	 * @param mC
	 * @param c
	 * @param i
	 */
	public Block(int mC, boolean c, Blob i) {
		moveCost = mC;
		collidable = c;

		try {
			image = new Image(i.getBinaryStream());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param b
	 * @param x
	 * @param y
	 */
	public Block(Block b, int x, int y){
		posX = x;
		posY = y;
		collidable = b.isCollidable();
		image = b.getImage();
		moveCost = b.getCost();
	}

	/**
	 * Returns whether the block is collidable
	 *
	 * @return
	 */
	public boolean isCollidable() {
		return collidable;
	}

	/**
	 * Returns the x coordinate of the block
	 *
	 * @return
	 */
	public int getX() {
		return posX;
	}


	/**
	 * Returns the y coordinate of the block
	 *
	 * @return
	 */
	public int getY() {
		return posY;
	}

	/**
	 * Sets the x coordinate of the block
	 *
	 * @param x
	 */
	public void setX(int x) {
		posX = x;
	}


	/**
	 * Sets the y coordinate of the block
	 *
	 * @param y
	 */
	public void setY(int y) {
		posY = y;
	}

	/**
	 * Sets the g value of the block (its current cost)
	 *
	 * @param a
	 */
	public void setG(double a) {
		g = a;
	}

	/**
	 * Sets the h value of the block (its heuristic distance)
	 *
	 * @param a
	 */
	public void setH(double a) {
		h = a;
	}

	/**
	 * Returns the image of the block
	 *
	 * @return
	 */
	public Image getImage(){
		return image;
	}

	/**
	 * Returns the f value of the block where f = g + h
	 *
	 * @return
	 */
	public double getF() {
		return h + g;
	}

	/**
	 * Returns the g value of the block (its current cost)
	 *
	 * @return
	 */
	public double getG() {
		return g;
	}

	/**
	 * Sets the precursor to the current block
	 *
	 * @param p
	 */
	public void setPrecursor(Block p) {
		precursor = p;
	}

	/**
	 * Returns the precursor to the current block
	 *
	 * @return
	 */
	public Block getPrecursor() {
		return precursor;
	}

	/**
	 * Returns a rectangle of the specified coordinates
	 *
	 * @param x
	 * @param y
	 * @param sizeX
	 * @param sizeY
	 * @return
	 */
	public Rectangle getBounds(double x, double y, double sizeX, double sizeY) {
		return new Rectangle(x * sizeX, y * sizeY, sizeX, sizeY);
	}

	/**
	 * Draws the image of the block
	 *
	 * @param g
	 * @param x
	 * @param y
	 * @param sizeX
	 * @param sizeY
	 */
	public void draw(GraphicsContext g, double x, double y, double sizeX, double sizeY) {
		g.drawImage(image, x, y, sizeX, sizeY);
	}

	/**
	 * Returns the movement cost of the block
	 *
	 * @return
	 */
	public int getCost() {
		return moveCost;
	}

}
