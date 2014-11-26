package ru.nsu.sumaneev.tetragon.painting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ResourceImage {
	
	private List<BufferedImage> mipLevels = null;
	
	private BufferedImage image = null;
	private BufferedImage image2 = null;
	
	private boolean isTrilinearFilteringOn = false;
	
	public ResourceImage(String path) throws IOException {
		
		image = ImageIO.read(new File(path));
		
		mipLevels = new ArrayList<BufferedImage>(11);
		
		generateMipLevels();
	}
	
	public void setTrilinearFiltering(boolean flag) {
		isTrilinearFilteringOn = flag;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public void initMip(double length) {
		
		int k = 0;
		
		double diff = 1;
		for (int i = 0; i < mipLevels.size(); ++i) {
			
			if (Math.abs((1 - length / mipLevels.get(i).getWidth())) < diff) {
				k = i;
				diff = Math.abs(1 - length / mipLevels.get(i).getWidth());
			}
		}
		
		image = mipLevels.get(k);
		
		
		if (0 == k) {
			image2 = mipLevels.get(k + 1);
			
		}
		else if (mipLevels.size() - 1 == k) {
			image2 = mipLevels.get(k - 1);
		}
		//	length > width
		//	length2 < width
		else if (length > image.getWidth()) {
			image2 = mipLevels.get(k - 1);
		}
		//	length < width
		//	length2 > width
		else {
			image2 = mipLevels.get(k + 1);
		}
		//System.out.println(length + " " + image.getWidth() + " " + image2.getWidth());
		
	}
	
	public int getColor(double x, double y) {
		
		int result = 0;
		
		if (isTrilinearFilteringOn) {
			
			//int color = getBilinearColor(x, y, image);
			int color = image.getRGB(
					checkBorderX((int) Math.round(x), image), 
					checkBorderY((int) Math.round(y), image));
			
			double x2 = (image.getWidth() > image2.getWidth()) ? x / 2 : x * 2;
			double y2 = (image.getHeight() > image2.getHeight()) ? y / 2 : y * 2;
			
			//int color2 = getBilinearColor(x2, y2, image2);
			int color2 = image2.getRGB(
					checkBorderX((int) Math.round(x2), image2), 
					checkBorderY((int) Math.round(y2), image2));
			
			
			for (int i = 0; i < 3; ++i) {
				
				int component = (getColorComponent(color, i) + getColorComponent(color2, i)) / 2;
				
				result = setColorComponent(result, i, component);
			}
			 
		} else {
			result = getBilinearColor(x, y, image);
		}
		
		
		return result;
	}
	
	private void generateMipLevels() {
		mipLevels.add(image);
		
		BufferedImage previousLevel = image;
		BufferedImage currentLevel = new BufferedImage(previousLevel.getWidth() / 2, previousLevel.getHeight() / 2, previousLevel.getType());
		
		while (currentLevel.getWidth() != 1) {
			
			for (int y = 0; y < currentLevel.getHeight(); ++y) {
				for (int x = 0; x < currentLevel.getWidth(); ++x) {
					
					/*
					Color color1 = new Color(previousLevel.getRGB(2 * x, 2 * y));
					Color color2 = new Color(previousLevel.getRGB(2 * x + 1, 2 * y));
					Color color3 = new Color(previousLevel.getRGB(2 * x, 2 * y + 1));
					Color color4 = new Color(previousLevel.getRGB(2 * x + 1, 2 * y + 1));
					
					
					
					int red = (int) Math.round(((double) color1.getRed() + color2.getRGB() + color3.getRed() + color4.getRed()) / 4);
					int green = (int) Math.round(((double) color1.getGreen() + color2.getGreen() + color3.getGreen() + color4.getGreen()) / 4);
					int blue = (int) Math.round(((double) color1.getBlue() + color2.getBlue() + color3.getBlue() + color4.getBlue()) / 4);
					
					Color c = new Color(red, green, blue);
					
					currentLevel.setRGB(x, y, c.getRGB());
					*/
					
					
					int color1 = (previousLevel.getRGB(2 * x, 2 * y));
					int color2 = (previousLevel.getRGB(2 * x + 1, 2 * y));
					int color3 = (previousLevel.getRGB(2 * x, 2 * y + 1));
					int color4 = (previousLevel.getRGB(2 * x + 1, 2 * y + 1));
					
					
					int color = 0;
					
					for (int i = 0; i < 3; ++i) {
						color = setColorComponent(color, i, 
									(
									getColorComponent(color1, i)
								+	getColorComponent(color2, i)
								+	getColorComponent(color3, i)
								+	getColorComponent(color4, i)
								) / 4
								);
					}
					
					currentLevel.setRGB(x, y, color);
				}
			}
			
			mipLevels.add(currentLevel);
			previousLevel = currentLevel;
			currentLevel = new BufferedImage(previousLevel.getWidth() / 2, previousLevel.getHeight() / 2, previousLevel.getType());
		}
	}
	
	private int getBilinearColor(double x, double y, BufferedImage image) {
		
		double xRatio = x - Math.floor(x);
		double yRatio = y - Math.floor(y);
		
		int lesserX = checkBorderX( (int) Math.round(x), image );
		int lesserY = checkBorderY( (int) Math.round(y), image );
		
		int greaterX = (lesserX == image.getWidth() - 1) ? lesserX : lesserX + 1;
		int greaterY = (lesserY == image.getHeight() - 1) ? lesserY : lesserY + 1;
		
		int result = 0;
		
        int ambientColors[] = {
        		image.getRGB( lesserX, lesserY ), 
        		image.getRGB( greaterX, lesserY ), 
        		image.getRGB( lesserX, greaterY ),
        		image.getRGB( greaterX, greaterY )};

        for ( int i = 0; i < 4; i++) {
        	
        	double component = (
        				getColorComponent(ambientColors[0], i) * (1 - xRatio) * (1 - yRatio)
        			+ 	getColorComponent(ambientColors[1], i) * xRatio * (1 - yRatio)
        			+ 	getColorComponent(ambientColors[2], i) * (1 - xRatio) * yRatio
        			+ 	getColorComponent(ambientColors[3], i) * xRatio * yRatio
        			); 
        	
            result = setColorComponent(result, i, (int) component);
        }
		
		return result;
	}
	
	private int checkBorderX(int x, BufferedImage image) {
		
		if (x >= image.getWidth()) {
			return image.getWidth() - 1;
		}
		
		if (x < 0) {
			return 0;
		}
		
		return x;
	}
	
	private int checkBorderY(int y, BufferedImage image) {
		
		if (y >= image.getHeight()) {
			return image.getHeight() - 1;
		}
		
		if (y < 0) {
			return 0;
		}
		
		return y;
	}
	
	private int getColorComponent(int color, int index) {
		
		return (color >> 8 * index) & 0xFF;
		
	}
	
	private int setColorComponent(int color, int index, int component) {

		return color | (component << (index * 8) );
		
	}
}

