package ru.nsu.sumaneev.CarFactory.goods;

public class Accessory  implements Detail{

	private long id = -1;
	
	public Accessory(long currentId){
		this.id = currentId;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
}
