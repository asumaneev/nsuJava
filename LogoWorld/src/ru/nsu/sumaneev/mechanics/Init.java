package ru.nsu.sumaneev.mechanics;

import org.apache.log4j.Logger;

/**
 * Perform function Init
 * 
 */
public class Init extends Command {
	
	/**
	 * Initialize {@link Field}
	 * 
	 * @param arguments - parameters of function INIT contains sizes of field 
	 * and(may not) initial coordinates of performer
	 * @param field - field with which works function
	 * @return 0 if function finished normal
	 */
	public int run(String arguments, Field field) throws IllegalArgumentException {
		
		//check inputs
		
		log.debug("split paremeters: " + arguments);
		
		String[] parameters = arguments.split("\\s");
		
		if ((4 != parameters.length) && (2 != parameters.length)){
			
			throw new IllegalArgumentException("invalid parameters in command \"INIT\": incorrect number");
		}
		
		Integer[] intParam = new Integer[parameters.length];
		
		log.debug("parse parameters to int");
		
		try{
			for (int i = 0; i < parameters.length; ++i){
				
				intParam[i] = Integer.parseInt(parameters[i]);
			}
		}
		catch(NumberFormatException e){
			
			throw new IllegalArgumentException("invalid parameters in command \"INIT\": all of them must be integers");
		}
		
		
		
		if ((intParam[0] <= 0) || (intParam[1] <= 0)){
			
			throw new IllegalArgumentException("invalid parameters in command \"INIT\": width or height of field are incorrect");
			
		}
		
		log.debug("resize field");
		
		field.resize(intParam[0], intParam[1]);
		
		if (4 == intParam.length){
		
			intParam[2] = validateCoordinate(intParam[2], intParam[0]);
			intParam[3] = validateCoordinate(intParam[3], intParam[1]);
			
			field.setAP(intParam[2], intParam[3]);
		}
		else{
			field.setAP(0, 0);
		}
		
		log.debug("finish");
		
		return 0;
	}
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Init.class.getName());
}
