package ru.nsu.sumaneev.knot.functions;

import ru.nsu.sumaneev.knot.util.GaussianElimination;
import ru.nsu.sumaneev.knot.util.Point;

public class SplineKnot implements ParameterizedKnot {

	private ParameterizedKnot knot = null;
	
	private int pointsNumber = 0;
	
	private double[][] a = null;
	private double[][] b = null;
	private double[][] c = null;
	private double[][] d = null;
	private double[][] e = null;
	private double[][] f = null;
	
	//	h1, h2, ...
	private double dt = 0;
	
	
	public SplineKnot(int pointsNumber, ParameterizedKnot knot) {
		
		this.pointsNumber = pointsNumber;
		this.knot = knot;
		
		dt = 1.0 / pointsNumber;
		
		double[] h = {
			1,
			dt,
			Math.pow(dt, 2),
			Math.pow(dt, 3),
			Math.pow(dt, 4),
			Math.pow(dt, 5)
				
		};
		
		a = new double[3][pointsNumber];
		b = new double[3][pointsNumber];
		c = new double[3][pointsNumber];
		d = new double[3][pointsNumber];
		e = new double[3][pointsNumber];
		f = new double[3][pointsNumber];

		for (int i = 0; i < pointsNumber; ++i) {

			Point p = knot.getPoint(i * dt);
			
			a[0][i] = p.getX();
			a[1][i] = p.getY();
			a[2][i] = p.getZ();
		}
		
		int size = 5 * pointsNumber;
		   
		for (int k = 0; k < 3; ++k) {
			
			double[][] parametersMatrix = new double[size][size];
	        double[] rightVector = new double[size];

			//	a[i] + b[i]h + c[i]^2 + d[i]h^3 + e[i]^4 + f[i]h^5 = g(t[i + 1])
			for (int i = 0; i < pointsNumber; ++i) {
	
				parametersMatrix[i][i] 						= h[1];
				parametersMatrix[i][i + pointsNumber] 		= h[2];
				parametersMatrix[i][i + 2 * pointsNumber] 	= h[3];
				parametersMatrix[i][i + 3 * pointsNumber] 	= h[4];
				parametersMatrix[i][i + 4 * pointsNumber] 	= h[5];
				
				double t = (i + 1) % pointsNumber * dt;
				
				switch (k) {
				case 0:
					rightVector[i] = knot.getPoint(t).getX() - a[0][i];
					break;
				case 1:
					rightVector[i] = knot.getPoint(t).getY() - a[1][i];
					break;
				case 2:
					rightVector[i] = knot.getPoint(t).getZ() - a[2][i];
					break;
				default:
					return;
				}
			}
			
			//	b[i] - b[i + 1] + 2c[i]h + 3d[i]h^2 + 4e[i]h^3 + 5f[i]h^4
			for (int i = 1 ; i < pointsNumber; ++i) {
				int j = pointsNumber + i - 1;
				
				parametersMatrix[j][i - 1] 						= h[0];
				parametersMatrix[j][i]							= -h[0];
				parametersMatrix[j][i - 1 + pointsNumber]		= 2 * h[1];
				parametersMatrix[j][i - 1 + 2 * pointsNumber]	= 3 * h[2];
				parametersMatrix[j][i - 1 + 3 * pointsNumber]	= 4 * h[3];
				parametersMatrix[j][i - 1 + 4 * pointsNumber]	= 5 * h[4];
				
			}
			
			parametersMatrix[pointsNumber + pointsNumber - 1][pointsNumber - 1]						= h[0];
			parametersMatrix[pointsNumber + pointsNumber - 1][pointsNumber + 0]						= -h[0];
			parametersMatrix[pointsNumber + pointsNumber - 1][pointsNumber - 1 + pointsNumber]		= 2 * h[1];
			parametersMatrix[pointsNumber + pointsNumber - 1][pointsNumber - 1 + 2 * pointsNumber]	= 3 * h[2];
			parametersMatrix[pointsNumber + pointsNumber - 1][pointsNumber - 1 + 3 * pointsNumber]	= 4 * h[3];
			parametersMatrix[pointsNumber + pointsNumber - 1][pointsNumber - 1 + 4 * pointsNumber]	= 5 * h[4];
			
			
			//	c[i] - c[i + 1] + 3d[i]h + 6e[i]h^2 + 10f[i]h^3
			for (int i = 1; i < pointsNumber; ++i) {
				int j = 2 * pointsNumber + i - 1;
				
				parametersMatrix[j][i - 1 + pointsNumber]		 = h[0];
				parametersMatrix[j][i + pointsNumber]			 = -h[0];
				parametersMatrix[j][i - 1 + 2 * pointsNumber]	 = 3 * h[1];
				parametersMatrix[j][i - 1 + 3 * pointsNumber]	 = 6 * h[2];
				parametersMatrix[j][i - 1 + 4 * pointsNumber]	 = 10 * h[3];
			}
			
			parametersMatrix[2 * pointsNumber + pointsNumber - 1][pointsNumber + pointsNumber - 1]		= h[0];
			parametersMatrix[2 * pointsNumber + pointsNumber - 1][pointsNumber + pointsNumber]			= -h[0];
			parametersMatrix[2 * pointsNumber + pointsNumber - 1][pointsNumber - 1 + 2 * pointsNumber]	= 3 * h[1];
			parametersMatrix[2 * pointsNumber + pointsNumber - 1][pointsNumber - 1 + 3 * pointsNumber]	= 6 * h[2];
			parametersMatrix[2 * pointsNumber + pointsNumber - 1][pointsNumber - 1 + 4 * pointsNumber]	= 10 * h[3];
	
			
			//	d[i] - d[i + 1] + 4e[i]h + 10f[i]h^2
			for (int i = 1; i < pointsNumber; ++i) {
				int j = 3 * pointsNumber + i - 1;
				
				
				parametersMatrix[j][i - 1 + 2 * pointsNumber]	= h[0];
				parametersMatrix[j][i + 2 * pointsNumber]		= -h[0];
				parametersMatrix[j][i - 1 + 3 * pointsNumber]	= 4 * h[1];
				parametersMatrix[j][i - 1 + 4 * pointsNumber]	= 10 * h[2];
			}
			
			parametersMatrix[3 * pointsNumber + pointsNumber - 1][pointsNumber - 1 + 2 * pointsNumber]	= h[0];
			parametersMatrix[3 * pointsNumber + pointsNumber - 1][pointsNumber + 2 * pointsNumber]		= -h[0];
			parametersMatrix[3 * pointsNumber + pointsNumber - 1][pointsNumber - 1 + 3 * pointsNumber]	= 4 * h[1];
			parametersMatrix[3 * pointsNumber + pointsNumber - 1][pointsNumber - 1 + 4 * pointsNumber]	= 10 * h[2];
			
			
			//	e[i] - e[i + 1] + 5f[i]h
			for (int i = 1; i < pointsNumber; ++i) {
				int j = 4 * pointsNumber + i - 1;
				
				
				parametersMatrix[j][i - 1 + 3 * pointsNumber]	= h[0];
				parametersMatrix[j][i + 3 * pointsNumber]		= -h[0];
				parametersMatrix[j][i - 1 + 4 * pointsNumber]	= 5 * h[1];
			}
			
			parametersMatrix[4 * pointsNumber + pointsNumber - 1][pointsNumber - 1 + 3 * pointsNumber]	= h[0];
			parametersMatrix[4 * pointsNumber + pointsNumber - 1][pointsNumber + 3 * pointsNumber]		= -h[0];
			parametersMatrix[4 * pointsNumber + pointsNumber - 1][pointsNumber - 1 + 4 * pointsNumber]	= 5 * h[1];
			
			/*
			for (int i = 0; i < size; ++i) {
				
				for (int j = 0; j < size; ++j) {
					System.out.print(parametersMatrix[j][i] + " ");
				}
				
				System.out.println("   " + rightVector[i]);
				
			}
			System.out.println();
			*/
			
			double[] result = GaussianElimination.lsolve(parametersMatrix, rightVector);
			
			for (int i = 0; i < pointsNumber; ++i) {
				b[0][i] = result[i];
				c[0][i] = result[i + pointsNumber];
				d[0][i] = result[i + 2 * pointsNumber];
				e[0][i] = result[i + 3 * pointsNumber];
				f[0][i] = result[i + 4 * pointsNumber];
			}
		
		}
	}
	
