import java.util.ArrayList;

public class Run {
	int PC;
	ArrayList<String> registersFile = new ArrayList<String>();
	ArrayList<ArrayList<Stage>> julie = new ArrayList<ArrayList<Stage>>();
	ArrayList<Integer> registerStatus = new ArrayList<Integer>();
	ArrayList<ArrayList<RowScoreboard>> scoreboard = new ArrayList<ArrayList<RowScoreboard>>();
	ROB rob;

	public Run(ArrayList<String> ins, int numberOfEntryROB, int memoryCycles,
			int org, ArrayList<String> data, int cacheNumber,
			ArrayList<Integer> cycles, ArrayList<Integer> cacheSize,
			ArrayList<Integer> lineSize, ArrayList<Integer> associativity,
			ArrayList<DCache.WritePolicy> writePolicy) {
		PC = org;
		for (int i = 0; i < 8; i++)
			registersFile.add("00");
		for (int i = 0; i < 8; i++)
			registerStatus.add(-1);
		rob = new ROB(numberOfEntryROB);
		MemoryHandler.initMemoryHandler(memoryCycles, org, ins, data,
				cacheNumber, cycles, cacheSize, lineSize, associativity,
				writePolicy);
	}

	public void Issue(Instruction I) {
		if (!registersFile.get(I.regA).isEmpty()) {
			RowScoreboard current = new RowScoreboard(true, I.type, -1, -1,
					Integer.parseInt(registersFile.get(I.regA)), -1, PC);
		}

	}
}
