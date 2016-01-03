package week4.own;

public class Constant implements Function, Integrandable {

	private double x;
	
	public Constant(double x) {
		this.x = x;
	}
	
	public double apply(double number) {
		return x;
	}

	public Function derivative() {
		return new Constant(0);
	}
	
	public String toString() {
		Double y = x;
		return y.toString();
	}

	public Function integrand() {
		return new LinearProduct(new Constant(x), new Exponent(1));
	}
}


