package ru.nsu.sumaneev.puzzle.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import ru.nsu.sumaneev.puzzle.view.controlObservers.BlendObserver;
import ru.nsu.sumaneev.puzzle.view.controlObservers.FilterObserver;
import ru.nsu.sumaneev.puzzle.view.controlObservers.InitAnimationObserver;
import ru.nsu.sumaneev.puzzle.view.controlObservers.SlideAnimationObserver;
import ru.nsu.sumaneev.puzzle.view.controlObservers.StartStopAnimationObserver;

public class ControlPanel extends JPanel implements InitAnimationObserver,
													StartStopAnimationObserver,
													SlideAnimationObserver,
													FilterObserver,
													BlendObserver {

	private static final String TITLE_INIT_BUTTON = "Init";
	private static final String TITLE_START_STOP_ANIMATION_BUTTON = "Start/stop";
	private static final String TITLE_FILTER_CHECKBOX = "Filter";
	private static final String TITLE_BLEND_CHECKBOX = "Blend";
	
	private static final int OFFSET = 5;
	private static final int BORDER = 5;
	

	private JPanel buttonsPanel = null;
	private JPanel sliderPanel = null;
	private JPanel checkBoxPanel = null;
	
	
	private JButton initButton = null;
	private JButton startStopAnimationButton = null;
	
	private JSlider animationSlider = null;
	
	private JCheckBox filterCheckBox = null;
	private JCheckBox blendCheckBox = null;	
	
	public ControlPanel() {
	
		initUI();
		
	}


	@Override
	public void setBlendActionListener(ActionListener listener) {
		
		blendCheckBox.addActionListener(listener);
		
	}

	@Override
	public void setFilterActionListener(ActionListener listener) {
		
		filterCheckBox.addActionListener(listener);
	}

	@Override
	public void setSlideAnimationChangeListener(ChangeListener listener) {
		
		animationSlider.addChangeListener(listener);		
	}

	@Override
	public void setStartStopAnimationActionListener(ActionListener listener) {
		
		startStopAnimationButton.addActionListener(listener);
		
	}

	@Override
	public void setInitAnimationActionListener(ActionListener listener) {
		
		initButton.addActionListener(listener);
		
	}

	private void initUI() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//	initialize buttons
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		initButton = new JButton(TITLE_INIT_BUTTON);
		startStopAnimationButton = new JButton(TITLE_START_STOP_ANIMATION_BUTTON);
		
		buttonsPanel.add(initButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		buttonsPanel.add(startStopAnimationButton);
		
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, BORDER, 0, BORDER));
		
		add(buttonsPanel);
		//add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		
		//	initialize slider
		sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		
		animationSlider = new JSlider(0, 360, 0);
		animationSlider.setMajorTickSpacing(60);
		animationSlider.setPaintTicks(true);
		animationSlider.setPaintLabels(true);
		
		
		sliderPanel.add(animationSlider);
		//sliderPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		//sliderPanel.add(animationSliderTitle);
		//sliderPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		//sliderPanel.add(animationSliderValue);
		
		sliderPanel.setBorder(BorderFactory.createEmptyBorder(0, BORDER, 0, BORDER));
		
		add(sliderPanel);
		//add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		
		//	initialize checkboxes
		checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
		
		filterCheckBox = new JCheckBox(TITLE_FILTER_CHECKBOX);
		blendCheckBox = new JCheckBox(TITLE_BLEND_CHECKBOX);
		
		checkBoxPanel.add(filterCheckBox);
		checkBoxPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		checkBoxPanel.add(blendCheckBox);
		
		checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(0, BORDER, 0, BORDER));
		
		add(checkBoxPanel);
		//add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		
		setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
		
	}	
	
}
