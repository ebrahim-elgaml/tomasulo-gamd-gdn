package gui;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import engine.DCache;
import engine.ICache;
import engine.Run;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		GridPane grid = new GridPane();
		// grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		Scene scene = new Scene(grid, 1024, 768);
		stage.setScene(scene);
		stage.setTitle("Tomasulo");

		final TextArea code = new TextArea();
		code.setPrefWidth(800);
		code.setPrefHeight(600);
		code.autosize();
		grid.add(code, 0, 0, 1, 1);

		Button run = new Button("Run");
		grid.add(run, 0, 1, 1, 1);
		run.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				String[] lines = code.getText().toString().split("\n");
				Run run = new Run((ArrayList<String>) Arrays.asList(lines), 4,
						10, 0, new ArrayList<String>(), 2,
						new ArrayList<Integer>(), new ArrayList<Integer>(),
						new ArrayList<Integer>(), new ArrayList<Integer>(),
						new ArrayList<DCache.WritePolicy>(),
						new ArrayList<ICache.WritePolicy>(), 0);
			}
		});
		;

		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