	@Override
	public Point getPoint(double t) {
		
		return getSpline(t);
	}

	@Override
	public double getPeriod() {
		return knot.getPeriod();
	}

	
	private Point getSpline(double t) {
		
		int i = 0;

		if (t >= (pointsNumber - 1) * dt) {
			i = pointsNumber - 1;
		}
		else {
		
			for (int k = 0; k < pointsNumber - 1; ++k) {
				
				if ((t >= k * dt) && (t < (k + 1) * dt)) {
					i = k;
					break;
				} 
				
			}
			
		}
	
		return new Point(
				getSpline( t - i * dt, a[0][i], b[0][i], c[0][i], d[0][i], e[0][i], f[0][i]),
				getSpline( t - i * dt, a[1][i], b[1][i], c[1][i], d[1][i], e[1][i], f[1][i]),
				getSpline( t - i * dt, a[2][i], b[2][i], c[2][i], d[2][i], e[2][i], f[2][i])
				);
	}
	
	private double getSpline(double dt, double a, double b, double c, double d, double e, double f) {
		return (
					a
				+ 	b * Math.pow(dt, 1)
				+ 	c * Math.pow(dt, 2)
				+ 	d * Math.pow(dt, 3)
				+ 	e * Math.pow(dt, 4)
				+ 	f * Math.pow(dt, 5)
				);
	}
}
