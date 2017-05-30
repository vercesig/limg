package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;


public class Game {

	private ArrayList<Player> playerList;
	private Board board;
	
	private HashMap<String, Deck<DevelopmentCard>> decks;	
	private ExcommunicationCard[] excommunicationCards;
	
	private int blackDice;
	private int whiteDice;
	private int orangeDice;
	
	private boolean flag2PlayersGame=true;
	
	public Game(ArrayList<Player> players){
		this.playerList = players;
		this.board = new Board();
		this.decks = new HashMap<String, Deck<DevelopmentCard>>();
		this.excommunicationCards = new ExcommunicationCard[3];
		
		if(this.playerList.size()>2){
			this.flag2PlayersGame=false;
		}		
	}
	
	public Boolean getFlag2PlayersGame(){
		return this.flag2PlayersGame;
	}
	
	public ArrayList<Player> getPlayerList(){
		return this.playerList;
	}
	
	public void setPlayerOrder(ArrayList<Player> playerList){
		this.playerList = playerList;
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public HashMap<String, Deck<DevelopmentCard>> getDecks(){
		return this.decks;
	}
	
	public Deck<DevelopmentCard> getDeck(String type){
		return this.decks.get(type);
	}
	
	public void setDeck(String type, Deck<DevelopmentCard> deck){
		this.decks.put(type, deck);
	}
	
	public void setExcommunicationCard(ExcommunicationCard card, int period){
		this.excommunicationCards[period-1] = card;
	}
	
	public ExcommunicationCard getExcommunicationCard(int period){
		return this.excommunicationCards[period-1];
	}
	
	public int getBlackDiceValue(){
		return this.blackDice;
	}
	
	public int getWhiteDiceValue(){
		return this.whiteDice;
	}
	
	public int getOrangeDiceValue(){
		return this.orangeDice;
	}
	
	public void setBlackDiceValue(int value){
		this.blackDice = value;
	}
	
	public void setWhiteDiceValue(int value){
		this.whiteDice = value;
	}
	
	public void setOrangeDiceValue(int value){
		this.orangeDice = value;
	}
	
}
