package engine;

import java.util.ArrayList;

public class MemoryHandler {
	static Memory memory;
	static DCache dataCache;
	static ICache instructionCache;

	public static void initMemoryHandler(int memoryCycles, int org,
			ArrayList<String> instructions, ArrayList<String> data,
			int cacheNumber, ArrayList<Integer> cycles,
			ArrayList<Integer> cacheSize, ArrayList<Integer> lineSize,
			ArrayList<Integer> associativity,
			ArrayList<DCache.WritePolicy> dWritePolicy,
			ArrayList<ICache.WritePolicy> iWritePolicy) {
		memory = new Memory(memoryCycles, org, instructions, data);
		DCache.memory = memory;
		DCache currentCache = dataCache;
		for (int i = 0; i < cacheNumber; ++i) {
			currentCache = new DCache(cycles.get(i), cacheSize.get(i),
					lineSize.get(i), associativity.get(i), dWritePolicy.get(i));
			currentCache = currentCache.next;
		}
		ICache currentCache1 = instructionCache;
		for (int i = 1; i < cacheNumber; ++i) {
			currentCache1 = new ICache(cycles.get(i), cacheSize.get(i),
					lineSize.get(i), associativity.get(i), iWritePolicy.get(i));
			currentCache1 = currentCache1.next;
		}
	}

	public static String readData(int address) {
		DCache.time = 0;
		if (dataCache != null) {
			return dataCache.read(address);
		}
		DCache.time = memory.cycles;
		return memory.loadData(address);
	}

	public static void writeData(int address, String data) {
		DCache.time = 0;
		dataCache.write(address, data);
	}

	public static Instruction readInstruction(int address) {
		ICache.time = 0;
		if (instructionCache != null) {
			return instructionCache.read(address);
		}
		ICache.time = memory.cycles;
		return memory.loadInstruction(address);
	}

	public static void writeInstruction(int address, String data) {
		ICache.time = 0;
		instructionCache.write(address, data);
	}
}
