
public class Instruction {
	Type type;
	int regA;
	int regB;
	int imm;
	public Instruction(Type t, int rA, int rB, int imm){
		this.type = t;
		this.regA = rA;
		this.regB = rB;
		this.imm = imm;
	}
}
