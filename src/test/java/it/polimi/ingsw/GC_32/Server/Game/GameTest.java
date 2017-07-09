package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class GameTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	public Game game;
	public ArrayList<Player> playerList;
	public Player playerOne;
	
	@Before
	public void initTest() throws IOException{
		Setup setup = new Setup();
		setup.loadCard("test.json");
		this.playerList = new ArrayList<Player>();
		this.playerOne = new Player();
		playerList.add(playerOne);
		GameRegistry.getInstance().registerPlayer(playerOne, ConnectionType.FAKE);
		this.game = new Game(playerList, UUID.randomUUID());
	}
	
	@Test
	public void checkGetPlayerList(){
		assertNotNull(this.game.getPlayerList());
	}
	
	@Test
	public void checkGetBoard(){
		assertNotNull(this.game.getBoard());
	}
	
	@Test
	public void checkGetDeck(){
		assertNotNull(this.game.getDeck("TERRITORYCARD"));
	}
		
	@Test
	public void checkGetExcomunitcationCard(){
		assertNotNull(this.game.getExcommunicationCard(1));
	}
	
	/**
     * checks if the diceroll properly affects the player
     */
	@Test
	public void checkDiceRoll(){
		Player player = mock(Player.class);
		FamilyMember[] fmArray = new FamilyMember[4];
		fmArray[1] = spy(new FamilyMember(player));
		fmArray[2] = spy(new FamilyMember(player));
		fmArray[3] = spy(new FamilyMember(player));
		when(player.getFamilyMember()).thenReturn(fmArray);
		when(player.getResources()).thenReturn(new ResourceSet());
		ArrayList<Player> playerList = new ArrayList<>();
		playerList.add(player);
		Game game = new Game(playerList, UUID.randomUUID());
		game.diceRoll();
		// 1 time for stubbing + 4 times when initting TurnManager
		// + the 3 times we are checking for
		verify(player, times(8)).getFamilyMember();
		verify(fmArray[1]).setActionValue(anyInt());
		verify(fmArray[2]).setActionValue(anyInt());
		verify(fmArray[3]).setActionValue(anyInt());
	}
	
	@Test
	public void checkLock(){
		this.game.setLock(this.playerOne.getUUID());
		assertEquals(this.playerOne.getUUID(), this.game.getLock());
	}
	
	@Test
	public void checkBoardNotNull(){
		assertNotNull(this.game.getBoard());
	}
	
	/**
	 * checks if the proper handle is called regarding the action type
	 */
	@Test
	public void checkMakeMove(){
	    ActionHandler mockActionHandler = mock(ActionHandler.class);
	    this.game.setActionHandler(mockActionHandler);
	    Action action = new Action("PRODUCTION", 3, 0, 0);
	    game.makeMove(playerOne, action);
	    verify(mockActionHandler).handleProduction(playerOne, action);
	    action = new Action("HARVEST", 3, 0, 1);
        game.makeMove(playerOne, action);
        verify(mockActionHandler).handleHarvest(playerOne, action);
        action = new Action("COUNCIL", 3, 0, 2);
        game.makeMove(playerOne, action);
        verify(mockActionHandler).handleCouncil(playerOne, action);
        action = new Action("MARKET", 3, 0, 3);
        game.makeMove(playerOne, action);
        verify(mockActionHandler).handleMarket(playerOne, action);
        action = new Action("TOWER", 3, 0, 4);
        game.makeMove(playerOne, action);
        verify(mockActionHandler).handleTower(playerOne, action);
	}
}
