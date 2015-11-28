import java.util.ArrayList;

import javafx.util.Pair;

public class MemoryHandler {
	static Memory memory;
	static Cache headCache;
	
	public static void initMemoryHandler(int memoryCycles, int org, ArrayList<String> instructions, ArrayList<String> data, int cacheNumber, ArrayList<Integer> cycles, ArrayList<Integer> cacheSize, ArrayList<Integer> lineSize, ArrayList<Integer> associativity, ArrayList<Cache.WritePolicy> writePolicy) {
		memory = new Memory(memoryCycles, org, instructions, data);
		Cache.memory = memory;
		Cache currentCache = headCache;
		for (int i = 0; i < cacheNumber; ++i) {
			currentCache = new Cache(cycles.get(i), cacheSize.get(i), lineSize.get(i), associativity.get(i), writePolicy.get(i));
			currentCache = currentCache.next;
		}
	}
	
	public static Pair<String, Integer> read(int address) {
		Cache.time = 0;
		return headCache.read(address);
	}
	
	public static int write(int address, String data) {
		Cache.time = 0;
		headCache.write(address, data);
		return Cache.time;
	}
}
