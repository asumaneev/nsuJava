package ru.nsu.sumaneev.tetragon.painting;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import ru.nsu.sumaneev.knot.util.GaussianElimination;
import ru.nsu.sumaneev.tetragon.util.Point;
import ru.nsu.sumaneev.tetragon.util.Tetragon;
import ru.nsu.sumaneev.tetragon.util.TetragonException;
import ru.nsu.sumaneev.tetragon.view.MainWindow;

public class TetragonPainter {

	private static final Color LINE_COLOR = Color.RED;
	private static final Color POINT_COLOR = Color.BLUE;
	private static final int POINT_SIZE = 5;
	
	
	//private static final double EPS = 0.00001;
	
	private ResourceImage image = null;
	private Canvas canvas = null;
	private MainWindow mainWindow = null;
	
	private List<Tetragon> tetragons = null;
	private List<Point> points = null;
	
	public TetragonPainter(ResourceImage image, Canvas canvas, MainWindow mainWindow) {
		this.image = image;
		this.canvas = canvas;
		this.mainWindow = mainWindow;
		
		tetragons = new LinkedList<Tetragon>();
		points = new LinkedList<Point>();
	}
	
	public void draw() {
		
		if ((canvas.getWidth() != mainWindow.getWidth()) || (canvas.getHeight() != mainWindow.getHeight())) {
			canvas.resize(mainWindow.getDrawPanelWidth(), mainWindow.getDrawPanelHeight());			
		}
		
		canvas.clearCanvas();
		
		Graphics2D graphics = (Graphics2D) canvas.getBufferedImage().getGraphics();
		
		for (Tetragon tetragon : tetragons) {
			
			int minX = tetragon.getMinX();
			int maxX = tetragon.getMaxX();
			
			int minY = tetragon.getMinY();
			int maxY = tetragon.getMaxY();
			
			if ((maxX >= canvas.getWidth()) || (maxY >= canvas.getHeight())) {
				continue;
			}
			
			
			double lengthAB = tetragon.getA().getLength(tetragon.getB());
			double lengthBC = tetragon.getB().getLength(tetragon.getC());
			double lengthCD = tetragon.getC().getLength(tetragon.getD());
			double lengthAD = tetragon.getA().getLength(tetragon.getD());
			
			
			double maxLength = Math.max(Math.max(lengthAB, lengthBC), Math.max(lengthCD, lengthAD));
			image.initMip(maxLength);
			
			
			if (tetragon.isConcave()) {
				
				Point[] firstTriangle = new Point[3];
				Point[] secondTriangle = new Point[3];

				Point[] firstImageTriangle = new Point[3];
				Point[] secondImageTriangle = new Point[3];
				
				switch (tetragon.getConcaveVertexIndex()) {
				case 0:					
					firstTriangle[0] = tetragon.getA();
					firstTriangle[1] = tetragon.getB();
					firstTriangle[2] = tetragon.getC();
					
					firstImageTriangle[0] = new Point(0, 0);
					firstImageTriangle[1] = new Point(image.getWidth(), 0);
					firstImageTriangle[2] = new Point(image.getWidth(), image.getHeight());
					
					secondTriangle[0] = tetragon.getA();
					secondTriangle[1] = tetragon.getC();
					secondTriangle[2] = tetragon.getD();
					
					secondImageTriangle[0] = new Point(0, 0);
					secondImageTriangle[1] = new Point(image.getWidth(), image.getHeight());
					secondImageTriangle[1] = new Point(0, image.getHeight());
					
					break;
					
				case 2:
					
					firstTriangle[0] = tetragon.getC();
					firstTriangle[1] = tetragon.getA();
					firstTriangle[2] = tetragon.getB();
					
					firstImageTriangle[1] = new Point(image.getWidth(), image.getHeight());
					firstImageTriangle[2] = new Point(image.getWidth(), 0);
					firstImageTriangle[0] = new Point(0, 0);
					
					secondTriangle[0] = tetragon.getC();
					secondTriangle[1] = tetragon.getA();
					secondTriangle[2] = tetragon.getD();
					
					secondImageTriangle[0] = new Point(image.getWidth(), image.getHeight());
					secondImageTriangle[1] = new Point(0, 0);
					secondImageTriangle[2] = new Point(0, image.getHeight());
					
					break;
				case 1:
					
					firstTriangle[0] = tetragon.getB();
					firstTriangle[1] = tetragon.getA();
					firstTriangle[2] = tetragon.getD();
					
					firstImageTriangle[0] = new Point(image.getWidth(), 0);
					firstImageTriangle[1] = new Point(0, 0);
					firstImageTriangle[2] = new Point(0, image.getHeight());
					
					secondTriangle[0] = tetragon.getB();
					secondTriangle[1] = tetragon.getC();
					secondTriangle[2] = tetragon.getD();
					
					secondImageTriangle[0] = new Point(image.getWidth(), 0);
					secondImageTriangle[1] = new Point(image.getWidth(), image.getHeight());
					secondImageTriangle[2] = new Point(0, image.getHeight());
					
					break;
				case 3:
					firstTriangle[1] = tetragon.getB();
					firstTriangle[2] = tetragon.getA();
					firstTriangle[0] = tetragon.getD();
					
					firstImageTriangle[1] = new Point(image.getWidth(), 0);
					firstImageTriangle[2] = new Point(0, 0);
					firstImageTriangle[0] = new Point(0, image.getHeight());
					
					secondTriangle[1] = tetragon.getB();
					secondTriangle[2] = tetragon.getC();
					secondTriangle[0] = tetragon.getD();
					
					secondImageTriangle[1] = new Point(image.getWidth(), 0);
					secondImageTriangle[2] = new Point(image.getWidth(), image.getHeight());
					secondImageTriangle[0] = new Point(0, image.getHeight());
					
					break;
				}
				
//				double[][] matrix = new double[16][16];
//				
//				double[] leftVector = new double[16];
//				double[] rightVector = new double[16];
//				
//				for (int i = 0; i < 3; ++i) {
//					
//					leftVector[2 * i] = firstTriangle[i].getX();
//					leftVector[2 * i + 1] = firstTriangle[i].getY();
//					
//					leftVector[2 * i + 6] = secondTriangle[i].getX();
//					leftVector[2 * i + 6 + 1] = secondTriangle[i].getY();
//					
//					rightVector[2 * i] = firstImageTriangle[i].getX();
//					rightVector[2 * i + 1] = firstImageTriangle[i].getY();
//					
//					rightVector[2 * i + 6] = secondImageTriangle[i].getX();
//					rightVector[2 * i + 6 + 1] = secondImageTriangle[i].getY();
//				}
				
//				rasteriseTriangle(firstTriangle[0], firstTriangle[1], firstTriangle[2], Color.CYAN.getRGB());
//				rasteriseTriangle(secondTriangle[0], secondTriangle[1], secondTriangle[2], Color.ORANGE.getRGB());
				
				int det = 
						(secondTriangle[2].getX() - secondTriangle[0].getX()) * (secondTriangle[1].getY() - secondTriangle[0].getY())
						- (secondTriangle[1].getX() - secondTriangle[0].getX()) * (secondTriangle[2].getY() - secondTriangle[0].getY());
				
				 double[] secondParameters = new double[] {
						 1.0 * (secondTriangle[0].getY() - secondTriangle[2].getY()) / det, 
						 1.0 * (secondTriangle[2].getX() - secondTriangle[0].getX()) / det, 
						 1.0 * (secondTriangle[0].getX() * (secondTriangle[2].getY() - secondTriangle[0].getY()) - (secondTriangle[2].getX() - secondTriangle[0].getX()) * secondTriangle[0].getY()) / det, 
						 1.0 * (secondTriangle[1].getY() - secondTriangle[0].getY()) / det, 
						 1.0 * (secondTriangle[0].getX() - secondTriangle[1].getX()) / det, 
						 1.0 * ((secondTriangle[1].getX() - secondTriangle[0].getX()) * secondTriangle[0].getY() - (secondTriangle[1].getY() - secondTriangle[0].getY()) * secondTriangle[0].getX()) / det, 
						 0, 
						 0};
				 
				 rasteriseTriangle(firstTriangle[0], firstTriangle[1], firstTriangle[2], secondParameters);
			}
			else {
			
				double[][] matrix = new double[8][8];
				
				//double[] rightVector = {			//	from image to tetragon
				double[] leftVector = {				//	from tetragon to image
						tetragon.getA().getX(),
						tetragon.getA().getY(),
						
						tetragon.getB().getX(),
						tetragon.getB().getY(),
						
						tetragon.getC().getX(),
						tetragon.getC().getY(),
						
						tetragon.getD().getX(),
						tetragon.getD().getY()};
				//double[] leftVector = {
				double[] rightVector = {
						0, 0,
						image.getWidth(), 0,
						image.getWidth(), image.getHeight(),
						0, image.getHeight()
				};
				
				for (int i = 0; i < 4; ++i) {
					
					//	xi yi 1 0 0 0 -Xixi -Xiyi
					matrix[2 * i][0] = leftVector[2 * i];
					matrix[2 * i][1] = leftVector[2 * i + 1];
					matrix[2 * i][2] = 1;
					matrix[2 * i][3] = 0;
					matrix[2 * i][4] = 0;
					matrix[2 * i][5] = 0;
					matrix[2 * i][6] = -(rightVector[2 * i]) * leftVector[2 * i];
					matrix[2 * i][7] = -(rightVector[2 * i]) * leftVector[2 * i + 1];
					
					//	0 0 0 xi yi 1 -Yixi -Yiyi
					matrix[2 * i + 1][0] = 0;
					matrix[2 * i + 1][1] = 0;
					matrix[2 * i + 1][2] = 0;
					matrix[2 * i + 1][3] = leftVector[2 * i];
					matrix[2 * i + 1][4] = leftVector[2 * i + 1];
					matrix[2 * i + 1][5] = 1;
					matrix[2 * i + 1][6] = -(rightVector[2 * i + 1]) * leftVector[2 * i];
					matrix[2 * i + 1][7] = -(rightVector[2 * i + 1]) * leftVector[2 * i + 1];
					
				}
				
				//	a b c d e f g h 1
				double[] parameters = GaussianElimination.lsolve(matrix, rightVector);
				
				for (int y = minY; y < maxY; ++y) {
					for (int x = minX; x < maxX; ++x) {
				//for (int y = 0; y < canvas.getHeight(); ++y) {
					//for (int x = minX; x < canvas.getWidth(); ++x) {
						
						if (false == tetragon.pointBelongs(x, y, canvas.getWidth() - 1, canvas.getHeight() - 1)) {
							continue;
						}
						
						
						double X = ( 
								(parameters[0] * x + parameters[1] * y + parameters[2]) 
								/ (parameters[6] * x + parameters[7] * y + 1));
						double Y = ( 
								(parameters[3] * x + parameters[4] * y + parameters[5]) 
								/ (parameters[6] * x + parameters[7] * y + 1));
						
						canvas.setPixel(x, y, image.getColor((X), (Y)));
						
						
						/*
						if (true == tetragon.pointBelongs(x, y, canvas.getWidth() - 1, canvas.getHeight() - 1)) {
							continue;
						}
						
						int newX = minX + (maxX - x);
						int newY = minY + (maxY - y);
						
						if (false == tetragon.pointBelongs(newX, newY, canvas.getWidth() - 1, canvas.getHeight() - 1)) {
							continue;
						}
						
						double X = ( 
								(parameters[0] * x + parameters[1] * y + parameters[2]) 
								/ (parameters[6] * x + parameters[7] * y + 1));
						double Y = ( 
								(parameters[3] * x + parameters[4] * y + parameters[5]) 
								/ (parameters[6] * x + parameters[7] * y + 1));
						
						canvas.setPixel(newX, newY, image.getColor((X), (Y)));
						*/
					}
				}
			}
			
			graphics.setColor(LINE_COLOR);
			
			drawLine(tetragon.getA(), tetragon.getB(), graphics);
			drawLine(tetragon.getB(), tetragon.getC(), graphics);
			drawLine(tetragon.getC(), tetragon.getD(), graphics);
			drawLine(tetragon.getD(), tetragon.getA(), graphics);
			
			//graphics.setColor(POINT_COLOR);
			
			graphics.setColor(Color.black);
			drawPoint(tetragon.getA(), graphics);
			graphics.setColor(Color.blue);
			drawPoint(tetragon.getB(), graphics);
			graphics.setColor(Color.magenta);
			drawPoint(tetragon.getC(), graphics);
			graphics.setColor(Color.green);
			drawPoint(tetragon.getD(), graphics);
			
		}
		
		graphics.setColor(POINT_COLOR);
		
		for (Point point : points) {
			
			drawPoint(point, graphics);
			
		}
		
	}
	
