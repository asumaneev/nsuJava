package ru.nsu.sumaneev.puzzle.model;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Triangle {

	private static int sideSegmentsNumber = 0;
	
	private static int canvasSize = 0;
	
	private static int imageSize = 0;
	
	//	ImageSize / SIDE_SEGMENTS_NUMBER
	private static int cathetusLength = 0;
	
	private static ResourceImage image = null;
	
	private static int borderColor = Color.black.getRGB();
	
	//	coordinates of triangle's vertices relative to triangle's center
	//	never change after first setting
	private static double[] verticesX = {0, 0, 0};
	private static double[] verticesY = {0, 0, 0};
	
	
	//	coordinates of vertices relative  to canvas
	private int[] canvasX = {0, 0, 0};
	private int[] canvasY = {0, 0, 0};
	
	
	//	moving coordinates of  triangle's center relative to canvas
	private double centerX = 0;
	private double centerY = 0;
	
	
	//	first and last coordinates of center relative to canvas
	private double startX = 0;
	private double startY = 0;
	private double endX = 0;
	private double endY = 0;
	
	
	//	coordinates of triangle's center relative to image
	//	never change after first setting
	private double imageX = 0;
	private double imageY = 0;
	
	
	//	transformation
	
	private double startAngle = 0;
	private double currentAngle = 0;
	
	//	matrix of affine transformation
	//	[cos		sin		0]
	//	[-sin		cos		0]
	//	[tx			ty		1]
	//	rotate relative to (0, 0) - center
	//	and move from (0, 0) to (centerX, centerY)
	private double[][] affineMatrix = new double[3][3];
	
	/**
	 * must be called before using class 
	 * @param image - resource image
	 * @param canvasSize - size of image will be painted
	 * @param sideSegmentsNumber - number of squares on the side
	 */
	public static void initializeTriangle(ResourceImage image, int canvasSize, int sideSegmentsNumber) {
		
		Triangle.image = image;
		Triangle.canvasSize = canvasSize;
		
		if (image.getHeight() != image.getWidth()) {
			
			//TODO: handle error
			
		}
		
		Triangle.imageSize = image.getWidth();
		
		Triangle.sideSegmentsNumber = sideSegmentsNumber;
		
		Triangle.cathetusLength = Triangle.imageSize / sideSegmentsNumber;
		
		
		//		relative to triangle's center stuff
		
		//	vertex 0
		verticesX[0] = ( - ((double) cathetusLength / 3) );
		verticesY[0] = ( - ((double) cathetusLength / 3) );
		//	vertex 1
		verticesX[1] = ( verticesX[0] );
		verticesY[1] = ( verticesY[0] + cathetusLength );
		//	vertex 2
		verticesX[2] = ( verticesX[0] + cathetusLength );
		verticesY[2] = ( verticesY[0] );
	}
	
	public static int getCathetusLength() {
		return cathetusLength;
	}
	
	public Triangle(double imageX, double imageY, double startX, double startY, double endX, double endY, double startAngle) {
		
		//	relative to image stuff
		
		this.imageX = imageX;
		this.imageY = imageY;
		
		
		//	relative to canvas stuff
		
		this.startX = startX;
		this.startY = startY;
		
		this.centerX = startX;
		this.centerY = startY;
		
		this.endX = endX;
		this.endY = endY;

		
		//	affine stuff
		
		if ((0 != startAngle) || (Math.PI == startAngle)) {
			//TODO: handle error	
		}
		
		this.startAngle = startAngle;
		this.currentAngle = startAngle;
		
		//	transport triangle from (0, 0) to (centerX, centerY)
		//	and rotate on startAngle
		setRotationMatrix(affineMatrix, startAngle);
		setTranslationMatrix(affineMatrix, 0, 0, centerX, centerY);
		
		transformVertices(canvasX, canvasY, verticesX, verticesY, affineMatrix);
		
	}
	
	public void setEndPoint(double endX, double endY) {
		this.endX = endX;
		this.endY = endY;
	}
	
	public void setCurrentAngle(double angle) {
		currentAngle = angle;
		
		setRotationMatrix(affineMatrix, currentAngle);
	}
	
	public void addAngle(double dAngle) {
		currentAngle += dAngle;
		
		setRotationMatrix(affineMatrix, currentAngle);
	}
	
	public void moveOn(double dx, double dy) {
		
		centerX += dx;
		centerY += dy;
		
		setTranslationMatrix(affineMatrix, 0, 0, centerX, centerY);
	}
	
	public void moveTo(double x, double y) {
		
		centerX = x;
		centerY = y;
		
		setTranslationMatrix(affineMatrix, 0, 0, centerX, centerY);
	}
	
	public void translateTriangle() {
		transformVertices(canvasX, canvasY, verticesX, verticesY, affineMatrix);
	}
	
	public void draw(BufferedImage canvas) {
		
		rasteriseTriangle(canvasX[0], canvasY[0], canvasX[1], canvasY[1], canvasX[2], canvasY[2], canvas);
		
		drawBresenhamLine(canvasX[0], canvasY[0], canvasX[1], canvasY[1], canvas);
		drawBresenhamLine(canvasX[0], canvasY[0], canvasX[2], canvasY[2], canvas);
		drawBresenhamLine(canvasX[2], canvasY[2], canvasX[1], canvasY[1], canvas);
		
	}
	
	public double getStartX() {
		return startX;
	}
	public double getStartY() {
		return startY;
	}
	
	public double getCurrentX() {
		return centerX;
	}
	public double getCurrentY() {
		return centerY;
	}
	
	public double getEndX() {
		return endX;
	}
	public double getEndY() {
		return endY;
	}
	
	/*
	 * movement of triangle
	 * 
	 */
	
	private void setRotationMatrix(double[][] matrix, double angle) {
		
		if (3 != matrix.length) {
			//TODO: handle error	
		}
		else if ((3 != matrix[0].length) || (3 != matrix[1].length) || (3 != matrix[1].length) ) {
			//TODO: handle error
		}
		
		matrix[0][0] = Math.cos(angle);
		matrix[0][1] = Math.sin(angle);
		
		matrix[1][0] = - matrix[0][1];
		matrix[1][1] = matrix[0][0];
		
		matrix[0][2] = 0;
		matrix[1][2] = 0;
		matrix[2][2] = 1;
	}
	
	private void setTranslationMatrix(double[][] matrix, double x1, double y1, double x2, double y2) {
		
		if (3 != matrix.length) {
			//TODO: handle error	
		}
		else if ((3 != matrix[0].length) || (3 != matrix[1].length) || (3 != matrix[1].length) ) {
			//TODO: handle error
		}
		
		matrix[2][0] = x2 - x1;
		matrix[2][1] = y2 - y1;
		
		matrix[0][2] = 0;
		matrix[1][2] = 0;
		matrix[2][2] = 1;
	}
	
	
	private void transformVertices(int[] newX, int[] newY, double[] oldX, double[] oldY,  double[][] transformMatrix) {
		for (int i = 0; i < 3; ++i) {
			
			double[] newCoordinates = transformCoordinates(oldX[i], oldY[i], transformMatrix);
			
			newX[i] = (int) Math.round(newCoordinates[0] );
			newY[i] = (int) Math.round( newCoordinates[1] );
		}
	}	
	
	private double[] transformCoordinates(double x, double y, double[][] transformMatrix) {
		
		double[] newCoordinates = {0, 0};
		
		newCoordinates[0] = ( transformMatrix[0][0] * x + transformMatrix[0][1] * y + transformMatrix[2][0] );
		newCoordinates[1] = ( transformMatrix[1][0] * x + transformMatrix[1][1] * y + transformMatrix[2][1] );
		
		
		return newCoordinates;
	}
	
	/*
	 * coordinates conversion
	 */
	
	private double[] fromCanvasToImage(int canvasX, int canvasY) {
		
		double[][] goHomeMatrix = new double[3][3];
		
		setRotationMatrix(goHomeMatrix, -currentAngle + startAngle);
		
		goHomeMatrix[2][0] = 0;
		goHomeMatrix[2][1] = 0;
		
		double[] imageCoordinates = transformCoordinates((double) canvasX - centerX, (double) canvasY - centerY, goHomeMatrix);
		
		imageCoordinates[0] += imageX;
		imageCoordinates[1] += imageY;
		
		
		
		return imageCoordinates;
	}
	
	
	/*
	 * drawing
	 */
	
	private void rasteriseTriangle(int x1, int y1, int x2, int y2, int x3, int y3, BufferedImage canvas) {
	
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
	
		/*
		int color = Color.red.getRGB();
		
		if (image.getFilter()) {
			
			if (image.getBlen()) {
				color = Color.green.getRGB();
			}
			else {
				color = Color.yellow.getRGB();
			}	
		}
		else if (image.getBlen()) {
			color = Color.blue.getRGB();
		}
		*/
		
		//	upper half-triangle
		for (int i = y1; i < y2; ++i) {
			
			for (int j = (int) Math.round(wx1); j < Math.round(wx2); ++j) {
				
				double[] imageCoordinates = fromCanvasToImage(j, i);
				
				try {
					canvas.setRGB(j, i, image.getBlend( image.getColor(imageCoordinates[0], imageCoordinates[1]), Canvas.BACKGROUND_COLOR ));
				}
				catch (Exception e) {
					System.out.println("x: " + centerX + " y: " + centerY);
					System.out.println(e.getMessage());
					System.exit(-1);
				}
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
			
				double[] imageCoordinates = fromCanvasToImage(j, i);
				
				try {
					canvas.setRGB(j, i, image.getBlend( image.getColor(imageCoordinates[0], imageCoordinates[1]), Canvas.BACKGROUND_COLOR ));
				}
				catch (Exception e) {
					System.out.println("x: " + j + " y: " + i);
					System.out.println(e.getMessage());
					System.exit(-1);
				}
			}
			xStart += dx13Orig;
			xEnd += dx23;
		}
		
		
		
	} 
	
	private void drawBresenhamLine(int x1, int y1, int x2, int y2, BufferedImage canvas) {
		
		int dx = x2 - x1;
		int dy = y2 - y1;
	 
		int incx = (dx > 0) ? 1 : (dx < 0) ? -1 : 0;
		int incy = (dy > 0) ? 1 : (dy < 0) ? -1 : 0;

		
		dx = Math.abs(dx);
		dy = Math.abs(dy);
	 
		int pdx = 0;
		int pdy = 0;
		int es = 0;
		int el = 0;
		
		if (dx > dy) {
			
			pdx = incx;
			es = dy;
			el = dx;
		}
		else {
			pdy = incy;
			es = dx;
			el = dy;
		}
	 
		int x = x1;
		int y = y1;
		int err = el/2;
		
		canvas.setRGB(x, y, borderColor);
	 
		for (int t = 0; t < el; ++t) {
			
			err -= es;
			
			if (err < 0)
			{
				err += el;
				x += incx;
				y += incy;
			}
			else
			{
				x += pdx;
				y += pdy;
			}
	 
			canvas.setRGB(x, y, borderColor);
		}
	}
	
}
