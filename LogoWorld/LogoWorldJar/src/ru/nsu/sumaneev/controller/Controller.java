package ru.nsu.sumaneev.controller;

import java.io.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ru.nsu.sumaneev.mechanics.Command;
import ru.nsu.sumaneev.mechanics.Field;


/**
 * Read user's data, parse it and call needed functions
 * 
 */
public class Controller{

	/**
	 * Constructor of class {@link Controller}
	 */
	public Controller() throws Exception{
		
		try{
			factory = new Factory(factoryConfigFile);
		}
		catch(IOException e){
		
			log.fatal("factory crash", e);
			
			viewer.printError(e.getMessage());
			
			throw new Exception("Error: program was aborted");
		}
		catch(ClassNotFoundException e){
			
			log.error("factory can not find class", e);
			
			viewer.printError(e.getMessage());
			
			
		}
		catch(NullPointerException e){
			
			log.fatal("factory crash", e);
			
			viewer.printError(e.getMessage());
			
			throw new Exception("Error: program was aborted");
			
		}
		
		log.debug("factory is created");
		
		log.debug("finish");
	}
	
	/**
	 * Run the controller
	 * 
	 * Controller begin to receive user's data, 
	 * parse it and call needed functions
	 * 
	 * @param args - initial arguments of process
	 */
	public void run(String[] args) throws Exception{
	
		String inputString = null;
			
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		
		while (null != (inputString = buf.readLine())){
			
			try{
				
				log.debug("read data");

				int res = parser.parseArguments(inputString);
				
				if (1 == res){
					
					log.debug("program is closedcorrectly");
					
					//user want to close program
					return;
				}
				else if (2 == res){
					
					viewer.help();
					
					continue;
				}
			}
			catch(IOException e){
				
				log.error("error in reading user's data", e);
				
				viewer.printError(e.getMessage());
				
				continue;
			}
			
			log.debug("call command \"" + parser.commandName + "\" with parameters \"" + parser.arguments + "\"");
			
			try{
				Command command = factory.newInstance(parser.commandName);
				command.run(parser.arguments, field);
			}
			catch(IllegalArgumentException e){
				
				log.error("can not call command: \"" + parser.commandName + "\"", e);
				
				viewer.printError(e.getMessage());
				
				continue;
			}
			
			viewer.printField(field);
			
		}
		
		log.debug("finish");
		
		buf.close();
	}
	
	public static void main(String[] args) throws Exception{
		
		PropertyConfigurator.configure("log4j.properties");
		
		Controller ctrl = new Controller();
		
		ctrl.run(args);
		
		log.debug("finish");
		
		return;
		
	}
	
	private Factory factory = null;
	
	private Parser parser = new Parser();
	
	private Field field = new Field();
	
	private View viewer = new View();
	
	private String factoryConfigFile = "factory.cfg";
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Controller.class.getName());
}
