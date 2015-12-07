package week4.own;

public class LinearProduct extends Product implements Function, Integrandable {

	public LinearProduct(Constant f1, Function f2) {
		super(f1, f2);
	}
	
	public Function derivative() {
		return new Product(super.f1, super.f2.derivative());
	}
	
	public Function integrand() {
		Function result;
		if (f2 instanceof Integrandable) {
			Integrandable g2 = (Integrandable) super.f2;
			result = new Product(super.f1, g2.integrand());
		} else {
			result = null;
		}
		return result;
	}
	public String toString(){
		return (f1 " + " f2.to)
	}
}
