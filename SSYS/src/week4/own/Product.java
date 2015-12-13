package week4.own;

public class Product implements Function {

	Function f1;
	Function f2;
	
	public Product(Function f1, Function f2) {
		this.f1 = f1;
		this.f2 = f2;
	}
	
	@Override
	public double apply(double number) {
		return f1.apply(number) * f2.apply(number);
	}

	@Override
	public Function derivative() {
		return new Sum(new Product(f1, f2.derivative()), new Product(f1.derivative(), f2));
	}
	
	public String toString() {
		return f1.toString() + " * " + f2.toString();
	}
	
}
