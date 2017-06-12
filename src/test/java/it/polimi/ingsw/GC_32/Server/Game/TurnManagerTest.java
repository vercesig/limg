package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TurnManagerTest{
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 
	
	public TurnManager turnManager;
	
	@Mock
	public Game game;
	
	@Test
	public void checkTurnId(){
		this.turnManager = new TurnManager(this.game);
	}
}