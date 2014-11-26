package ru.nsu.sumaneev.puzzle.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ResourceImage {
	
	private BufferedImage image = null;
	
	private boolean isFilterOn = false;
	private boolean isBlendOn = false;
	
	
	public ResourceImage(String path) throws IOException {
		
		image = ImageIO.read(new File(path));
	}
	
	
	
	public void setFilter(boolean isFilterOn) {
		this.isFilterOn = isFilterOn;
	}
	
	public void setBlend(boolean isBlendOn) {
		this.isBlendOn = isBlendOn;
	}
	
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public BufferedImage getBufferedImage() {
		return image;
	}
	
	
	public int getColor(double x, double y) {
		
		int color = 0;
		
		if (isFilterOn) {
			color = getBilinearColor(x, y);
		}
		else {
			color = getSimpleColor(x, y);
		}
			
		return color;
	}
	
	public int getBlend(int src, int dest) {

		if (!isBlendOn) {
			return src;
		}
	
		double alpha = getColorComponent(src, 3) / 255.0;
		
		int result = setColorComponent(0, 3, 0xFF);
		
		for (int i = 0; i < 3; ++i) {

			double component = getColorComponent(dest, i) + (getColorComponent(src, i) - getColorComponent(dest, i)) * alpha;
			
			result = setColorComponent(result, i, (int) component);
		}
		
		
		 return result;		 
	}

	private int getSimpleColor(double x, double y) {
		
		int xx = checkBorderX( (int) Math.round(x) );
		int yy = checkBorderY( (int) Math.round(y) );
		
		return image.getRGB( xx, yy );
	}
	
	private int getBilinearColor(double x, double y) {
				
		double xRatio = x - Math.floor(x);
		double yRatio = y - Math.floor(y);
		
		int lesserX = checkBorderX( (int) Math.round(x) );
		int lesserY = checkBorderY( (int) Math.round(y) );
		
		int greaterX = (lesserX == getWidth() - 1) ? lesserX : lesserX + 1;
		int greaterY = (lesserY == getHeight() - 1) ? lesserY : lesserY + 1;
		
		int result = 0;
		
        int ambientColors[] = {
        		image.getRGB( lesserX, lesserY ), 
        		image.getRGB( greaterX, lesserY ), 
        		image.getRGB( lesserX, greaterY ),
        		image.getRGB( greaterX, greaterY )};

        for ( int i = 0; i < 4; i++) {
        	
        	double component = (
        			getColorComponent(ambientColors[0], i) * (1 - xRatio) * (1 - yRatio)
        			+ getColorComponent(ambientColors[1], i) * xRatio * (1 - yRatio)
        			+ getColorComponent(ambientColors[2], i) * (1 - xRatio) * yRatio
        			+ getColorComponent(ambientColors[3], i) * xRatio * yRatio
        			); 
        	
            result = setColorComponent(result, i, (int) component);
        }
		
		return result;
	}
	
	private int checkBorderX(int x) {
		
		if (x >= getWidth()) {
			return getWidth() - 1;
		}
		
		if (x < 0) {
			return 0;
		}
		
		return x;
	}
	
	private int checkBorderY(int y) {
		
		if (y >= getHeight()) {
			return getHeight() - 1;
		}
		
		if (y < 0) {
			return 0;
		}
		
		return y;
	}
	
	/**
	 * 
	 * @param color
	 * @param index : 0 - red, 1 - green, 2 - blue, 3 - alpha
	 * @return
	 */
	private int getColorComponent(int color, int index) {
		
		return (color >> 8 * index) & 0xFF;
		
	}
	
	/**
	 * 
	 * @param color
	 * @param index : 0 - red, 1 - green, 2 - blue, 3 - alpha
	 * @param component
	 * @return
	 */
	private int setColorComponent(int color, int index, int component) {

		return color | (component << (index * 8) );
		
	}
}
