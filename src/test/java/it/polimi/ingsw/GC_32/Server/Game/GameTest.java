package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class GameTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	public Game game;
	public ArrayList<Player> playerList;
	public Player playerOne;
		
	@Test
	public void checkGetPlayerList() throws IOException{
		this.playerList = new ArrayList<Player>();
		this.playerOne = new Player();
		playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		assertNotNull(this.game.getPlayerList());
	}
	
	@Test
	public void checkGetBoard() throws IOException{
		this.playerList = new ArrayList<Player>();
		this.playerOne = new Player();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		assertNotNull(this.game.getBoard());
	}
			
	@Test
	public void checkGetDeck() throws IOException{
		Setup setup = new Setup();
		try{
			setup.loadCard();
		}catch(Exception e){}
		this.playerOne = new Player();
		this.playerList = new ArrayList<Player>();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		Deck<DevelopmentCard> deck = new Deck<DevelopmentCard>();
		assertNotNull(this.game.getDeck("TERRITORYCARD"));
	}
		
	@Test
	public void checkGetExcomunitcationCard() throws IOException{
		Setup setup = new Setup();
		try{
			setup.loadCard();
		}catch(Exception e){}
		this.playerOne = new Player();
		this.playerList = new ArrayList<Player>();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
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
	public void checkLock() throws IOException{
		this.playerOne = new Player();
		this.playerList = new ArrayList<Player>();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		this.game.setLock("TESTLOCK");
		assertEquals("TESTLOCK", this.game.getLock());
	}
	
	@Test
	public void checkBoardNotNull(){
		this.game = new Game(new ArrayList<Player>());
		assertNotNull(this.game.getBoard());
	}
}
