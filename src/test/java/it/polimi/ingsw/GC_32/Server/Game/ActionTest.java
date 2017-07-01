package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;

import org.junit.Test;

public class ActionTest {
	public Action action;
	
	@Test
	public void checkType(){
		this.action = new Action("TestType",0,0,0);
		assertEquals("TestType", this.action.getActionType());
	}
	
	@Test
	public void checkValue(){
		this.action = new Action("TestType",5,0,0);
		assertEquals(5, this.action.getActionValue());
	}
	
	@Test
	public void checkSpaceId(){
		this.action = new Action("TestType",0,2,0);
		assertEquals(2, this.action.getActionSpaceId());
	}
	
	@Test
	public void checkRegionId(){
		this.action = new Action("TestType",0,0,3);
		assertEquals(3, this.action.getRegionId());
	}
	
	@Test
	public void checkToString(){
		this.action = new Action("TestType",0,0,0);
		assertNotNull(this.action.toString());
	}
}