package ru.nsu.sumaneev.mechanics;

import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * Place where performer draws
 *
 */

public class Field {

	/**
	 *  Constructor of class Field
	 */
	public Field(){
		
		ap = new int[2];
		
		ap[0] = 0;
		ap[1] = 0;
	}
	
	/**
	 *  Create new field 
	 * 
	 *  Change size of field, frees cells, 
	 * move performer to (0, 0) and make marker not drawing 
	 * 
	 * @param x - width of new field
	 * @param y - height of new field
	 */
	
	void resize(int x, int y){

		log.debug("resize field");
		
		width = x;
		height = y;
		
		log.debug("create new field");
		
		field = new Cell[width][height];
		
		log.debug("fill new field");
		
		for (int i = 0; i < width; ++i){
			
			Arrays.fill(field[i], Cell.FREE);
			
		}
			
		ap[0] = 0;
		ap[1] = 0;
		
		marker = Cell.FREE;
	}

	/**
	 *  Move performer to new coordinates without drawing 
	 * 
	 * @param x - new x coordinate
	 * @param y - new y coordinate
	 */
	void setAP(int x, int y){
		
		log.debug("set new coordiantes for AP: " + x + " " + y);
		
		log.debug("mark previous cell with " + marker);
		
		field[ap[0]][ap[1]] = marker;
		
		ap[0] = x;
		ap[1] = y;
	}
	
	
	/**
	 *  Move perform along X 
	 * 
	 *  If drawing mode is available performer 
	 * reserves the line behind him 
	 * 
	 * @param direction - direction of performer
	 * <ul>
	 * <li> 1 -  if performer moves right
	 * <li> -1 - if performer moves left 
	 * </ul>
	 */
	void moveX(int direction, int steps){
		
		log.debug("move AP along X on " + steps + " steps in " + direction);
		
		for (int i = 0; i < steps; ++i){
		
			if (Cell.DRAWN != field[ap[0]][ap[1]])
				field[ap[0]][ap[1]] = marker;
			
			ap[0] += direction;
			
			ap[0] = Command.validateCoordinate(ap[0], width);
			ap[1] = Command.validateCoordinate(ap[1], height);
			
		}
	}
		
	/**
	 *  Move perform along Y 
	 * 
	 *  If drawing mode is available performer 
	 * reserves the line behind him 
	 * 
	 * @param direction - direction of performer
	 * <ul>
	 * <li> 1 -  if performer moves up
	 * <li> -1 - if performer moves down 
	 * </ul>
	 */
	void moveY(int direction, int steps){
		
		log.debug("move AP along Y on " + steps + " steps in " + direction);
		
		for (int i = 0; i < steps; ++i){			
			
			if (Cell.DRAWN != field[ap[0]][ap[1]])
				field[ap[0]][ap[1]] = marker;
			
			ap[1] += direction;
			
			ap[0] = Command.validateCoordinate(ap[0], width);
			ap[1] = Command.validateCoordinate(ap[1], height);
			
		}
	}
	
	/**
	 *  set the drawing mode 
	 * 
	 * @param drawing - true if drawing mode is on
	 */
	void setDrawing(boolean drawing){
		
		log.debug("set drawing mode: " + drawing);
		
		if (drawing){
			
			marker = Cell.DRAWN;
		}
		else{
			
			marker = Cell.FREE;
		}
	}
	
	/**
	 *  Return state of cell 
	 * 
	 * @param x - x coordinate of cell
	 * @param y - y coordinate of cell
	 * 
	 * @return state of cell: FREE || AP || DRAWED
	 */
	public Cell getCell(int x, int y) throws IllegalArgumentException{
		
		if ((x < 0) || (x >= width) || (y < 0) || (y >= height)){
			
			log.error("invalid coordinates: " + x + " " + y);
			
			throw new IllegalArgumentException("invalid coordinates of cell");
			
		}
		
		return field[x][y];
	}
	
	/**
	 * Return x coordinate of AP
	 * 
	 */
	public int getXAP(){
	
		return ap[0];
	}
	
	/**
	 * Return y coordinate of AP
	 * 
	 */	
	public int getYAP(){
		return ap[1];
	}
	/**
	 *  Return width of field 
	 * 
	 * @return width of field
	 */
	public int width(){
		return width;
	}

	/**
	 *  Return width of height 
	 * 
	 * @return height of field
	 */
	public int height(){
		return height;
	}

	private int height = 0;
	private int width = 0;
	
	private Cell[][] field = null; 
	
	private int[] ap = null;
	
	private Cell marker = Cell.FREE;
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Field.class.getName());

}
