package engine;
import java.util.ArrayList;

public class ICache {
	int cycles;
	int cacheSize;
	int lineSize;
	int associativity;
	Type type;
	WritePolicy writePolicy;

	ICache next;
	static Memory memory;

	public enum WritePolicy {
		WRITE_THROUGH, WRITE_BACK
	};

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
		}

		public Row(boolean valid, boolean dirty, int tag, String data) {
			this.valid = valid;
			this.dirty = dirty;
			this.tag = tag;
			this.data = data;
		}
	}

	public ICache(int cycles, int cacheSize, int lineSize, int associativity, WritePolicy writePolicy) {
		this.cycles = cycles;
		this.cacheSize = cacheSize;
		this.lineSize = lineSize;
		this.associativity = associativity;
		this.writePolicy = writePolicy;
		sets = new ArrayList<ArrayList<Row>>(cacheSize
				/ (lineSize * associativity));
		offsetSize = (int) (Math.log(lineSize) / Math.log(2));
		indexSize = (int) (Math.log(cacheSize / (lineSize * associativity)) / Math
				.log(2));
		tagSize = 16 - offsetSize - indexSize;
	}

	public Instruction read(int address) {
		time += cycles;
		int offset = address & ((int) Math.pow(2, offsetSize) - 1);
		int index = (address >> offsetSize)
				& ((int) Math.pow(2, indexSize) - 1);
		int tag = (address >> (offsetSize + indexSize))
				& ((int) Math.pow(2, tagSize) - 1);
		ArrayList<Row> set = sets.get(index);
		for (int i = 0; i < set.size(); ++i) {
			Row row = set.get(i);
			if (row.tag == tag) {
				return Helper.stringToInstruction(row.data.substring(2 * offset, 2 * (offset + 1)),address);
			}
		}
		if (next != null) {
			return next.read(address);
		}
		// return new Pair<>(memory.loadData(address), time + memory.cycles);
		return memory.loadInstruction(address);
	}
	public int readCycles(int address) {
		time += cycles;
		int offset = address & ((int) Math.pow(2, offsetSize) - 1);
		int index = (address >> offsetSize)
				& ((int) Math.pow(2, indexSize) - 1);
		int tag = (address >> (offsetSize + indexSize))
				& ((int) Math.pow(2, tagSize) - 1);
		ArrayList<Row> set = sets.get(index);
		for (int i = 0; i < set.size(); ++i) {
			Row row = set.get(i);
			if (row.tag == tag) {
				return time;
			}
		}
		if (next != null) {
			return next.readCycles(address);
		}
		return time + memory.cycles;
	}
	public void write(int address, String data) {
		time += cycles;
		int index = (address >> offsetSize)
				& ((int) Math.pow(2, indexSize) - 1);
		int tag = (address >> (offsetSize + indexSize))
				& ((int) Math.pow(2, tagSize) - 1);
		ArrayList<Row> set = sets.get(index);
		for (int i = 0; i < set.size(); ++i) {
			Row row = set.get(i);
			if (row.tag == -1) {
				row.valid = true;
				row.dirty = true;
				row.tag = tag;
				row.data = data;
				return;
			}
		}
		if (set.get(0).dirty) {
			int oldAddress = set.get(0).tag << (indexSize + offsetSize);
			if (next != null) {
				next.write(oldAddress, set.get(0).data);
			} else {
				time += memory.cycles;
				memory.storeData(oldAddress, set.get(0).data);
			}
		}
		set.get(0).valid = true;
		set.get(0).dirty = true;
		set.get(0).tag = tag;
		set.get(0).data = data;
		if (writePolicy == WritePolicy.WRITE_THROUGH) {
			if (next != null) {
				next.write(address, data);
			} else {
				time += memory.cycles;
				memory.storeData(address, data);
			}
		}
	}
	public int writeCycles(int address, String data) {
		time += cycles;
		int index = (address >> offsetSize)
				& ((int) Math.pow(2, indexSize) - 1);
		int tag = (address >> (offsetSize + indexSize))
				& ((int) Math.pow(2, tagSize) - 1);
		ArrayList<Row> set = sets.get(index);
		for (int i = 0; i < set.size(); ++i) {
			Row row = set.get(i);
			if (row.tag == -1) {
				return time;
			}
		}
		if (set.get(0).dirty) {
			int oldAddress = set.get(0).tag << (indexSize + offsetSize);
			if (next != null) {
				next.writeCycles(oldAddress, set.get(0).data);
			} else {
				return time += memory.cycles;			
			}
		}
		
		if (writePolicy == WritePolicy.WRITE_THROUGH) {
			if (next != null) {
				next.writeCycles(address, data);
			} else {
				return time += memory.cycles;
			}
		}
		return time;
	}
}
