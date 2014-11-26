package ru.nsu.sumaneev.pusher.world;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Main class for cells of {@link World}
 *
 */
public class Cell {

	private int x = 0;
	private int y = 0;
	
	private Image image = null;
	

	public Cell(int x, int y, int size, String imgFile) throws IOException{
		this.x = x;
		this.y = y;
		
		BufferedImage img = ImageIO.read(new File(imgFile));

        this.image = img.getScaledInstance(size, size, Image.SCALE_DEFAULT);
	}
	
	public Image getImage() {
        return this.image;
    }
	
	public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }
    
    public void move(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    
	protected void setImage(String name) throws IOException {


    }

}
