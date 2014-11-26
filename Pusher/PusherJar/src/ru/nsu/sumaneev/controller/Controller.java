package ru.nsu.sumaneev.controller;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import ru.nsu.sumaneev.pusher.model.Level;
import ru.nsu.sumaneev.pusher.model.ModelController;
import ru.nsu.sumaneev.pusher.model.ScoresTable;
import ru.nsu.sumaneev.pusher.view.View;

/**
 * Controller initializes resources and original UI
 * also it is able to return listeners for {@link View}
 * and its panels
 */
public class Controller implements ListenersGetter{

	/*
	 * Models
	 */
	private ScoresTable scoresTable = null;
	
	
	private ModelController modelController = null;

	private int choosedLevelID = 0;
	
	/*
	 * Viewer
	 */
	private View viewer = null;	
	
	/*
	 * Source files
	 */
	private String scoresTableFile = "scores.sk";
	private String levelsDir = "levels/";
	private String[] levelsFiles = {
		
			"level1.sk",
			"level2.sk",
			"level3.sk",
			"level4.sk",
			"level5.sk",
			"level6.sk",
			"level7.sk",
			"level8.sk",
			"level9.sk",
			"level10.sk",
	};
	/**
	 * Initializes {@link ModelController}, {@link ScoresTable}
	 * and {@link View}
	 */
	public Controller(){
		
		modelController = new ModelController();
		
		viewer = new View(this, modelController);
		
		try {
			scoresTable = new ScoresTable(scoresTableFile);
		} catch (IOException e) {
			viewer.printException(e);
			System.exit(1);
		}	
	}
	
	/**
	 * Launch UI
	 */
	public void run(){
		SwingUtilities.invokeLater(new Runnable() {

            public void run() {
            	viewer.initMainScreen();
                viewer.setVisible(true);
            }
        });
	}

	/**
	 * Finishes level.
	 * If player sets new score - asks for name
	 * and change information about this level in scores record
	 */	
	private void finishLevel(){
		
		Level l = scoresTable.getLevel(choosedLevelID);
		
		long newTime = modelController.getTime();
		long newSteps = modelController.getSteps();
		
		if (newTime < l.getTime() || (l.getTime() < 0)){
						
			String playerName = viewer.finishLevel(true);
			
			
			if (null != playerName){
				
				l = new Level(playerName, newTime, newSteps);
				
				scoresTable.setScore(choosedLevelID, l);
				
				try {
					scoresTable.save(scoresTableFile);
				} catch (IOException e) {
					viewer.printException(e);
				}
			}		
		}
		else {
			viewer.finishLevel(false);
		}
		
		
		
		viewer.initMainScreen();
	}
	
	
	/**
	 *returns listener of starting level
	 *
	 *@param comboBox - JComboBox, where level was chosen
	 */
	@Override
	public ActionListener getStartListener(JComboBox comboBox){
		
		return new StartListener(comboBox);
	}
	
	/**
	 *returns listener of showing scores record
	 */
	@Override
	public ActionListener getScoresListener() {
		return new ScoresListener();
	}
	
	/**
	 *returns listener of clearing scores' record 
	 */
	@Override
	public ActionListener getScoresRestartListener() {
		return new ScoresRestartListener();
	}
	
	/**
	 * returns listener of choosing levels in comboBox
	 * and showing information about them on display
	 * 
	 * @param comboBox - JComboBox where levels are chosen
	 * @param display - JTextArea where information is shown
	 */
	@Override
	public ItemListener getComboBoxListener(JComboBox comboBox,
			JTextArea display) {
		
		
		ComboBoxListener comboBoxListener = new ComboBoxListener();
		comboBoxListener.initListener(comboBox, display);
		
		return comboBoxListener;
	}
	
	/**
	 * returns listener which restart level
	 */
	@Override
	public ActionListener getRestartListener(){
		return new RestartListener();
	}
	
	/**
	 * returns listener of keys
	 */
	@Override
	public KeyListener getKeyListener() {
		return new ArrowsAdapter();
	}
	
	@Override
	public ActionListener getBreakListener() {
		return new BreakListener();
	}
	
	/*
	 * ActionListeners
	 */
	
	/**
	 * This listener shows dialog with information about scores
	 * of each level
	 * 	 
	 */
	class ScoresListener implements ActionListener{

		private final int WIDTH = 500;
		private final int HEIGHT = 400;
		
		ScoresTable table = null;
		
		private JDialog scoresDialog = new JDialog();
		
		private JPanel panel = null;
		private JTextArea area = null;
		private JButton close = null;
		
		public ScoresListener(){
			this.table = Controller.this.scoresTable;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0){
			initScoresDialog();
			scoresDialog.setVisible(true);
		}
		/**
		 * Initializes dialog with levels' scores
		 */
		private void initScoresDialog(){
			
			free();
			
			scoresDialog.setLayout(new BoxLayout(scoresDialog.getContentPane(), BoxLayout.Y_AXIS));
			
			scoresDialog.add(Box.createRigidArea(new Dimension(0, 10)));
			
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			
			JScrollPane pane = new JScrollPane();
			
			initTextArea();
			
			pane.getViewport().add(area);
	        panel.add(pane);
	        
	        scoresDialog.add(panel);
	        
	        scoresDialog.add(Box.createRigidArea(new Dimension(0, 10)));
	        
	        close = new JButton("Close");
	        close.addActionListener(new ActionListener() {

	            public void actionPerformed(ActionEvent event) {
	                scoresDialog.dispose();
	            }
	        });
	        
	        close.setAlignmentX(0.5f);
	        scoresDialog.add(close);
	        
	        scoresDialog.add(Box.createRigidArea(new Dimension(0, 5)));
	        
	        scoresDialog.setModalityType(ModalityType.APPLICATION_MODAL);

	        scoresDialog.setTitle("Scores table");
	        scoresDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        scoresDialog.setLocationRelativeTo(null);
	        scoresDialog.setSize(WIDTH, HEIGHT);
		}
		
