package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class BoardTest{
	
	private Board board;
	
	//TODO: fix this
	@Ignore
	@Test
	public void checkTowerRegionSet(){
		Setup setup = new Setup();
		try{
			setup.loadCard();
		}
		catch(Exception e){}
		this.board = new Board();
		assertEquals(4, this.board.getTowerRegion().length);
	}
	
	@Test
	public void checkGetRegion(){
		this.board = new Board();
		assertTrue(this.board.getRegion(0) instanceof ProductionRegion);
		assertTrue(this.board.getRegion(1) instanceof HarvestRegion);
		assertTrue(this.board.getRegion(2) instanceof CouncilRegion);
		assertTrue(this.board.getRegion(3) instanceof MarketRegion);
		assertNull(this.board.getRegion(4));
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