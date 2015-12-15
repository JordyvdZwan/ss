package ss.week6;

import java.util.Scanner;

public class Hello {

	public Hello() {
		Scanner in = new Scanner(System.in);
		String name = in.nextLine();
		System.out.println("hello " + name);
	}
	
	public static void main(String args[]) {
		Hello hello = new Hello();
	}
}
