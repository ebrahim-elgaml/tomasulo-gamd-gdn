package engine;

public class Instruction {
	Type type;
	int regA;
	int regB;
	int imm;
	int indexOfInstruction;
	int addressOfInstruction;
	int noOfCycles;

	public Instruction(Type t, int addressOfInstruction, int rA, int rB, int imm) {
		this.addressOfInstruction = addressOfInstruction;
		this.indexOfInstruction = addressOfInstruction - Run.origin;
		this.type = t;
		this.regA = rA;
		this.regB = rB;
		this.imm = imm;
		this.noOfCycles = Run.instructionCycles.get(t);
	}

	public Type getType() {
		return type;
	}

	public int getRegA() {
		return regA;
	}

	public int getRegB() {
		return regB;
	}

	public int getImm() {
		return imm;
	}

	public int getIndexOfInstruction() {
		return indexOfInstruction;
	}

	public int getAddressOfInstruction() {
		return addressOfInstruction;
	}

	public int getNoOfCycles() {
		return noOfCycles;
	}

	public void execute() {
		if (this.type == Type.LW) {
			this.load();
			return;
		}
		if (this.type == Type.SW) {
			this.store();
			return;
		}
		if (this.type == Type.BEQ) {
			this.branch();
			return;
		}
		if (this.type == Type.ADD) {
			this.add();
			return;
		}
		if (this.type == Type.SUB) {
			this.sub();
			return;
		}
		if (this.type == Type.ADDI) {
			this.addi();
			return;
		}
		if (this.type == Type.NAND) {
			this.nand();
			return;
		}
		if (this.type == Type.MUL) {
			this.mul();
			return;
		}
	}

	public void load() {
		String baseAddress = Run.registersFile.get(regB);
		int address = imm + Helper.hexToDecimal(baseAddress);
		int julieColumn = this.addressOfInstruction - Run.origin;
		Run.julie.get(Run.clock).set(julieColumn, Stage.EXEC);
		if (noOfCycles == -1) {
			noOfCycles = MemoryHandler.dataCache.readCycles(address);
		}
		if (noOfCycles > 0) {
			noOfCycles--;
			return;
		}
		for (int i = 0; i < Run.scoreboard.size(); i++) {
			if (Run.scoreboard.get(i).instructionAddress == this.addressOfInstruction) {
				Run.scoreboard.get(i).result = MemoryHandler.readData(address);
				System.out.println("data read by load="+MemoryHandler.readData(address));
				Run.scoreboard.get(i).address = address;
				break;
			}
		}
		return;
	}

	public void store() {
		String baseAddress = Run.registersFile.get(regB);
		int address = imm + Helper.hexToDecimal(baseAddress);
		int julieColumn = this.addressOfInstruction - Run.origin;
		Run.julie.get(Run.clock).set(julieColumn, Stage.EXEC);
		for (int i = 0; i < Run.scoreboard.size(); i++) {
			if (Run.scoreboard.get(i).instructionAddress == this.addressOfInstruction) {
				Run.scoreboard.get(i).result = Run.registersFile.get(regA);
				Run.scoreboard.get(i).address = address;
				break;
			}
		}
		return;
	}

	public void branch() {
		int julieColumn = this.addressOfInstruction - Run.origin;
		Run.julie.get(Run.clock).set(julieColumn, Stage.EXEC);
		if (noOfCycles > 0) {
			noOfCycles--;
			return;
		}
		String value1 = Run.registersFile.get(regA);
		String value2 = Run.registersFile.get(regB);
		if (value1.equals(value2) && imm < 0 || !value1.equals(value2)
				&& imm > 0) {
			for (int i = 0; i < Run.scoreboard.size(); i++) {
				if (Run.scoreboard.get(i).instructionAddress == this.addressOfInstruction) {
					Run.scoreboard.get(i).result = "true";
					Run.scoreboard.get(i).address = addressOfInstruction + imm + 1;
					break;
				}
			}
		} else {
			for (int i = 0; i < Run.scoreboard.size(); i++) {
				if (Run.scoreboard.get(i).instructionAddress == this.addressOfInstruction) {
					Run.scoreboard.get(i).address = addressOfInstruction + 1;
					Run.scoreboard.get(i).result = "false";
					Run.mispredicte++;
					break;
				}
			}
		}
	}

