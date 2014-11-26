package ru.nsu.sumaneev.controller;

import org.apache.log4j.Logger;

import ru.nsu.sumaneev.mechanics.Cell;
import ru.nsu.sumaneev.mechanics.Field;

/**
 * View is responsible for displaying output values
 * such as field or errors
 */
public class View {

	/**
	 * Print formatted information about error
	 *@param errorLog - string with information about error
	 */
	public void printError(String errorLog){
		
		log.debug("print error: " + errorLog);
		
		System.out.println("Error: " + errorLog);
		
		log.debug("finish");

	}	

	/**
	 * Print formatted field in System.out
	 *
	 * @param field - The field to be printed
	 *  <ul>
     * 	<li>"O" - free cell without performer or mark</li>
     * 	<li>"*" - marked cell</li>
     * 	<li>"A" - cell with performer"</li>
     * </ul>
	 */
	public void printField(Field field){
		
		log.debug("print field");
		
		System.out.print(" ");
		
		for (int i = 0; i < field.width(); ++i){
			
			System.out.print("--");
		}
		
		System.out.print(" \n");
		
		for (int i = 0; i < field.height(); ++i){
			System.out.print("|");
			
			for (int j = 0; j < field.width(); ++j){
				
				if ((j == field.getXAP()) && (i == field.getYAP())){
					
					System.out.print("A ");
					
				}
				else if(field.getCell(j, i).equals(Cell.DRAWN)){
					System.out.print("* ");
				}
				else if (field.getCell(j, i).equals(Cell.FREE)){
					System.out.print("O ");
				}
				
			}
			
			System.out.println("|");			
		}
		
		System.out.print(" ");
		
		for (int i = 0; i < field.width(); ++i){
			
			System.out.print("--");
		}
		
		System.out.print(" \n");
		
		log.debug("finish");
		
	}
	
	/**
	 * Print program's manual 
	 */
	public void help(){
		
		log.debug("print help message");
		
		System.out.println("Help yourself");
		
		log.debug("finish");
	}
	
	private static org.apache.log4j.Logger log = Logger.getLogger(View.class.getName());
}
