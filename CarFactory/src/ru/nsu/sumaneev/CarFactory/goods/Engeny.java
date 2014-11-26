package ru.nsu.sumaneev.CarFactory.goods;

public class Engeny implements Detail{

	private long id = 0;
	
	public Engeny(long currentId){
		this.id = currentId;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
}
