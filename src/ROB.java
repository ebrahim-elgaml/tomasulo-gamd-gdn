
public class ROB {
	RowROB [] array;
	int head; 
	int tail;
	
	public RowROB[] getArray() {
		return array;
	}
	public void setArray(RowROB[] array) {
		this.array = array;
	}
	public int getHead() {
		return head;
	}
	public void setHead(int head) {
		this.head = head;
	}
	public int getTail() {
		return tail;
	}
	public void setTail(int tail) {
		this.tail = tail;
	}
	public ROB(int size) {
		array = new RowROB[size];
		head = -1;
		tail = 0; 
	}
	public boolean push(RowROB row) {
		if ((head == 0 && tail == array.length - 1) || head - tail == 1) 
			return false;
		array[tail] = new RowROB(row.insType,row.dest, row.value,row.ready);
		tail ++; 
		if(tail == array.length) 
			tail = array.length -1; 
		if (head == -1)
			head = 0;
		return true;
	}
	public RowROB pop() {
		if (head == -1 || head == tail)
			return null; 
		RowROB temp = array[head];
		head ++; 
		if (head == array.length)
			head = 0;
		return temp;
	}
}
