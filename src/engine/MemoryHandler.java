package engine;
import java.util.ArrayList;

public class MemoryHandler {
	static Memory memory;
	static DCache dataCache;
	static ICache instructionCache;

	public static void initMemoryHandler(int memoryCycles, int org, ArrayList<String> instructions,
			ArrayList<String> data, int cacheNumber, ArrayList<Integer> cycles, ArrayList<Integer> cacheSize,
			ArrayList<Integer> lineSize, ArrayList<Integer> associativity, ArrayList<DCache.WritePolicy> dWritePolicy,
			ArrayList<ICache.WritePolicy> iWritePolicy) {
		memory = new Memory(memoryCycles, org, instructions, data);
		DCache.memory = memory;
		DCache currentCache = dataCache;
		for (int i = 0; i < cacheNumber; ++i) {
			currentCache = new DCache(cycles.get(i), cacheSize.get(i), lineSize.get(i), associativity.get(i),
					dWritePolicy.get(i));
			currentCache = currentCache.next;
		}
		ICache currentCache1 = instructionCache;
		for (int i = 0; i < cacheNumber; ++i) {
			currentCache1 = new ICache(cycles.get(i).intValue(), cacheSize.get(i).intValue(),
					lineSize.get(i).intValue(), associativity.get(i).intValue(), iWritePolicy.get(i));
			currentCache1 = currentCache1.next;
		}
	}

	public static String read(int address) {
		DCache.time = 0;
		return dataCache.read(address);
	}

	public static void write(int address, String data) {
		DCache.time = 0;
		dataCache.write(address, data);
	}
}
