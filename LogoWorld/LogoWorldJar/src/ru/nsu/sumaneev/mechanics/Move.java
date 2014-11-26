package ru.nsu.sumaneev.mechanics;

import org.apache.log4j.Logger;

/**
 * Perform function Move
 */
public class Move extends Command {

	/**
	 * Move performer in declared direction
	 * @param arguments - direction and number of steps
	 * <ul>
	 * <li>0 - left
	 * <li>1 - up
	 * <li>2 - right
	 * <li>3 - down
	 * </ul>
	 * @param field - field with which works function 
	 * 
	 * @return 0 if function finished normal
	 */
	public int run(String arguments, Field field) throws IllegalArgumentException{

		if ((0 == field.width()) || (0 == field.height())){
			
			throw new IllegalArgumentException("can not call command \"MOVE\": field is not initializate");
		}
		
		log.debug("split parameters: " + arguments);
		
		String[] parameters = arguments.split("\\s");
			
		if ((2 != parameters.length) && (1 != parameters.length)){
			
			throw new IllegalArgumentException("invalid parameters in command \"MOVE\": incorrect number");
			
		}
				
		int steps = 0;
		
		try{
			steps = (1 == parameters.length) ? 1 : Integer.parseInt(parameters[1]);
		}
		catch(NumberFormatException e){
			throw new IllegalArgumentException("invalid second parameter in command \"TELEPORT\": must be integer");
		}
		
		if (steps < 0){
			
			throw new IllegalArgumentException("invalid parameters in command \"MOVE\": incorrect number of steps");
		}
		
		log.debug("move AP to the \"" + parameters[0] + "\" on " + steps + "step(s)");
		
		if (parameters[0].equalsIgnoreCase("l")){
			
			field.moveX(-1, steps);
		}
		else if (parameters[0].equalsIgnoreCase("u")){
			
			field.moveY(-1, steps);
		}
		else if (parameters[0].equalsIgnoreCase("r")){
			
			field.moveX(1, steps);
		}
		else if (parameters[0].equalsIgnoreCase("d")){
			
			field.moveY(1, steps);
		}
		else{
			
			throw new IllegalArgumentException("invalid parameters in command \"MOVE\": first argument must be [L|U|R|D]");
		}
		
		log.debug("finish");
		
		return 0;
		
	}
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Move.class.getName());

}
