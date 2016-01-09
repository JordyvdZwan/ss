package ss.week7.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class FineGrainedIntCell implements IntCell {

	private int value = 0;
	private boolean available = false;
	final Lock lock = new ReentrantLock();
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();
	
	
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
