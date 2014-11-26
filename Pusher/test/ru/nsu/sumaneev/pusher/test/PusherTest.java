package ru.nsu.sumaneev.pusher.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ru.nsu.sumaneev.pusher.model.Model;
import ru.nsu.sumaneev.pusher.world.CellType;

public class PusherTest {

	String testModelsDir = "testLevels/";
	
	Model mainModel = null;
	Model moveUp = null;
	Model moveRight = null;
	Model moveDown = null;
	Model moveLeft = null;
	Model moveBox = null;
	
	public boolean isEqual(Model first, Model second){
		
		List<ArrayList<CellType>> fieldOne = first.getFiled();
		List<ArrayList<CellType>> fieldTwo = second.getFiled();
		
		for (int i = 0; i < fieldOne.size(); ++i){
			
			ArrayList<CellType> lineOne = fieldOne.get(i);
			ArrayList<CellType> lineTwo = fieldTwo.get(i);
			
			for (int j = 0; j < lineOne.size(); ++j){
				
				if (lineOne.get(j) != lineTwo.get(j)){
					return false;
				}
			}
			
		}

		return true;
		
	}
	
	@Before
	public void init() throws FileNotFoundException, IOException{
		
		mainModel = new Model(testModelsDir + "mainLevel.sk");
		moveUp = new Model(testModelsDir + "moveUp.sk");
		moveRight = new Model(testModelsDir + "moveRight.sk");
		moveDown = new Model(testModelsDir + "moveDown.sk");
		moveLeft = new Model(testModelsDir + "moveLeft.sk");
		moveBox = new Model(testModelsDir + "moveBox.sk");
		
	}
	
	/*
	 * up
	 * right
	 * right (wall)
	 * down
	 * left
	 * left
	 * left (box box)
	 * down (box)
	 * down (box wall)
	 */
	
	@Test
	public void moveUpTest(){
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.UP));
		
		assertTrue(this.isEqual(mainModel, moveUp));
	}
	
	@Test
	public void moveRightTest(){
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.RIGHT));
		
		assertTrue(this.isEqual(mainModel, moveRight));
	}
	
	@Test
	public void moveWallTest(){
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.RIGHT));
		assertFalse("Was moved: ", mainModel.move(Model.Direction.RIGHT));
		
		assertTrue(this.isEqual(mainModel, moveRight));
	}
	
	@Test
	public void moveDownTest(){
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.DOWN));
		
		assertTrue(this.isEqual(mainModel, moveDown));
	}
	
	@Test
	public void moveLeftTest(){
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.LEFT));
		
		assertTrue(this.isEqual(mainModel, moveLeft));
	}

	@Test
	public void moveBoxBoxTest(){
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.LEFT));
		assertFalse("Was moved: ", mainModel.move(Model.Direction.LEFT));
		
		assertTrue(this.isEqual(mainModel, moveLeft));
	}

	@Test
	public void moveBoxTest(){
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.LEFT));
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.DOWN));
		
		assertTrue(this.isEqual(mainModel, moveBox));
	}

	@Test
	public void moveBoxWallTest(){
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.LEFT));
		assertTrue("Wasn't moved: ", mainModel.move(Model.Direction.DOWN));
		assertFalse("Was moved: ", mainModel.move(Model.Direction.DOWN));
		
		assertTrue(this.isEqual(mainModel, moveBox));
	}
}
