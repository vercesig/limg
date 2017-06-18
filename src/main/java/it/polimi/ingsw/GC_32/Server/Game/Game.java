package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Network.ServerMessageFactory;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;


public class Game implements Runnable{

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private ArrayList<Player> playerList;
	private Board board;
	
	private HashMap<String, Deck<DevelopmentCard>> decks;	
	private ExcommunicationCard[] excommunicationCards;
	
	private int blackDice;
	private int whiteDice;
	private int orangeDice;
		
	private String lock;
	
	private TurnManager turnManager;
	private boolean runGameFlag = true;
	
	public Game(ArrayList<Player> players){
		LOGGER.log(Level.INFO, "setting up game...");
		this.playerList = players;
		this.board = new Board();
		this.turnManager = new TurnManager(this);
		LOGGER.log(Level.INFO, "loading cards...");
		this.decks = new HashMap<String, Deck<DevelopmentCard>>(CardRegistry.getInstance().getDevelopmentDecks());
		this.excommunicationCards = new ExcommunicationCard[3];	
		for(int i=0; i<3; i++){
			this.excommunicationCards[i] = CardRegistry.getInstance().getDeck(i+1).drawRandomElement();
		}
		LOGGER.log(Level.INFO, "decks succesfully loaded");
		
		LOGGER.log(Level.INFO, "setting first turn order");
		Random randomGenerator = new Random();
		ArrayList<Player> startPlayerOrder = new ArrayList<Player>();
		int playerListSize = this.playerList.size();
		
		for(int i=0; i<playerListSize; i++){
			int randomNumber = randomGenerator.nextInt(playerList.size());
			startPlayerOrder.add(playerList.get(randomNumber));
			playerList.remove(randomNumber);
		}
		playerList = startPlayerOrder;
		
		LOGGER.log(Level.INFO, "setting up players resources");
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
		LOGGER.log(Level.INFO, "done");
	}
	
	public void run(){
		LOGGER.log(Level.INFO, "notifying connected players on game settings...");
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildGMSTRTmessage(this));

		playerList.forEach(player -> {
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(player));
		});
		
		// do tempo ai thread di rete di spedire i messaggi in coda
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
		LOGGER.log(Level.INFO, "done");	
		
		// svuoto la recivedQueue dai messaggi di game setting
		LOGGER.log(Level.INFO, "processing game setting messages..");
		MessageManager.getInstance().getRecivedQueue().forEach(message -> {
			JsonObject JsonMessage = Json.parse(message.getMessage()).asObject();
			switch(message.getOpcode()){
			case "CHGNAME":
				int playerIndex = playerList.indexOf(PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID()));
				this.playerList.get(playerIndex).setPlayerName(JsonMessage.get("NAME").asString());
				LOGGER.log(Level.INFO, "player "+message.getPlayerID()+" setted his name to "+JsonMessage.get("NAME").asString());

				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildNAMECHGmessage(message.getPlayerID(), JsonMessage.get("NAME").asString()));
				break;
			}
			MessageManager.getInstance().getRecivedQueue().clear();
			
			// do tempo ai thread di rete di spedire i messaggi in coda
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {}
		});
		LOGGER.log(Level.INFO, "done");
		
		LOGGER.log(Level.INFO, "ready to play");
		this.board.placeCards(this);
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCHGBOARDSTATmessage(getBoard()));
		LOGGER.log(Level.INFO, "notified players on card layout");
		
		diceRoll();
		LOGGER.log(Level.INFO, "dice rolled");
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildDICEROLLmessage(blackDice, whiteDice, orangeDice));
		
		// do tempo ai thread di rete di spedire i messaggi in coda
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
		
		LOGGER.log(Level.INFO, "giving lock to the first player...");
		setLock(playerList.get(0).getUUID());
		LOGGER.log(Level.INFO, "player "+getLock()+" has the lock");
		
		// ask action
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildTRNBGNmessage(getLock()));
		
		while(runGameFlag){
			if(MessageManager.getInstance().hasMessage()){
				MessageManager.getInstance().getRecivedQueue().forEach(message -> {
					JsonObject Jsonmessage = Json.parse(message.getMessage()).asObject();
					switch(message.getOpcode()){
					case "CHGNAME":
						int playerIndex = playerList.indexOf(PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID()));
						this.playerList.get(playerIndex).setPlayerName(Jsonmessage.get("NAME").asString());
						LOGGER.log(Level.INFO, "player "+message.getPlayerID()+" changed name to "+Jsonmessage.get("NAME").asString());
						break;
					case "ASKACT":
						LOGGER.log(Level.INFO, "processing ASKACT message from "+message.getPlayerID());
						int pawnID = Jsonmessage.get("PAWNID").asInt();
						int actionValue = PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID()).getFamilyMember()[pawnID].getActionValue();
						
						int regionID = Jsonmessage.get("REGIONID").asInt();
						int spaceID = Jsonmessage.get("SPACEID").asInt();
						String actionType = Jsonmessage.get("ACTIONTYPE").asString();
						
						Action action = new Action(actionType,actionValue,regionID,spaceID);
						
						// MoveChecker
						break;
					case "TRNEND":
						if(!turnManager.isGameEnd()){
							LOGGER.log(Level.INFO, message.getPlayerID()+" has terminated his turn");
							
							if(turnManager.isRoundEnd()){
								LOGGER.log(Level.INFO, "round end");
								
								if(turnManager.isPeriodEnd()){
									LOGGER.log(Level.INFO, "period "+turnManager.getRoundID()/2+" finished");
								}
							}
							LOGGER.log(Level.INFO, "giving lock to the next player");
							setLock(turnManager.nextPlayer().getUUID());
							LOGGER.log(Level.INFO, "player "+getLock()+" has the lock");
							// ask action
							MessageManager.getInstance().sendMessge(ServerMessageFactory.buildTRNBGNmessage(getLock()));
						}else{
							LOGGER.log(Level.INFO, "game end");
							//stopGame();
						}
						break;
					}
				});
				MessageManager.getInstance().getRecivedQueue().clear();
			}
		}
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
	
	public String getLock(){
		return this.lock;
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
	
	private void checkExcommunication(){
		int excommunicationLevel = 3 + turnManager.getTurnID()/2 -1 ; //calcolo punti fede richiesti 
		playerList.forEach(player -> {
			if(player.getResources().getResource("VICTORY")<=excommunicationLevel){
				System.out.println("TIE! beccati la scomunica!");
			}
		});
	}
	
	public void moveFamiliar(Player owner, int pawnID, int regionID, int spaceID){
		FamilyMember familyMember = playerList.get(playerList.indexOf(owner)).getFamilyMember()[pawnID];
		this.board.getRegion(regionID).getActionSpace(spaceID).addFamilyMember(familyMember);
	}
}
