package ru.nsu.sumaneev.pusher.world;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JPanel;

/**
 * Panel where level is painted
 * 
 */
@SuppressWarnings("serial")
public class World extends JPanel {
	
	private int width = 0;
	private int height = 0;
	
	private final int cellSize = 40;
	
	private List<Cell> walls = new LinkedList<Cell>();
	private List<Cell> specPlaces = new LinkedList<Cell>();
	private List<Cell> boxes = new LinkedList<Cell>();
	private Cell pusher = null;
	
	private Map<CellType, String> imageFiles = null;
	
	private String imagesDir = "images/";
	
	public World(){
		imageFiles = new HashMap<CellType, String>();
		
		imageFiles.put(CellType.BOX, imagesDir + "box.png");
		imageFiles.put(CellType.PUSHER, imagesDir + "pusher.png");
		imageFiles.put(CellType.SPECIAL_PLACE, imagesDir + "specPlace.png");
		imageFiles.put(CellType.WALL, imagesDir + "wall.png");
	}
	
	/**
	 * Initializes field of level and print it
	 * @param field - interpretation of launched level
	 * @throws IOException
	 */
	public void initWorld(List<ArrayList<CellType>> field) throws IOException{
		
		int x = 0;
		int y = 0;
		
		for (ArrayList<CellType> line: field){
			
			for (CellType c: line){
				
				switch (c){
				case BOX:{
					boxes.add(new Cell(x, y, cellSize, imageFiles.get(c)));
					break;
				}
				case PUSHER:{
					pusher = new Cell(x, y, cellSize, imageFiles.get(c));
					break;
				}
				case SPECIAL_PLACE:{
					specPlaces.add(new Cell(x, y, cellSize, imageFiles.get(c)));
					break;
				}
				case WALL:{
					walls.add(new Cell(x, y, cellSize, imageFiles.get(c)));
					break;
				}
				case FREE:{
					break;
				}
				default:
					throw new IOException("invalid cell type");
				
				}
				
				x += cellSize;
				
			}
			
			y += cellSize;
			
			if (width < x){
				width = x;
			}
			
			x = 0;
			
		}
		
		height = y;

		setPreferredSize(new Dimension(width, height));
		repaint();
	}
	
	/**
	 * Update places of box and pusher. Repaint updated field
	 * @param field - new interpretation of level
	 * @throws IOException
	 */
	public void updateWorld(List<ArrayList<CellType>> field) throws IOException{
		
		boxes.clear();
		specPlaces.clear();
		
		int x = 0;
		int y = 0;
		
		for (ArrayList<CellType> line: field){
			
			for (CellType c: line){
				
				switch (c){
				case BOX:{
					boxes.add(new Cell(x, y, cellSize, imageFiles.get(c)));
					break;
				}
				case PUSHER:{
					pusher = new Cell(x, y, cellSize, imageFiles.get(c));
					break;
				}
				case SPECIAL_PLACE:{
					specPlaces.add(new Cell(x, y, cellSize, imageFiles.get(c)));
					break;
				}
				case WALL:{
					break;
				}
				case FREE:{
					break;
				}
				default:
					throw new IOException("invalid cell type");
				
				}
				
				x += cellSize;
			}
			
			x = 0;
			y += cellSize;
		}
		
		repaint();
	}
	
	private void repaintWorld(Graphics g){
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		List<Cell> world = new LinkedList<Cell>();
		world.addAll(walls);
		world.addAll(boxes);
		world.addAll(specPlaces);
		world.add(pusher);
		
		for (Cell cell: world){
			
			g.drawImage(cell.getImage(), cell.x(), cell.y(), this);
		}
	}
	
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        repaintWorld(g);
    }

}
