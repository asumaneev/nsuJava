package ru.nsu.sumaneev.knot.util;

public class Point {

	private double x = 0;
	private double y = 0;
	private double z = 0;
	
	public Point() {
		
	}
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}


	public void setX(double x) {
		this.x = x;
	}


	public void setY(double y) {
		this.y = y;
	}


	public void setZ(double z) {
		this.z = z;
	}


	public double getX() {
		return x;
	}


	public double getY() {
		return y;
	}


	public double getZ() {
		return z;
	}
	
}
