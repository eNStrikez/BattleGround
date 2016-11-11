package game;

import javafx.scene.image.Image;

public interface Weapon {
	public void doDamage(Entity e);
	public boolean checkCollision(Entity e);
	public Image getImage();
}
