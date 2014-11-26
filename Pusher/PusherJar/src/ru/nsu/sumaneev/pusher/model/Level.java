package ru.nsu.sumaneev.pusher.model;


/**
 * It contains information about level: 
 * best player, his time and steps
 */
public class Level {

	private String bestPlayer = null;
	private long time = 0;
	private long steps = 0;
	
	public Level(String player, long time, long steps){

		this.bestPlayer = player;
		this.time = time;
		this.steps = steps;
	}
	
	public String getPlayerName(){
		return bestPlayer;
	}
	
	public long getTime(){
		return time;
	}
	
	public long getSteps(){
		return steps;
	}
	
}
