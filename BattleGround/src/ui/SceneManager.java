package ui;

import game.Game;
import javafx.scene.Scene;

public class SceneManager {
	Selection selection;
	Menu menu;
	Game game;
	Scene menuScene, gameScene;

	/**
	 * Sets the selection window
	 *
	 * @param s
	 */
	public void setSelection(Selection s){
		selection = s;
	}

	/**
	 * Sets the menu window
	 *
	 * @param m
	 */
	public void setMenu(Menu m){
		menu = m;
	}

	/**
	 * Sets the game window
	 *
	 * @param g
	 */
	public void setGame(Game g){
		game = g;
	}

	/**
	 * Sets the game scene
	 *
	 * @param gS
	 */
	public void setGameScene(Scene gS){
		gameScene = gS;
	}

	/**
	 * Sets the menu scene
	 *
	 * @param mS
	 */
	public void setMenuScene(Scene mS){
		menuScene = mS;
	}

	/**
	 * Shows the menu window
	 */
	public void selectMenu(){
		menu.setMenu();
	}

	public void selectSelection(Scene scene){
		menu.setScene(scene);
	}

	/**
	 * Shows the game window1
	 */
	public void selectGame(){
		menu.setScene(gameScene);
	}

	/**
	 * Creates a new game
	 *
	 * @param sX
	 * @param sY
	 * @param c
	 * @param diff
	 */
	public void newGame(double sX, double sY, Character c, String diff, Modifier mod){
		menu.createGame(sX, sY, c, diff, mod);
	}
}
