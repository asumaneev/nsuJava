package ru.nsu.sumaneev.CarFactrory.factory;

import java.util.concurrent.Callable;

import ru.nsu.sumaneev.CarFactory.goods.Accessory;
import ru.nsu.sumaneev.CarFactory.goods.Body;
import ru.nsu.sumaneev.CarFactory.goods.Car;
import ru.nsu.sumaneev.CarFactory.goods.Engeny;
import ru.nsu.sumaneev.CarFactory.storage.StorageController;


public class Worker implements Callable<Car>{
	
	private StorageController<Body> bstorage = null;
	private StorageController<Engeny> estorage = null;
	private StorageController<Accessory> astorage = null;
	
	private static Long currentCarID = 0L;
	
	public Worker(CarFactory factory){
		
		bstorage = factory.bodyStorage();
		estorage = factory.engenyStorage();
		astorage = factory.accessoryStorage();
	}
	
	@Override
	public Car call() throws Exception {

		Body b = bstorage.get();
		Engeny e = estorage.get();
		Accessory a = astorage.get();
		
		synchronized(currentCarID){
			return new Car(currentCarID++, b, e, a);
		}
	}

}
