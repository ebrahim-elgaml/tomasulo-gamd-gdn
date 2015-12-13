package engine;

public class RowROB {
	Type insType;
	int dest; 
	int value;
	boolean ready;
	boolean last;
	public RowROB(Type insT,int dest,int value,boolean ready,boolean last) {
		this.insType = insT; 
		this.dest = dest; 
		this.value = value;
		this.ready = ready; 
		this.last = last;
	}
}
