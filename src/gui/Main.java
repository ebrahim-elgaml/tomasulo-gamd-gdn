package gui;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import engine.DCache;
import engine.FunctionalUnits;
import engine.ICache;
import engine.MemoryHandler;
import engine.Run;
import engine.Type;

public class Main extends Application {

	String[] lines;
	ArrayList<String> aLines;
	ArrayList<String> data;
	Run run;

	@Override
	public void start(Stage stage) throws Exception {
		final int numberOfEntryROB = Integer.parseInt(JOptionPane
				.showInputDialog("Reorder buffer entries"));
		final int memoryCycles = Integer.parseInt(JOptionPane
				.showInputDialog("Memory cycles"));
		final int org = Integer.parseInt(JOptionPane.showInputDialog("Org"));
		final int cacheNumber = Integer.parseInt(JOptionPane
				.showInputDialog("Number of Caches"));
		final ArrayList<Integer> cycles = new ArrayList<Integer>();
		final ArrayList<Integer> cacheSize = new ArrayList<Integer>();
		final ArrayList<Integer> lineSize = new ArrayList<Integer>();
		final ArrayList<Integer> associativity = new ArrayList<Integer>();
		final ArrayList<DCache.WritePolicy> dWritePolicy = new ArrayList<DCache.WritePolicy>();
		final ArrayList<ICache.WritePolicy> iWritePolicy = new ArrayList<ICache.WritePolicy>();
		final ArrayList<FunctionalUnits> FunctionalUnit = new ArrayList<FunctionalUnits>();
		for (int i = 0; i < cacheNumber; ++i) {
			int cycle = Integer.parseInt(JOptionPane
					.showInputDialog("Cache Cycle " + (i + 1)));
			cycles.add(cycle);
			int size = Integer.parseInt(JOptionPane
					.showInputDialog("Cache Size " + (i + 1)));
			cacheSize.add(size);
			int line = Integer.parseInt(JOptionPane
					.showInputDialog("Line Size " + (i + 1)));
			lineSize.add(line);
			int assoc = Integer.parseInt(JOptionPane
					.showInputDialog("Associativity " + (i + 1)));
			associativity.add(assoc);
			int dWrite = Integer.parseInt(JOptionPane
					.showInputDialog("DCache Write Policy " + (i + 1)));
			dWritePolicy.add(dWrite == 0 ? DCache.WritePolicy.WRITE_BACK
					: DCache.WritePolicy.WRITE_THROUGH);
			int iWrite = Integer.parseInt(JOptionPane
					.showInputDialog("ICache Write Policy " + (i + 1)));
			iWritePolicy.add(iWrite == 0 ? ICache.WritePolicy.WRITE_BACK
					: ICache.WritePolicy.WRITE_THROUGH);
		}
		final int widthSuperscaler = Integer.parseInt(JOptionPane
				.showInputDialog("Supersalar width"));

		int lw = Integer.parseInt(JOptionPane.showInputDialog("LW CYCLES"));
		Run.instructionCycles.put(Type.LW, lw);
		int sw = Integer.parseInt(JOptionPane.showInputDialog("SW"));
		Run.instructionCycles.put(Type.SW, sw);
		int jmp = Integer.parseInt(JOptionPane.showInputDialog("JMP"));
		Run.instructionCycles.put(Type.JMP, jmp);
		int beq = Integer.parseInt(JOptionPane.showInputDialog("BEQ"));
		Run.instructionCycles.put(Type.BEQ, beq);
		int jalr = Integer.parseInt(JOptionPane.showInputDialog("JALR"));
		Run.instructionCycles.put(Type.JALR, jalr);
		int ret = Integer.parseInt(JOptionPane.showInputDialog("RET"));
		Run.instructionCycles.put(Type.RET, ret);
		int add = Integer.parseInt(JOptionPane.showInputDialog("ADD"));
		Run.instructionCycles.put(Type.ADD, add);
		int sub = Integer.parseInt(JOptionPane.showInputDialog("SUB"));
		Run.instructionCycles.put(Type.SUB, sub);
		int addi = Integer.parseInt(JOptionPane.showInputDialog("ADDI"));
		Run.instructionCycles.put(Type.ADDI, addi);
		int nand = Integer.parseInt(JOptionPane.showInputDialog("NAND"));
		Run.instructionCycles.put(Type.NAND, nand);
		int mul = Integer.parseInt(JOptionPane.showInputDialog("MUL"));
		Run.instructionCycles.put(Type.MUL, mul);
		
		int lwf = Integer.parseInt(JOptionPane.showInputDialog("LW UNITS"));
		for(int i=0;i<lwf;i++)
			FunctionalUnit.add(FunctionalUnits.LOAD);
		int swf = Integer.parseInt(JOptionPane.showInputDialog("SW"));
		for(int i=0;i<swf;i++)
			FunctionalUnit.add(FunctionalUnits.STORE);
		int addf = Integer.parseInt(JOptionPane.showInputDialog("ADD"));
		for(int i=0;i<addf;i++)
			FunctionalUnit.add(FunctionalUnits.ADD);
		int addif = Integer.parseInt(JOptionPane.showInputDialog("ADDI"));
		for(int i=0;i<addif;i++)
			FunctionalUnit.add(FunctionalUnits.ADDI);
		int nandf = Integer.parseInt(JOptionPane.showInputDialog("NAND"));
		for(int i=0;i<nandf;i++)
			FunctionalUnit.add(FunctionalUnits.LOGICAL);
		int mulf = Integer.parseInt(JOptionPane.showInputDialog("MUL"));
		for(int i=0;i<mulf;i++)
			FunctionalUnit.add(FunctionalUnits.MULTIPLY);

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
		grid.add(code, 0, 0, 3, 1);
		
		Button compileButton = new Button("Compile");
		grid.add(compileButton, 0, 1, 1, 1);
		compileButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				lines = code.getText().toString().split("\n");
				aLines = new ArrayList<String>();
				for (int i = 0; i < lines.length; ++i) {
					aLines.add(lines[i]);
				}
				data = new ArrayList<String>();
				run = new Run(aLines, numberOfEntryROB, memoryCycles, org,
						data, cacheNumber, cycles, cacheSize, lineSize,
						associativity, dWritePolicy, iWritePolicy,
						widthSuperscaler, FunctionalUnit);
				for (int i = 0; i < lines.length; ++i) {
					aLines.add(lines[i]);
				}
			}
		});

		Button runButton = new Button("Run");
		grid.add(runButton, 1, 1, 1, 1);
		runButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				run.AlwaysRun(lines.length);
//				MemoryHandler.writeData(0, "05");
//				System.out.println(MemoryHandler.readData(0));
			}
		});

		Button memory = new Button("Memory");
		grid.add(memory, 2, 1, 1, 1);
		memory.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				MemoryGUI window = new MemoryGUI();
				Stage memStage = new Stage();
				memStage.show();

				try {
					window.start(memStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
