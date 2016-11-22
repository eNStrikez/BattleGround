package ui;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Menu extends Stage {

	Scene scene, optionsScene, statsScene;
	Selection selection;
	GridPane root;
	Button start, stats, quit, options;
	SceneManager sManager;

	public Menu() {

		root = new GridPane();
		root.setVgap(50);
		root.setHgap(10);
		scene = new Scene(root, getMaxWidth(), getMaxHeight());

		sManager = new SceneManager();
		sManager.setMenu(this);
		sManager.setMenuScene(this.getScene());
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		setWidth(bounds.getWidth());
		setHeight(bounds.getHeight());

		start = new Button("START");
		stats = new Button("STATS");
		quit = new Button("QUIT");
		options = new Button("OPTIONS");

		start.setAlignment(Pos.CENTER);
		stats.setAlignment(Pos.CENTER);
		quit.setAlignment(Pos.CENTER);
		options.setAlignment(Pos.CENTER);

		start.setOnAction(e -> {
			selection = new Selection(getMaxWidth(), getMaxHeight());
			sManager.setSelection(selection);
			selection.setSceneManager(sManager);
			setScene(selection.getScene());
		});

		quit.setOnAction(e -> {
			close();
		});

		options.setOnAction(e -> {

		});

		stats.setOnAction(e -> {

		});

		root.add(start, 0, 0);
		root.add(stats, 0, 1);
		root.add(options, 0, 2);
		root.add(quit, 0, 3);

		setScene(scene);
	}
}