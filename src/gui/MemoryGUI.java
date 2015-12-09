package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import engine.Instruction;
import engine.MemoryHandler;
import engine.Type;

public class MemoryGUI extends Application{
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		GridPane grid = new GridPane();
		// grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		Scene scene = //new Scene(grid, 1024, 768);
				new Scene(new Group());
		//stage.setScene(scene);
		stage.setTitle("Memory");
		Label label = new Label("Main Memory");
		TableView table = new TableView();
		TableColumn address = new TableColumn("Address");
        TableColumn index = new TableColumn("Index");
        TableColumn regA = new TableColumn("Reg A");
        TableColumn regB = new TableColumn("Reg B");
        TableColumn imm = new TableColumn("Imm");
        table.getColumns().addAll(address,index,regA,regB,imm);
        ObservableList instructions = FXCollections.observableArrayList(engine.MemoryHandler.memory.instructions);
        address.setCellValueFactory(new PropertyValueFactory<Instruction,String>("addressOfInstruction"));
        regA.setCellValueFactory(new PropertyValueFactory<Instruction,String>("regA"));
        index.setCellValueFactory(new PropertyValueFactory<Instruction,String>("indexOfInstruction"));
        regB.setCellValueFactory(new PropertyValueFactory<Instruction,String>("regB"));
        imm.setCellValueFactory(new PropertyValueFactory<Instruction,String>("imm"));
        table.setItems(instructions);
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
		
	}
}
