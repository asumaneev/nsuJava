package ru.nsu.sumaneev.mechanics;

import org.apache.log4j.Logger;

/**
 * Perform function Teleport
 */
public class Teleport extends Command {

	/**
	 * change coordinates of performer without drawing line
	 * 
	 * @param arguments - new coordinates of performer
	 * @param field - field with which works function
	 * 
	 *  @return 0 if function finished normal
	 */
	public int run(String arguments, Field field) throws IllegalArgumentException{
		
		
		
		if ((0 == field.width()) || (0 == field.height())){
			
			throw new IllegalArgumentException("can not call command \"MOVE\": field is not initializate");
		}
		
		log.debug("split parameters: " + arguments);
		
		String[] parameters = arguments.split("\\s");
		
		if (2 != parameters.length){
			
			throw new IllegalArgumentException("invalid parameters in command \"TELEPORT\": incorrect number");
		}
		
		Integer x = 0;
		Integer y = 0;
		
		log.debug("parse parameters to int");
		
		try{
			x = Integer.parseInt(parameters[0]);
			y = Integer.parseInt(parameters[1]);
		}
		catch(NumberFormatException e){
			
			throw new IllegalArgumentException("invalid parameters in command \"TELEPORT\": must be integers");
		}
			
		x = validateCoordinate(x, field.width());
		y = validateCoordinate(y, field.height());

		log.debug("set new coordinates");
		
		field.setAP(x, y);
		
		return 0;		
	}
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Draw.class.getName());
}
