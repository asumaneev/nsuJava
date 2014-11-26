package ru.nsu.sumaneev.CarFactory.goods;

public class Body implements Detail{

	private long id = 0;
	
	public Body(long currentBodyId){
		this.id = currentBodyId;
	}
	
	@Override
	public long getId() {
		return id;
	}

}
