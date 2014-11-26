package ru.nsu.sumaneev.isoline.painting;

import java.awt.image.BufferedImage;

public class Canvas {
	
	private BufferedImage canvas = null;
	
	//	u0 = 0, v0 = 0
	//	u1 = width, v1 = height
	
	private int width = 0;
	private int height = 0;
	
	
	public Canvas(int width, int height) {
		
		this.width = width;
		this.height = height;
		
		resetCanvas();		
	}
	
	public void resetCanvas() {
		
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	public void clearCanvas() {

		if (null == canvas) {
			resetCanvas();
		}
		else {
			for (int y = 0; y < height; ++y) {
				for (int x = 0; x < width; ++x) {
					canvas.setRGB(x, y, 0);
				}
			}
		}
	}
	
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		
		resetCanvas();
	}
	
	
	public BufferedImage getBufferedImage() {
		return canvas;
	}
	
	public void setPixel(int x, int y, int color) {
		canvas.setRGB(x, y, color);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getColor(int x, int y) {
		return canvas.getRGB(x, y);
	}
	
}
