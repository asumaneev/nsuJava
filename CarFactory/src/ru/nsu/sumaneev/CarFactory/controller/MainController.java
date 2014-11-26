package ru.nsu.sumaneev.CarFactory.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.nsu.sumaneev.CarFactory.goods.Accessory;
import ru.nsu.sumaneev.CarFactory.goods.Body;
import ru.nsu.sumaneev.CarFactory.goods.Car;
import ru.nsu.sumaneev.CarFactory.goods.Engeny;
import ru.nsu.sumaneev.CarFactory.gui.View;
import ru.nsu.sumaneev.CarFactory.observers.WorkTimeObservable;
import ru.nsu.sumaneev.CarFactory.producer.Producer;
import ru.nsu.sumaneev.CarFactory.storage.StorageController;
import ru.nsu.sumaneev.CarFactrory.factory.CarFactory;

/*
 * TODO: close listener
 * storage size listener
 * producer time listener
 * 
 * listener is able to give its name 
 */

public class MainController implements ActionListenresGetter, Closer{

	private String configFile = "config.ini";
	
	private int storageBodySize = 0;
	private int storageEngenySize = 0;
	private int storageAccessorySize = 0;
	
	private int storageCarSize = 0;
	
	private int numOfAccessoryProducer = 0;
	private int numOfWorkers = 0;
	private int numOfDealers = 0;
	
	
	private StorageController<Body> bstorage = null;
	private StorageController<Engeny> estorage = null;
	private StorageController<Accessory> astorage = null;
	
	private StorageController<Car> cstorage = null;
	
	private CarFactory carFactory = null;
	
	private FactoryController factoryController = null;
	
	private Producer<Body> bproducer = null;
	private Producer<Engeny> eproducer = null;
	private List<Producer<Accessory>> aproducers = null;
	
	private List<Dealer> dealers = null;
	
	private List<Thread> threads = new LinkedList<Thread>();
	
	private View view = null;
	
	public static void main(String[] args) throws InterruptedException{
		new MainController("config.ini").run();
	}
	
	
	public MainController(String config){
		
		if (null != config){
			this.configFile = config;
		}
		

		
		try {
			parse();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		/*
		 * initialize storages
		 */
		bstorage = new StorageController<Body>(storageBodySize);
		estorage = new StorageController<Engeny>(storageEngenySize);
		astorage = new StorageController<Accessory>(storageAccessorySize);
		
		cstorage = new StorageController<Car>(storageCarSize);
		
		/*
		 * initialize producers
		 */
		bproducer = new Producer<Body>(Body.class, bstorage, 1000);
		eproducer = new Producer<Engeny>(Engeny.class, estorage, 1000);
		
		
		aproducers = new ArrayList<Producer<Accessory>>(numOfAccessoryProducer);
		
		for (int i = 0; i < numOfAccessoryProducer; ++i){
			aproducers.add(new Producer<Accessory>(Accessory.class, astorage, 1000));
		}
		
		
		/*
		 * initialize factory
		 */
		
		carFactory = new CarFactory(numOfWorkers, bstorage, estorage, astorage, cstorage);
		
		factoryController = new FactoryController(cstorage, carFactory);
		
		/*
		 * initialize dealers
		 */
		dealers = new ArrayList<Dealer>(numOfDealers);
		for (int i = 0; i < numOfDealers; ++i){
			dealers.add(new Dealer(i, 2000, factoryController));
		}
		
		view = new View(this, this);
	}
	
	public void run() throws InterruptedException{
		
		Thread t = new Thread(bproducer);
		threads.add(t);
		t.start();
		
		t = new Thread(eproducer);
		threads.add(t);
		t.start();
		
		for (Producer<Accessory> aproducer: aproducers){
			t = new Thread(aproducer);
			threads.add(t);
			t.start();
		}
		
		
		for (Dealer d: dealers){
			t = new Thread(d);
			threads.add(t);
			t.start();
		}
		
		view.start();
		
		SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                view.setVisible(true);
            }
        });
		
	}
	

	@Override
	public ChangeListener getSliderListener(JSlider slider, SliderType type) {
		
		List<WorkTimeObservable> p = new LinkedList<WorkTimeObservable>();
		
		switch(type){
		case ACCESSORY:
			for (WorkTimeObservable producer: aproducers){
				p.add(producer);
			}	
			break;
		case BODY:
			p.add(bproducer);
			break;
		case ENGENY:
			p.add(eproducer);
			break;
		case DEALER:
			for (WorkTimeObservable dealer: dealers){
				p.add(dealer);
			}
			break;
		default:
			return null;
		}
		
		return new SliderListener(slider, p);
	}
	
	@Override
	public SizeObserver getSizeObserver() {
		return new SizeGetter();
	}
	
	@Override
	public void close() {

		view.stop();
		
		for (Thread t: threads){
			t.interrupt();
		}
		
		carFactory.stop();
				
		factoryController.stop();	
		
	}
	
	private void parse() throws IOException{
		
		Properties prop = new Properties();
		
			FileInputStream conf = new FileInputStream(configFile);
			
			try {
				
				prop.load(conf);
				
				Set<String> keySet = prop.stringPropertyNames();
				
				for (String s: keySet){
				
					int n = Integer.parseInt(prop.getProperty(s));
					
					switch (s){
					
					case "StorageBodySize":{
						storageBodySize = n; 
						break;
					}
					case "StorageMotorSize":{
						storageEngenySize = n;
						break;
					}
					case "StorageAccessorySize":{
						storageAccessorySize = n;
						break;
					}
					case "StorageAutoSize":{
						storageCarSize = n;
						break;
					}
					case "AccessorySuppliers":{
						numOfAccessoryProducer = n;
						break;
					}
					case "Workers":{
						numOfWorkers = n;
						break;
					}
					case "Dealers":{
						numOfDealers = n;
						break;
					}
					default:{
						throw new IOException("unknown property");
					}
					}
				}
			
			}
			finally {
				conf.close();
			}
			
			
		
		
	}
	
	class SliderListener implements ChangeListener{
		
		private JSlider slider = null;
		private List<WorkTimeObservable> producers = null;
		
		public SliderListener(JSlider slider, List<WorkTimeObservable> producers){
			
			this.slider = slider;
			this.producers = producers;
		}

		@Override
		public void stateChanged(ChangeEvent arg0) {
		
			int newTime = slider.getValue();
			
			for (WorkTimeObservable producer: producers){
				producer.setWorkTime(newTime);
			}
		}
			
		
		
	}


	class SizeGetter implements SizeObserver{

		@Override
		public int getSize(SliderType slider) {
			
			int size = 0;
			
			switch(slider){
			case ACCESSORY:
				size = astorage.size();
				break;
			case BODY:
				size = bstorage.size();
				break;
			case CAR:
				size = cstorage.size();
				break;
			case ENGENY:
				size = estorage.size();
				break;
			default:
				return -1;
			
			}
			
			return size;
		}

		@Override
		public long getAllCars() {

			return carFactory.getCarNumber();
		}

		@Override
		public long getCurrentExercise() {

			return carFactory.getExecrsizeNum();
		}
		
	}
}
