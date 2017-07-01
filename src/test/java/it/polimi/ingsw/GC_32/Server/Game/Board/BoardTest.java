package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class BoardTest{
	
	private Board board;
	
	@Before
	public void initTest() throws IOException{
		Setup setup = new Setup();
		setup.loadCard("test.json");
		setup.loadBonusSpace("bonus_space.json");
		setup.loadBonusTile("bonus_tile.json");
		this.board = new Board();
	}
	
	@Test
	public void checkTowerRegionSet(){
		assertEquals(4, this.board.getTowerRegion().length);
	}
	
	@Test
	public void checkGetRegion(){
		assertEquals(8, this.board.getRegionMap().size());
		assertTrue(this.board.getRegion(0) instanceof ProductionRegion);
		assertTrue(this.board.getRegion(1) instanceof HarvestRegion);
		assertTrue(this.board.getRegion(2) instanceof CouncilRegion);
		assertTrue(this.board.getRegion(3) instanceof MarketRegion);
		assertTrue(this.board.getRegion(4) instanceof TowerRegion);
		assertTrue(this.board.getRegion(5) instanceof TowerRegion);
		assertTrue(this.board.getRegion(6) instanceof TowerRegion);
		assertTrue(this.board.getRegion(7) instanceof TowerRegion);
		assertNull(this.board.getRegion(8));
	}
	
	@Test
	public void checkGetRegionMap(){
		this.board = new Board();
		assertNotNull(this.board.getRegionMap());
		for(int i = 0; i < 4; i++){
			assertEquals(this.board.getRegion(i), this.board.getRegionMap().get(i));
		}
	}
	
	@Test
	public void checkRagionIndividualGetters(){
		this.board = new Board();
		assertEquals(this.board.getRegion(0), this.board.getProductionRegion());
		assertEquals(this.board.getRegion(1), this.board.getHarvestRegion());
		assertEquals(this.board.getRegion(2), this.board.getCouncilRegion());
		assertEquals(this.board.getRegion(3), this.board.getMarketRegion());
	}
	
	@Test
	public void checkFlushBoard(){
		this.board = new Board();
		this.board.flushBoard();
	}
	
	@Test
	public void checkStringNotNull(){
		this.board = new Board();
		assertNotNull(this.board.toString());
	}
}
