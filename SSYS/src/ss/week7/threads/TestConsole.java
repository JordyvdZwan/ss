package ss.week7.threads;

public class TestConsole extends Thread {
	private Console c;
	private String name;
	public TestConsole() {
	}

	public void run() {
		sum();
	}

	private void sum() {
		int first = c.readInt(this.getName() + ": get number 1?");
		int second = c.readInt(this.getName() + ": get number 2?");
		int result = first + second;
		String textResult = first + " + " + second + " = " + result;
		c.print(this.getName() + ": " + textResult);
	}
	
	public static void main(String[] args) {
		TestConsole threadA = new TestConsole();
		TestConsole threadB = new TestConsole();
		threadA.start();
		threadB.start();
	}
}
