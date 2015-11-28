import java.util.ArrayList;

public class Run {
	int PC;
	ArrayList<String> registersFile = new ArrayList<String>();
	ArrayList<ArrayList<Stage>> julie = new ArrayList<ArrayList<Stage>>();
	ArrayList<Integer> registerStatus = new ArrayList<Integer>();
	ArrayList<ArrayList<RowScoreboard>>  scoreboard = new ArrayList<ArrayList<RowScoreboard>>();  
	ROB rob; 	
	public Run(ArrayList<String> ins,int numberOfEntryROB) {
		for(int i = 0; i < 8; i++) 
			registersFile.add("00");
		for (int i =0; i < 8; i++) 
			registerStatus.add(-1);
		rob = new ROB(numberOfEntryROB);
	}
}
