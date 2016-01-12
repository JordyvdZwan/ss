package ss.week7.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncedIntCell implements IntCell {

	private int value = 0;
	private boolean available = false;
	
	public void setValue(int valueArg) {
		synchronized (this) {
			while (isAvailable()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.value = valueArg;
			available = true;
			notifyAll();
			
		}
	}
	
	public int getValue() {
		synchronized (this) {
			while (!isAvailable()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			available = false;
			notifyAll();
			return value;
		}
	}
	
	public boolean isAvailable() {
		return available;
	}
	
} 
	


