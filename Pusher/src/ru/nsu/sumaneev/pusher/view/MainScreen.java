package ru.nsu.sumaneev.pusher.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import ru.nsu.sumaneev.controller.ListenersGetter;

/**
 * Panel for choosing levels
 *
 */
@SuppressWarnings("serial")
public class MainScreen extends JPanel{

	private final int OFFSET = 35;
	
	private int WIDTH = 0;
	private int HEIGHT = 0;
	
	private JPanel levelPanel = null;
	
	private JTextArea display = null;  
	private JComboBox<String> combobox = null;
	private JButton button = null;
	
	private ListenersGetter actions = null;
	
	/**
	 * Initializes panel
	 * @param listenersGetter - getter for listeners
	 * @param width - width of panel
	 * @param height - height of panel
	 */
	public MainScreen(ListenersGetter listenersGetter, int width, int height){
		
		this.actions = listenersGetter;
		
		WIDTH = width;
		HEIGHT = height;
	}
	
	/**
	 * Clears panel
	 */
	public void free(){
		
		removeAll();
	}

	/**
	 * Initializes panel, add display with information about levels,
	 * JComboBox for choosing level
	 * and start button
	 */
	public void initMainScreen() {
		
		free();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		initLevelPanel();
		
		add(levelPanel);
		
		add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		button = new JButton("Start");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(actions.getStartListener(combobox));
        add(button);
        
        add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
	}
	
	private void initLevelPanel(){
		
		levelPanel = new JPanel();
		levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.X_AXIS));
	
		levelPanel.add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		display = new JTextArea();
		
		display.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        display.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		display.setLineWrap(true);
		display.setWrapStyleWord(true);
		display.setBorder(LineBorder.createGrayLineBorder());
		display.setEditable(false);
		
		levelPanel.add(display);
		
		levelPanel.add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		combobox = new JComboBox<String>();
        combobox.setPreferredSize(new Dimension(140, 22));
        combobox.setMaximumSize(new Dimension(140, 22));
        combobox.addItemListener(actions.getComboBoxListener(combobox, display));
        combobox.setSelectedIndex(-1);
        levelPanel.add(combobox);

        levelPanel.add(Box.createRigidArea(new Dimension(OFFSET, 0)));
	}
	
}
