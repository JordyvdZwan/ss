package ss.week7.account;

public class MyThread extends Thread {
	double theAmount;
	int theFrequency;
	Account theAccount;	
	
	public MyThread(double amount, int frequency, Account account) {
		this.theAmount = amount;
		this.theFrequency = frequency;
		this.theAccount = account;
	}
	
	public void run() {
		while (theFrequency > 0) {
			theAccount.transaction(theAmount);
			theFrequency--;
		}
	}

}
