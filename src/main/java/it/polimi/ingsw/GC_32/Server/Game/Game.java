package it.polimi.ingsw.GC_32.Server.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.GameMessageFilter;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;


public class Game implements Runnable{

	private ArrayList<Player> playerList;
	private Board board;
	
	private HashMap<String, Deck<DevelopmentCard>> decks;	
	private ExcommunicationCard[] excommunicationCards;
	
	private int blackDice;
	private int whiteDice;
	private int orangeDice;
	
	private boolean flag2PlayersGame=true; //settare a seconda del numero di giocatori
	
	private String lock;
	
	public Game(ArrayList<Player> players) throws IOException{
		System.out.println("[GAME] setting up game...");
		this.playerList = players;
		this.board = new Board();
		System.out.println("[GAME] loading cards...");
		this.decks = new HashMap<String, Deck<DevelopmentCard>>(CardRegistry.getInstance().getDevelopmentDecks());
		this.excommunicationCards = new ExcommunicationCard[3];	
		for(int i=0; i<3; i++){
			this.excommunicationCards[i] = CardRegistry.getInstance().getDeck(i+1).drawRandomElement();
		}
		System.out.println("[GAME] decks succesfully loaded");
		
		System.out.println("[GAME] setting up players' resources");
		//TODO: associare PersonalBonusTile al giocatore
		for(int i=0; i<playerList.size(); i++){
			playerList.get(i).getResources().setResource("WOOD", 2);
			playerList.get(i).getResources().setResource("STONE", 2);
			playerList.get(i).getResources().setResource("SERVANTS", 3);
			// in base all'ordine di turno assegno le monete iniziali
			playerList.get(i).getResources().setResource("COINS", 5 + i);
			// setta punteggi a 0
			playerList.get(i).getResources().setResource("FAITH", 0);
			playerList.get(i).getResources().setResource("VICTORY", 0);
			playerList.get(i).getResources().setResource("MILITARY", 0);
			
		}
		System.out.println("[GAME] setting first turn order");
		Random randomGenerator = new Random();
		ArrayList<Player> startPlayerOrder = new ArrayList<Player>();
		int playerListSize = this.playerList.size();
		
		for(int i=0; i<playerListSize; i++){
			int randomNumber = randomGenerator.nextInt(playerList.size());
			startPlayerOrder.add(playerList.get(randomNumber));
			playerList.remove(randomNumber);
		}
		this.setPlayerOrder(startPlayerOrder);
		System.out.println("[GAME] done");
		
		// lancio thread per elaborazione messaggi in entrata
		/*GameMessageFilter messageFilter = new GameMessageFilter(this);
		Thread messageFilterThread = new Thread(messageFilter);
		messageFilterThread.start();
		*/
	}
	
	public void run(){
		System.out.println("[GAME] ready to play");
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