	public void addTetragon(Tetragon tetragon) {
		tetragons.add(tetragon);
	}
	
	public void addPoint(Point p) {
		points.add(p);
		
		if (4 == points.size()) {
			
			
			try {
				addTetragon(new Tetragon(points.get(0), points.get(1), points.get(2), points.get(3)));
			} catch (TetragonException e) {
				System.out.println(e.getMessage());
			}
			
			points.clear();
		}
	}
	
	public void clear() {
		
		canvas.clearCanvas();
		
		tetragons.clear();
		points.clear();
	}
	
	private void drawPoint(Point p1, Graphics2D graphics) {
		graphics.fillOval(p1.getX(), p1.getY(), POINT_SIZE, POINT_SIZE);
	}
	
	private void drawLine(Point p1, Point p2, Graphics2D graphics) {
		graphics.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	private void rasteriseTriangle(Point p1, Point p2, Point p3, double[] parameters) {
		rasteriseTriangle(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), parameters);
	}
	
	private void rasteriseTriangle(int x1, int y1, int x2, int y2, int x3, int y3, double[] parameters) {
		
		//	order vertices by y's increasing
		
		if (y2 < y1) {
			
			int x = x1;
			int y = y1;
			
			x1 = x2;
			y1 = y2;
			
			x2 = x;
			y2 = y;
		}
	
		if (y3 < y1) {
			
			int x = x1;
			int y = y1;
			
			x1 = x3;
			y1 = y3;
			
			x3 = x;
			y3 = y;
		}

		if (y2 > y3) {
			
			int x = x2;
			int y = y2;
			
			x2 = x3;
			y2 = y3;
			
			x3 = x;
			y3 = y;
		}
		
		
		double dx13 = 0;
		double dx12 = 0;
		double dx23 = 0;
		
		
		if (y3 != y1) {
			dx13 = ((double) x3 - x1) / (y3 - y1);
			
		}
		
		if (y2 != y1) {
			dx12 = ((double) x2 - x1) / (y2 - y1);
		}
		
		if (y2 != y3) {
			dx23 = ((double) x3 - x2) / (y3 - y2);
		}
		
		
		double wx1 = x1;
		double wx2 = wx1;
		
		double dx13Orig = dx13;
		
		if (dx13 > dx12) {
			double d = dx13;
			dx13 = dx12;
			dx12 = d;
		}
		
		double xStart = 0;
		double xEnd = 0;
		
		//	upper half-triangle
		for (int i = y1; i < y2; ++i) {
			
			for (int j = (int) Math.round(wx1); j < Math.round(wx2); ++j) {
				
				double X = ( 
						(parameters[0] * j + parameters[1] * i + parameters[2]) 
						/ (parameters[6] * j + parameters[7] * i + 1));
				double Y = ( 
						(parameters[3] * j + parameters[4] * i + parameters[5]) 
						/ (parameters[6] * j + parameters[7] * i + 1));
				
				canvas.setPixel(j, i, image.getColor((X), (Y)));
				
			}
			wx1 += dx13;
			wx2 += dx12;
		}
		
		//	if there is no upper half-triangle
		if (y1 == y2) {
			wx1 = x1;
			wx2 = x2;
		}
		
		if (dx13Orig < dx23) {
			double d = dx13Orig;
			dx13Orig = dx23;
			dx23 = d;
		}
		
		if (wx1 > wx2) {
			xStart = wx2;
			xEnd = wx1;
		}
		else {
			xStart = wx1;
			xEnd = wx2;
		}
		
		//	lower half-triangle
		for (int i = y2; i < y3; ++i) {
			for (int j = (int) Math.round(xStart); j <= Math.round(xEnd); ++j) {
				
				double X = ( 
						(parameters[0] * j + parameters[1] * i + parameters[2]) 
						/ (parameters[6] * j + parameters[7] * i + 1));
				double Y = ( 
						(parameters[3] * j + parameters[4] * i + parameters[5]) 
						/ (parameters[6] * j + parameters[7] * i + 1));
				
				canvas.setPixel(j, i, image.getColor((X), (Y)));
				
			}
			xStart += dx13Orig;
			xEnd += dx23;
		}
		
		
		
	}
	
}
