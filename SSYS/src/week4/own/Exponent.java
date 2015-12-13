package week4.own;

public class Exponent implements Function, Integrandable {

	double n;
	
	public Exponent(double n) {
		this.n = n;
	}
	
	public double apply(double number) {
		return Math.pow(number,n);
	}

	public Function derivative() {
		return new LinearProduct(new Constant(n), new Exponent(n - 1));
	}

	public Function integrand() {
		return new LinearProduct(new Constant(1 / (n + 1)), new Exponent(n + 1));
	}
	
	public String toString(){
		Double y = n;
		return (y.toString());
	}
}
