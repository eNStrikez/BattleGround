package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Character {
	String name;
	Image image;
	Object stats;

	double health;
	double speed;
	double accuracy;
	double skill;
	String weapon;
	String grenade;
	String melee;

	public Character(String n, double h, double sp, double a, double sk, String w, String g, String m){
		name = n;
		image = new Image("https://github.com/eNStrikez/BattleGround/blob/master/CT_" + n + ".png");
		health = h;
		speed = sp;
		accuracy = a;
		skill = sk;
		weapon = w;
		grenade = g;
		melee = m;
	}

	public void draw(GraphicsContext g, int sX, int sY){
		g.drawImage(image, (sX - image.getWidth())/2, (sY - image.getHeight())/2);
	}

	public Object getStats(){
		return stats;
	}
}
