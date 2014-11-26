package ru.nsu.sumaneev.isoline.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import ru.nsu.sumaneev.isoline.observers.ChangeParametersObserver;
import ru.nsu.sumaneev.isoline.observers.IsolineObserver;
import ru.nsu.sumaneev.isoline.observers.NetObserver;
import ru.nsu.sumaneev.isoline.observers.PaintingTypeObserver;
import ru.nsu.sumaneev.isoline.observers.ResetCustomIsolinesObserver;
import ru.nsu.sumaneev.isoline.painting.ColorController.ColorType;

public class ControlPanel extends JPanel implements 
											ChangeParametersObserver,
											IsolineObserver,
											NetObserver,
											PaintingTypeObserver,
											ResetCustomIsolinesObserver {

	private static final int OFFSET = 5;
	
	

	private JPanel linesPanel = null;
	private JCheckBox isolineCheckBox = null;
	private JCheckBox netCheckBox = null;
	
	
	//	stuff with colors: interpolation and dithering
	private JComboBox<ColorType> plotPaintingType = null;
	
	
	private JButton parametersButton = null;
	
	private JButton resetCustomIsolines = null;
	
	public ControlPanel() {
		initUI();
	}
	
	@Override
	public void setPaintingTypeListener(ItemListener listener) {
		plotPaintingType.addItemListener(listener);
		
	}

	@Override
	public void setNetListener(ActionListener listener) {
		netCheckBox.addActionListener(listener);
		
	}

	@Override
	public void setIsolineListener(ActionListener listener) {
		isolineCheckBox.addActionListener(listener);
		
	}

	@Override
	public void setChangeParametersListener(ActionListener listener) {
		
		parametersButton.addActionListener(listener);		
	}
	
	@Override
	public void setResetCustomIsolinesListener(ActionListener listener) {
		resetCustomIsolines.addActionListener(listener);
		
	}
	
	private void initUI() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		initLinesShowing();
		
		
		add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		
		initPlotPaintingType();
		
		
		add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		
		parametersButton = new JButton("parameters");
		
		add(parametersButton);
		
		
		add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		resetCustomIsolines = new JButton("reset custom isolines");
		
		add(resetCustomIsolines);
		
	}
	
	private void initPlotPaintingType() {
		
		plotPaintingType = new JComboBox<>();
		plotPaintingType.addItem(ColorType.SIMPLE);
		plotPaintingType.addItem(ColorType.INTERPOLATION);
		plotPaintingType.addItem(ColorType.INTERPOLATION_AND_DITHER);
		
		plotPaintingType.setPreferredSize(new Dimension(200, 20));
		plotPaintingType.setMaximumSize(new Dimension(200, 20));
		
		
		plotPaintingType.setSelectedIndex(0);
		
		add(plotPaintingType);

	}
	
	private void initLinesShowing() {
		
		linesPanel = new JPanel();
		linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.Y_AXIS));
		
		isolineCheckBox = new JCheckBox("show isolines");
		netCheckBox = new JCheckBox("show net");
		
		linesPanel.add(isolineCheckBox);
		linesPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		linesPanel.add(netCheckBox);
		
		add(linesPanel);
	}
}
