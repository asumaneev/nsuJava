package ru.nsu.sumaneev.CarFactory.storage;


public class StorageController<T> {
	
	private long maxSize = 0;
	
	private Storage<T> storage = null;

	public StorageController(long maxSize){
		this.maxSize = maxSize;
		
		storage = new Storage<T>(maxSize);
	}
	
	public void add(T newGood) throws InterruptedException{
		
		synchronized(storage){
			
			if (maxSize == storage.size()){
				storage.wait();
			}
			
			storage.add(newGood);
			
		}
		
		synchronized(storage){
			storage.notify();
		}
	}
	
	public synchronized T get() throws InterruptedException{
		
		T good = null;
		
		synchronized(storage){
			if (0 == storage.size()){
				storage.wait();
			}
			
			good = storage.get();
		}
		
		if (storage.size() < maxSize / 2){
			synchronized(storage){
				storage.notifyAll();
			}
		}
		
		if (good == null){
			System.out.println("THERE IS NULL!");
		}
		
		return good;
	}
	
	public int size(){
		return storage.size();
	}
}
