package ru.nsu.sumaneev.CarFactory.threadPool;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool<T> {

	private ExecutorService executor = null;
	private List<Callable<T>> commands = null;
	
	private int currentCommand = 0;
	
	public ThreadPool(int size, List<Callable<T>> commands){
		
		this.commands = commands;
		
		executor = Executors.newFixedThreadPool(size);
	}
	
	public synchronized Future<T> doCommand(){
		
		Future<T> f = executor.submit(commands.get(currentCommand++));
	
		currentCommand %= commands.size();

		return f;
	}
	
	public void shutdown(){
		executor.shutdown();
	}
	
}
