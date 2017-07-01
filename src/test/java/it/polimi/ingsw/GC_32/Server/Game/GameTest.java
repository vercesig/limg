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

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class GameTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	public Game game;
	public ArrayList<Player> playerList;
	public Player playerOne;
	
	@Before
	public void initTest(){
		Setup setup = new Setup();
		try{
			setup.loadCard("test.json");
		}
		catch(IOException e){}
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
		Deck<DevelopmentCard> deck = new Deck<DevelopmentCard>();
		assertNotNull(this.game.getDeck("TERRITORYCARD"));
	}
		
	@Test
	public void checkGetExcomunitcationCard(){
		ExcommunicationCard card = new ExcommunicationCard("TEST", 1);
		assertNotNull(this.game.getExcommunicationCard(1));
	}
	
	@Test
	public void checkDiceRoll(){
		Player player = mock(Player.class);
		FamilyMember[] fmArray = new FamilyMember[4];
		fmArray[1] = new FamilyMember(player);
		fmArray[2] = new FamilyMember(player);
		fmArray[3] = new FamilyMember(player);
		when(player.getFamilyMember()).thenReturn(fmArray);
		ArrayList<Player> playerList = new ArrayList<>();
		playerList.add(player);
		//Game game = new Game(playerList);
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
}
