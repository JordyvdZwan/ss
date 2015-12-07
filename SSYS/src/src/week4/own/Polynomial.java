package week4.own;

public class Polynomial implements Function, Integrandable {

	LinearProduct[] f;
	double[] n;
	
	public Polynomial(double[] n) {
		this.n = new double[n.length];
		System.arraycopy(n, 0, this.n, 0, n.length);
		f = new LinearProduct[n.length];
		for (int i = 0; i < n.length; i++) {
			f[i] = new LinearProduct(new Constant(n[i]) , new Exponent(i));
		}
	}

	@Override
	public double apply(double number) {
		double result = 0;
		for (int i = 0; i < f.length; i++) {
			result += f[i].apply(number);
		}
		return result;
	}

	@Override
	public Function derivative() {
		double[] g = new double[n.length - 1];
		for (int i = n.length - 1; i >= 1; i--) {
			g[i - 1] = n[i] * i;
			System.out.println(g[i - 1]);
		}
		return new Polynomial(g);
	}
	
	public Function integrand() {
		double[] g = new double[n.length + 1];
		for (int i = n.length - 1; i >= 0; i--) {
			g[i + 1] = n[i] / (i + 1);
		}
		g[0] = 0;
		return new Polynomial(g);
	}

	public String toString() {
		return n.toString();
	}
}
