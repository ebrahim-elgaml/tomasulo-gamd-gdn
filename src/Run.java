
import java.util.ArrayList;

public class Run {
	int PC , clock;
	ArrayList<String> registersFile = new ArrayList<String>();
	ArrayList<ArrayList<Stage>> julie = new ArrayList<ArrayList<Stage>>();
	ArrayList<Integer> registerStatus = new ArrayList<Integer>();
	ArrayList<RowScoreboard>  scoreboard = new ArrayList<RowScoreboard>(); 
	ArrayList<FunctionalUnits> FunctionalUnit = new ArrayList<FunctionalUnits>();///had5ol mn el user 3ayzha
	ROB rob = new ROB(100-1);//////Size will be give by the user 	

	public Run(ArrayList<String> ins, int numberOfEntryROB, int memoryCycles,
			int org, ArrayList<String> data, int cacheNumber,
			ArrayList<Integer> cycles, ArrayList<Integer> cacheSize,
			ArrayList<Integer> lineSize, ArrayList<Integer> associativity,
			ArrayList<DCache.WritePolicy> writePolicy) {
		PC = org;
		for (int i = 0; i < 8; i++)
			registersFile.add("00");
		for (int i = 0; i < 8; i++)
			registerStatus.add(-1);
		rob = new ROB(numberOfEntryROB);
		MemoryHandler.initMemoryHandler(memoryCycles, org, ins, data,  cacheNumber, cycles,  cacheSize,  lineSize,  associativity,  writePolicy); 
		InitializeScoreboard(FunctionalUnit);
	}
	//Initializing the functional units for each reservation station
	public void InitializeScoreboard(ArrayList<FunctionalUnits>FunctionalUnits){
		for(int i = 0;i<FunctionalUnits.size();i++){
			scoreboard.set(i , new RowScoreboard(FunctionalUnits.get(i),false, null, -1, -1, -1, -1,-1));
		}
		for (int i = 0;i<registerStatus.size();i++){
			registerStatus.set(i, -1);
		}
	}
	//	LW, SW, JMP, BEQ, JALR, RET, ADD, SUB, ADDI, NAND, MUL;
	public boolean Issue(Instruction I){
		//checking the type of the instruction
		switch(I.type){
		case LW : if(HandleLoad_Immediate(I)){PC++;return true;}else return false;
		case BEQ : if(HandleThreeOprands(I,FunctionalUnits.ADD)){setPC(I);return true;}else return false;
		case SW : if(HandleStore(I)){PC++;return true;}else return false;
		case JMP : return HandleJump(I);
		case JALR : return HandleJump_Link(I);
		case RET : return HandleReturn(I);
		case ADD : if(HandleThreeOprands(I,FunctionalUnits.ADD)){PC++;return true;}else return false;
		case NAND : if(HandleThreeOprands(I,FunctionalUnits.LOGICAL)){PC++;return true;}else return false;
		case MUL : if(HandleThreeOprands(I,FunctionalUnits.MULTIPLY)){PC++;return true;}else return false;
		}
		return false;
	}
	// Branch prediction mechanism
	public void setPC(Instruction I){
		if(Binaryform(I.imm).charAt(0)=='1')
			PC++;
		else
			PC = 1+I.imm;

	}
	//Jump: branches to the address PC+1+regA+imm
	public boolean HandleJump(Instruction I){
		PC=1+I.regA+I.imm;
		return true;
	}
	//Jump and link register: Stores the value of PC+1 in regA and branches (unconditionally) to the address in regB.
	public boolean HandleJump_Link(Instruction I){
		I.regA = PC+1;
		PC = I.regB;
		return true;
	}
	public boolean HandleReturn(Instruction I){
		PC = I.regA;
		return true;
	}
	//Special case Instructions Load and Immediate value
	//Assuming ScoreBoard Contains Hexadecimal value 
	public boolean HandleLoad_Immediate(Instruction I){
		Type t = I.type;
		int reservationStationNumber = (t.equals(Type.LW))?EmptyFunctionalUnit(FunctionalUnits.LOAD):
			EmptyFunctionalUnit(FunctionalUnits.ADD);
		if(reservationStationNumber!=-1){
			RowScoreboard RS = new RowScoreboard();
			int rs = I.regB;
			int offset = I.imm;
			int rd = I.regA;
			boolean Issue = true;
			RowROB current;
			int ROBLOC = registerStatus.get(rs);
			if(registerStatus.get(rs)!=-1){
				if(rob.getArray()[ROBLOC]!=null && rob.getArray()[ROBLOC].ready){
					RS.vj = rob.getArray()[ROBLOC].value;
					RS.qj = 0;
				}
				else{
					RS.qj = ROBLOC;
					current = new RowROB(t, rd,0, false);
					Issue = rob.push(current);
				}
			}
			else{
				RS.vj = Integer.parseInt(registersFile.get(rs));
				RS.qj = 0;
			}
			RS.busy = true;
			RS.destination = rd;
			RS.address = offset;
			if(Issue){
				registerStatus.set(rd,ROBLOC);
				scoreboard.set(reservationStationNumber, RS);
			}
			else
				return false;
			ArrayList<Stage> crrnt = julie.get(clock);
			if(crrnt != null){
				crrnt.set(I.number,Stage.ISSUE);
			}
			else{
				crrnt = new ArrayList<Stage>();
				crrnt.set(I.number,Stage.ISSUE);
			}
			return true;
		}
		return false;
	}
	//special case of issuing Store
	public boolean  HandleStore(Instruction I){
		int reservationStationNumber =EmptyFunctionalUnit(FunctionalUnits.STORE);
		if(reservationStationNumber!=-1){
			RowScoreboard RS = new RowScoreboard();
			int rd = I.regB;
			int offset = I.imm;
			int rs = I.regA;
			RowROB current;
			int ROBLOC = registerStatus.get(rs);
			if(registerStatus.get(rs)!=-1){
				if(rob.getArray()[ROBLOC]!=null && rob.getArray()[ROBLOC].ready){
					RS.vk = rob.getArray()[ROBLOC].value;
					RS.qk = 0;
				}
				else{
					RS.qk = ROBLOC;
				}
			}
			else{
				RS.vk = Integer.parseInt(registersFile.get(rs));
				RS.qk = 0;
			}
			/// setting for register containing the address 
			ROBLOC = registerStatus.get(rd);
			if(registerStatus.get(rd)!=-1){
				if(rob.getArray()[ROBLOC]!=null && rob.getArray()[ROBLOC].ready){
					RS.vj = rob.getArray()[ROBLOC].value;
					RS.qj = 0;
				}
				else{
					RS.qj = ROBLOC;
				}
			}
			else{
				RS.vj = Integer.parseInt(registersFile.get(rd));
				RS.qj = 0;
			}

			current = new RowROB(Type.SW,0,0, false);
			RS.busy = true;
			RS.address = offset;	
			if(	rob.push(current))
				scoreboard.set(reservationStationNumber, RS);
			else
				return false;
			ArrayList<Stage> crrnt = julie.get(clock);
			if(crrnt != null){
				crrnt.set(I.number,Stage.ISSUE);
			}
			else{
				crrnt = new ArrayList<Stage>();
				crrnt.set(I.number,Stage.ISSUE);
			}
			return true;
		}
		else
			return false;
	}
	//handles the issuing of any 3 operand instruction 
	public boolean HandleThreeOprands(Instruction I,FunctionalUnits f){
		int reservationStationNumber =EmptyFunctionalUnit(f);
		if(reservationStationNumber!=-1){
			RowScoreboard RS = new RowScoreboard();
			int rs = I.regB;
			int rt = I.imm;
			int rd = I.regA;
			RowROB current;
			int ROBLOC = registerStatus.get(rt);
			/// setting for register second operand
			if(registerStatus.get(rt)!=-1){
				if(rob.getArray()[ROBLOC]!=null && rob.getArray()[ROBLOC].ready){
					RS.vk = rob.getArray()[ROBLOC].value;
					RS.qk = 0;
				}
				else{
					RS.qk = ROBLOC;
				}
			}
			else{
				RS.vk = Integer.parseInt(registersFile.get(rs));
				RS.qk = 0;
			}
			/// setting for register first operand
			ROBLOC = registerStatus.get(rs);
			if(registerStatus.get(rs)!=-1){
				if(rob.getArray()[ROBLOC]!=null && rob.getArray()[ROBLOC].ready){
					RS.vj = rob.getArray()[ROBLOC].value;
					RS.qj = 0;
				}
				else{
					RS.qj = ROBLOC;
				}
			}
			else{
				RS.vj = Integer.parseInt(registersFile.get(rs));
				RS.qj = 0;
			}
			RS.busy = true;
			current = new RowROB(I.type,rd,0, false);
			if(rob.push(current)){
				registerStatus.set(rd, ROBLOC);
				scoreboard.set(reservationStationNumber, RS);
			}
			else
				return false;
			ArrayList<Stage> crrnt = julie.get(clock);
			if(crrnt != null){
				crrnt.set(I.number,Stage.ISSUE);
			}
			else{
				crrnt = new ArrayList<Stage>();
				crrnt.set(I.number,Stage.ISSUE);
			}
			return true;
		}
		else
			return false;

	}
	//returns position of the suitable RS 
	public int EmptyFunctionalUnit(FunctionalUnits unitType){
		int indx = -1;
		for(int i = 0;i < scoreboard.size();i++)
		{
			RowScoreboard current = scoreboard.get(i);
			if(current.Type.equals(unitType)&& !current.busy){
				return i;
			}
		}
		return indx;
	}
	// converts from decimal to binary
	  public static String Binaryform(int num){

		  StringBuilder buf1=new StringBuilder();
		    StringBuilder buf2=new StringBuilder();
		    while (num != 0){
		        int digit = num % 2;
		        buf1.append(digit); // apend 0101 order
		        num = num/2;
		    }
		    String binary=buf1.reverse().toString();// reverse to get binary 1010
		    int length=binary.length();
		    if(length<8){
		       while (8-length>0){
		           buf2.append("0");// add zero until length =8
		           length++;
		       }
		    }
		    String bin=buf2.toString()+binary;// binary string with leading 0's
		    return (bin);
   }
	///case the immediate vale is hexadecimal 
	public static boolean isNegative(int Hex) {
		return (Hex/10>=8);

	}
	
}
