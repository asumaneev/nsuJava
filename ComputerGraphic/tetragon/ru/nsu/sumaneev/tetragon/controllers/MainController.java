package ru.nsu.sumaneev.tetragon.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.swing.SwingUtilities;

import ru.nsu.sumaneev.tetragon.painting.Canvas;
import ru.nsu.sumaneev.tetragon.painting.ResourceImage;
import ru.nsu.sumaneev.tetragon.painting.TetragonPainter;
import ru.nsu.sumaneev.tetragon.util.Point;
import ru.nsu.sumaneev.tetragon.util.Tetragon;
import ru.nsu.sumaneev.tetragon.util.TetragonException;
import ru.nsu.sumaneev.tetragon.view.MainWindow;

public class MainController implements DrawPanelRepainter {

	private String imagePath = "images/earth3.png";
	//private String imagePath = "images/puzzle.png";
	
	private MainWindow mainWindow = null;
	
	private ResourceImage image = null;
	private Canvas canvas = null;
	
	private TetragonApexListener tetragonApexListener = null;
	private TetragonPainter tetragonPainter = null;
	
	public MainController() throws IOException {
		
		canvas = new Canvas(400, 400);
		image = new ResourceImage(imagePath);
		
		
		tetragonApexListener = new TetragonApexListener();

		mainWindow = new MainWindow(canvas.getWidth(), canvas.getHeight());
		
		tetragonPainter = new TetragonPainter(image, canvas, mainWindow);
		
		mainWindow.setDrawPanelRepainter(this);
		mainWindow.addMouseListener(tetragonApexListener);
		
		mainWindow.setClearButtonListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				tetragonPainter.clear();
				
				repaint();
				
			}
		});
		
		mainWindow.setTrilinearFilteringListener(new ActionListener() {
			
			private boolean flag = false;
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				
				flag = !flag;
				
				image.setTrilinearFiltering(flag);
				
				repaint();
			}
		});
		
		mainWindow.setRandomConcaveTetragonButtonListener(new ActionListener() {
			
			private Random random = new Random();
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
//				Point a = new Point(1, 1);
//				Point b = new Point(1, canvas.getHeight() - 1);
//				Point c = new Point(canvas.getWidth() - 1, canvas.getHeight() - 1);
//				Point d = new Point(canvas.getWidth() / 4, canvas.getHeight() / 2);
				
				
				for (;;) {
				
				
					Point a = new Point(
							getRandom(0, canvas.getWidth() / 2 - 20), 
							getRandom(0, canvas.getHeight() / 2));
					
					Point b = new Point(
							getRandom(0, canvas.getWidth() / 2 - 20),
							getRandom(canvas.getHeight() / 2 + a.getY() / 4, canvas.getHeight() - 1));
					
					Point c = new Point(
							getRandom(canvas.getWidth() / 2 + a.getX() / 4, canvas.getWidth() - 1),
							getRandom(canvas.getHeight() / 2 + a.getY() / 4, canvas.getHeight() - 1));
					
					Point d = new Point(
							getRandom(0, canvas.getWidth() / 2 - 20),
							getRandom(canvas.getHeight() / 2 + a.getY() / 4, canvas.getHeight() - 1));
				
					
					if (b.getX() < d.getX()) {
						b.setX(d.getX() + (d.getX() - a.getX()) / 6);
					}
					
					if (b.getY() > d.getY()) {
						b.setY(d.getY() - (c.getY() - d.getY()) / 6);
					}
					
					if (b.getX() < a.getX()) {
						b.setX(a.getX() + (c.getX() - a.getX()) / 6);
					}
					if (b.getY() > c.getY()) {
						b.setY(c.getY() - (c.getY() - a.getY()) / 6);
					}
					
					Tetragon t = null;
					try {
						t = new Tetragon(a, b, c, d);
					} catch (TetragonException e) {
						System.out.println(e.getMessage());
						
						break;
					}
					
					if (t.isConcave()) {
					
						tetragonPainter.addTetragon(t);
						
						break;
					}
					
					
					
				}
				
				repaint();
			}
			
			private int getRandom(int min, int max) {
				
				if (min > max) {
					return max;
				}
				
				return random.nextInt(max - min) + min;
			}
		});
		
		mainWindow.setRandomConvexTetragonButtonListener(new ActionListener() {
			
			private Random random = new Random();
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				/*
				Point a = new Point(canvas.getWidth() / 2, canvas.getHeight() - 1);
				Point b = new Point(1, canvas.getHeight() / 2);
				Point c = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
				Point d = new Point(canvas.getWidth() - 1, canvas.getHeight() / 2);
				*/
				
				
				for (;;) {
				
					Point a = new Point(
							getRandom(0, canvas.getWidth() / 2 - 20), 
							getRandom(0, canvas.getHeight() / 2));
					
					Point b = new Point(
							getRandom(canvas.getWidth() / 2 + a.getX() / 4, canvas.getWidth() - 1),
							getRandom(0, canvas.getHeight() / 2));
					
					Point c = new Point(
							getRandom(canvas.getWidth() / 2 + a.getX() / 4, canvas.getWidth() - 1),
							getRandom(canvas.getHeight() / 2 + a.getY() / 4, canvas.getHeight() - 1));
					
					Point d = new Point(
							getRandom(0, canvas.getWidth() / 2 - 20),
							getRandom(canvas.getHeight() / 2 + a.getY() / 4, canvas.getHeight() - 1));
					
					
					Tetragon t = null;
					try {
						t = new Tetragon(a, b, c, d);
					} catch (TetragonException e1) {
						System.out.println(e1.getMessage());
						
						break;
					}
					
					if (!t.isConcave()) {
					
						tetragonPainter.addTetragon(t);
						
						break;
					}
				
				}
				
				repaint();
			}
			
			private int getRandom(int min, int max) {
				
				if (min > max) {
					return max;
				}
				
				return random.nextInt(max - min) + min;
			}
		});
		
	}
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {

            public void run() {
            	mainWindow.setVisible(true);
            }
        });
	}

	@Override
	public BufferedImage getImage() {
		
		tetragonPainter.draw();
		
		return canvas.getBufferedImage();
	}
	
	
	private void repaint() {
		
		mainWindow.repaint();
	}
	
	private class TetragonApexListener implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			
			addPoint(arg0.getX(), arg0.getY());
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}
	
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			//mouseClicked(arg0);
		}
		
		private void addPoint(int x, int y) {
	
			tetragonPainter.addPoint(new Point(x, y));
			
			repaint();
			
		}
		
	}
}
