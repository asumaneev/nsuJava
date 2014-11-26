package ru.nsu.sumaneev.pusher.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import ru.nsu.sumaneev.controller.ListenersGetter;
import ru.nsu.sumaneev.observer.Observable;
import ru.nsu.sumaneev.observer.Observer;
import ru.nsu.sumaneev.pusher.model.Level;
import ru.nsu.sumaneev.pusher.world.CellType;
import ru.nsu.sumaneev.pusher.world.World;

/**
 * Panel with painted level
 * 
 *
 */
@SuppressWarnings("serial")
public class GameScreen extends JPanel implements Observer {

	private final int OFFSET = 10;
	private final int TIME_OFFSET = 1000;
	
	private JPanel display = null;
	private JTextArea stepsArea = null;
	private JTextArea timeArea = null;
	private JTextArea levelArea = null;
	
	private World world = null;
	
	private Observable observable = null;
	
	private List<ArrayList<CellType>> field = null;
	
	private long time = 0;
	private long steps = 0;
	
	private Timer timer = null;
	
	/**
	 * Initializes panel, register it in observable 
	 * and get arrows listener from listenerGetter
	 * 
	 * @param listenerGetter - getter to get the arrows listener
	 * @param observable - model with needed information
	 * @throws IOException
	 */
	public GameScreen(ListenersGetter listenerGetter, final Observable observable) throws IOException{
		
		addKeyListener(listenerGetter.getKeyListener());
		
		this.observable = observable;
		
		observable.register(this);
		
		ActionListener taskPerformer = new ActionListener() {
		    
			@Override
			public void actionPerformed(ActionEvent evt) {

				time = observable.getTime();
				updateTime();
		    }
		  };
		
		timer = new Timer(TIME_OFFSET, taskPerformer);
		
		initGameScreen();



	}
	
	/**
	 * Clean panel
	 */
	public void free(){

		removeAll();
		
	}
	
	/**
	 * Asks for new information from observable
	 * and repaint screen
	 */
	@Override
	public void update() throws IOException {
		field = observable.getModel();
		steps = observable.getSteps();
		
		world.updateWorld(field);

		updateSteps();
	}
	
	/**
	 * Stops updating timer 
	 */
	public void stopTimer(){
		timer.stop();
	}
	

	private void initGameScreen() throws IOException {
		
		free();
		
		
		
		field = observable.getModel();
		steps = observable.getSteps();
		time = observable.getTime();
		
		world = new World();
		
		world.initWorld(field);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		initDisplay();
		
		updateSteps();
		
		timer.start();
		
		add(display);
		
		add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
		add(world);
		
		add(Box.createRigidArea(new Dimension(OFFSET, 0)));
		
	}


	
	private void initDisplay() {
		display = new JPanel();
		
		display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
		
		display.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		stepsArea = new JTextArea();
		
		stepsArea.setLineWrap(true);
        stepsArea.setWrapStyleWord(true);
        stepsArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        stepsArea.setEditable(false);
		
        display.add(stepsArea);
        
        display.add(Box.createRigidArea(new Dimension(0, OFFSET)));
        
        timeArea = new JTextArea();
        
        timeArea.setLineWrap(true);
        timeArea.setWrapStyleWord(true);
        timeArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        timeArea.setEditable(false);        
        
        display.add(timeArea);
        
        display.add(Box.createRigidArea(new Dimension(0, OFFSET)));
        
        levelArea = new JTextArea();
        
        levelArea.setLineWrap(true);
        levelArea.setWrapStyleWord(true);
        levelArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        levelArea.setEditable(false);
    	
        Level l = observable.getBestResult();
        
        if (l.getTime() < 0){
        
        	levelArea.append("Best player: " + l.getPlayerName() + "\n");
	        levelArea.append("Best time: 	--" + "\n");
	        levelArea.append("Steps: 	--");
        
        }
        else {
        	levelArea.append("Best player: " + l.getPlayerName() + "\n");
	        levelArea.append("Best time: " + l.getTime() + "\n");
	        levelArea.append("Steps: " + l.getSteps());
        }
        
        display.add(levelArea);
		
	}
	
	private void updateSteps(){
		stepsArea.setText("");
		
		stepsArea.append("Steps: " + steps);
	}
	
	private void updateTime(){
		timeArea.setText("");
		
		timeArea.append("Time: " + time);
	}
}
