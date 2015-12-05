public class Instruction {
	Type type;
	int regA;
	int regB;
	int imm;
	
	public Instruction(Type t, int rA, int rB, int imm) {
		this.type = t;
		this.regA = rA;
		this.regB = rB;
		this.imm = imm;
	}
	

	public String load() {
		String baseAddress = Run.registersFile.get(regB);
		int address = imm + Helper.hexToDecimal(baseAddress);
		return MemoryHandler.read(address);
		
	}
	public int store() {
		String baseAddress = Run.registersFile.get(regB);
		int address = imm + Helper.hexToDecimal(baseAddress);
		return address;
	}
	
	public int add(){
		return 0;
	}
	
	public int sub(){
		return 0;
	}
	
	public int mult(){
		return 0;
	}
	
	public int nand(){
		return 0;
	}
	
	public int mul(){
		return 0;

	}
}
