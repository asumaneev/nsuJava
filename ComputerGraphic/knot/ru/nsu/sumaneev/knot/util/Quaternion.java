package ru.nsu.sumaneev.knot.util;

public class Quaternion {

	private double w = 0;
	
	private double x = 0;
	private double y = 0;
	private double z = 0;
	
	public Quaternion(double w, double x, double y, double z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Quaternion(Point p) {
		w = 0;
		
		x = p.getX();
		y = p.getY();
		z = p.getZ();
	}
	
	public Quaternion(double angle, Point p) {
		
		w = Math.cos(angle / 2);
		
		double sin = Math.sin(angle / 2);
		x = p.getX() * sin;
		y = p.getY() * sin;
		z = p.getZ() * sin;
	}
	
	public void setW(double w) {
		this.w = w;
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
	
	public Quaternion add(Quaternion q) {
		double w = this.w + q.w;
		double x = this.x + q.x;
		double y = this.y + q.y;
		double z = this.z + q.z;
		
		return new Quaternion(w, x, y, z);
	}

	public Quaternion mul(Quaternion q) {

		double w = this.w * q.w - this.x * q.x - this.y * q.y + this.z * q.z;
		double x = this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y;
		double y = this.w * q.y + this.y * q.w + this.z * q.x - this.x * q.z;
		double z = this.w * q.z + this.z * q.w + this.x * q.y - this.y * q.x;
		
		return  new Quaternion(w, x, y, z);		
	}

	
	public Quaternion div(double value) {
		
		double w = this.w / value;
		double x = this.x / value;
		double y = this.y / value;
		double z = this.z / value;
		
		return new Quaternion(w, x, y, z);
	}
	
	public Quaternion conjugated() {
		return new Quaternion(w, -x, -y, -z);
	}
	
	public double norm() {
		return (w * w + x * x + y * y + z * z);
	}
	
	public Quaternion inv() {
		double norm = norm();
		
		return conjugated().div(norm);
	}
	
	public Point toPoint() {
		return new Point(x, y, z);
	}
	
}
