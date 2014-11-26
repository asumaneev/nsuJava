package ru.nsu.sumaneev.knot.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

import ru.nsu.sumaneev.knot.functions.BezierKnot;
import ru.nsu.sumaneev.knot.functions.HermiteKnot;
import ru.nsu.sumaneev.knot.functions.ParameterizedKnot;
import ru.nsu.sumaneev.knot.functions.SplineKnot;
import ru.nsu.sumaneev.knot.functions.TorusKnot;
import ru.nsu.sumaneev.knot.functions.TrefoilKnot;
import ru.nsu.sumaneev.knot.painting.Canvas;
import ru.nsu.sumaneev.knot.painting.KnotPainter;
import ru.nsu.sumaneev.knot.view.MainWindow;

public class MainController implements DrawPanelRepainter {

	private int pointsNumber = 40;
	
	
	private ParameterizedKnot knot = null;
	private KnotPainter knotPainter = null;
	
	
	private MainWindow mainWindow = null;
	private MainWindowListener mainWindowListener = null;
	
	private Canvas canvas = null;
	
	public MainController() {
		
		//	initialize knot
		knot = new TrefoilKnot();
		

		//	initialize canvas
		//TODO: change on minimum size of knot (cube)
		canvas = new Canvas(400, 400);
		
		//	initialize mainWindow
		mainWindow = new MainWindow(canvas.getWidth(), canvas.getHeight());
		mainWindow.setDrawPanelRepainter(this);
		
		mainWindowListener = new MainWindowListener();
		
		mainWindow.addKeyListener(mainWindowListener);
		mainWindow.addMouseListener(mainWindowListener);
		mainWindow.addMouseMotionListener(mainWindowListener);
		mainWindow.addMouseWheelListener(mainWindowListener);
		
		
		mainWindow.setKnotTypeListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				mainWindow.setFocusable(true);
				
				JComboBox<KnotType> source = (JComboBox<KnotType>) e.getSource();
				
				source.setFocusable(false);
				
				KnotType type = (KnotType) source.getSelectedItem();
				
				if (KnotType.TREFOIL_KNOT == type) {
					knot = new TrefoilKnot();
					
					knotPainter.setKnot(knot);
				}
				else if (KnotType.TOURUS_KNOT == type) {
					knot = new TorusKnot();
					
					knotPainter.setKnot(knot);
				}
				else if (KnotType.BEZIER_TREFOIL_KNOT == type) {
					knot = new BezierKnot(pointsNumber, new TrefoilKnot());
					
					knotPainter.setKnot(knot);
				}
				else if (KnotType.HERMITE_TREFOIL_KNOT == type) {
					knot = new HermiteKnot(pointsNumber, new TrefoilKnot());
					
					knotPainter.setKnot(knot);
				}
				else if (KnotType.SPLINE_TREFOIL_KNOT == type) {
					knot = new SplineKnot(pointsNumber, new TrefoilKnot());
					
					knotPainter.setKnot(knot);
				}
				
				
				repaint();
			}
		});
		
		//	initialize painter
		knotPainter = new KnotPainter(knot, canvas, mainWindow, 100, 
				mainWindowListener.getCurrentDistanceFromScreenToKnot(), 
				mainWindowListener.getCurrentHorizontalAngle(), 
				mainWindowListener.getCurrentVerticalAngle());
		
		
		repaint();
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
		
		knotPainter.refresh();
		knotPainter.drawCube();
		knotPainter.drawKnot();
		
		return canvas.getBufferedImage();
	}
	
	
	private void repaint() {
		
		mainWindow.setDistanceLabel(mainWindowListener.getCurrentDistanceFromScreenToKnot());
		mainWindow.setHorizontalAngleLabel(mainWindowListener.getCurrentHorizontalAngle());
		mainWindow.setVerticalAngleLabel(mainWindowListener.getCurrentVerticalAngle());
		
		mainWindow.repaint();
	}
	
	private class MainWindowListener extends MouseInputAdapter implements KeyListener {

		private static final int ANGLE_STEPS_NUMBER = 100;
		private static final int TIME_DELAY = 30;
		
		private double currentHorizontalAngle = 0;
		private double currentVerticalAngle = 0;
		private double currentDistanceFromScreenToKnot = 100;
		
		private double period = 2 * Math.PI;
		
		private double angleStep = period / ANGLE_STEPS_NUMBER;
		private double scaleStep = 6.0;
		
		
		private Set<Integer> pressedKeys = null;
		
		private Timer keyRepaintTimer = null;
		
		
		
		private int centerX = 0;
		private int centerY = 0;
		
		public MainWindowListener() {
			 pressedKeys = new HashSet<>();
			 
			 keyRepaintTimer = new Timer(TIME_DELAY, new ActionListener() {
				
				 @Override
				public void actionPerformed(ActionEvent e) {
					
					
					if (0 == pressedKeys.size()) {
						return;
					}
					
					for (Integer keyCode : pressedKeys) {
						
						switch (keyCode) {
						case KeyEvent.VK_UP:
						
							currentVerticalAngle -= angleStep;
							
							/*
							if (currentVerticalAngle < 0) {
								currentVerticalAngle = period + currentVerticalAngle;
							}
							*/
							knotPainter.setVerticalAngle(currentVerticalAngle);
							
							break;
						case KeyEvent.VK_DOWN:
							
							currentVerticalAngle += angleStep;
							
							/*
							if (currentVerticalAngle >= period) {
								currentVerticalAngle = 0;
							}
							*/
							
							knotPainter.setVerticalAngle(currentVerticalAngle);
							
							break;
							
						case KeyEvent.VK_LEFT:
							
							currentHorizontalAngle += angleStep;
							/*
							if (currentHorizontalAngle >= period) {
								currentHorizontalAngle = 0;
							}
							*/
							knotPainter.setHorizontalAngle(currentHorizontalAngle);
							
							break;
							
						case KeyEvent.VK_RIGHT:
							
							currentHorizontalAngle -= angleStep;
							/*
							if (currentHorizontalAngle < 0) {
								currentHorizontalAngle = period + currentHorizontalAngle;
							}
							*/
							knotPainter.setHorizontalAngle(currentHorizontalAngle);
							
							break;
						case KeyEvent.VK_X:
						case KeyEvent.VK_PLUS:
							
							currentDistanceFromScreenToKnot += scaleStep;
							knotPainter.setDistanceFromScreenToKnot(currentDistanceFromScreenToKnot);
							
							break;
						case KeyEvent.VK_Z:
						case KeyEvent.VK_MINUS:
							
							currentDistanceFromScreenToKnot -= scaleStep;
							knotPainter.setDistanceFromScreenToKnot(currentDistanceFromScreenToKnot);
							
							break;
						}				
					}
				
					repaint();
				}	
			});
			 
			 keyRepaintTimer.start();
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			int code = e.getKeyCode();
			
			if ( (KeyEvent.VK_UP == code) 
					|| (KeyEvent.VK_UP == code) 
					|| (KeyEvent.VK_DOWN == code) 
					|| (KeyEvent.VK_LEFT == code)
					|| (KeyEvent.VK_RIGHT == code)
					|| (KeyEvent.VK_X == code)
					|| (KeyEvent.VK_Z == code) ) {
				pressedKeys.add(code);
			}
			
			if (false == keyRepaintTimer.isRunning()) {
				keyRepaintTimer.start();
			}
		}

		public double getCurrentHorizontalAngle() {
			return currentHorizontalAngle;
		}

		public double getCurrentVerticalAngle() {
			return currentVerticalAngle;
		}

		public double getCurrentDistanceFromScreenToKnot() {
			return currentDistanceFromScreenToKnot;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
			pressedKeys.remove(e.getKeyCode());
			
			if (0 == pressedKeys.size()) {
				keyRepaintTimer.stop();
			}
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
	
		}
		
		@Override
        public void mouseWheelMoved( MouseWheelEvent e ) {
            currentDistanceFromScreenToKnot += e.getWheelRotation() * scaleStep;
            
            knotPainter.setDistanceFromScreenToKnot(currentDistanceFromScreenToKnot);
            
            repaint();
        }

       
        @Override
        public void mousePressed( MouseEvent e ) {
        	centerX = e.getX();
        	centerY = e.getY();
        }

        @Override
        public void mouseDragged( MouseEvent e ) {

        	//currentHorizontalAngle = (currentHorizontalAngle + (1) * Math.toRadians(centerX - e.getX()) / 2 + period) % period;
        	currentHorizontalAngle = (currentHorizontalAngle + (1) * Math.toRadians(centerX - e.getX()) / 2);
        	
        	//currentVerticalAngle = (currentVerticalAngle + (-1) * Math.toRadians(centerY - e.getY()) / 2 + period) % period;
        	currentVerticalAngle = (currentVerticalAngle + (-1) * Math.toRadians(centerY - e.getY()) / 2);
        	
        	centerX = e.getX();
        	centerY = e.getY();
        	
        	knotPainter.setHorizontalAngle(currentHorizontalAngle);
        	knotPainter.setVerticalAngle(currentVerticalAngle);
        	
        	repaint();
        }
	}

}
