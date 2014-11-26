package ru.nsu.sumaneev.knot.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import ru.nsu.sumaneev.knot.controllers.DrawPanelRepainter;

public class DrawPanel extends JPanel {

	
	private BufferedImage currentImage = null;
	
	private DrawPanelRepainter repainter = null;
	
	public DrawPanel(int width, int height) {
		
		setSize(width, height);
	}
	
	public void setRepainter(DrawPanelRepainter repainter) {
		this.repainter = repainter;
	}
	
	@Override
    public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		doDraw(g);
			
	}
	
	private void doDraw(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		if (null != repainter)
			currentImage = repainter.getImage();
		
		
		if (null != currentImage)
			g2d.drawImage(currentImage, null, 0, 0);
	}

}
