package ru.nsu.sumaneev.tetragon.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ru.nsu.sumaneev.tetragon.controllers.DrawPanelRepainter;

public class MainWindow extends 
							JFrame {

	private static final String TITLE_APPLICATION = "Tetragon";
	private static final int OFFSET = 10;
	
	private int drawPanelWidth = 0;
	private int drawPanelHeight = 0;
	
	private DrawPanel tranformedImagePanel = null;
	
	private JPanel controlPanel = null;
	private JButton randomConvexTetragonButton = null;
	private JButton randomConcaveTetragonButton = null;
	private JButton clearButton = null;
	
	private JCheckBox trilinearFilteringCheckBox = null;
	
	public MainWindow(int drawPanelWidth, int drawPanelHeight) {
		
		this.drawPanelWidth = drawPanelWidth;
		this.drawPanelHeight = drawPanelHeight;
		
		initUI();
	}
	
	public void setDrawPanelRepainter(DrawPanelRepainter repainter) {
		tranformedImagePanel.setRepainter(repainter);
	}
	public void repaint() {
		tranformedImagePanel.repaint();
		tranformedImagePanel.setFocusable(true);
	}
	public int getDrawPanelWidth() {
		return tranformedImagePanel.getWidth();
	}
	
	public int getDrawPanelHeight() {
		return tranformedImagePanel.getHeight();
	}
	
	public void setRandomConvexTetragonButtonListener(ActionListener listener) {
		randomConvexTetragonButton.addActionListener(listener);
	}
	public void setRandomConcaveTetragonButtonListener(ActionListener listener) {
		randomConcaveTetragonButton.addActionListener(listener);
	}
	public void setClearButtonListener(ActionListener listener) {
		clearButton.addActionListener(listener);
	}
	public void setTrilinearFilteringListener(ActionListener listener) {
		trilinearFilteringCheckBox.addActionListener(listener);
	}
	
	@Override
	public void addMouseListener(MouseListener listener) {
		tranformedImagePanel.addMouseListener(listener);
	}

	private void initUI() {

		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		initDrawPanel();
		add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		initControlPanel();
	
		setTitle(TITLE_APPLICATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setSize(new Dimension(drawPanelWidth + 100, drawPanelHeight));
        
        tranformedImagePanel.setFocusable(true);
		
	}
	private void initDrawPanel() {
		tranformedImagePanel = new DrawPanel(drawPanelWidth, drawPanelHeight);
		
		tranformedImagePanel.setBorder(new EmptyBorder(OFFSET, OFFSET / 2, OFFSET, OFFSET / 2));
		
		add(tranformedImagePanel);
	}
	
	
	private void initControlPanel() {
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		
		randomConvexTetragonButton = new JButton("convex tetragon");
		randomConcaveTetragonButton = new JButton("concave tetragon");
		clearButton = new JButton("clear");
		
		trilinearFilteringCheckBox = new JCheckBox("trilinear filtering");
		
		controlPanel.setBorder(new EmptyBorder(OFFSET, OFFSET / 2, OFFSET, OFFSET / 2));
		
		controlPanel.add(randomConvexTetragonButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		controlPanel.add(randomConcaveTetragonButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		controlPanel.add(clearButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		controlPanel.add(trilinearFilteringCheckBox);
		
		add(controlPanel);
	}
	
}
