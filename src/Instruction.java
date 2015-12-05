
public class Instruction {
	Type type ;
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

}
