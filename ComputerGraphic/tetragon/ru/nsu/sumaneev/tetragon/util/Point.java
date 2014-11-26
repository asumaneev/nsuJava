package ru.nsu.sumaneev.tetragon.util;

public class Point {

	private int x = 0;
	private int y = 0;
	
	public Point() {
		
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}


	public void setX(int x) {
		this.x = x;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}
	
	public double getLength(Point other) {
		return 
					(other.getX() - this.getX()) * (other.getX() - this.getX())
				+	(other.getY() - this.getY()) * (other.getY() - this.getY());
	}
	
}
