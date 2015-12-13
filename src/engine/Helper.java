package engine;

public class Helper {

	public static Type toType(String s) {
		if (s.equalsIgnoreCase("LW")) {
			return Type.LW;
		} else if (s.equalsIgnoreCase("SW")) {
			return Type.SW;
		} else if (s.equalsIgnoreCase("JMP")) {
			return Type.JMP;
		} else if (s.equalsIgnoreCase("BEQ")) {
			return Type.BEQ;
		} else if (s.equalsIgnoreCase("JALR")) {
			return Type.JALR;
		} else if (s.equalsIgnoreCase("RET")) {
			return Type.RET;
		} else if (s.equalsIgnoreCase("ADD")) {
			return Type.ADD;
		} else if (s.equalsIgnoreCase("SUB")) {
			return Type.SUB;
		} else if (s.equalsIgnoreCase("ADDI")) {
			return Type.ADDI;
		} else if (s.equalsIgnoreCase("NAND")) {
			return Type.NAND;
		} else if (s.equalsIgnoreCase("MUL")) {
			return Type.MUL;
		}

		return null;
	}

	public static int hexToDecimal(String hex) {
		if (hex.equals("false"))
			return -300;
		if (hex.equals("true"))
			return -400;
		int result = 0;
		for (int i = 0; i < hex.length(); i++) {
			String character = hex.charAt(i) + "";
			switch (character) {
			case ("F"):
				result += 15 * Math.pow(16, i);
				break;
			case ("E"):
				result += 14 * Math.pow(16, i);
				break;
			case ("D"):
				result += 13 * Math.pow(16, i);
				break;
			case ("C"):
				result += 12 * Math.pow(16, i);
				break;
			case ("B"):
				result += 11 * Math.pow(16, i);
				break;
			case ("A"):
				result += 10 * Math.pow(16, i);
				break;
			default:
				result += Integer.parseInt(character) * Math.pow(16, i);
			}
		}
		return result;
	}

	public static String decimalToHex(int decimal) {
		String s = "";
		do {
			int x = decimal % 16;
			switch (x) {
			case (10):
				s += 'A';
				break;
			case (11):
				s += 'B';
				break;
			case (12):
				s += 'C';
				break;
			case (13):
				s += 'D';
				break;
			case (14):
				s += 'E';
				break;
			case (15):
				s += 'F';
				break;
			default:
				s += x;
			}
			decimal /= 16;
		} while (decimal > 0);
		String sRev = "";
		for (int i = 0; i < s.length(); i++) {
			sRev += s.charAt(i);
		}
		return (sRev.length() > 1) ? sRev : "0" + sRev;
	}

	public static Instruction stringToInstruction(String s,
			int addressOfInstruction) {

		String[] inst = s.split(" ");
		Type t = toType(inst[0]);
		int regA = 0, regB, imm;
		imm = regB = -100;
		regA = Integer.parseInt(inst[1].substring(1));
		if (inst.length >= 4) {
			if (t == Type.ADDI || t == Type.SW || t == Type.LW || t == Type.BEQ) {
				regB = Integer.parseInt(inst[2].substring(1));
				imm = Integer.parseInt(inst[3]);
			} else {
				regB = Integer.parseInt(inst[2].substring(1));
				imm = Integer.parseInt(inst[3].substring(1));
			}
		} else if (inst.length == 3) {
/*
addi r1 r1 5
beq r1 r1 1
addi r2 r2 10
addi r3 r3 17
*/
			if (t == Type.JMP) {
				imm = Integer.parseInt(inst[2]);
			} else {
				regB = Integer.parseInt(inst[2].substring(1));
			}
		}
		return new Instruction(t, addressOfInstruction, regA, regB, imm);
	}
}
