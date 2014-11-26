package ru.nsu.sumaneev.puzzle.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;


/**
 * Container for image which can be represent on desktop
 * Contains triangles in some positions depends on time range
 * 
 * @author Artem
 *
 */

public class Canvas {

	public static final int BACKGROUND_COLOR = Color.white.getRGB(); 
	
	private int WIDTH = 0;
	private int HEIGHT = 0;
	
	private BufferedImage canvas = null;
	
	private List<Triangle> triangles = new LinkedList<Triangle>();
	
	
	
	public Canvas(int width, int height) {
		
		setSize(width, height);
		
	}
	
	public void setSize(int width, int heght) {
		WIDTH = width;
		HEIGHT = heght;
		
		initCanvas();
		
		
	}

	public void addTriangle(Triangle t) {
		triangles.add(t);
	}
	
	public void drawAll() {
		for (Triangle t : triangles) {
			t.draw(canvas);
		}
	}
	
	
	public BufferedImage getBufferedImage() {
		return canvas;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public List<Triangle> getTriangles() {
		return triangles;
	}
	
	public void clear() {
		initCanvas();
		triangles.clear();
	}
	
	public void clearImage() {
		initCanvas();
	}
	
	private void initCanvas() {
		
		if (null == canvas) {
			canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB );
		}

		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				canvas.setRGB(i, j, BACKGROUND_COLOR);
			}
		}
	}
	
}
