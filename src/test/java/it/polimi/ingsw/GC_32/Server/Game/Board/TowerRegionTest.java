package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;

import org.junit.Test;

public class TowerRegionTest{
	public TowerRegion towerRegion;
	
	@Test
	public void checkCardType(){
		this.towerRegion = new TowerRegion(0, 4);
		this.towerRegion.setTypeCard("Production");
		assertEquals(this.towerRegion.getTypeCard(), "Production");
		
	}
	
	@Test
	public void checkLayers(){
		this.towerRegion = new TowerRegion(0, 4);
		assertEquals(this.towerRegion.getTowerLayers().length, 4);
		
	}
	
	@Test
	public void checkToStringNotNull(){
		this.towerRegion = new TowerRegion(0, 4);
		assertNotNull(this.towerRegion.toString());
	}
}