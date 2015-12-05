
public class Helper {
	
	public static Type toType(String s){
		if(s.equalsIgnoreCase("LW")){
			return Type.LW;
		}else if(s.equalsIgnoreCase("SW")){
			return Type.SW;
		}else if(s.equalsIgnoreCase("JMP")){
			return Type.JMP;
		}else if(s.equalsIgnoreCase("BEQ")){
			return Type.BEQ;
		}else if(s.equalsIgnoreCase("JALR")){
			return Type.JALR;
		}else if(s.equalsIgnoreCase("RET")){
			return Type.RET;
		}else if(s.equalsIgnoreCase("ADD")){
			return Type.ADD;
		}else if(s.equalsIgnoreCase("SUB")){
			return Type.SUB;
		}else if(s.equalsIgnoreCase("ADDI")){
			return Type.ADDI;
		}else if(s.equalsIgnoreCase("NAND")){
			return Type.NAND;
		}else if(s.equalsIgnoreCase("MUL")){
			return Type.MUL;
		}
		
		return null;
	}
	public static Instruction stringToInstruction(String s,int number){
		String[] inst = s.split(" ");
		Type t = toType(inst[0]);
		int regA , regB, imm;
		imm =  regB = -100;
		regA = Integer.parseInt(inst[1].substring(2));
		if(inst.length >= 4){
			regB = Integer.parseInt(inst[2].substring(2));
			imm = Integer.parseInt(inst[3].substring(2));
		}else if (inst.length == 3){
			regB = Integer.parseInt(inst[2].substring(2));
		}
		return new Instruction(t,number, regA, regB, imm);
	}
}
