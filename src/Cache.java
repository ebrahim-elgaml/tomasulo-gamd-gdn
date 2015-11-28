import java.util.*;

public class Cache {
	int cycles;
	int cacheSize;
	int lineSize;
	int associativity;
	
	enum WritePolicy { WRITE_THROUGH, WRITE_BACK };
	
	int offsetSize;
	int indexSize;
	int tagSize;
	
	ArrayList<HashSet<ArrayList<Row>>> sets;
	
	class Row {
		boolean valid;
		boolean dirty;
		int tag;
		String data;
		
		public Row() {
			valid = false;
			dirty = false;
			tag = 0;
			data = "";
		}
		
		public Row(boolean valid, boolean dirty, int tag, String data) {
			this.valid = valid;
			this.dirty = dirty;
			this.tag = tag;
			this.data = data;
		}
	}
	
	public Cache(int cycles, int cacheSize, int lineSize, int associativity, WritePolicy writePolicy) {
		this.cycles = cycles;
		sets = new ArrayList<HashSet<ArrayList<Row>>>(cacheSize / (lineSize * associativity));
		offsetSize = (int) (Math.log(lineSize) / Math.log(2));
		indexSize = (int) (Math.log(cacheSize / (lineSize * associativity)) / Math.log(2));
		tagSize = 16 - offsetSize - indexSize;
	}
	
	public String read(int address) {
		int offset = address & ((int) Math.pow(2, offsetSize) - 1);
		int index = (address >> offsetSize) & ((int) Math.pow(2, indexSize) - 1);
		int tag = (address >> (offsetSize + indexSize)) & ((int) Math.pow(2, tagSize) - 1);
		HashSet<ArrayList<Row>> set = sets.get(index);
		Iterator<ArrayList<Row>> it = set.iterator();
		while(it.hasNext()) {
			
		}
		return "";
	}
	
	public void write(int address, String data) {
		
	}
}
