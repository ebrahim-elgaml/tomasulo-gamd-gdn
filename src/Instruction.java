public class Instruction {
	Type type;
	int regA;
	int regB;
	int imm;
	int number;
	public Instruction(Type t,int number, int rA, int rB, int imm){
		this.number = number;
		this.type = t;
		this.regA = rA;
		this.regB = rB;
		this.imm = imm;
	}
	

	public void load() {
		
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
