package ss.week7.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestSyncConsole extends Thread {
	private static SyncConsole c;
	private String name;
	private static Lock rl = new ReentrantLock();
	
	public TestSyncConsole() {
	}

	public void run() {
		sum();
	}

	private void sum() {
		sum2(this.getName());
	}
	
	private static void sum2(String name) {
		rl.lock();
		try {
		int first = c.readInt(name + ": get number 1?");
		int second = c.readInt(name + ": get number 2?");
		int result = first + second;
		String textResult = first + " + " + second + " = " + result;
		c.print(name + ": " + textResult + "\n");
		} finally {
			rl.unlock();
		}
	}
	
	public static void main(String[] args) {
		TestSyncConsole threadA = new TestSyncConsole();
		TestSyncConsole threadB = new TestSyncConsole();
		threadA.start();
		threadB.start();
	}
}
