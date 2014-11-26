package ru.nsu.sumaneev.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
/**
 * Responsible for parsing entered data and finding
 * commands' names and their arguments
 *
 */
public class Parser {

	/**
	 * Parse arguments which user entered
	 * 
	 * @param args - array of entered arguments
	 * 
	 * @return 0 if arguments are correct or 
	 * 1 if there is need to finish program 
	 */
	public int parseArguments(String args) throws IOException{
		
		log.debug("parse data: " + args);
		
		int terminator = args.indexOf(" ");
		
		commandName = (-1 == terminator) ? args : args.substring(0, terminator); 
		
		commandName = commandName.toUpperCase();
		
		log.debug("recieved command name: " + commandName);
		
		if (commandName.equalsIgnoreCase("quit") || (commandName.equalsIgnoreCase("exit"))){
			
			return 1;
		}
		
		if (commandName.equalsIgnoreCase("help")){
			
			return 2;
		}
			
		arguments = (-1 != terminator) ? args.substring(terminator + 1) : ""; 
		
		log.debug("recieved arguments: " + arguments);
		
		log.debug("finish");
		
		return 0;
	}
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Parser.class.getName());
	
	String commandName;
	String arguments;

}
