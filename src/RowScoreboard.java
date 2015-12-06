
public class RowScoreboard {
	boolean busy;
	String Type;
	FunctionalUnits unit;
	Type operation; 
	int vj; 
	int destination;
	int vk; 
	int qj; 
	int qk; 
	int address; 
	int instructionAddress;
	public RowScoreboard(){}
	public RowScoreboard(FunctionalUnits unit,boolean busy, Type op,int vj,int vk, int qj, int qk, int address,int insAdd) {
		this.unit = unit;
		this.busy = busy; 
		this.operation = op; 
		this.vj = vj;
		this.vk = vk; 
		this.qj = qj; 
		this.qk = qk; 
		this.address = address;
		this.instructionAddress = insAdd;
	}
	
	public RowScoreboard(boolean busy, String type, Type operation, int vj,
			int destination,int vk, int qj, int qk, int address) {
		this.busy = busy;
		this.vj = vj;
		this.vk = vk;
		this.qj = qj;
		this.qk = qk;
		this.address = address;
		this.operation = operation;
		this.destination = destination;

	}
}