		private void initTextArea(){
			
			area = new JTextArea();
			
			String unfulfild = "--";
			
			for (int i = 0; i < table.getNumberOfLevels(); ++i){
			
				Level l = table.getLevel(i);
				
				area.append("Level: " + i + "\n");
				area.append("Best player: " + l.getPlayerName() + "\n");
				
				if (l.getTime() < 0){
					area.append("Time:  " + unfulfild + "\n");
					area.append("Steps: " + unfulfild + "\n");
				}
				else{
					
					area.append("Time:  " + l.getTime() + "sec\n");
					area.append("Steps: " + l.getSteps() + "\n");
				}
				
				
				area.append("\n");
				
			}
			
			area.setLineWrap(true);
	        area.setWrapStyleWord(true);
	        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	        area.setEditable(false);
		}
		
		private void free(){
			if (null != panel){
				scoresDialog.remove(panel);
			}
			if (null != close){
				scoresDialog.remove(close);
			}
		}
	}

	/**
	 * Listener of clearing scores' record
	 *
	 */
	class ScoresRestartListener implements ActionListener{

		ScoresTable scoresTable = null;
		
		public ScoresRestartListener(){
			this.scoresTable = Controller.this.scoresTable;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			for (int i = 0; i < scoresTable.getNumberOfLevels(); ++i){
				
				scoresTable.setScore(i, new Level("none", -1, -1));
				
			}
			
			try {
				scoresTable.save(scoresTableFile);
			} catch (IOException e) {
				viewer.printException(e);
				return;
			}
			
		}
		
	}
	
	/**
	 *  Listener of returning to mainScreen
	 */
	class BreakListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			modelController.stop();
			
			viewer.initMainScreen();
			
		}
		
	}
	
	/**
	 * Listener of JComboBox where levels are chosen
	 *
	 */
	class ComboBoxListener implements ItemListener{
		
		ScoresTable scoresTable = null;
		
		JComboBox<String> comboBox = null;
		JTextArea display = null;
		
		public ComboBoxListener(){
			this.scoresTable = Controller.this.scoresTable;
		}
		/**
		 * Initialize ComboBoxListener
		 * @param comboBox - JComboBox where levels are chosen
		 * @param display - JTextArea where chosen level information is shown
		 */
		public void initListener(JComboBox<String> comboBox, JTextArea display){
			this.comboBox = comboBox;
			this.display = display;
			
			for (int i = 0; i < scoresTable.getNumberOfLevels(); ++i){
				comboBox.addItem("level " + (i + 1));
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			 
			if (e.getStateChange() == ItemEvent.SELECTED) {
		            @SuppressWarnings("unchecked")
					JComboBox<String> combo = (JComboBox<String>) e.getSource();
		            
		            int choosedLevelID = combo.getSelectedIndex();

		            display.setText("");
		            
		            Level l = scoresTable.getLevel(choosedLevelID);
		            
		            String unfulfild = "--";
					
					display.append("Best player: " + l.getPlayerName() + "\n");
					
					if (l.getTime() < 0){
						display.append("Time: " + unfulfild + "\n");
						display.append("Steps: " + unfulfild + "\n");
					}
					else{
						display.append("Time: " + l.getTime() + " sec\n");
						display.append("Steps: " + l.getSteps() + "\n");
					}
		            
		        }
			
		}

		
	}

	/**
	 * Listener of starting level
	 */
	class StartListener implements ActionListener{

		JComboBox comboBox = null;

		/**
		 * Initializes listener with comboBox
		 * @param comboBox - JComboBox where level was chosen
		 */
		public StartListener(JComboBox comboBox){
			this.comboBox = comboBox;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {

			int choosedLevelID = comboBox.getSelectedIndex();
			
			Controller.this.choosedLevelID = choosedLevelID;
			
			try {
				modelController.start(Controller.this.levelsDir + Controller.this.levelsFiles[choosedLevelID], 
						Controller.this.scoresTable.getLevel(choosedLevelID));
			}
			catch(IndexOutOfBoundsException e){
				
				viewer.printException(new IndexOutOfBoundsException("Pleas, choose one of levels"));
				return;
			}
			catch (Exception e){
				viewer.printException(e);
				return;
			}
			
			try {
				viewer.initGameScreen();
			} catch (IOException e) {
				viewer.printException(e);
				return;
			}
		}
		
	}

	/**
	 * Listener of restarting level
	 */
	class RestartListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {

			modelController.restart();
			
			try {
				modelController.notifyObservers();
			} catch (IOException e) {
				viewer.printException(e);
				return;
			}
			
		}
		
	}
	
	/**
	 * Listener of arrow keys
	 * when key has been pressed ArrowsAdapter changes model,
	 * notifies model's observers and checks model for finishing.
	 * If level is finished it notifies controller 
	 */
	class ArrowsAdapter extends KeyAdapter{
		
		@Override
        public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			
			if (KeyEvent.VK_LEFT == key){
				modelController.moveLeft();
			}
			else if (KeyEvent.VK_RIGHT == key){
				modelController.moveRight();
			}
			else if (KeyEvent.VK_DOWN == key){
				modelController.moveDown();
			}
			else if (KeyEvent.VK_UP == key){
				modelController.moveUp();
			}

			try {
				modelController.notifyObservers();
			} catch (IOException e1) {
				viewer.printException(e1);
				return;
			}
			
			if (modelController.isFinished()){
				
				Controller.this.finishLevel();
			}
		}
	}

}


