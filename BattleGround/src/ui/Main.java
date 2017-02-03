package ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	public final static double FRAME_RATE = 100;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage = new Menu();
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
