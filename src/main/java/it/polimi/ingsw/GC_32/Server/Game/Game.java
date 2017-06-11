package it.polimi.ingsw.GC_32.Server.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.GameMessageFilter;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;


public class Game {

	private ArrayList<Player> playerList;
	private Board board;
	
	private HashMap<String, Deck<DevelopmentCard>> decks;	
	private ExcommunicationCard[] excommunicationCards;
	
	private int blackDice;
	private int whiteDice;
	private int orangeDice;
	
	private boolean flag2PlayersGame=true; //settare a seconda del numero di giocatori
	
	private String lock;
	
	private TurnManager turnManager;
	private Setup setupgame;
	
	public Game(ArrayList<Player> players) throws IOException{
		this.playerList = players;
		this.board = new Board();
		//this.board.setTowerRegion(4);
		this.decks = new HashMap<String, Deck<DevelopmentCard>>();
		this.excommunicationCards = new ExcommunicationCard[3];	
		
		this.setupgame = new Setup(this);
		
		this.turnManager = new TurnManager(this);
/*		turnManager.gameStart();
		
		// lancio thread per elaborazione messaggi in entrata
		GameMessageFilter messageFilter = new GameMessageFilter(this);
		Thread messageFilterThread = new Thread(messageFilter);
	0	messageFilterThread.start();
	*/	
	}
	
	public TurnManager getTurnManager(){
		return this.turnManager;
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
	
	public void setLock(String player){
		this.lock = player;
	}
	
	public String getLock(){
		return this.lock;
	}
	
	public void moveFamiliar(Player owner, int pawnID, int regionID, int spaceID){
		FamilyMember familyMember = playerList.get(playerList.indexOf(owner)).getFamilyMember()[pawnID];
		this.board.getRegion(regionID).getActionSpace(spaceID).addFamilyMember(familyMember);
	}
}