	public void add() {
		int julieColumn = this.addressOfInstruction - Run.origin;
		Run.julie.get(Run.clock).set(julieColumn, Stage.EXEC);
		if (noOfCycles > 0) {
			noOfCycles--;
			return;
		}
		int value1 = Helper.hexToDecimal(Run.registersFile.get(regB));
		int value2 = Helper.hexToDecimal(Run.registersFile.get(imm));
		for (int i = 0; i < Run.scoreboard.size(); i++) {
			if (Run.scoreboard.get(i).instructionAddress == this.addressOfInstruction) {
				Run.scoreboard.get(i).result = Helper
						.decimalToHex((value1 + value2) & 255);
				break;
			}
		}
	}

	public void sub() {
		int julieColumn = this.addressOfInstruction - Run.origin;
		Run.julie.get(Run.clock).set(julieColumn, Stage.EXEC);
		if (noOfCycles > 0) {
			noOfCycles--;
			return;
		}
		int value1 = Helper.hexToDecimal(Run.registersFile.get(regB));
		int value2 = Helper.hexToDecimal(Run.registersFile.get(imm));
		for (int i = 0; i < Run.scoreboard.size(); i++) {
			if (Run.scoreboard.get(i).instructionAddress == this.addressOfInstruction) {
				Run.scoreboard.get(i).result = Helper
						.decimalToHex((value1 - value2) & 255);
				break;
			}
		}
	}

	public void mul() {
		int julieColumn = this.addressOfInstruction - Run.origin;
		Run.julie.get(Run.clock).set(julieColumn, Stage.EXEC);
		if (noOfCycles > 0) {
			noOfCycles--;
			return;
		}
		int value1 = Helper.hexToDecimal(Run.registersFile.get(regB));
		int value2 = Helper.hexToDecimal(Run.registersFile.get(imm));
		for (int i = 0; i < Run.scoreboard.size(); i++) {
			if (Run.scoreboard.get(i).instructionAddress == this.addressOfInstruction) {
				Run.scoreboard.get(i).result = Helper
						.decimalToHex((value1 * value2) & 255);
				break;
			}
		}
	}

	public void nand() {
		int julieColumn = this.addressOfInstruction - Run.origin;
		Run.julie.get(Run.clock).set(julieColumn, Stage.EXEC);
		if (noOfCycles > 0) {
			noOfCycles--;
			return;
		}
		int value1 = Helper.hexToDecimal(Run.registersFile.get(regB));
		int value2 = Helper.hexToDecimal(Run.registersFile.get(imm));
		for (int i = 0; i < Run.scoreboard.size(); i++) {
			if (Run.scoreboard.get(i).instructionAddress == this.addressOfInstruction) {
				Run.scoreboard.get(i).result = Helper
						.decimalToHex((value1 & value2) ^ 255);
				break;
			}
		}
	}

	public void addi() {
		int julieColumn = this.addressOfInstruction - Run.origin;
		System.out.println(Run.clock);
		Run.julie.get(Run.clock).set(julieColumn, Stage.EXEC);
		if (noOfCycles > 0) {
			noOfCycles--;
			return;
		}
		if (noOfCycles < 0)
			return;
		int value1 = Helper.hexToDecimal(Run.registersFile.get(regB));
		for (int i = 0; i < Run.scoreboard.size(); i++) {
			if (Run.scoreboard.get(i).instructionAddress == this.addressOfInstruction) {
				Run.scoreboard.get(i).result = Helper
						.decimalToHex((value1 + this.imm) & 255);
				break;
			}
		}
		System.out.println("Hex: "
				+ Helper.decimalToHex((value1 + this.imm) & 255));
	}

	public String toString() {
		return type.toString();
	}
}
