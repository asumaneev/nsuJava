package ru.nsu.sumaneev.CarFactory.controller;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public interface ActionListenresGetter {

	public ChangeListener getSliderListener(JSlider slider, SliderType good);
	
	public SizeObserver getSizeObserver();
	
}
