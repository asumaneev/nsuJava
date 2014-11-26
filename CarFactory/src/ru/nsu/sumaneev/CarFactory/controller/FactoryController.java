package ru.nsu.sumaneev.CarFactory.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import ru.nsu.sumaneev.CarFactory.goods.Car;
import ru.nsu.sumaneev.CarFactory.storage.StorageController;
import ru.nsu.sumaneev.CarFactrory.factory.CarFactory;


public class FactoryController {
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private StorageController<Car> storage = null;
	private CarFactory factory = null;
	
	private String logFileName = "Log.txt";
	private PrintWriter logFile = null;
		
	public FactoryController(StorageController<Car> storage, CarFactory factory){
		this.storage = storage;
		this.factory = factory;
		
		try {
			 logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFileName)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop(){
		
		
			if (null != logFile){
				logFile.close();
			}
	}
	
	public Car getCar(Dealer dealer) throws InterruptedException, IOException, ExecutionException{
	
		factory.newCar();
		
		Car c = null;

		Thread.sleep(5000);
		
		c = storage.get();
		
		synchronized(logFile){
			logFile.println(dateFormat.format(new Date()) + ": Dealer " + dealer.getId() + 
					": Auto " + c.getID()
					+ " (Body: " + c.getBodyID()
					+ ", Motor: " + c.getEngenyID()
					+ ", Accessory: " + c.getAccessoryID() + ")");
		}
		
		logFile.flush();
		
		return c;
		
	}
	
}
