package ru.nsu.sumaneev.pusher.view;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ru.nsu.sumaneev.controller.ListenersGetter;
import ru.nsu.sumaneev.observer.Observable;

/**
 * Main GUI
 */
@SuppressWarnings("serial")
public class View extends JFrame {
	
	private final int WIDTH = 700;
	private final int HEIGHT = 500;
	
	private ToolBar toolbar = null;
	private MainScreen mainScreen = null;
	private GameScreen gameScreen = null;
	
	private ListenersGetter actions = null;
	
	private Observable observable = null;
	
	private String playerName = null;
	
	/**
	 * Initializes View
	 * @param actions
	 * @param observable
	 */
	public View(ListenersGetter actions, Observable observable){
		
		this.actions = actions;
		this.observable = observable;		
		
		mainScreen = new MainScreen(actions, WIDTH / 4, HEIGHT / 4);
		
		setTitle("Pusher");
		setSize(WIDTH, HEIGHT);
		setFocusable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Initializes main screen
	 */
	public void initMainScreen(){
		freeScreen();
		
		toolbar = new ToolBar(actions);
		
		toolbar.initMainToolBar();

		mainScreen.initMainScreen();
		mainScreen.setVisible(true);
		
		
		add(mainScreen);
		setJMenuBar(toolbar);
		
		pack();
	}
	
	/**
	 * Initializes game screen
	 * @throws IOException
	 */
	public void initGameScreen() throws IOException{
	
		freeScreen();
		
		toolbar.initGameToolBar();

		gameScreen = new GameScreen(actions, observable);
	
		add(gameScreen);
		
		gameScreen.setFocusable(true);
		gameScreen.requestFocusInWindow();
	
		setJMenuBar(toolbar);
			
		pack();
	}
	
	/**
	 * Prints dialog with exception
	 * @param e
	 */
	public void printException(Exception e){
		
		JOptionPane.showMessageDialog(this.getContentPane(), e, "Error", JOptionPane.ERROR_MESSAGE);
		
	}
	
	/**
	 * Show message that level is finished
	 * If there is new score name of new best player will be asked
	 * @param newScore - must be true if player has set new score
	 * @return new player's name if new score was set or
	 * 		   null if there was not new score or player does not enter his name  
	 */
	public String finishLevel(boolean newScore){
		gameScreen.stopTimer();
		
		if (newScore){
		
			Pattern pattern = Pattern.compile("\\w{1,}");
			
			for (;;){
				
				playerName =  JOptionPane.showInputDialog(this.getContentPane(), "You have new score! Enrer yout name: ", 
						"New Score", JOptionPane.INFORMATION_MESSAGE);
				
				if (null == playerName){
					break;
				}
				
				if (false == (pattern.matcher(playerName)).matches()){
					JOptionPane.showMessageDialog(this.getContentPane(), "Invalid name, try again", "Error", JOptionPane.WARNING_MESSAGE);
				}
				else {
					break;
				}
				
			}
			
		}
		else{
			JOptionPane.showMessageDialog(this.getContentPane(), "You have won this level!", "Win", JOptionPane.INFORMATION_MESSAGE);
		}
		
		return playerName;
	}
	
	private void freeScreen(){
		if (null != toolbar){
			toolbar.free();
		}
		
		if (null != mainScreen){
			mainScreen.setVisible(false);
			remove(mainScreen);
		}
		
		if (null != gameScreen){
			gameScreen.setVisible(false);
			gameScreen.free();
			remove(gameScreen);
		}
	}
}
