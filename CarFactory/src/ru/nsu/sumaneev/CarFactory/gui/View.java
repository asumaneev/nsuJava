package ru.nsu.sumaneev.CarFactory.gui;

import java.awt.Dimension; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

import ru.nsu.sumaneev.CarFactory.controller.ActionListenresGetter;
import ru.nsu.sumaneev.CarFactory.controller.Closer;
import ru.nsu.sumaneev.CarFactory.controller.SizeObserver;
import ru.nsu.sumaneev.CarFactory.controller.SliderType;

@SuppressWarnings("serial")
public class View extends JFrame{

	private final int WIDTH = 700;
	private final int HEIGHT = 500;
	
	private final int OFFSET = 20;
	
	private int CHECK_TIME = 100;
	
	private ActionListenresGetter action = null;
	private Closer closer = null;
	
	private WindowListener exitListener = null;
	
	private JPanel mainPanel = new JPanel();
	
	private JPanel slidersPanel = new JPanel();
	private JSlider bodyProducerSlider = null;
	private JSlider engenyProducerSlider = null;
	private JSlider accessoryProducerSlider = null;
	private JSlider dealerSlider = null;
	
	private JPanel infoPanel = new JPanel();
	private JLabel bodyStorageLable = new JLabel();
	private JLabel engenyStorageLable = new JLabel();
	private JLabel accessoryStorageLable = new JLabel();
	private JLabel carStorageLable = new JLabel();
	private JLabel allCars = new JLabel();
	private JLabel currentExercise = new JLabel();
	
	private Timer timer = null;
	
	public View(ActionListenresGetter action, final Closer closer){
		
		this.action = action;
		this.closer = closer;
		
		/*
		 * Timer
		 */
		timer = new Timer(CHECK_TIME, new InfoTimer(action.getSizeObserver()));
		
		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		/*
		 * Main panel
		 */
		initUI();
		
		add(mainPanel);	
		
		add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		/*
		 * close button
		 */
		JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {

            	closer.close();
            	
            	System.exit(0);
            	
            }
        });
        add(close);
		
        /*
         * close listener
         */
        WindowListener exitListener = new WindowAdapter(){
        	
        	 @Override
             public void windowClosing(WindowEvent e) {
                 
        		 closer.close();
        		 
        		 System.exit(0);
             }
        	
        };
        
        addWindowListener(exitListener);
		setTitle("Factory");
		setSize(WIDTH, HEIGHT);
		setFocusable(false);
        setLocationRelativeTo(null);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        
		pack();
	}
	
	public void start(){
		
		timer.start();
	}
	
	public void stop(){
		timer.stop();
	}
	
	private void initUI(){

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		initSlidersPanel();
		
		mainPanel.add(slidersPanel);
		
		mainPanel.add(Box.createRigidArea(new Dimension(2 * OFFSET, 0)));
		
		initInfoPanel();
		
		mainPanel.add(infoPanel);
		
	}
	
	private void initSlidersPanel(){
		
		slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.Y_AXIS));
		
		JLabel bodyTitle = new JLabel();
		bodyTitle.setText("Body producer offset");
		slidersPanel.add(bodyTitle);
		
		bodyProducerSlider = new JSlider(0, 10000);
		bodyProducerSlider.setPreferredSize(new Dimension(200, 30));
		bodyProducerSlider.addChangeListener(action.getSliderListener(bodyProducerSlider, SliderType.BODY));
		
		slidersPanel.add(bodyProducerSlider);
		slidersPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		JLabel engenyTitle = new JLabel();
		engenyTitle.setText("Engeny producer offset");
		slidersPanel.add(engenyTitle);
		
		engenyProducerSlider = new JSlider(0, 10000);
		engenyProducerSlider.setPreferredSize(new Dimension(200, 30));
		engenyProducerSlider.addChangeListener(action.getSliderListener(engenyProducerSlider, SliderType.ENGENY));
		
		slidersPanel.add(engenyProducerSlider);
		slidersPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		JLabel accessoryTitle = new JLabel();
		accessoryTitle.setText("Accessory producer offset");
		slidersPanel.add(accessoryTitle);
		
		accessoryProducerSlider = new JSlider(0, 10000);
		accessoryProducerSlider.setPreferredSize(new Dimension(200, 30));
		accessoryProducerSlider.addChangeListener(action.getSliderListener(accessoryProducerSlider, SliderType.ACCESSORY));
		
		slidersPanel.add(accessoryProducerSlider);
		slidersPanel.add(Box.createRigidArea(new Dimension(0, OFFSET)));
		
		JLabel dealerTitle = new JLabel();
		dealerTitle.setText("Dealer producer offset");
		slidersPanel.add(dealerTitle);
		
		dealerSlider = new JSlider(0, 10000);
		dealerSlider.setPreferredSize(new Dimension(200, 30));
		dealerSlider.addChangeListener(action.getSliderListener(dealerSlider, SliderType.DEALER));
		
		slidersPanel.add(dealerSlider);
		
	}
	
	private void initInfoPanel(){
		
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
		infoPanel.add(bodyStorageLable);
		bodyStorageLable.setPreferredSize(new Dimension(200, 15));
		infoPanel.add(Box.createRigidArea(new Dimension(0, OFFSET / 2)));
		
		infoPanel.add(engenyStorageLable);
		engenyStorageLable.setPreferredSize(new Dimension(200, 15));
		infoPanel.add(Box.createRigidArea(new Dimension(0, OFFSET / 2)));
		
		infoPanel.add(accessoryStorageLable);
		accessoryStorageLable.setPreferredSize(new Dimension(200, 15));
		infoPanel.add(Box.createRigidArea(new Dimension(0, OFFSET / 2)));
		
		infoPanel.add(carStorageLable);
		carStorageLable.setPreferredSize(new Dimension(200, 15));
		infoPanel.add(Box.createRigidArea(new Dimension(0, OFFSET / 2)));
		
		infoPanel.add(allCars);
		allCars.setPreferredSize(new Dimension(150, 15));
		infoPanel.add(Box.createRigidArea(new Dimension(0, OFFSET / 2)));
		
		infoPanel.add(currentExercise);
		currentExercise.setPreferredSize(new Dimension(200, 15));
	}
	
	class InfoTimer implements ActionListener{

		private SizeObserver sizeObserver = null;
		
		public InfoTimer(SizeObserver sizeObserver){
			this.sizeObserver = sizeObserver;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			int size = sizeObserver.getSize(SliderType.BODY);
			bodyStorageLable.setText("Bodies in storage: " + size);
			
			size = sizeObserver.getSize(SliderType.ENGENY);
			engenyStorageLable.setText("Engenies in storage: " + size);
			
			size = sizeObserver.getSize(SliderType.ACCESSORY);
			accessoryStorageLable.setText("Accessories in storage: " + size);
			
			size = sizeObserver.getSize(SliderType.CAR);
			carStorageLable.setText("Cars in storage: " + size);
			
			long s = sizeObserver.getAllCars();
			allCars.setText("All cars: " + s);
			
			s = sizeObserver.getCurrentExercise();
			currentExercise.setText("Current workers: " + s);
			
		}		
	}

}

