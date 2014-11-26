package ru.nsu.sumaneev.mechanics;

import org.apache.log4j.Logger;

/**
 * Base class of available commands
 * such as {@link Init} and {@link Move}</p> 
 * 
 */
public abstract class Command {
	/**
	 * Abstract function for classes {@link Init} and {@link Move} etc.
	 * 
	 * @param arguments - array of arguments which user entered
	 * @param field - field which {@link Controller} uses
	 * 
	 * @return 0 if finished correctly
	 */
	public abstract int run(String arguments, Field field) throws IllegalArgumentException;

	
	/**
	 * Validate coordinate that it does not be out of range of {@link Field} size
	 * 
	 * @param coord - value of coordinate
	 * @param size - size of field's board
	 * 
	 * @return validate value of coordinate
	 */
	static int validateCoordinate(final int coord, final int size){
		
		log.debug("validate coordinate " + coord + "by size " + size);
		
		return ((coord % size) + size) % size;
	}
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Command.class.getName());
}
