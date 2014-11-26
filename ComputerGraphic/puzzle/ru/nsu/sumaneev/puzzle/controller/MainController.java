package ru.nsu.sumaneev.puzzle.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.nsu.sumaneev.puzzle.model.Canvas;
import ru.nsu.sumaneev.puzzle.model.ResourceImage;
import ru.nsu.sumaneev.puzzle.model.Triangle;
import ru.nsu.sumaneev.puzzle.view.MainWindow;

public class MainController {

	private static final int SIDE_SEGMENTS_NUMBER = 4;
	
	private double imageOnCanvasX = 0;
	private double imageOnCanvasY = 0;
	
	private double sideLength = 0;
	
	private double centerPointLength = 0;
	
	
	//	GUI
	private MainWindow window = null;
	
	
	//	action listeners
	private InitAnimationActionListern initAnimationListener	 			= new InitAnimationActionListern();
	private StartStopAnimationActionListern startStopAnimationActionListern = new StartStopAnimationActionListern();
	private SlideAnimationChangeListern slideAnimationChangeListern			= new SlideAnimationChangeListern();
	private FilterActionListern	filterActionListern 						= new FilterActionListern();
	private BlendActionListern blendActionListern 							= new BlendActionListern(); 
	
	
	// resources
	private String imagePath = "images/puzzle.png";
	private ResourceImage resourceImage = null;
	
	
	//	canvas where triangles will be prepared
	private Canvas canvas = null;
	
	
	private AnimationController animationController = null;
	
	public MainController() {
		
		try {
			resourceImage = new ResourceImage(imagePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.exit(-1);
		}
		
		//	initialize canvas
		canvas = new Canvas((resourceImage.getWidth() * 9) / 4, (resourceImage.getHeight() * 9) / 4);
		
		//	initialize image separation
		imageOnCanvasX = (canvas.getWidth() - resourceImage.getWidth() ) / 2;
		imageOnCanvasY = (canvas.getHeight() - resourceImage.getHeight() ) / 2;
		sideLength = resourceImage.getWidth() / SIDE_SEGMENTS_NUMBER;
		centerPointLength = sideLength / 3;
		
		
		Triangle.initializeTriangle(resourceImage, canvas.getWidth(), SIDE_SEGMENTS_NUMBER);
		
		//	initialize canvas
		resetTriangles();		
		
		//	initialize GUI
		window = new MainWindow((resourceImage.getWidth() * 9) / 4, (resourceImage.getHeight() * 9) / 4);
	
		//	set action listeners
		window.setInitAnimationActionListener(initAnimationListener);
		window.setStartStopAnimationActionListener(startStopAnimationActionListern);
		window.setSlideAnimationChangeListener(slideAnimationChangeListern);
		window.setFilterActionListener(filterActionListern);
		window.setBlendActionListener(blendActionListern);
		
		//	initialize animation controller
		animationController = new AnimationController(canvas, window);
	}
	
	public void run() {
		
		SwingUtilities.invokeLater(new Runnable() {

            public void run() {
            	window.setVisible(true);
            }
        });
	}
	
	
	public static void main(String[] args) {

		MainController controller = new MainController();
		controller.run();
	}
	
	
	/**
	 * removes triangles from canvas,
	 * clears canvas,
	 * adds new triangles,
	 * paints them on the canvas
	 */
	private void resetTriangles() {
		canvas.clear();
		
		//	add triangles
		for (int i = 0; i < SIDE_SEGMENTS_NUMBER; ++i) {
			for (int j = 0; j < SIDE_SEGMENTS_NUMBER; ++j) {
				
				//	upper triangle of square
				double centerX = j * sideLength + centerPointLength;
				double centerY = i * sideLength + centerPointLength;
				
				
				canvas.addTriangle(new Triangle(centerX, centerY, 
						centerX + imageOnCanvasX, centerY + imageOnCanvasY, 
						//centerX + imageOnCanvasX + Triangle.getCathetusLength(), centerY + imageOnCanvasY + Triangle.getCathetusLength(),
						-1, -1,
						0));
				

				//	lower triangle of square
				centerX = (j + 1) * sideLength - centerPointLength;
				centerY = (i + 1) * sideLength - centerPointLength;
				
				
				canvas.addTriangle(new Triangle(
						centerX, centerY, 
						centerX + imageOnCanvasX, centerY + imageOnCanvasY, 
						//centerX + imageOnCanvasX - Triangle.getCathetusLength(), centerY + imageOnCanvasY - Triangle.getCathetusLength(),
						-1, -1,
						Math.PI));
				
				
			}
		}
		canvas.drawAll();
	}
	
	private class InitAnimationActionListern implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			startStopAnimationActionListern.stopAnimation();
			animationController.init();
							
			resetTriangles();
						
			window.paint(canvas.getBufferedImage());
			
		}
	}
	
	private class StartStopAnimationActionListern implements ActionListener {

		boolean state = false;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (false == state) {
				startAnimation();
			}
			else {
				stopAnimation();
			}
		}
		
		public void startAnimation() {
			if (false == animationController.isRunning()) {
				animationController.startAnimation();
			}
			state = true;
		}
		public void stopAnimation() {
			if (true == animationController.isRunning()) {
				animationController.stopAnimation();
			}
			state = false;
		}
	}
	
	private class SlideAnimationChangeListern implements ChangeListener {
		
		@Override
		public void stateChanged(ChangeEvent e) {

			JSlider slider = (JSlider) e.getSource();
			
			startStopAnimationActionListern.stopAnimation();
			
			animationController.doSlide(slider.getValue());
		}
		
	}
	
	private class FilterActionListern implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			//TODO: add stopping animation if need
			
			boolean value = ((JCheckBox) e.getSource()).isSelected();
		
			resourceImage.setFilter(value);
			
			
			
			//	repaint canvas
			canvas.clearImage();
			canvas.drawAll();
			window.paint(canvas.getBufferedImage());
			
		}
	}
	
	private class BlendActionListern implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			//TODO: add stopping animation if need
			
			boolean value = ((JCheckBox) e.getSource()).isSelected();
			
			resourceImage.setBlend(value);
			
			//	repaint canvas
			canvas.clearImage();
			canvas.drawAll();
			window.paint(canvas.getBufferedImage());
			
		}
	}	
}
