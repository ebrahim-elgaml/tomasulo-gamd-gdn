import java.util.ArrayList;

public class MemoryHandler {
	static Memory memory;
	static DCache dataCache, instructionCache;

	public static void initMemoryHandler(int memoryCycles, int org,
			ArrayList<String> instructions, ArrayList<String> data,
			int cacheNumber, ArrayList<Integer> cycles,
			ArrayList<Integer> cacheSize, ArrayList<Integer> lineSize,
			ArrayList<Integer> associativity,
			ArrayList<DCache.WritePolicy> writePolicy) {
		memory = new Memory(memoryCycles, org, instructions, data);
		DCache.memory = memory;
		DCache currentCache = dataCache;
		for (int i = 0; i < cacheNumber; ++i) {
			currentCache = new DCache(cycles.get(i), cacheSize.get(i),
					lineSize.get(i), associativity.get(i), writePolicy.get(i));
			currentCache = currentCache.next;
		}
		currentCache = instructionCache;
		for (int i = 0; i < cacheNumber; ++i) {
			currentCache = new DCache(cycles.get(i), cacheSize.get(i),
					lineSize.get(i), associativity.get(i), writePolicy.get(i));
			currentCache = currentCache.next;
		}
	}

	public static Pair<String, Integer> read(int address) {
		DCache.time = 0;
		return dataCache.read(address);
	}

	public static int write(int address, String data) {
		DCache.time = 0;
		dataCache.write(address, data);
		return DCache.time;
	}
}
