package ru.nsu.sumaneev.knot.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Utilites {
	
	private static final double SCALE = 100;

	public static Point fromKnotToScreenCoordinates(Point knotPoint, double distanceFromScreenToKnotCenter,
			double horizontalAngle, double verticalAngle) {
		
		// rotation: q * v * q^-1
		
		Quaternion v = new Quaternion(knotPoint);
		
		//	horizontal rotation
		Quaternion q = new Quaternion(horizontalAngle, new Point(0, 1, 0));
		v = q.mul(v).mul(q.inv());
		
		v.setW(0);
		
		q = new Quaternion(verticalAngle, new Point(1, 0, 0));
		v = q.mul(v).mul(q.inv());
		
		Point result = v.toPoint();
		result.setZ(result.getZ() + distanceFromScreenToKnotCenter);
		
		return result;
	}
	
	
	public static double getScreenX(Point p, double distanceToEye, double screenWidth) {
		//	k * x / (z + k)
		
		double x = SCALE * (( distanceToEye * p.getX() ) / ( distanceToEye + p.getZ() )) + screenWidth / 2;
		
		return x;
	}
	
	public static double getScreenY(Point p, double distanceToEye, double screenHeight) {
		//	k * y / (z + k)
		
		double y = SCALE * 	(( distanceToEye * p.getY() ) / ( distanceToEye + p.getZ() )) + screenHeight / 2;
		
		return y;
	}
	
	
	public static void drawLine(int x1, int y1, int x2, int y2, BufferedImage canvas, Color color) {
		Graphics2D g =  (Graphics2D) canvas.getGraphics();
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
	}
}
