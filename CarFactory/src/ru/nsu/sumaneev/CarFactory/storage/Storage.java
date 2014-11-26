package ru.nsu.sumaneev.CarFactory.storage;

import java.util.LinkedList;
import java.util.List;

public class Storage<T> {

	private List<T> goods = new LinkedList<T>();
	private long maxSize = 0;
	
	
	public Storage(long size){
		this.maxSize = size;
	}
	
	public void setMaxSize(int size){
		this.maxSize = size;
	}
	
	public int size(){
		return goods.size();
	}
	
	public int add(T newGood){
		
		if (goods.size() == maxSize){
			return -1;
		}
		
		synchronized (goods){ 
			goods.add(newGood);
		}
		
		
		return 0;
	}
	
	public T get(){
		
		if (goods.isEmpty()){
			return null;
		}

		synchronized (goods){
		
			return goods.remove(0);
		}
	}
	
}
