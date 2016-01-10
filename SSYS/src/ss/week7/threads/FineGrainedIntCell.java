package ss.week7.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class FineGrainedIntCell implements IntCell {

	private int value = 0;
	private boolean available = false;
	public final Lock lock = new ReentrantLock();
	public final Condition notFull = lock.newCondition();
	public final Condition notEmpty = lock.newCondition();
	
	//NOTE volgorde klopt niet...?
	public void setValue(int valueArg) {
		lock.lock();
		try {
			while (isAvailable()) {
				try {
					notEmpty.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.value = valueArg;
			available = true;
			notFull.signal();
		} finally {
			lock.unlock();
		}
	}

	public int getValue() {
		lock.lock();
		try {
			while (!isAvailable()) {
				try {
					notFull.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			available = false;
			notEmpty.signal();
			return value;
		} finally {
			lock.unlock();
			
		}
	}
	
	public boolean isAvailable() {
		return available;
	}
}
