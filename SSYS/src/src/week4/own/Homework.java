package week4.own;
import week4.own.Function;

public class Homework {

	public static void main(String args[]) {
		Homework homework = new Homework();
	}
	
	
	public Homework() {
		Function f1;
		f1 = new Sum(new Exponent(1), new Exponent(2));
		Integrandable f4 = (Integrandable) f1;
		Function f2 = f4.integrand();
		Function f3 = f1.derivative();
		System.out.println("f(x)= " + f1.toString() +", f(3) = " + f1.apply(3));
		System.out.println("f(x)= " + f2.toString() +", f(3) = " + f2.apply(3));
		System.out.println("f(x)= " + f3.toString() +", f(3) = " + f3.apply(3));

	}

}