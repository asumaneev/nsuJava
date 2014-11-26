package ru.nsu.sumaneev.mechanics;

import org.apache.log4j.Logger;

/**
 * Perform function Ward
 */
public class Ward extends Command {

	/**
	 * make performer not to draw a lines
	 * @param arguments - empty array
	 * @param field - field with which works function 
	 * 
	 * @return 0 if function finished normal
	 */
	public int run(String arguments, Field field){
		
		if (0 != arguments.length()){
			
			throw new IllegalArgumentException("invalid parameters in command \"DRAW\": command need no arguments");
		}
		
		log.debug("make field not draw");
		
		field.setDrawing(false);
		
		log.debug("finish");
		
		return 0;
	}
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Ward.class.getName());

}
