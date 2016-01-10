package ss.week7.account;

public class AccountSync {

	public static void main(String[] args) {
		for (int i = 0; i < 2000; i++) {
			Account account = new Account();
			Thread threadOne = new MyThread(5, 2000, account);
			Thread threadTwo = new MyThread(-5, 2000, account);
			threadOne.start();
			threadTwo.start();
			System.out.println(account.getBalance());
		}
	}
}
