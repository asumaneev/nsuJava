package ru.nsu.sumaneev.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import ru.nsu.sumaneev.mechanics.Command;
/**
 * <Contains all available for user functions and let to call this functions
 */

public class Factory{

	/**
	 *Read config file and receive list of available functions
	 *
	 *@param configFile - name of file with configuration
	 */
	@SuppressWarnings("unchecked")
	public Factory(String configFile) throws Exception{
	
		//open current directory
		File dir = new File(".");
		//find separator
		String separator = System.getProperty("file.separator");
		//open config file
		
		Properties prop = new Properties();
		
		log.debug("open config file: \"" + dir.getCanonicalPath() + separator + configFile + "\"");
		try (FileInputStream in = new FileInputStream(dir.getCanonicalPath() + separator + configFile)){
		
			log.debug("load configuration from config file");
		
			prop.load(in);
		}
				
		Set<String> keys = prop.stringPropertyNames();
		
		log.debug("fill map with classes' and commands' names");
		
		for (String key: keys){
			
			try{
	
				commandsClasses.put(key, (Class<Command>) Class.forName(prop.getProperty(key)));
			}
			catch(ClassNotFoundException e){
				
				log.debug("can not load class", e);
				
				throw new ClassNotFoundException("can not load class from config file: \"" + prop.getProperty(key) + "\"");
				
			}
		}
		
		log.debug("finish");
	}
	
	/**
	 * Looks for function and call it if this function is available
	 * 
	 * @param name - name of function
	 * @param args - parameters of this function
	 * @param field - field with which this function will work
	 */
	
	public Command newInstance(String name) throws Exception  {
		
		log.debug("get new command: " + name);
		
		Command command = null;
		
		try{
			command = commandsClasses.get(name).newInstance();
			
		}
		catch(NullPointerException e){
			log.debug("can not call command", e);
			
			throw new IllegalArgumentException("unknown command \"" + name + "\"");
		}
		
		log.debug("finish");
		
		return command;
	}
	
	
	private Map<String, Class<Command>> commandsClasses = new HashMap<String, Class<Command>>();
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Factory.class.getName());

}
