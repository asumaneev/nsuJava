package ru.nsu.sumaneev.knot.functions;

import ru.nsu.sumaneev.knot.util.Point;

public class TorusKnot implements ParameterizedKnot {

	@Override
	public Point getPoint(double t) {
		
		t *= 2 * Math.PI;
		
		double x = ( (2 + Math.cos(3 * t)) * Math.cos(2 * t) );
		double y = ( (2 + Math.cos(3 * t)) * Math.sin(2 * t) );
		double z = ( Math.sin(3 * t) );
		
		return new Point(x, y, z);
	}

	@Override
	public double getPeriod() {
		// TODO Auto-generated method stub
		return 2 * Math.PI;
	}

}
