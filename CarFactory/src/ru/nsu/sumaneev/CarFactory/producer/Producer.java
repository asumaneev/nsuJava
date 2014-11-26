package ru.nsu.sumaneev.CarFactory.producer;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicLong;

import ru.nsu.sumaneev.CarFactory.goods.Detail;
import ru.nsu.sumaneev.CarFactory.observers.WorkTimeObservable;
import ru.nsu.sumaneev.CarFactory.storage.StorageController;

public class Producer<T extends Detail> implements Runnable, WorkTimeObservable {

	private AtomicLong currentId = new AtomicLong(0);
	private Long workTime = 0L;
	
	private Class<T> c = null;
	
	private StorageController<T> storage = null;
	
	public Producer(Class<T> c, StorageController<T> storage, long workTime){
		this.storage = storage;
		this.workTime = workTime;
		
		this.c = c;
	}
	
	@Override
	public void run() {
		
		T d = null;
		
		for (;;){
			try {
				Thread.sleep(workTime);
			} catch (InterruptedException e) {
				return;
			}
			

			try {
				d = getDetail();
			} catch (Exception e1) {
				currentId.decrementAndGet();
				continue;
			}
			
			try {
				storage.add(d);
			} catch (InterruptedException e) {
				return;
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private T getDetail() throws Exception {
		
		T d = null;
		
	
		Constructor<?>[] constructors =  c.getDeclaredConstructors();
		
		try{
		
			d = (T) constructors[0].newInstance(currentId.get());
		}
		catch (Exception e){
			e.printStackTrace();
			throw e;
		}
		
		currentId.incrementAndGet();
		
		return d;
	}

	@Override
	public void setWorkTime(long time) {
		
		synchronized(workTime){
			workTime = time;
		}
		
	}

}
