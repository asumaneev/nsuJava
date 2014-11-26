package ru.nsu.sumaneev.isoline.functions;

public class LegendFunction extends Function {

	public LegendFunction(int a, int b, int c, int d, int k, int m) {
		super(a, b, c, d, k, m);
	}

	@Override
	public double getValue(double x, double y) {

		if (false == super.checkPoint(x, y)) {
			throw new IllegalArgumentException("Point (" + x + ", " + y + ") is not in domain");
		}

		
		return -y;
	}

}
