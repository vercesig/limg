package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;

public class GameTest {
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
	public void checkGetDecks() throws IOException{
		this.playerList = new ArrayList<Player>();
		this.playerOne = new Player();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		assertNotNull(this.game.getDecks());
	}
		
	@Test
	public void checkGetDeck() throws IOException{
		this.playerOne = new Player();
		this.playerList = new ArrayList<Player>();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		Deck<DevelopmentCard> deck = new Deck<DevelopmentCard>();
		game.setDeck("DeckTest", deck);
		assertNotNull(this.game.getDeck("DeckTest"));
	}
	
	@Test
	public void checkSetPlayerOrder() throws IOException{
		this.playerList = new ArrayList<Player>();
		this.playerOne = new Player();
		this.playerList.add(playerOne);
		
		Player playerTwo = new Player();
		playerTwo.setPlayerName("NumberTwo");
		ArrayList<Player> playerOrderedList = new ArrayList<Player>();
		PlayerRegistry.getInstance().addPlayer(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerTwo);
		
		playerOne.setPlayerName("NumberOne");
		playerOrderedList.add(playerTwo);
		
		this.game = new Game(playerList);
		game.setPlayerOrder(playerOrderedList);
		assertEquals(true, game.getPlayerList().contains(playerTwo));
	}
	
	@Test
	public void checkSetExcomunitcationCard() throws IOException{
		this.playerOne = new Player();
		this.playerList = new ArrayList<Player>();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		ExcommunicationCard card = new ExcommunicationCard("TEST", 1);
		game.setExcommunicationCard(card, card.getPeriod());
		assertEquals("TEST", game.getExcommunicationCard(1).getName());
	}
	
	@Test
	public void checkGetExcomunitcationCard() throws IOException{
		this.playerOne = new Player();
		this.playerList = new ArrayList<Player>();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		ExcommunicationCard card = new ExcommunicationCard("TEST", 1);
		game.setExcommunicationCard(card, card.getPeriod());
		assertNotNull(this.game.getExcommunicationCard(1));
	}
	
	@Test
	public void checkBlackDiceValue() throws IOException{
		this.playerOne = new Player();
		this.playerList = new ArrayList<Player>();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		this.game.setBlackDiceValue(1);
		assertEquals(1, this.game.getBlackDiceValue());
	}
	
	@Test
	public void checkOrangeDiceValue() throws IOException{
		this.playerOne = new Player();
		this.playerList = new ArrayList<Player>();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		this.game.setOrangeDiceValue(1);
		assertEquals(1, this.game.getOrangeDiceValue());
	}
	
	@Test
	public void checkWhiteDiceValue() throws IOException{
		this.playerOne = new Player();
		this.playerList = new ArrayList<Player>();
		this.playerList.add(playerOne);
		PlayerRegistry.getInstance().addPlayer(playerOne);
		this.game = new Game(playerList);
		this.game.setWhiteDiceValue(1);
		assertEquals(1, this.game.getWhiteDiceValue());
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
}
