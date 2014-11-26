package ru.nsu.sumaneev.pusher.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nsu.sumaneev.pusher.world.CellType;

/**
 * Model contains field of level and mechanics for moving pusher
 * 
 */
public class Model {
	
	private final String SIGNATUER = "Pusher.Level.Sumaneev";
	
	private int width = -1;
	private int height = -1;
	
	private int pusherX = -1;
	private int pusherY = -1;
	
	private int numOfCompleatBoxes = 0;
	private int numOfAllBoxes = 0;
	
	private final char wall = 'x';
	private final char free = '.';
	private final char pusher = 't';
	private final char box = '*';
	private final char spec_place = '&';

	private List<ArrayList<Cell>> field = new ArrayList<ArrayList<Cell>>();
	
	private Map<Character, Cell> cellTypes = new HashMap<Character, Cell>();

	/**
	 * Construct empty model
	 */
	public Model(){
		initTypes();
	}
	
	/**
	 * Construct model and fill its field from file
	 * 
	 * 
	 * @param modelFile - file with field's interpretation
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Model(String modelFile) throws FileNotFoundException, IOException{
		initTypes();
		initModel(modelFile);
	}
	
	
	@Override
	public Model clone(){
		
		Model cloneModel = new Model();
		
		cloneModel.width = width;
		cloneModel.height = height;
		cloneModel.pusherX = pusherX;
		cloneModel.pusherY = pusherY;
		cloneModel.numOfAllBoxes = numOfAllBoxes;
		cloneModel.numOfCompleatBoxes = numOfCompleatBoxes;
		
		cloneModel.field = new ArrayList<ArrayList<Cell>>(height);
		
		for (int i = 0; i < field.size(); ++i){
			
			ArrayList<Cell> newLine = new ArrayList<Cell>(width);
			
			cloneModel.field.add(newLine);
			
			for (int j = 0; j < field.get(i).size(); ++j){
				newLine.add(field.get(i).get(j));
			}
			
		}
		
		return cloneModel;
		
	}
	
	/**
	 * move pusher in specified direction
	 * 
	 * @param direction - up, down, left or right direction
	 * @return true if pusher was moved 
	 * @throws IndexOutOfBoundsException
	 */
	public boolean move(Direction direction) throws IndexOutOfBoundsException{
		
		int prevPusherX = pusherX;
		int prevPuserY = pusherY;
		
		switch (direction){
		
		case UP:{
			moveY(-1);
			break;
		}
		case DOWN:{
			moveY(+1);
			break;
		}
		case LEFT:{
			moveX(-1);
			break;
		}
		case RIGHT:{
			moveX(1);
			break;
		}
		}
		
		if ((prevPusherX != pusherX) || (prevPuserY != pusherY)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks for completing level
	 * @return true if all boxes are on special places
	 */
	public boolean isComplete(){
		
		return numOfCompleatBoxes == numOfAllBoxes;
	}
	
	/**
	 * Returns DTO of model's field
	 * @return interpretation of field
	 */
	public List<ArrayList<CellType>> getFiled(){
		
		List<ArrayList<CellType>> f = new ArrayList<ArrayList<CellType>>(height);
			
		for (int i = 0; i < height; ++i){
			f.add(new ArrayList<CellType>(width));
			
			for (int j = 0; j < width; ++j){
				CellType c = null;
				
				switch (field.get(i).get(j)){
				case BOX:{
					c = CellType.BOX;
					break;
				}
				case FREE:{
					c = CellType.FREE;
					break;
				}
				case SPEC_PLACE:{
					c = CellType.SPECIAL_PLACE;
					break;
				}
				case SPEC_PLACE_WITH_BOX:{
					c = CellType.BOX;
					break;
				}
				case WALL:{
					c = CellType.WALL;
					break;
				}
				default:
					break;
				
				}
				
				f.get(i).add(c);
			}
		}
		
		f.get(pusherY).set(pusherX, CellType.PUSHER);
		
		return f;
	}
	
	/*
	 * *******************************************************************************
	 * *******************************************************************************
	 */
	private void initTypes(){
		cellTypes.put(wall, Cell.WALL);
		cellTypes.put(free, Cell.FREE);
		cellTypes.put(box, Cell.BOX);
		cellTypes.put(spec_place, Cell.SPEC_PLACE);
		
		//where pusher are this cell is free by default
		cellTypes.put(pusher, Cell.PUSHER);
	}
	
	private void initModel(String modelFile) throws FileNotFoundException, IOException{
		
		BufferedReader reader = null;
		
		try{
			
			reader = new BufferedReader(new FileReader(modelFile));
			//read signature
			String buffer = reader.readLine();
			
			if (!buffer.equals(SIGNATUER)){
				throw new IOException("invalid input file: \"" + modelFile + "\"");
			}
			
			//read field
			while (null != (buffer = reader.readLine())){
			
				//initialize width one time
				if (-1 == width){
					width = buffer.length();
					height = 0;
				}
				
				++height;
				if (width != buffer.length()){
					throw new IOException("invalid level: different width in line " + height + "\nexpected " + width);
				}
				
				ArrayList<Cell> newLine = new ArrayList<Cell>(width);
				
				field.add(newLine);
				
				//fill new line
				for (int i = 0; i < width; ++i){
								
					Cell c = cellTypes.get(buffer.charAt(i));
					
					if (null == c){
						throw new IOException("invlid cell at " + height + ":" + (i + 1));
					}
					
					if ((Cell.PUSHER == c) && (-1 != pusherX)){
						throw new IOException("pusher is specified more than one time at " + height + ":" + (i + 1));
					}
					else if (Cell.PUSHER == c){
						
						c = Cell.FREE;
						
						pusherX = i;
						pusherY = height - 1;
					}
					
					newLine.add(c);
					
					if (Cell.BOX == c){
						++numOfAllBoxes;
					}
				}		
			}
			
		}
		catch (FileNotFoundException e){
			
			throw new FileNotFoundException("model file not found: \"" + modelFile + "\"");
			
		}
		finally{
			if (null != reader){
				reader.close();
			}
		}
	}
	
	private void moveX(int direction) throws IndexOutOfBoundsException{

		Cell nCell = field.get(pusherY).get(pusherX + direction);
		
		//deadlock
		if (Cell.WALL == nCell){
			return;
		}
		
		if ((Cell.SPEC_PLACE == nCell) || (Cell.FREE == nCell)){
			
			pusherX += direction;

			return;
		}
		
		//BOX or SPEC_PLACE_WITH_BOX
		Cell nnCell = field.get(pusherY).get(pusherX + 2 * direction);
		
		//deadlock
		if ((Cell.WALL == nnCell) || (Cell.BOX == nnCell) || (Cell.SPEC_PLACE_WITH_BOX == nnCell)){
			return;
		}
		
		
		if ((Cell.SPEC_PLACE == nnCell) || (Cell.FREE == nnCell)){
			
			//set nnCell
			if (Cell.SPEC_PLACE == nnCell){
				
				field.get(pusherY).set(pusherX + 2 * direction, Cell.SPEC_PLACE_WITH_BOX);
				++numOfCompleatBoxes;
			}
			else{
				field.get(pusherY).set(pusherX + 2 * direction, Cell.BOX);
			}
			
			//set nCell
			if (Cell.SPEC_PLACE_WITH_BOX == nCell){
				
				
				field.get(pusherY).set(pusherX + direction, Cell.SPEC_PLACE);
				--numOfCompleatBoxes;				
			}
			else{
				//under box there is free place
				field.get(pusherY).set(pusherX + direction, Cell.FREE);
			}
			
			//set pusher
			pusherX += direction;
		}
	}
	
	private void moveY(int direction){
		
		Cell nCell = field.get(pusherY + direction).get(pusherX);
		
		//deadlock
		if (Cell.WALL == nCell){
			return;
		}
		
		if ((Cell.SPEC_PLACE == nCell) || (Cell.FREE == nCell)){
			
			pusherY += direction;
			
			return;
		}
		
		//BOX or SPEC_PLACE_WITH_BOX
		Cell nnCell = field.get(pusherY + 2 * direction).get(pusherX);
		
		//deadlock
		if ((Cell.WALL == nnCell) || (Cell.BOX == nnCell) || (Cell.SPEC_PLACE_WITH_BOX == nnCell)){
			return;
		}
		
		
		if ((Cell.SPEC_PLACE == nnCell) || (Cell.FREE == nnCell)){
			
			//set nnCell
			if (Cell.SPEC_PLACE == nnCell){
				
				field.get(pusherY  + 2 * direction).set(pusherX, Cell.SPEC_PLACE_WITH_BOX);
				++numOfCompleatBoxes;
			}
			else{
				field.get(pusherY + 2 * direction).set(pusherX, Cell.BOX);
			}
			
			//set nCell
			if (Cell.SPEC_PLACE_WITH_BOX == nCell){
				
				
				field.get(pusherY + direction).set(pusherX, Cell.SPEC_PLACE);
				--numOfCompleatBoxes;				
			}
			else{
				//under box there is free place
				field.get(pusherY + direction).set(pusherX, Cell.FREE);
			}
			
			//set pusher
			pusherY += direction;
		}
	}
	
	public static enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	static enum Cell{
		
		WALL,
		FREE,
		BOX,
		SPEC_PLACE,
		SPEC_PLACE_WITH_BOX,
		PUSHER
		
	}

}
