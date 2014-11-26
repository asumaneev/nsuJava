package ru.nsu.sumaneev.puzzle.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import ru.nsu.sumaneev.puzzle.model.Canvas;
import ru.nsu.sumaneev.puzzle.model.Triangle;
import ru.nsu.sumaneev.puzzle.view.MainWindow;

public class AnimationController implements ActionListener {

	//	time of full animation in ms
	private static final int ANIMATION_FULL_TIME = 5000;
	//	time step for timer
	private static final int ANIMATION_TIME_STEP = 1000/32;
	//	number of steps in animation
	private static final int ANIMATION_STEPS_NUMBER = ANIMATION_FULL_TIME / ANIMATION_TIME_STEP;
	private static final double ANIMATION_ANGLE_STEP = Math.toRadians(360 / ANIMATION_STEPS_NUMBER);
	
	
	private double[] circleRadiuses = null;
	private double[][] circleCenters = null;
	private double[] circlePhases = null;
	
	//	slider steps number = 360
	//	slider angle step = math.toRadians(t)
	
	private Canvas canvas = null;
	private MainWindow window = null;
	
	private Timer timer = null;
	
	private int currentStep = 0;
	
	private Random random = null;
	
	public AnimationController(Canvas canvas, MainWindow window) {
		this.canvas = canvas;
		this.window = window;
		
		canvas.clearImage();
		
		timer = new Timer(ANIMATION_TIME_STEP, this);
		
		random = new Random();
		
		init();
	}
	
	public void init() {
		
		currentStep = 0;
		
		List<Triangle> triangles = canvas.getTriangles();
		
		circleRadiuses = new double[triangles.size()];
		circleCenters = new double[triangles.size()][2];
		circlePhases = new double[triangles.size()];

		int i = 0;
		for (Triangle t : triangles) {
			
			double endX = random.nextDouble() * canvas.getWidth();
			double endY = random.nextDouble() * canvas.getHeight();

			int cathetus = 2 * Triangle.getCathetusLength();
			
			if (endX + cathetus >= canvas.getWidth()) {
				endX -= cathetus;
			}
			else if (endX - cathetus <= 0) {
				endX += cathetus;
			}
			
			if (endY + cathetus >= canvas.getHeight()) {
				endY -= cathetus;
			}
			else if (endY - cathetus <= 0) {
				endY += cathetus;
			}
			
			t.setEndPoint(endX, endY);
			
			
			//	set x0
			circleCenters[i][0] = (t.getEndX() + t.getStartX()) / 2;
			//	set y0
			circleCenters[i][1] = (t.getEndY() + t.getStartY()) / 2;
			
			double dx = t.getEndX() - t.getStartX();
			double dy = t.getEndY() - t.getStartY();
			
			//	set R = sqrt(dx^2 + dy^2) / 2
			circleRadiuses[i] = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) / 2;
			
			if (dx == 0) {
				if (dy == 0) {
					circlePhases[i] = 0;
				}
				else if (dy > 0) {
					circlePhases[i] = Math.PI / 2;
				}
				else {
					circlePhases[i] = -Math.PI / 2;
				}
			}
			else if (dx > 0) {
				if (dy > 0) {
					circlePhases[i] = Math.atan(Math.abs(dy / dx));
				}
				else {
					circlePhases[i] = 2 * Math.PI - Math.atan(Math.abs(dy / dx));
				}
			}
			else {
				if (dy > 0) {
					circlePhases[i] = Math.PI - Math.atan(Math.abs(dy / dx));
				}
				else {
					circlePhases[i] = Math.PI + Math.atan(Math.abs(dy / dx));
				}
			}
			
			circlePhases[i] -= Math.PI;
			
			++i;
		}
		
	}
	
	public void startAnimation() {
		timer.start();
	}
	
	public void stopAnimation() {
		timer.stop();
	}
	
	public boolean isRunning() {
		return timer.isRunning();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		doStep(ANIMATION_ANGLE_STEP);
	}
	
	public void doSlide(int sliderValue) {
		setStep(sliderValue);
	}
	
	private void repaint() {
		
		canvas.clearImage();
		
		canvas.drawAll();
		window.paint(canvas.getBufferedImage());
	}

	
	private void doStep(double dAngle) {
		List<Triangle> triangles = canvas.getTriangles();
		
		int i = 0;
		for (Triangle triangle : triangles) {
		
			double nextX = getCircleX((double) currentStep / ANIMATION_STEPS_NUMBER, circleCenters[i][0], circleRadiuses[i], circlePhases[i]);
			double nextY = getCircleY((double) currentStep / ANIMATION_STEPS_NUMBER, circleCenters[i][1], circleRadiuses[i], circlePhases[i]);
			
			triangle.moveTo(nextX, nextY);
			
			//	rotate triangle relatively to its center
			triangle.addAngle(dAngle);
			
			
			triangle.translateTriangle();
			
			++i;
		}
		
		repaint();
		
		//	increment time
		//	currentStep in [0, ANIMATION_STEPS_NUMBER)
		currentStep = (currentStep + 1) % (ANIMATION_STEPS_NUMBER);
	}
	
	//	t in [0, 360)
	private void setStep(int t) {
		
		t %= 360;
		
		List<Triangle> triangles = canvas.getTriangles();
		
		double[] startAngle = {0, Math.PI};
		int i = 0;
		for (Triangle triangle : triangles) {
			
			double nextX = getCircleX((double) t / 360, circleCenters[i][0], circleRadiuses[i], circlePhases[i]);
			double nextY = getCircleY((double) t / 360, circleCenters[i][1], circleRadiuses[i], circlePhases[i]);
			
			
			triangle.moveTo(nextX, nextY);
			
			//	rotate triangle relatively to its center
			triangle.setCurrentAngle(Math.toRadians(t) + startAngle[i % 2]);
			
			
			triangle.translateTriangle();
			
			++i;
		}
		
		repaint();
		
	}
	
	//	t in [0, 1]
	private double getCircleX(double t, double x0, double R, double phase) {
		return x0 + R * Math.cos(t * (2 * Math.PI) + phase);
	}
	//	t in [0, 1]
	private double getCircleY(double t, double y0, double R, double phase) {
		return y0 + R * Math.sin(t * (2 * Math.PI) + phase);
	}
}
