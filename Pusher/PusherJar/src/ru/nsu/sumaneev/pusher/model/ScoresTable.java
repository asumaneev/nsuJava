package ru.nsu.sumaneev.pusher.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Consider record with best results of levels
 */
public class ScoresTable{
	
	Map<Integer, Level> levels = new HashMap<Integer, Level>();
	
	/**
	 * Load scores from file
	 * 
	 * @param recordsTableFile - file with levels' scores 
	 * @throws IOException
	 */
	public ScoresTable(String recordsTableFile) throws IOException{

		BufferedReader reader = null;
		
		try{
			
			reader = new BufferedReader(new FileReader(recordsTableFile));
			
		}
		catch (FileNotFoundException e){
			
			throw new FileNotFoundException("record's file not found: \"" + recordsTableFile + "\"");
			
		}
		
		String buffer;
		
		Pattern pattern = Pattern.compile("(-?[0-9]){1,1}\\s\\w{1,}\\s[0-9-1]{1,}\\s(-?[0-9]){1,}");
		
		int line = 0;
		try{
			while (null != (buffer = reader.readLine())){
				++line;
				if (!pattern.matcher(buffer).matches()){
					throw new IOException("invalid line in recordFile " + recordsTableFile + " at: " + line);
				}
				
				String[] row = buffer.split("\\s");
						
				levels.put(Integer.parseInt(row[0]), new Level(row[1], Long.parseLong(row[2]), Long.parseLong(row[3])));
			}
		}
		finally{
			reader.close();
		}
		
		
	}

	/**
	 * Returns number of existing levels
	 * @return number of levels
	 */
	
	public int getNumberOfLevels(){
		return levels.size();
	}
	
	/**
	 * Returns {@link Level} which corresponds to id-number
	 * @param id - number of level
	 * @return - information about scores
	 */
	public Level getLevel(int id){

		Level l = null;
		
		try{
			l = levels.get(id);
		}
		catch (Exception e){
			l = null;
		}
		return l;
		
	}
	
	/**
	 * Sets new score of {@link Level} which corresponds to id-number
	 * 
	 * @param id number of level which will be replaced 
	 * @param newScore - new result
	 * @throws NullPointerException
	 */
	public void setScore(int id, Level newScore) throws NullPointerException{
		
		levels.put(id, newScore);
	}
	
	/**
	 * Save current scores into the file
	 * @param scoreFile - file where scores will be stored
	 */
	public void save(String scoreFile) throws IOException{
		
		PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter(scoreFile)));
		
		try{
		
			Set<Integer> keySet = levels.keySet();
			
			for (int i: keySet){
				
				Level l = levels.get(i);
				
				fout.print(i);
				
				fout.print(" " + l.getPlayerName());
				
				fout.print(" " + l.getTime());
				
				fout.print(" " + l.getSteps());
				
				fout.println();
				
			}
		}
		finally{
			fout.close();
		}
		
		
	}
}