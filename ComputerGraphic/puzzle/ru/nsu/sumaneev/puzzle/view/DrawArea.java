package ru.nsu.sumaneev.puzzle.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DrawArea extends JPanel {

	private static int imageWidth = 0;
	private static int imageHeight = 0;
	
	private BufferedImage currentImage = null;
	
	public DrawArea(int width, int height) {
				
		imageWidth = width;
		imageHeight = height;		
	}
	
	
	@Override
    public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		doDraw(g);
			
	}
	
	public void paint(BufferedImage newImage) {
		
		currentImage = newImage;
		
		repaint();
	}
	
	
	private void doDraw(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;

		//TODO: remove IF
		if (null != currentImage) {
			
			g2d.drawImage(currentImage, null, (getWidth() - imageWidth) / 2, (getHeight() - imageHeight) / 2);
		
		}
		
	}
}
