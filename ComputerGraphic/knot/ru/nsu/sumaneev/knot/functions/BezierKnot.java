package ru.nsu.sumaneev.knot.functions;

import ru.nsu.sumaneev.knot.util.Point;

public class BezierKnot implements ParameterizedKnot {

	private int pointsNumber = 0;
	
	//	p3 * t^3 + p2 * t^2 * (1 - t) + p1 * t * (1 - t)^2 + p0 * (1 - t)^3 
	
	private double[] startParameters = null;
	
	//	p0	
	private Point[] startPoints = null;
	//	p3
	//	start[i] = end[i + 1]
	private Point[] endPoints = null;
	
	
	private ParameterizedKnot knot = null;
		
	public BezierKnot(int pointsNumber, ParameterizedKnot knot) {
		
		this.knot = knot;
		
		this.pointsNumber = pointsNumber;
		
		startPoints = new Point[pointsNumber];
		endPoints = new Point[pointsNumber];
		
		startParameters = new double[pointsNumber];
		
		double t = 0;
		double step = 1.0 / pointsNumber;
		for (int i = 0; i < pointsNumber; ++i) {
			
			startParameters[i] = t;
			
			startPoints[i] = knot.getPoint(t);
			endPoints[(i + pointsNumber - 1) % pointsNumber] = startPoints[i];
			
			t += step;
		}
	}

	@Override
	public Point getPoint(double t) {

		Point[] p = new Point[4];
		
		
		if (t >= startParameters[pointsNumber - 1]) {
			p[0] = startPoints[pointsNumber - 1];
			p[3] = endPoints[pointsNumber - 1];
		}
		else {
			for (int i = 0; i < pointsNumber - 1; ++i) {
					
				if ((t >= startParameters[i]) && (t < startParameters[i + 1])) {
					p[0] = startPoints[i];
					p[3] = endPoints[i];
					
					break;
				}
				
			}
		}
		
		return getBezierValue(t, p);
	}

	@Override
	public double getPeriod() {
		
		return knot.getPeriod();
	}
	
	private Point getBezierValue(double t, Point[] p) {
		
//		double[] pX = {p[0].getX(), p[1].getX(), p[2].getX(), p[3].getX()};
//		double[] pY = {p[0].getY(), p[1].getY(), p[2].getY(), p[3].getY()};
//		double[] pZ = {p[0].getZ(), p[1].getZ(), p[2].getZ(), p[3].getZ()};
		
		
		double[] pX = {p[0].getX(), 0, 0, p[3].getX()};
		double[] pY = {p[0].getY(), 0, 0, p[3].getY()};
		double[] pZ = {p[0].getZ(), 0, 0, p[3].getZ()};
		
		
		return new Point(
				getBezierValue(t, pX), 
				getBezierValue(t, pY), 
				getBezierValue(t, pZ));
	}
	
	private double getBezierValue(double t, double[] p) {
			
//		return 
//					p[0] 						* Math.pow(1 - t, 3)
//				+ 	p[1] 	* t 				* Math.pow(1 - t, 2)
//				+ 	p[2] 	* Math.pow(t, 2)	* (1 - t)
//				+	p[3]	* Math.pow(t, 3)
//				;
		
		return (1 - t) * p[0] + t * p[3];
	}
}
