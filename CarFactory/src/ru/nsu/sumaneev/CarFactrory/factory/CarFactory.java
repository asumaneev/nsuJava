package ru.nsu.sumaneev.CarFactrory.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import ru.nsu.sumaneev.CarFactory.goods.Accessory;
import ru.nsu.sumaneev.CarFactory.goods.Body;
import ru.nsu.sumaneev.CarFactory.goods.Car;
import ru.nsu.sumaneev.CarFactory.goods.Engeny;
import ru.nsu.sumaneev.CarFactory.storage.StorageController;
import ru.nsu.sumaneev.CarFactory.threadPool.ThreadPool;


public class CarFactory{
	
	private StorageController<Body> bstorage = null;
	private StorageController<Engeny> estorage = null;
	private StorageController<Accessory> astorage = null;
	
	private StorageController<Car> cstorage = null;
	
	private List<Callable<Car>> workers = null;
	
	private ThreadPool<Car> pool = null;
	
	private AtomicLong numOfAllCars = new AtomicLong(0);
	
	private AtomicLong numOfExercises = new AtomicLong(0);
		
	public CarFactory(int numOfWorkers, StorageController<Body> bstorage, StorageController<Engeny> estorage, 
			StorageController<Accessory> astorage, StorageController<Car> cstorage){
		
		this.astorage = astorage;
		this.bstorage = bstorage;
		this.estorage = estorage;
		
		this.cstorage = cstorage;
		
		workers = new ArrayList<Callable<Car>>(numOfWorkers);
		
		for (int i = 0; i < numOfWorkers; ++i){
			workers.add(new Worker(this));
		}
		
		pool = new ThreadPool<Car>(numOfWorkers, workers);
		
	}
	
	public void newCar() throws InterruptedException, ExecutionException{
	
		numOfExercises.incrementAndGet();
		
		Car car = pool.doCommand().get();
		
		numOfExercises.decrementAndGet();

		cstorage.add(car);
		
		numOfAllCars.incrementAndGet();
	}
	
	public void stop(){
		
		pool.shutdown();
	}

	StorageController<Body> bodyStorage(){
		return bstorage;
	}
	
	StorageController<Engeny> engenyStorage(){
		return estorage;
	}
	
	StorageController<Accessory> accessoryStorage(){
		return astorage;
	}

	public long getCarNumber() {
		return numOfAllCars.get();
	}

	public long getExecrsizeNum() {
		return numOfExercises.get();
	}

}
