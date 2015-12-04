import java.util.ArrayList;


public class Cache {
	int cycles;
	int cacheSize;
	int lineSize;
	int associativity;
	WritePolicy writePolicy;
	
	Cache next;
	static Memory memory;
	
	enum WritePolicy { WRITE_THROUGH, WRITE_BACK };
	
	int offsetSize;
	int indexSize;
	int tagSize;
	
	static int time;
	
	ArrayList<ArrayList<Row>> sets;
	
	class Row {
		boolean valid;
		boolean dirty;
		int tag;
		String data;
		
		public Row() {
			valid = false;
			dirty = false;
			tag = -1;
			data = "";
		}
		
		public Row(boolean valid, boolean dirty, int tag, String data) {
			this.valid = valid;
			this.dirty = dirty;
			this.tag = tag;
			this.data = data;
		}

		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		public boolean isDirty() {
			return dirty;
		}

		public void setDirty(boolean dirty) {
			this.dirty = dirty;
		}

		public int getTag() {
			return tag;
		}

		public void setTag(int tag) {
			this.tag = tag;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
	}
	
	public Cache(int cycles, int cacheSize, int lineSize, int associativity, WritePolicy writePolicy) {
		this.cycles = cycles;
		this.cacheSize = cacheSize;
		this.lineSize = lineSize;
		this.associativity = associativity;
		this.writePolicy = writePolicy;
		sets = new ArrayList<ArrayList<Row>>(cacheSize / (lineSize * associativity));
		offsetSize = (int) (Math.log(lineSize) / Math.log(2));
		indexSize = (int) (Math.log(cacheSize / (lineSize * associativity)) / Math.log(2));
		tagSize = 16 - offsetSize - indexSize;
	}
		
	public Pair<String, Integer> read(int address) {
		time += cycles;
		int offset = address & ((int) Math.pow(2, offsetSize) - 1);
		int index = (address >> offsetSize) & ((int) Math.pow(2, indexSize) - 1);
		int tag = (address >> (offsetSize + indexSize)) & ((int) Math.pow(2, tagSize) - 1);
		ArrayList<Row> set = sets.get(index);
		for (int i = 0; i < set.size(); ++i) {
			Row row = set.get(i);
			if (row.getTag() == tag) {
				return new Pair<>(row.getData().substring(2 * offset, 2 * (offset + 1)), time);
			}
		}
		if (next != null) {
			return next.read(address);
		}
		//TODO memory
		return new Pair<>(memory.loadData(address), time + memory.cycles);
	}
	
	public void write(int address, String data) {
		time += cycles;
		int index = (address >> offsetSize) & ((int) Math.pow(2, indexSize) - 1);
		int tag = (address >> (offsetSize + indexSize)) & ((int) Math.pow(2, tagSize) - 1);
		ArrayList<Row> set = sets.get(index);
		for (int i = 0; i < set.size(); ++i) {
			Row row = set.get(i);
			if (row.getTag() == -1) {
				row.setValid(true);
				row.setDirty(true);
				row.setTag(tag);
				row.setData(data);
				return;
			}
		}
		if (set.get(0).isDirty()) {
			int oldAddress = set.get(0).getTag() << (indexSize + offsetSize);
			if (next != null) {
				next.write(oldAddress, set.get(0).getData());
			} else {
				time += memory.cycles;
				memory.storeData(oldAddress, set.get(0).getData());
			}
		}
		set.get(0).setValid(true);
		set.get(0).setDirty(true);
		set.get(0).setTag(tag);
		set.get(0).setData(data);
		if (writePolicy == WritePolicy.WRITE_THROUGH) {
			if (next != null) {
				next.write(address, data);
			} else {
				time += memory.cycles;
				memory.storeData(address, data);
			}
		}
	}
}
