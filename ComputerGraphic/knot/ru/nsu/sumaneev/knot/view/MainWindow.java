package ru.nsu.sumaneev.knot.view;

import java.awt.Dimension;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.nsu.sumaneev.knot.controllers.DrawPanelRepainter;
import ru.nsu.sumaneev.knot.controllers.KnotType;


public class MainWindow extends JFrame {

	private static final String TITLE_APPLICATION = "KNOT";
	private static final int OFFSET = 10;
	
	//	panel for plot and its legend
	private DrawPanel knotPanel;
	
	
	
	private JPanel controlPanel = null;
	private JComboBox<KnotType> knotComboBox = null;
	
	private JPanel distanceAndAnglePanel = null;
	private JLabel distanceTitle = null;
	private JLabel verticalAngleTitle= null;
	private JLabel horizontalAngleTitle = null;
	private JLabel distanceValue = null;
	private JLabel verticalAngleValue = null;
	private JLabel horizontalAngleValue = null;
	
	private int width = 0;
	private int height = 0;
	
	public MainWindow(int width, int height) {
		this.width = width;
		this.height = height;
		
		initUI();
	}
	
	public void setDrawPanelRepainter(DrawPanelRepainter repainter) {
		knotPanel.setRepainter(repainter);
	}
	
	public void repaint() {
		knotPanel.repaint();
	}
	
	public int getDrawPanelWidth() {
		return knotPanel.getWidth();
	}
	
	public int getDrawPanelHeight() {
		return knotPanel.getHeight();
	}

	public void setKnotTypeListener(ItemListener listener) {
		knotComboBox.addItemListener(listener);
	}
	
	public void setDistanceLabel(double value) {
		distanceValue.setText(String.valueOf( (Math.round(value * 100) / 100.0) ));
	}
	public void setHorizontalAngleLabel(double value) {
		horizontalAngleValue.setText(String.valueOf( (Math.round(value * 100) / 100.0) ));
	}
	public void setVerticalAngleLabel(double value) {
		verticalAngleValue.setText(String.valueOf( (Math.round(value * 100) / 100.0) ));
	}
	
	private void initUI() {
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		initControlPanel();
		
		add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		initDrawPanel();
		
		setSize(width, height + 50);
		setTitle(TITLE_APPLICATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setFocusable(true);
	}
	
	private void initDrawPanel() {
		knotPanel = new DrawPanel(width, height);
		
		add(knotPanel);
	}
	
	private void initControlPanel() {
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		
		controlPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		//	initialize labels panel
		distanceAndAnglePanel = new JPanel();
		distanceAndAnglePanel.setLayout(new BoxLayout(distanceAndAnglePanel, BoxLayout.X_AXIS));
		
		distanceTitle = new JLabel("distance:");
		verticalAngleTitle = new JLabel("vertical:");
		horizontalAngleTitle = new JLabel("horizontal:");
		
		distanceValue = new JLabel();
		verticalAngleValue = new JLabel();
		horizontalAngleValue = new JLabel();
		
		
		distanceAndAnglePanel.add(distanceTitle);
		distanceAndAnglePanel.add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		distanceAndAnglePanel.add(distanceValue);
		distanceAndAnglePanel.add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		distanceAndAnglePanel.add(verticalAngleTitle);
		distanceAndAnglePanel.add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		distanceAndAnglePanel.add(verticalAngleValue);
		distanceAndAnglePanel.add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		distanceAndAnglePanel.add(horizontalAngleTitle);
		distanceAndAnglePanel.add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		distanceAndAnglePanel.add(horizontalAngleValue);
		
		controlPanel.add(distanceAndAnglePanel);
		controlPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		
		//	initialize combo box
		knotComboBox = new JComboBox<>();
		knotComboBox.addItem(KnotType.TREFOIL_KNOT);
		knotComboBox.addItem(KnotType.TOURUS_KNOT);
		knotComboBox.addItem(KnotType.BEZIER_TREFOIL_KNOT);
		knotComboBox.addItem(KnotType.HERMITE_TREFOIL_KNOT);
		knotComboBox.addItem(KnotType.SPLINE_TREFOIL_KNOT);
		knotComboBox.setMaximumSize(new Dimension(width, 40));
		
		controlPanel.add(knotComboBox);
		controlPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		add(controlPanel);
	}
}
