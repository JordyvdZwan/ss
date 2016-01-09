package ss.week7.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncedIntCell implements IntCell {

	private int value = 0;
	private boolean available = false;
	
	public void setValue(int valueArg) {
		this.value = valueArg;
		available = true;
	}

	public int getValue() {
		available = false;
		return value;
	}
	
	public boolean isAvailable() {
		return available;
	}
}

