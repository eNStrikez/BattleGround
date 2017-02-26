package ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	public final static double FRAME_RATE = 100;
	public final static String IP = "192.168.0.18";

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage = new Menu();
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
