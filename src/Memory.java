import java.util.ArrayList;

public class Memory {
	ArrayList<Instruction> instructions = new ArrayList<Instruction>();
	ArrayList<String> data = new ArrayList<String>();
	int cycles;
	int base;
	public Memory(int c, int base, ArrayList<String> inst, ArrayList<String> d){
		cycles = c;
		this.base = base;
		for(int i = 0; i< inst.size(); i++){
			instructions.add(Helper.stringToInstruction(inst.get(i)));
		}
		for(int i = 0; i< d.size(); i++){
			data.add(d.get(i));
		}
	}
	
	public void storeData(int index, String d){
		data.add(index - base, d);
	}
	
	public Instruction loadInstruction(int index){
		return instructions.get(index - base);
	}
	
	public String loadData(int index){
		return data.get(index - base);
	}
}
