package ru.nsu.sumaneev.isoline.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.nsu.sumaneev.isoline.observers.ChangeParametersObserver;
import ru.nsu.sumaneev.isoline.observers.IsolineObserver;
import ru.nsu.sumaneev.isoline.observers.NetObserver;
import ru.nsu.sumaneev.isoline.observers.PaintingTypeObserver;
import ru.nsu.sumaneev.isoline.observers.ResetCustomIsolinesObserver;


public class MainWindow extends JFrame implements 
										ChangeParametersObserver,
										IsolineObserver,
										NetObserver,
										PaintingTypeObserver,
										ResetCustomIsolinesObserver {

	private static final String TITLE_APPLICATION = "Isoline";
	private static final int OFFSET = 10;
	
	//	panel for plot and its legend
	private JPanel plotPanel = null;
	private JPanel legendPanel = null;
	private DrawPanel plot = null;
	private DrawPanel legend = null;
	
	
	private JPanel crucialValuesPanel = null;
	private double[] crucialValues = null;
	
	//	control panel
	private ControlPanel controlPanel = null;
	
	
	//	status bar
	private JPanel statusBarPanel = null;
	private JLabel statusBarPlotLabel = null;
	
	public MainWindow(DrawPanel plot, DrawPanel legend, double[] crucialValues) {
		this.plot = plot;
		this.legend = legend;
		this.crucialValues = crucialValues;
		initUI();
	}
	
	/*
	public void repaintPlot(BufferedImage newImage) {
		plot.paint(newImage);
	}
	
	
	public int getDrawPanelWidth() {
		return plot.getWidth();
	}
	
	public int getDrawPanelHeight() {
		return plot.getHeight();
	}
	*/
	
/*	
	public void setDrawPanelRepainter(DrawPanelRepainter repainter) {
		plot.setDrawPanelRepainter(repainter);
	}
*/
	
	public void setPlotStatus(double x, double y, double value) {
		statusBarPlotLabel.setText("f(" + x + ", " + y +") = " + value);
	}
	
	@Override
	public void setPaintingTypeListener(ItemListener listener) {
		controlPanel.setPaintingTypeListener(listener);
	}

	@Override
	public void setNetListener(ActionListener listener) {
		controlPanel.setNetListener(listener);
		
	}

	@Override
	public void setIsolineListener(ActionListener listener) {
		controlPanel.setIsolineListener(listener);
		
	}

	@Override
	public void setChangeParametersListener(ActionListener listener) {
		controlPanel.setChangeParametersListener(listener);
		
	}
	
	@Override
	public void setResetCustomIsolinesListener(ActionListener listener) {
		controlPanel.setResetCustomIsolinesListener(listener);
	}
	
	private void initUI() {
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		
		//	initialize control panel
		controlPanel = new ControlPanel();
		controlPanel.setBorder(BorderFactory.createEmptyBorder(0, OFFSET, 0, OFFSET));
		add(controlPanel);
		
		
		add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		
		//	initialize plot stuff
		initPlotPanel();
		
		
		add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		
		//	initialize status bar
		initStatusBar();
		
		
		add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		setTitle(TITLE_APPLICATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        pack();
	}
	
	private void initPlotPanel() {
		
		plotPanel = new JPanel();
		plotPanel.setLayout(new BoxLayout(plotPanel, BoxLayout.X_AXIS));
		//plotPanel.setLayout(new BorderLayout());
		
		plotPanel.setBorder(BorderFactory.createEmptyBorder(0, OFFSET, 0, OFFSET));
		
		plotPanel.add(plot);
		//plotPanel.add(plot, BorderLayout.CENTER);
		
		
		plotPanel.add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		
		legendPanel = new JPanel();
		legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS));
		
		
		initCrucialValues();
		
		
		legendPanel.add(legend);
		legendPanel.add(crucialValuesPanel);
		
		plotPanel.add(legendPanel);
		//plotPanel.add(legendPanel, BorderLayout.EAST);
		
		add(plotPanel);
	}
	
	private void initCrucialValues() {
		crucialValuesPanel = new JPanel();
		crucialValuesPanel.setLayout(new BoxLayout(crucialValuesPanel, BoxLayout.Y_AXIS));
		
		int step =  (int) (Math.round( (double) legend.getHeight() ) / crucialValues.length);
		
		JLabel label = new JLabel(String.valueOf(Math.round(crucialValues[0])));
		
		crucialValuesPanel.add(label);
		
		for (int i = 1; i < crucialValues.length; ++i) {
			label = new JLabel(String.valueOf(Math.round(crucialValues[i])));
			
			crucialValuesPanel.add(Box.createRigidArea(new Dimension(0, step)));
			crucialValuesPanel.add(label);
		}
	}
	
	private void initStatusBar() {
		
		statusBarPanel = new JPanel();
		statusBarPanel.setLayout(new BoxLayout(statusBarPanel, BoxLayout.X_AXIS));
		
		statusBarPanel.setBorder(BorderFactory.createEmptyBorder(0, OFFSET, 0, OFFSET));
		
		statusBarPlotLabel = new JLabel("plot values");
		statusBarPanel.add(statusBarPlotLabel);
		
		//statusBarPanel.add(new JLabel("status bar"));
		
		add(statusBarPanel);
	}
}
