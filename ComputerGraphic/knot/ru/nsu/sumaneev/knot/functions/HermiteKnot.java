package ru.nsu.sumaneev.knot.functions;

import ru.nsu.sumaneev.knot.util.Point;

public class HermiteKnot implements ParameterizedKnot {

	private int pointsNumber = 0;
	
	
	private double[] startParameters = null;
	
	//	p0	
	private Point[] startPoints = null;
	//	p3
	//	start[i] = end[i + 1]
	private Point[] endPoints = null;
	
	//	derivatives in start points
	//	( p(k + 1) - pk ) / ( 2 * (t(k + 1) - tk) ) + (  pk - p(k - 1) ) / ( 2 * (tk - t(k - 1)) )
	private Point[] derPoints = null;
	

	private ParameterizedKnot knot = null;
	
	public HermiteKnot(int pointsNumber, ParameterizedKnot knot) {
		
		this.knot = knot;
		
		this.pointsNumber = pointsNumber;
		
		startPoints = new Point[pointsNumber];
		endPoints = new Point[pointsNumber];
		derPoints = new Point[pointsNumber];
		
		startParameters = new double[pointsNumber];

		double t = 0;
		double step = 1.0 / pointsNumber;
		for (int i = 0; i < pointsNumber; ++i) {

			startParameters[i] = t;
			
			startPoints[i] = knot.getPoint(t);
			endPoints[(i + pointsNumber - 1) % pointsNumber] = startPoints[i];
			
			t += step;
		}
		
		//	t is near to period (likely is period)
		derPoints[0] = getDerivative(startPoints[0], startPoints[pointsNumber - 1], endPoints[0], 0, t - step, t + step);
		
		t = step;
		for (int i = 1; i < pointsNumber; ++i) {
			derPoints[i] = getDerivative(startPoints[i], startPoints[i - 1], endPoints[i], t, t - step, t + step);
		}
	}
	
	@Override
	public Point getPoint(double t) {
		
		Point[] p = new Point[4];
		
		
		if (t >= startParameters[pointsNumber - 1]) {
			p[0] = startPoints[pointsNumber - 1];
			p[1] = endPoints[pointsNumber - 1];
			
			p[2] = derPoints[pointsNumber - 1];
			p[3] = derPoints[0];
		}
		else {
			for (int i = 0; i < pointsNumber - 1; ++i) {
					
				if ((t >= startParameters[i]) && (t < startParameters[i + 1])) {
					p[0] = startPoints[i];
					p[1] = endPoints[i];
					
					p[2] = derPoints[i];
					p[3] = derPoints[i + 1];
					
					break;
				}
				
			}
		}
		
		
		return getHermit(t, p);
	}

	@Override
	public double getPeriod() {
		return knot.getPeriod();
	}
	
	/**
	 * p[0] = pk
	 * p[1] = p(k + 1)
	 * p[2] = Dpk
	 * p[3] = Dp(k + 1)
	 * @param t
	 * @param p
	 * @return
	 */
	
	private Point getHermit(double t, Point[] p) {
		
		double[] pX = {p[0].getX(), p[1].getX(), p[2].getX(), p[3].getX()};
		double[] pY = {p[0].getY(), p[1].getY(), p[2].getY(), p[3].getY()};
		double[] pZ = {p[0].getZ(), p[1].getZ(), p[2].getZ(), p[3].getZ()};
		
		return new Point(
				getHermit(t, pX),
				getHermit(t, pY),
				getHermit(t, pZ)
				);
	}
	
	private double getHermit(double t, double[] p) {
		return (
					p[0] * ( 2 * Math.pow(t, 3) - 3 * Math.pow(t, 2) + 1 )
				+ 	p[1] * ( -2 *  Math.pow(t, 3) + 3 * Math.pow(t, 2) )
				+	p[2] * ( Math.pow(t, 3) - 2 * Math.pow(t, 2) + t )
				+	p[3] * ( Math.pow(t, 3) - Math.pow(t, 2) )
				
				);
	}
	
	
	private Point getDerivative(
			Point currentPoint, Point previousPoint, Point nextPoint, 
			double currentT, double previousT, double nextT) {
		
		return new Point(
				getDerivative(currentPoint.getX(), previousPoint.getX(), nextPoint.getX(), currentT, previousT, nextT) / 5,
				getDerivative(currentPoint.getY(), previousPoint.getY(), nextPoint.getY(), currentT, previousT, nextT) / 5,
				getDerivative(currentPoint.getZ(), previousPoint.getZ(), nextPoint.getZ(), currentT, previousT, nextT) / 5
				);
	}
	
	private double getDerivative(
			double currentValue, double previousValue, double nextValue, 
			double currentT, double previousT, double nextT) {
		
		return (( nextValue - currentValue ) / ( nextT - currentT ) + ( currentValue - previousValue ) / ( currentT - previousT )) / 2;
	}
}
