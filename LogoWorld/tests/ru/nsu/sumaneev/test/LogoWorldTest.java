package ru.nsu.sumaneev.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ru.nsu.sumaneev.controller.Factory;
import ru.nsu.sumaneev.mechanics.Cell;
import ru.nsu.sumaneev.mechanics.Field;

public class LogoWorldTest {
	
	@Before
	public void initialization() throws Exception{
				
		factory = new Factory("factory.cfg");
	}
	
	@Test
	public void initTest() throws Exception{
		
		factory.newInstance("INIT").run(width + " " + height + " " + x + " " + y, field);
		
		assertEquals(width, field.width());
		assertEquals(height, field.height());
		assertEquals(x, field.getXAP());
		assertEquals(y, field.getYAP());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void initTestExcept() throws Exception{
		
		factory.newInstance("wrong command");
	}
	
	@Test
	public void moveTest() throws Exception{

		factory.newInstance("INIT").run(width + " " + height + " " + x + " " + y, field);
		
		
		factory.newInstance("MOVE").run("l 1", field);
		factory.newInstance("MOVE").run("u 1", field);
		
		assertEquals((((x - 1) % width) + width) % width, field.getXAP());
		assertEquals((((y - 1) % height) + height) % height, field.getYAP());
		
		factory.newInstance("MOVE").run("r 1", field);
		factory.newInstance("MOVE").run("d 1", field);
		
		assertEquals(x, field.getXAP());
		assertEquals(y, field.getYAP());
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void moveTestExcept() throws Exception{
		
		factory.newInstance("MOVE").run("s 1", field);
	}

	@Test
	public void teleportTest() throws Exception{
		
		factory.newInstance("INIT").run(width + " " + height + " " + x + " " + y, field);
		
		int x1 = x + 3;
		int y1 = y + 2;
		
		factory.newInstance("TELEPORT").run(x1 + " " + y1, field);
		
		assertEquals(x1, field.getXAP());
		assertEquals(y1, field.getYAP());
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void teleportTestExcept() throws Exception{
		
		factory.newInstance("TELEPORT").run("1 sda", field);
	}
	
	@Test
	public void DrawTest() throws Exception{
		
		
		factory.newInstance("INIT").run(width + " " + height + " " + x + " " + y, field);
		
		//Test draw
		factory.newInstance("DRAW").run("", field);
		
		factory.newInstance("MOVE").run("d 3", field);
		
		for (int i = x; i < (((x + 3) % width) + width) % width; i =(((i + 1) % width) + width) % width){
			
			assertEquals(Cell.DRAWN, field.getCell(x, i));
		}
		
		//Test ward
		factory.newInstance("WARD").run("", field);
		
		factory.newInstance("MOVE").run("d 3", field);
		
		for (int i = x; i < (((x + 3) % width) + width) % width; i =(((i + 1) % width) + width) % width){
			
			assertEquals(Cell.DRAWN, field.getCell(x, i));
			
		}

	}
	
	int width = 5;
	int height = 5;
	int x = 0;
	int y = 0;

	Field field = new Field();
	Factory factory = null;
	
}
