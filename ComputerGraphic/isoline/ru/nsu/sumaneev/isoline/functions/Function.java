package ru.nsu.sumaneev.isoline.functions;

public abstract class Function {

	
	//	D-area
	private int a = 0;
	private int b = 0;
	
	private int c = 0;
	private int d = 0;
	
	//	net
	private int k = 0;
	private int m = 0;
	
	protected double stepX = 0;
	protected double stepY = 0;
	
	protected double minValue = 0;
	protected double maxValue = 0;
	
	public Function(int a, int b, int c, int d, int k, int m) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.k = k;
		this.m = m;
		
		setMinMaxValues();
	}
	
	public void setA(int a) {
		this.a = a;
	}
	public void setB(int b) {
		this.b = b;
	}
	public void setC(int c) {
		this.c = c;
	}
	public void setD(int d) {
		this.d = d;
	}
	public void setK(int k) {
		this.k = k;
	}
	public void setM(int m) {
		this.m = m;
	}
	
	public void refresh() {
		setMinMaxValues();
	}
	
	/**
	 * 
	 * @param x x's coordinate
	 * @param y y's coordinate
	 * @return value of function in point (x, y)
	 * @throws IllegalArgumentException if point (x, y) is out from domain
	 */
	public abstract double getValue(double x, double y);
	
	public int getA() {
		return a;
	}
	public int getB() {
		return b;
	}
	public int getC() {
		return c;
	}
	public int getD() {
		return d;
	}
	public int getK() {
		return k;
	}
	public int getM() {
		return m;
	}
	
	public double getMinValue() {
		//return minValue + 0.1;
		return minValue;
	}
	public double getMaxValue() {
		//return maxValue - 0.1;
		return maxValue;
	}
	
	protected boolean checkPoint(double x, double y) {
		return (x >= a) || (x <= b) || (y >= c) || (y <= d);
	}

	protected void setMinMaxValues() {
		
		stepX = ( (double) (b - a) ) / k;
		stepY = ( (double) (d - c) ) / m;
		
		minValue = Double.POSITIVE_INFINITY;
		maxValue = Double.NEGATIVE_INFINITY;
		
		for (double x = a; x <= b; x += stepX) {
			for (double y = c; y <= d; y += stepY) {
				double value = getValue(x, y);
				if (value < minValue) {
					minValue = value;
				}

				if (value > maxValue) {
					maxValue = value;
				}
				
			}
		}
		
	}
}
