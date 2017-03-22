package ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Modifier {
	String name;
	String type;
	double multiplier;

	/**
	 * @param n
	 * @param t
	 * @param m
	 */
	public Modifier(String n, String t, double m) {
		name = n;
		type = t;
		multiplier = m;
	}

	/**
	 * Returns the stat the modifier effects
	 *
	 * @return
	 */
	public String getStat() {
		return type;
	}

	/**
	 * Returns the multiplier the modifier effects
	 *
	 * @return
	 */
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * Draws the user's stats when affected by the selected modifier
	 * 
	 * @param g
	 * @param sX
	 * @param sY
	 * @param c
	 */
	public void drawStats(GraphicsContext g, double sX, double sY, Character c) {
		// Clears the previous drawing(s)
		g.clearRect(0, 0, sX, sY);
		g.setFill(new Color(0.8, 0.8, 0.8, 1));

		// Draws the name of the mod in capitals
		g.setFont(new Font("Consolas", 20));
		g.fillText(name.toUpperCase(), sX / 8, sY / 8);
		g.setFont(new Font("Consolas", 16));

		// Draws the stat bars and labels
		g.fillText("Health", sX / 8, sY / 4 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4, sX / 2, sY / 32);
		g.fillText("Speed", sX / 8, sY / 4 + sY / 16 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4 + sY / 16, sX / 2, sY / 32);
		g.fillText("Accuracy", sX / 8, sY / 4 + sY / 8 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4 + sY / 8, sX / 2, sY / 32);
		g.fillText("Skill", sX / 8, sY / 4 + 3 * sY / 16 + sY / 32, sX / 3);
		g.fillRect(sX / 3, sY / 4 + 3 * sY / 16, sX / 2, sY / 32);
		// Draws the original stats of the character
		g.setFill(Color.MEDIUMAQUAMARINE);
		g.fillRect(sX / 3, sY / 4, (c.getHealth() / 250) * sX / 2, (sY / 32));
		g.fillRect(sX / 3, sY / 4 + sY / 16, (c.getSpeed() / 10) * sX / 2, (sY / 32));
		g.fillRect(sX / 3, sY / 4 + sY / 8, c.getAccuracy() * sX / 2, (sY / 32));
		g.fillRect(sX / 3, sY / 4 + 3 * sY / 16, (c.getSkill() / 5) * sX / 2, (sY / 32));
		g.setFill(Color.CRIMSON);
		// Draws the change in stats caused by the mod on the player's stats
		if (type.equals("Health")) {
			g.fillRect(sX / 3, sY / 4, (c.getHealth() / 250) * sX / 2, (sY / 32));
			g.setFill(Color.MEDIUMAQUAMARINE);
			g.fillRect(sX / 3, sY / 4, (multiplier * c.getHealth() / 250) * sX / 2, (sY / 32));
		} else if (type.equals("Speed")) {
			g.fillRect(sX / 3, sY / 4 + sY / 16, (c.getSpeed() / 10) * sX / 2, (sY / 32));
			g.setFill(Color.MEDIUMAQUAMARINE);
			g.fillRect(sX / 3, sY / 4 + sY / 16, multiplier * (c.getSpeed() / 10) * sX / 2, (sY / 32));
		} else if (type.equals("Accuracy")) {
			g.fillRect(sX / 3, sY / 4 + sY / 8, c.getAccuracy() * sX / 2, (sY / 32));
			g.setFill(Color.MEDIUMAQUAMARINE);
			g.fillRect(sX / 3, sY / 4 + sY / 8, multiplier * c.getAccuracy() * sX / 2, (sY / 32));
		} else if (type.equals("Skill")) {
			g.fillRect(sX / 3, sY / 4 + 3 * sY / 16, (c.getSkill() / 5) * sX / 2, (sY / 32));
			g.setFill(Color.MEDIUMAQUAMARINE);
			g.fillRect(sX / 3, sY / 4 + 3 * sY / 16, (multiplier * c.getSkill() / 5) * sX / 2, (sY / 32));
		}
	}
}
