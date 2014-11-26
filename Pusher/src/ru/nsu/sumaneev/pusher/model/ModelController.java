package ru.nsu.sumaneev.pusher.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

import ru.nsu.sumaneev.observer.Observable;
import ru.nsu.sumaneev.observer.Observer;
import ru.nsu.sumaneev.pusher.world.CellType;

/**
 * Control behavior of {@link Model}, count time of playing
 * and steps.
 * Has observers and is able to notifies them
 */
public class ModelController implements Observable{
	
	private Model sokobanModel = null;
	private Model defaultSokobanModel = null;
	
	private long steps = 0;
	
	private long time = 0;		//in seconds
	
	private Timer timer = null;
	
	private final int TIME_OFFSET = 1000;
	
	private List<Observer> observers = new LinkedList<Observer>();
	
	private Level bestResult = null;
	
	/**
	 * Initializes {@link Model} from file and starts playing
	 * 
	 * @param modelFile - file with model's interpretation
	 * @param prevResult - scores of current best player
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	
	public void start(String modelFile, Level prevResult) throws FileNotFoundException, IOException{
		
		sokobanModel = new Model(modelFile);
		
		ActionListener taskPerformer = new ActionListener() {
		    
			@Override
			public void actionPerformed(ActionEvent evt) {
				++time;
			}
		};
		
		time = 0;
		steps = 0;
		
		bestResult = new Level(prevResult.getPlayerName(), prevResult.getTime(), prevResult.getSteps());
		
		timer = new Timer(TIME_OFFSET, taskPerformer);
		
		this.defaultSokobanModel = sokobanModel.clone();
		
		timer.start();
			
	}
	
	/**
	 * Clear time and steps, restores model
	 */
	public void restart(){
		sokobanModel = defaultSokobanModel.clone();

		time = 0;
		timer.restart();
		
		steps = 0;
	}

	/**
	 * move pusher up
	 */
	public void moveUp(){
		if (sokobanModel.move(Model.Direction.UP)){
			++steps;
		}
	}

	/**
	 * move pusher down
	 */
	public void moveDown(){
		if (sokobanModel.move(Model.Direction.DOWN)){
			++steps;
		}
	}
	
	/**
	 * move pusher left
	 */
	public void moveLeft(){
		if (sokobanModel.move(Model.Direction.LEFT)){
			++steps;
		}
	}
	
	/**
	 * move pusher right
	 */
	public void moveRight(){
		if (sokobanModel.move(Model.Direction.RIGHT)){
			++steps;
		}
	}
	
	/**
	 * registers observer to notify it in future
	 */
	@Override
	public void register(Observer observer) {
		observers.add(observer);
	}

	/**
	 * removes observer
	 */
	@Override
	public void remove(Observer observer) {
		observers.remove(observer);
		
	}

	/**
	 * notifies all registered observers
	 */
	@Override
	public void notifyObservers() throws IOException {
		for (Observer observer : observers){
			observer.update();
		}		
	}

	/**
	 * returns interpretation of {@link Model}
	 */
	@Override
	public List<ArrayList<CellType>> getModel() {
		if (null != sokobanModel){
			return sokobanModel.getFiled();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Checks model for finishing level
	 */
	@Override
	public boolean isFinished(){
		
		return sokobanModel.isComplete();
		
	}
	
	/**
	 * Returns current time of playing
	 */
	@Override
	public long getTime() {
		return time;
	}

	/**
	 * Returns number of pusher's steps
	 */
	@Override
	public long getSteps() {
		return steps;
	}
	
	/**
	 * Stops timer
	 */
	public void stop(){
		timer.stop();
	}

	/**
	 * Returns {@link Level} with best result of this level
	 */
	@Override
	public Level getBestResult() {
		return bestResult;
	}
	
}
