package ru.nsu.sumaneev.knot.functions;

import ru.nsu.sumaneev.knot.util.Point;

public class TrefoilKnot implements ParameterizedKnot {

	public Point getPoint(double t) {
		
		t *= 2 * Math.PI;
		
		double x = ( Math.sin(t) + 2 * Math.sin(2 * t) );
		double y = ( Math.cos(t) - 2 * Math.cos(2 * t) );
		double z = ( -Math.sin(3 * t) );
		
		
		
		return new Point(x, y, z);
	}
	
	
	@Override
	public double getPeriod() {
		return 2 * Math.PI;
	}
}
