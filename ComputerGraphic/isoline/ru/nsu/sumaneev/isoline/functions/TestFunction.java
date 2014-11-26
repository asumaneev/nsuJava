package ru.nsu.sumaneev.isoline.functions;

public class TestFunction extends Function {

	public TestFunction(int a, int b, int c, int d, int k, int m) {
		super(a, b, c, d, k, m);
	}

	@Override
	public double getValue(double x, double y) {

		if (false == super.checkPoint(x, y)) {
			throw new IllegalArgumentException("Point (" + x + ", " + y + ") is not in domain");
		}
		
		
		return Math.sin(x)+ 2 * Math.sin(x);
		//return Math.sin(0.1 * (x + y));
		//return x * x * x + y * y * y;
		//return x * x + y * y ;
		//return x + y;
	}
	
	

}
