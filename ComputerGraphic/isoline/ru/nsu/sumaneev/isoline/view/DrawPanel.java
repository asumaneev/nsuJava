package ru.nsu.sumaneev.isoline.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import ru.nsu.sumaneev.isoline.painting.DrawPanelRepainter;

public class DrawPanel extends JPanel {

	
	private BufferedImage currentImage = null;
	
	private DrawPanelRepainter repainter = null;
	
	public DrawPanel(int width, int height) {
		
		setSize(width, height);
	}
	
	public void paint(BufferedImage newImage) {
		currentImage = newImage;
		
		repaint();
	}
	
	
	@Override
    public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		doDraw(g);
			
	}
	
	public void setDrawPanelRepainter(DrawPanelRepainter repainter) {
		this.repainter = repainter;
	}
	
	public void setMouseMoveListener(MouseMotionListener listener) {
		addMouseMotionListener(listener);
	}
	
	public void setMouseClickListener(MouseListener listener) {
		addMouseListener(listener);
	}
	
	private void doDraw(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		//if (null != repainter) 
		{
			currentImage = repainter.getImage();
		}
		
		//if (null != currentImage) 
		{
			
			g2d.drawImage(currentImage, null, 0, 0);
		
		}
		
	}

}
