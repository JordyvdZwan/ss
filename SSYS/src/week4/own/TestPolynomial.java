package week4.own;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestPolynomial {

	@Before
	public void setUp() throws Exception {
		double[] g = new double[] {3,2,1};
		Polynomial poly = new Polynomial(g);
	}

	@Test
	public void testApply() {
		double[] g = new double[] {3,2,1};
		Polynomial poly = new Polynomial(g);
		assertEquals(poly.apply(2),11.0,0.02);
	}
	
	@Test
	public void testDerivative() {
		double[] g = new double[] {3,2,1};
		Polynomial poly = new Polynomial(g);

		assertEquals(poly.derivative().apply(2),6.0,0.02);
	}
	
	@Test
	public void testIntegrand() {
		double[] g = new double[] {3,2,1};
		Polynomial poly = new Polynomial(g);
		assertEquals(poly.integrand().apply(2),12.666666666,0.02);
	}

}
