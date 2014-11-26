package ru.nsu.sumaneev.puzzle.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import ru.nsu.sumaneev.puzzle.view.controlObservers.BlendObserver;
import ru.nsu.sumaneev.puzzle.view.controlObservers.FilterObserver;
import ru.nsu.sumaneev.puzzle.view.controlObservers.InitAnimationObserver;
import ru.nsu.sumaneev.puzzle.view.controlObservers.SlideAnimationObserver;
import ru.nsu.sumaneev.puzzle.view.controlObservers.StartStopAnimationObserver;

public class MainWindow extends 
							JFrame 
						implements 
							InitAnimationObserver,
							StartStopAnimationObserver,
							SlideAnimationObserver,
							FilterObserver,
							BlendObserver {

	private static final String TITLE_APPLICATION = "Puzzle";
	
	private static final int STATUS_BAR_OFFSET = 5;
	
	private int drawPanelWidth = 0;
	private int drawPanelHeight = 0;
	
	
	//	panel with init and start-stop button, 
	//	slider, filter and blend checkboxes  
	private ControlPanel control = null;
	
	private JPanel statusBar = null;
	
	private DrawArea drawArea = null;
	
	public MainWindow(int drawPanelWidth, int drawPanelHeight) {
		
		this.drawPanelWidth = drawPanelWidth;
		this.drawPanelHeight = drawPanelHeight;
		
		initUI();
	}
	
	@Override
	public void setBlendActionListener(ActionListener listener) {
		control.setBlendActionListener(listener);
		
	}

	@Override
	public void setFilterActionListener(ActionListener listener) {
		control.setFilterActionListener(listener);
		
	}

	@Override
	public void setSlideAnimationChangeListener(ChangeListener listener) {
		control.setSlideAnimationChangeListener(listener);
		
	}

	@Override
	public void setStartStopAnimationActionListener(ActionListener listener) {
		control.setStartStopAnimationActionListener(listener);
	}

	@Override
	public void setInitAnimationActionListener(ActionListener listener) {
		control.setInitAnimationActionListener(listener);
		
	}
	
	public void paint(BufferedImage image) {
		drawArea.paint(image);
	}
	
	private void initUI() {

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		//	add status bar
		initStatusBar();		
		add(statusBar);
		
		//	add draw area
		initDrawArea();
		add(drawArea);
		
		
		//	add control panel
		initControl();		
		add(control);
		
		
		setTitle(TITLE_APPLICATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setSize(new Dimension(400, 400));
		
	}
	
	//TODO: add parameters for status bar
	private void initStatusBar() {
		statusBar = new JPanel();
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS) );
		
		JLabel simpleLable1 = new JLabel("STATUS BAR GONNA BE HERE");
		JLabel simpleLable2 = new JLabel("IT GONNA BE HERE SOON");
		
		statusBar.add(simpleLable1);
		statusBar.add(simpleLable2);
		
		
	}
	
	private void initDrawArea() {
		drawArea = new DrawArea(drawPanelWidth, drawPanelHeight);
	}
	
	private void initControl() {
		control = new ControlPanel();
	}


	
}
