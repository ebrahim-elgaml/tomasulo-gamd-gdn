
public class RowROB {
	Type insType;
	int dest; 
	int value;
	boolean ready;
	public RowROB(Type insT,int dest,int value,boolean ready) {
		this.insType = insT; 
		this.dest = dest; 
		this.value = value;
		this.ready = ready; 
	}
}
