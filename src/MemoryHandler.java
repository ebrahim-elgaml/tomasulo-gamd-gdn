import java.util.ArrayList;

public class MemoryHandler {
	static Memory memory;
	static Cache dataCache, instructionCache;
	
	public static void initMemoryHandler(int memoryCycles, int org, ArrayList<String> instructions, ArrayList<String> data, int cacheNumber, ArrayList<Integer> cycles, ArrayList<Integer> cacheSize, ArrayList<Integer> lineSize, ArrayList<Integer> associativity, ArrayList<Cache.WritePolicy> writePolicy) {
		memory = new Memory(memoryCycles, org, instructions, data);
		Cache.memory = memory;
		Cache currentCache = dataCache;
		for (int i = 0; i < cacheNumber; ++i) {
			currentCache = new Cache(cycles.get(i), cacheSize.get(i), lineSize.get(i), associativity.get(i), Cache.Type.DATA, writePolicy.get(i));
			currentCache = currentCache.next;
		}
		currentCache = instructionCache;
		for (int i = 0; i < cacheNumber; ++i) {
			currentCache = new Cache(cycles.get(i), cacheSize.get(i), lineSize.get(i), associativity.get(i), Cache.Type.INSTRUCTION, writePolicy.get(i));
			currentCache = currentCache.next;
		}
	}
	
	public static Pair<String, Integer> read(int address) {
		Cache.time = 0;
		return dataCache.read(address);
	}
	
	public static int write(int address, String data) {
		Cache.time = 0;
		dataCache.write(address, data);
		return Cache.time;
	}
}
