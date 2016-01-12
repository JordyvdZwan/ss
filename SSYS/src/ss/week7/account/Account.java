package ss.week7.account;

import java.util.concurrent.locks.*;

public class Account {
	double balance = 0.0;
	final Lock lock = new ReentrantLock();
	final Condition decrementPossible = lock.newCondition();
	
	
	// er wordt nog steeds verkeerd gelezen
//	public synchronized void transaction(double amount) {
//		balance = balance + amount;
//	}
	
	public void transaction(double amount) {
		lock.lock();
		try {
			while (balance + amount < -1000) {
				decrementPossible.await();
			}
			if (balance > -1000) {
				decrementPossible.signal();
			}
			balance = balance + amount;
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} finally {
			lock.unlock();
		}
	}
	
	public synchronized double getBalance() {
		return balance;
	}
}
