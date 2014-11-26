package ru.nsu.sumaneev.tetragon.util;

public class Line {

	private int startX = 0;
	private int startY = 0;
	
	private int endX = 0;
	private int endY = 0;
	
	private boolean isLineVertical = false;
	
	
	private double k = 0;
	private double b = 0;
	
	
	public Line(int startX, int startY, int endX, int endY) {

		
		this.startX = startX;
		this.startY = startY;
		
		this.endX = endX;
		this.endY = endY;
		
		
		if (startX == endX) {
			isLineVertical = true;
		}
		else {
			k = ((double)(endY  - startY)) / (endX - startX);
			b = startY - k * startX;
			
		}
		
	}
	
	public double getY(double x) {
		return k * x + b;
	}
	
	public boolean isLineVertical() {
		return isLineVertical;
	}
	
	public double getStartX() {
		return startX;
	}

	public double getStartY() {
		return startY;
	}

	public double getEndX() {
		return endX;
	}

	public double getEndY() {
		return endY;
	}

	public double[] getIntersection(Line other) {
		
		
		double[] result = null;
		
		if (other.isLineVertical) {
			
			if (this.isLineVertical) {
				
				if (other.startX != this.startX) {
					return null;
				}
				
				result = new double[1];
				result[0] = this.startX;
				
				return result;
			}
			
			double x = other.startX;
			if ((x < this.startX) || (x > this.endX)) {
				return null;
			}
			double y = this.getY(x);
			
			result = new double[2];
			result[0] = x;
			result[1] = y;
			
			return result;
		}
		
		if (this.isLineVertical) {
			
			double x = this.startX;
			if ((x < other.startX) || (x > other.endX)) {
				return null;
			}
			double y = other.getY(x);
			
			result = new double[2];
			result[0] = x;
			result[1] = y;
			
			return result;
			
		}
		
		
		double x = ( (other.b - this.b) / (other.k - this.k) );
		
		if ((x < this.startX) || (x > this.endX) || (x < other.startX) || (x > other.endX)) {
			return null;
		}
		double y = this.getY(x);
		result = new double[2];
		result[0] = x;
		result[1] = y;
		return result;

		
	}
	
	public boolean getIntersection2(Line other) {
		
		double v1 = 
				(other.endX - other.startX) * (this.startY - other.startY)
			- 	(other.endY - other.startY) * (this.startX - other.startX);
		double v2 = 
					(other.endX - other.startX) * (this.endY - other.startY)
				- 	(other.endY - other.startY) * (this.endX - other.startX);
		double v3 = 
					(this.endX - this.startX) * (other.startY - this.startY)
				-	(this.endY - this.startY) * (other.startX - this.startX);
		double v4 = 
					(this.endX - this.startX) * (other.endY - this.startY)
				- 	(this.endY - this.startY) * (other.endX - this.startX);
		
		boolean result = ((v1 * v2 <= 0) && (v3 * v4 < 0));
		
		return result;
		
		/*
		return (
					intersect_1(this.startX, this.endX, other.startX, other.endX)
				&&	intersect_1(this.startY, this.endY, other.startY, other.endY)
				&&	(
						area(this.startX, this.startY, this.endX, this.endY, other.startX, other.startY) 
						* area(this.startX, this.startY, this.endX, this.endY, other.endX, other.endY) <= 0)
				&&	(
						area(other.startX, other.startY, other.endX, other.endY, this.startX, this.startY)
						* area(other.startX, other.startY, other.endX, other.endY, this.endX, this.endX) <= 0)
				);
		*/
	}
	
	private boolean intersect_1(int a, int b, int c, int d) {
		
		if (a > b) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		if (c > d) {
			int tmp = c;
			c = d;
			d = tmp;
		}
		
		return Math.max(a, c) <= Math.min(b, d);
	}
	
	private int area(int ax, int ay, int bx, int by, int cx, int cy) {

		return ( (bx - ax) * (cy - ay) - (by - ay) * (cx - ax) );
	} 
	
}
