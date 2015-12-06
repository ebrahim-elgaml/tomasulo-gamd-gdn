public class Instruction {
	Type type;
	int regA;
	int regB;
	int imm;
	int number;
	int cyclesLeft;

	public Instruction(Type t, int number, int rA, int rB, int imm) {
		this.number = number;
		this.type = t;
		this.regA = rA;
		this.regB = rB;
		this.imm = imm;
	}

	public void load() {

	}

	public String add() {
		 return Helper.decimalToHex((regB+imm)&255);
	}

	public String sub() {
		return Helper.decimalToHex((regB-imm)&255);
	}

	public String mul() {
		return Helper.decimalToHex((regB*imm)&255);
	}

	public String nand() {
		String and = Integer.toBinaryString(regB&imm);
		for(int i=0;i<and.length();++i){
			if(and.charAt(i)=='0')
				and = and.substring(0,i)+'1'+and.substring(i+1);
			else
				and = and.substring(0,i)+'0'+and.substring(i+1);
		}
		return Helper.decimalToHex(Integer.parseInt(and, 2));
	}

	public String addi() {
		return Helper.decimalToHex((regB+imm)&255);
	}
}
