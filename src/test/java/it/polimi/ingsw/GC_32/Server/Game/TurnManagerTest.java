package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TurnManagerTest{
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 
	
	public TurnManager turnManager;
	public Player player1;
	public Player player2;
	
	@Before
	public void initTest(){
		Game game = mock(Game.class);
		this.player1 = new Player();
		this.player2 = new Player();
		ArrayList<Player> playerList = new ArrayList<>();
		playerList.add(player1);
		playerList.add(player2);
		when(game.getPlayerList()).thenReturn(playerList);
		this.turnManager = new TurnManager(game);
	}
	
	@Test
	public void checkConstructor(){
		assertEquals(1, turnManager.getTurnID());
		assertEquals(0, turnManager.getRoundID());
		assertEquals(0, turnManager.getPeriod());
	}
}
