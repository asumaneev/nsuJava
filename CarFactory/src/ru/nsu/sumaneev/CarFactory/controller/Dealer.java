package ru.nsu.sumaneev.CarFactory.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ru.nsu.sumaneev.CarFactory.goods.Car;
import ru.nsu.sumaneev.CarFactory.observers.WorkTimeObservable;

public class Dealer implements Runnable, WorkTimeObservable{

	private int id = 0;
	private Long offset = 0L;
	
	private FactoryController fcontroller = null;
	
	public Dealer(int id, long offset, FactoryController fcontroller){
		
		this.id = id;
		this.offset = offset;
		
		this.fcontroller = fcontroller;
		
	}

	@Override
	public void run() {

		Car c = null;
		
		for(;;){
			try {
				Thread.sleep(offset);
			} catch (InterruptedException e) {
				return;
			}
			
			try {
				c = fcontroller.getCar(this);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 catch (InterruptedException e) {
					return;
			 }
		}
		
	}
	
	public int getId(){
		return id;
	}
	
	@Override
	public void setWorkTime(long time) {
		synchronized(offset){
			offset = time;
		}		
	}
	
}
