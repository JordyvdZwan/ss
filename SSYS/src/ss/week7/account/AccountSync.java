package ss.week7.account;

public class AccountSync {

	public static void main(String[] args) {
//		for (int i = 0; i < 40; i++) {
		Account account = new Account();
		Thread threadOne = new Thread(new MyThread(10, 3000, account));
		Thread threadTwo = new Thread(new MyThread(-200, 10, account));
		threadOne.start();
		threadTwo.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println(account.getBalance());
//		}
	}
}
