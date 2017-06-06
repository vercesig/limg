package it.polimi.ingsw.GC_32.Server.Game.Board;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.GC_32.Server.Game.Board.ActionSpace;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.Region;

public class RegionTest {

	public Board board;
	
	// test che verifica che toString throws NullPointerException
	@Test (expected = NullPointerException.class)
	public void testToStringNullPointerException(){
		board = new Board();
		board.setTowerRegion(4);
		
		for(Region region : board.getRegionMap()){
			for (int i = 0; i < 100; i++){	
				ActionSpace a = region.getActionSpace(i);
				System.out.println(a.toString()); // to Sring throws NullPointerException
			}
		}
	}
	
	// test che verifica che getActionSpace() gestisce la NullPointerException;
	@Test
	public void testGetActionSpaceNullPointerException(){
		board = new Board();
		board.setTowerRegion(4);
		ArrayList<ActionSpace> a = new ArrayList<ActionSpace>();
		
		for(Region region : board.getRegionMap()){
			for (int i = 0; i < 5; i++){
				a.add(region.getActionSpace(i));
			}
		}
		System.out.println(a.toString()); //su console non riesce a printarlo tutto
		System.out.println(a.size()); // 8 * 5 = 40
	}
	
	// test che verifica il corretto numero di regioni del gioco: 8
	@Test
	public void testCorrectNumberOfRegion(){
		board = new Board();
		board.setTowerRegion(4);
		assertEquals(8, board.getRegionMap().size());
	}
	
	@Test
	public void checkToStringNotNull(){
		Region region = new Region(0,4);
		assertNotNull(region.toString());
	}
}
