package ru.nsu.sumaneev.CarFactory.goods;

public class Car {

	private Body body = null;
	private Engeny engeny = null;
	private Accessory accessory = null;
	
	private long id = 0;
	
	public Car(long id, Body body, Engeny engeny, Accessory accessory){
		
		this.id = id;
		
		this.body = body;
		this.engeny = engeny;
		this.accessory = accessory;
		
	}
	
	public long getID(){
		return id;
	}
	
	public long getBodyID(){
		return body.getId();
	}
	
	public long getEngenyID(){
		return engeny.getId();
	}
	
	public long getAccessoryID(){
		return accessory.getId();
	}
	
}
