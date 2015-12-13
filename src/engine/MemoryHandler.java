package engine;

import java.util.ArrayList;

import engine.DCache.WritePolicy;

public class MemoryHandler {
	public static Memory memory;
	static DCache dataCache;
	static ICache instructionCache;
	public static int Ghit=0;
	public static int Gmiss=0;

	public static void initMemoryHandler(int memoryCycles, int org,
			ArrayList<String> instructions, ArrayList<String> data,
			int cacheNumber, ArrayList<Integer> cycles,
			ArrayList<Integer> cacheSize, ArrayList<Integer> lineSize,
			ArrayList<Integer> associativity,
			ArrayList<DCache.WritePolicy> dWritePolicy,
			ArrayList<ICache.WritePolicy> iWritePolicy) {
		memory = new Memory(memoryCycles, org, instructions, data);
		DCache.memory = memory;
		if (cacheNumber > 0) {
			dataCache = new DCache(cycles.get(0), cacheSize.get(0),
					lineSize.get(0), associativity.get(0), dWritePolicy.get(0));
		}
		DCache currentCache = dataCache;
		for (int i = 1; i < cacheNumber; ++i) {
			currentCache.next = new DCache(cycles.get(i), cacheSize.get(i),
					lineSize.get(i), associativity.get(i), dWritePolicy.get(i));
			currentCache = currentCache.next;
		}
		ICache.memory = memory;
		if (cacheNumber > 0) {
			instructionCache = new ICache(cycles.get(0), cacheSize.get(0),
					lineSize.get(0), associativity.get(0), iWritePolicy.get(0));
		}
		ICache currentCache1 = instructionCache;
		for (int i = 1; i < cacheNumber; ++i) {
			currentCache1.next = new ICache(cycles.get(i), cacheSize.get(i),
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
		if (dataCache != null) {
			System.out.println("data added");
			dataCache.write(address, data);
		} else {
			System.out.println("data added");
			memory.storeData(address, data);
		}
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
		if (instructionCache != null) {
			instructionCache.write(address, data);
		} else {
			memory.storeData(address, data);
		}
	}
}
