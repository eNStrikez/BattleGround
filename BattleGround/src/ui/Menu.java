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

	Scene scene;
	Selection selection;
	GridPane root;
	Button start, stats, quit, options;
	PasswordField pass;
	public Menu() {

		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		setWidth(bounds.getWidth());
		setHeight(bounds.getHeight());


		start = new Button("START");
		stats = new Button("STATS");
		quit = new Button("QUIT");
		options = new Button("OPTIONS");
		pass = new PasswordField();

		start.setAlignment(Pos.CENTER);
		stats.setAlignment(Pos.CENTER);
		quit.setAlignment(Pos.CENTER);
		options.setAlignment(Pos.CENTER);
		pass.setAlignment(Pos.BOTTOM_CENTER);


		start.setOnAction(e -> {
			selection = new Selection(getMaxWidth(), getMaxHeight(), pass.getText());
			setScene(selection.getScene());
		});

		quit.setOnAction(e -> {
			close();
		});

		options.setOnAction(e -> {

		});

		stats.setOnAction(e -> {

		});

		root = new GridPane();
		root.setVgap(50);
		root.setHgap(10);
		scene = new Scene(root, getMaxWidth(), getMaxHeight());

		root.add(start, 0, 0);
		root.add(stats, 0, 1);
		root.add(options, 0, 2);
		root.add(quit, 0, 3);
		root.add(pass, 1, 0);

		setScene(scene);
	}
}