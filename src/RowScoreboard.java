
public class RowScoreboard {
	boolean busy; 
	Type operation; 
	int vj; 
	int vk; 
	int qj; 
	int qk; 
	int address; 
	public RowScoreboard(boolean busy, Type op,int vj,int vk, int qj, int qk, int address) {
		this.busy = busy; 
		this.operation = op; 
		this.vj = vj;
		this.vk = vk; 
		this.qj = qj; 
		this.qk = qk; 
		this.address = address; 
	}
}
