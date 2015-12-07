package week4.own;

public class Sum implements Function, Integrandable {
	Function f1;
	Function f2;
	
	
	public Sum(Function f1, Function f2) {
		this.f1 = f1;
		this.f2 = f2;
	}
	
	@Override
	public double apply(double number) {
		return f1.apply(number)+f2.apply(number);
	}

	@Override
	public Function derivative() {
		return new Sum(f1.derivative(), f2.derivative());
	}

	public Function integrand() {
		Function result;
		if (f1 instanceof Integrandable && f2 instanceof Integrandable) {
			Integrandable g1 = (Integrandable) f1;
			Integrandable g2 = (Integrandable) f2;
			result = new Sum(g1.integrand(), g2.integrand());
		} else {result = null;}
		return result;
		
	}
	
	public String toString() {
		
	}
}
