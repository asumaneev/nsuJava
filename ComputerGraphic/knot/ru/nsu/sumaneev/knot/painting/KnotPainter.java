package ru.nsu.sumaneev.knot.painting;

import java.awt.Color;

import ru.nsu.sumaneev.knot.functions.ParameterizedKnot;
import ru.nsu.sumaneev.knot.util.Point;
import ru.nsu.sumaneev.knot.util.Utilites;
import ru.nsu.sumaneev.knot.view.MainWindow;


public class KnotPainter {
	
	private static final double STEPS_NUMBER = 1000;
	private static final Color KNOT_COLOR = Color.BLACK;
	private static final Color[] CUBE_COLORS = { Color.RED, Color.GREEN, Color.BLUE };
	

	private Canvas canvas = null;
	private MainWindow mainWindow = null;
	
	
	private ParameterizedKnot knot = null;
	
	private double timeStep = 0;
	
	//	in screen coordinates: k
	private double distanceToEye = 0;
	private double distanceFromScreenToKnot = 0;
	
	private double horizontalAngle = 0;
	private double verticalAngle = 0;
	
	
	private Point[] cubePoints = null;
	
	
	private boolean refreshed = false;
	
	public KnotPainter(ParameterizedKnot knot, Canvas canvas, MainWindow mainWindow, 
			double distanceToEye, double distanceFromScreenToKnotCenter, double horizontalAngle, double verticalAngle) {
		
		this.knot = knot;
		this.canvas = canvas;
		this.distanceToEye = distanceToEye;
		this.distanceFromScreenToKnot = distanceFromScreenToKnotCenter;
		this.mainWindow = mainWindow;
		this.horizontalAngle = horizontalAngle;
		this.verticalAngle = verticalAngle;
		
		
		timeStep = knot.getPeriod() / STEPS_NUMBER / (2 * Math.PI);
		
		cubePoints = new Point[8];
		initCube();
	}
	
	public void setKnot(ParameterizedKnot knot) {
		this.knot = knot;
		
		timeStep = knot.getPeriod() / STEPS_NUMBER / (2 * Math.PI);
		
		initCube();
	}
	
	public void setDistanceFromScreenToKnot(double distanceFromScreenToKnot) {
		this.distanceFromScreenToKnot = distanceFromScreenToKnot;
	}
	
	public void setHorizontalAngle(double angle) {
		horizontalAngle = angle;
	}
	
	public void setVerticalAngle(double angle) {
		verticalAngle = angle;
	}

	public void refresh() {
		
		if (!refreshed) {
			if ((canvas.getWidth() != mainWindow.getWidth()) || (canvas.getHeight() != mainWindow.getHeight())) {
				canvas.resize(mainWindow.getDrawPanelWidth(), mainWindow.getDrawPanelHeight());			
			}
			
			canvas.clearCanvas();
		}
		
		refreshed = true;
		
	}
	
	public void drawKnot() {
		
		refresh();

		double t = 0;
		
		Point firstKnotPoint = Utilites.fromKnotToScreenCoordinates(
				knot.getPoint(t),
				distanceFromScreenToKnot, 
				horizontalAngle, 
				verticalAngle);
		
		t += timeStep;
		Point secondKnotPoint = Utilites.fromKnotToScreenCoordinates(
				knot.getPoint(t),
				distanceFromScreenToKnot, 
				horizontalAngle, 
				verticalAngle);
		
		
		int xS1 = (int) Utilites.getScreenX(firstKnotPoint, distanceToEye, canvas.getWidth());
		int yS1 = (int) Utilites.getScreenY(firstKnotPoint, distanceToEye, canvas.getHeight());
		
		int xS2 = (int) Utilites.getScreenX(secondKnotPoint, distanceToEye, canvas.getWidth());
		int yS2 = (int) Utilites.getScreenY(secondKnotPoint, distanceToEye, canvas.getHeight());
		
		Utilites.drawLine(xS1, yS1, xS2, yS2, canvas.getBufferedImage(), KNOT_COLOR);
		
		int startX = xS1;
		int startY = yS1;
		
		t += timeStep;
		//TODO: is this t < 1 or other period?
		for (; t < 1; t += timeStep) {
			
			firstKnotPoint = secondKnotPoint;
			secondKnotPoint = Utilites.fromKnotToScreenCoordinates(
					knot.getPoint(t),
					distanceFromScreenToKnot, 
					horizontalAngle, 
					verticalAngle);
			
			xS1 = (int) Utilites.getScreenX(firstKnotPoint, distanceToEye, canvas.getWidth());
			yS1 = (int) Utilites.getScreenY(firstKnotPoint, distanceToEye, canvas.getHeight());
			
			xS2 = (int) Utilites.getScreenX(secondKnotPoint, distanceToEye, canvas.getWidth());
			yS2 = (int) Utilites.getScreenY(secondKnotPoint, distanceToEye, canvas.getHeight());
			
			Utilites.drawLine(xS1, yS1, xS2, yS2, canvas.getBufferedImage(), KNOT_COLOR);
		}
		
		Utilites.drawLine(xS2, yS2, startX, startY, canvas.getBufferedImage(), KNOT_COLOR);
		
		refreshed = false;
	}
	
