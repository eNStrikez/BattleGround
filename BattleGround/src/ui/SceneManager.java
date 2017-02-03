package ui;

import game.Game;
import javafx.scene.Scene;

public class SceneManager {
	Selection selection;
	Menu menu;
	Game game;
	Scene menuScene, optionScene, statsScene, gameScene;

	public SceneManager(){

	}

	public void setSelection(Selection s){
		selection = s;
	}

	public void setMenu(Menu m){
		menu = m;
	}

	public void setGame(Game g){
		game = g;
	}

	public void setGameScene(Scene gS){
		gameScene = gS;
	}

	public void setMenuScene(Scene mS){
		menuScene = mS;
	}
	public void setOptionScene(Scene oS){
		optionScene = oS;
	}
	public void setStatsScene(Scene sS){
		statsScene = sS;
	}

	public void selectMenu(){
		menu.setMenu();
	}

	public void selectGame(){
		menu.setScene(gameScene);
	}

	public void newGame(double sX, double sY, Character c, String diff){
		menu.createGame(sX, sY, c, diff);
	}
}
