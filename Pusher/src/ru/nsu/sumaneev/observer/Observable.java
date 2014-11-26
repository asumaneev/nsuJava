package ru.nsu.sumaneev.observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.nsu.sumaneev.pusher.model.Level;
import ru.nsu.sumaneev.pusher.world.CellType;

public interface Observable {
	
	public void register(Observer observer);
	
	public void remove(Observer observer);
	
	public void notifyObservers() throws IOException;
	
	public long getTime();
	
	public long getSteps();
	
	public Level getBestResult();
	
	public List<ArrayList<CellType>> getModel();

	public boolean isFinished();

}
