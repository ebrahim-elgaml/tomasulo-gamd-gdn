package engine;

import java.util.ArrayList;

public class Memory {
	public static Instruction instructions[] = new Instruction[65536];
	public static String data[] = new String[65536];
	int cycles;
	int base;

	public Memory(int c, int base, ArrayList<String> inst, ArrayList<String> d) {
		cycles = c;
		this.base = base;
		for (int i = 0; i < inst.size(); i++) {
			instructions[i] = Helper.stringToInstruction(inst.get(i), i);
		}
		for (int i = 0; i < d.size(); i++) {
			data[i] = d.get(i);
		}
		for(int i=0;i<data.length;i++)
			data[i]="00";
	}

	public void storeData(int index, String d) {
		System.out.println("data added");
		data[index - base] = d;
		System.out.println("data:" + (index - base) + " " + data[index - base]);
	}

	public Instruction loadInstruction(int index) {
		return instructions[index - base];
	}

	public String loadData(int index) {
		return data[index - base];
	}
}
