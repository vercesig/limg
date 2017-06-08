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
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;


public class Game implements Runnable{

	private ArrayList<Player> playerList;
	private Board board;
	
	private HashMap<String, Deck<DevelopmentCard>> decks;	
	private ExcommunicationCard[] excommunicationCards;
	
	private int blackDice;
	private int whiteDice;
	private int orangeDice;
		
	private String lock;
	
	private TurnManager turnManager;
	
	public Game(ArrayList<Player> players) throws IOException{
		System.out.println("[GAME] setting up game...");
		this.playerList = players;
		this.board = new Board();
		this.turnManager = new TurnManager(this);
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
		playerList = startPlayerOrder;
		System.out.println("[GAME] done");
	}
	
	public void run(){
		System.out.println("[GAME] ready to play");
		System.out.println("[GAME] giving lock to the first player...");
		setLock(playerList.get(0).getUUID());
		System.out.println("[GAME] player "+getLock()+" has the lock");
		//PlayerRegistry.getInstance().getPlayerFromID(getLock()).makeAction();
	}
	
	public ArrayList<Player> getPlayerList(){
		return this.playerList;
	}
		
	public Board getBoard(){
		return this.board;
	}
	
	public String getLock(){
		return this.lock;
	}
		
	public Deck<DevelopmentCard> getDeck(String type){
		return this.decks.get(type);
	}
	
	public ExcommunicationCard getExcommunicationCard(int period){
		return this.excommunicationCards[period-1];
	}
	
	public void setLock(String player){
		this.lock = player;
	}
	
	private void diceRoll(){
		Random randomGenerator = new Random();
		this.blackDice = 1+randomGenerator.nextInt(6);
		this.orangeDice = 1+randomGenerator.nextInt(6);
		this.whiteDice = 1+randomGenerator.nextInt(6);
		playerList.forEach(player -> {
			player.getFamilyMember()[1].setActionValue(this.blackDice);
			player.getFamilyMember()[2].setActionValue(this.orangeDice);
			player.getFamilyMember()[3].setActionValue(this.whiteDice);
		});
	}
	
	private void updateTurnOrder(){
		ArrayList<Player> oldTurnOrder = new ArrayList<Player>(playerList); //vecchio ordine di turno	
		ArrayList<FamilyMember> councilRegionState = board.getCouncilRegion().getOccupants();		
		ArrayList<Player> newTurnOrder = new ArrayList<Player>();
		
		//aggiorno stato dell'ordine di turno quardando i familiari in councilRegion
		for(FamilyMember f : councilRegionState){
			if(!newTurnOrder.contains(f.getOwner())){
				newTurnOrder.add(f.getOwner());
			}
		}
		//player che non hanno piazzato familiari nel councilregion
		for(Player p : oldTurnOrder){
			if(!newTurnOrder.contains(p)){
				newTurnOrder.add(p);
			}
		}	
		playerList = newTurnOrder;	
	}
	
	private void checkExcommunication(){
		int excommunicationLevel = 3 + turnManager.getTurnID()/2 -1 ; //calcolo punti fede richiesti 
		playerList.forEach(player -> {
			if(player.getResources().getResouce("VICTORY")<=excommunicationLevel){
				System.out.println("TIE! beccati la scomunica!");
			}
		});
	}
	
	public void moveFamiliar(Player owner, int pawnID, int regionID, int spaceID){
		FamilyMember familyMember = playerList.get(playerList.indexOf(owner)).getFamilyMember()[pawnID];
		this.board.getRegion(regionID).getActionSpace(spaceID).addFamilyMember(familyMember);
	}
}