	public void drawCube() {
		
		Point[] cubePointsInScreen = new Point[8];
		int[] xx = new int[8];
		int[] yy = new int[8];
		
		for (int i = 0; i < 8; ++i) {
			cubePointsInScreen[i] = Utilites.fromKnotToScreenCoordinates(
					cubePoints[i], 
					distanceFromScreenToKnot, 
					horizontalAngle, 
					verticalAngle);
			
			xx[i] = (int) Utilites.getScreenX(cubePointsInScreen[i], distanceToEye, canvas.getWidth());
			yy[i] = (int) Utilites.getScreenY(cubePointsInScreen[i], distanceToEye, canvas.getHeight());
		}
		
		
		for (int i = 0; i < 4; ++i) {
			
			if (i != 3)
			{
				
				Utilites.drawLine(
						xx[i], yy[i], 
						xx[i + 1], yy[i + 1], 
						canvas.getBufferedImage(), CUBE_COLORS[0]);
				
				Utilites.drawLine(
						xx[i + 4], yy[i + 4], 
						xx[i + 5], yy[i + 5], 
						canvas.getBufferedImage(), CUBE_COLORS[1]);
				
			
			}
			
			
			Utilites.drawLine(
					xx[i], yy[i], 
					xx[i + 4], yy[i + 4], 
					canvas.getBufferedImage(), CUBE_COLORS[2]);
			
		}
		
		
		Utilites.drawLine(
				xx[0], yy[0], 
				xx[3], yy[3], 
				canvas.getBufferedImage(), CUBE_COLORS[2]);
		
		Utilites.drawLine(
				xx[4], yy[4], 
				xx[7], yy[7], 
				canvas.getBufferedImage(), CUBE_COLORS[2]);
		
		//refreshed = false;

	}

	private void initCube() {
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		
		double minZ = Double.MAX_VALUE;
		double maxZ = Double.MIN_VALUE;
		
		for (double t = 0; t < knot.getPeriod() / (2 * Math.PI); t += timeStep ) {
			
			Point p = knot.getPoint(t);
			
			if (p.getX() < minX) {
				minX = p.getX();
			}
			
			if (p.getX() > maxX) {
				maxX = p.getX();
			}
			
			if (p.getY() < minY) {
				minY = p.getY();
			}
			
			if (p.getY() > maxY) {
				maxY = p.getY();
			}
			
			if (p.getZ() < minZ) {
				minZ = p.getZ();
			}
			
			if (p.getZ() > maxZ) {
				maxZ = p.getZ();
			}
		}
		
		cubePoints[0] = new Point(minX, minY, minZ);
		cubePoints[1] = new Point(minX, maxY, minZ);
		cubePoints[2] = new Point(minX, maxY, maxZ);
		cubePoints[3] = new Point(minX, minY, maxZ);
		cubePoints[4] = new Point(maxX, minY, minZ);
		cubePoints[5] = new Point(maxX, maxY, minZ);
		cubePoints[6] = new Point(maxX, maxY, maxZ);
		cubePoints[7] = new Point(maxX, minY, maxZ);
	}
}
