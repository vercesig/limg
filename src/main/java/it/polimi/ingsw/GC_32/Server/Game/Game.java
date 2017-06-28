package it.polimi.ingsw.GC_32.Server.Game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;

import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;


public class Game implements Runnable{

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private ArrayList<Player> playerList;
	private Board board;
	
	private HashMap<String, Deck<DevelopmentCard>> decks;	
	private ExcommunicationCard[] excommunicationCards;
	
	private int blackDice;
	private int whiteDice;
	private int orangeDice;
		
	private UUID lock;
	
	private TurnManager turnManager;
	private MoveChecker mv;
	private ContextManager cm;
	
	// context management
	//private HashMap<ContextType , Object[]> contextQueue;
	private HashSet<String> waitingContextResponseSet;
	private HashMap<String, JsonValue> contextInfoContainer;
	private HashMap<UUID, Action> memoryAction;
	private final UUID gameUUID;
	
	private boolean runGameFlag = true;
	
	public Game(ArrayList<Player> players, UUID uuid){
		this.gameUUID = uuid;
		this.mv = new MoveChecker();
		this.cm = new ContextManager(this);
		MessageManager.getInstance().registerGame(this);
		
		this.memoryAction = new HashMap<>();
		waitingContextResponseSet = new HashSet<String>();
		contextInfoContainer = new HashMap<String, JsonValue>();
		
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
		
		LOGGER.log(Level.INFO, "decks succesfprivateully loaded");
		
		LOGGER.log(Level.INFO, "setting up players resources");

		// assegnazione casuale bonusTile
		ArrayList<PersonalBonusTile> bonusTile = GameConfig.getInstance().getBonusTileList();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int k=0; k<bonusTile.size(); k++){
			list.add(new Integer(k));
		}
		Collections.shuffle(list);
		
		for(int i=0,j=0; i<playerList.size(); i++,j++){
			playerList.get(i).registerGame(this.gameUUID);
			playerList.get(i).getResources().setResource("WOOD", 2);
			playerList.get(i).getResources().setResource("STONE", 2);
			playerList.get(i).getResources().setResource("SERVANTS", 3);
			// in base all'ordine di turno assegno le monete iniziali
			playerList.get(i).getResources().setResource("COINS", 5 + i);
			// setta punteggi a 0
			playerList.get(i).getResources().setResource("FAITH_POINTS", 0);
			playerList.get(i).getResources().setResource("VICTORY_POINTS", 0);
			playerList.get(i).getResources().setResource("MILITARY_POINTS", 0);
	
			playerList.get(i).setPersonalBonusTile(bonusTile.get(list.get(j)));
		}
		LOGGER.log(Level.INFO, "done");
	}
	
	public void run(){
		LOGGER.log(Level.INFO, "notifying connected players on game settings...");
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildGMSTRTmessage(this));

		playerList.forEach(player -> {
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(this, player));
		});
		
		// do tempo ai thread di rete di spedire i messaggi in coda
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
		LOGGER.log(Level.INFO, "done");	
		LOGGER.log(Level.INFO, "processing game setting messages..");
		LOGGER.log(Level.INFO, "done");
		
		LOGGER.log(Level.INFO, "ready to play");
		this.board.placeCards(this);
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCHGBOARDSTATmessage(this, getBoard()));
		LOGGER.log(Level.INFO, "notified players on card layout");
		
		diceRoll();
		LOGGER.log(Level.INFO, "dice rolled");
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildDICEROLLmessage(this, blackDice, whiteDice, orangeDice));
		
		// do tempo ai thread di rete di spedire i messaggi in coda
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
		
		LOGGER.log(Level.INFO, "giving lock to the first player...");
		setLock(turnManager.nextPlayer());
		LOGGER.log(Level.INFO, "player "+getLock()+" has the lock");
		
		// ask action
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildTRNBGNmessage(this, getLock()));
		GameMessage message = null;
		
		while(runGameFlag){
			try{
				message = MessageManager.getInstance().getQueueForGame(this.gameUUID).take();
			} catch(InterruptedException e){
				Thread.currentThread().interrupt();
				LOGGER.log(Level.FINEST, "InterruptedException when taking packet", e);
			}

			if(message != null){
				JsonObject Jsonmessage = message.getMessage().asObject();
				switch(message.getOpcode()){
					case "ASKACT":
						LOGGER.log(Level.INFO, "processing ASKACT message from "+message.getPlayerID());
						int index = playerList.indexOf(GameRegistry.getInstance().getPlayerFromID(message.getPlayerUUID())); 
						int pawnID = Jsonmessage.get("FAMILYMEMBER_ID").asInt();
						int actionValue = GameRegistry.getInstance().getPlayerFromID(message.getPlayerUUID())
																	.getFamilyMember()[pawnID].getActionValue();
	
						int regionID = Jsonmessage.get("REGIONID").asInt();
						int spaceID = Jsonmessage.get("SPACEID").asInt();
						String actionType = Jsonmessage.get("ACTIONTYPE").asString();
	
						Action action = new Action(actionType,actionValue,regionID,spaceID);
						action.setAdditionalInfo(new JsonObject().add("FAMILYMEMBER_ID", Jsonmessage.get("FAMILYMEMBER_ID").asInt()));
						action.getAdditionalInfo().add("COSTINDEX", Jsonmessage.get("COSTINDEX").asInt()); // Cost Index
						action.getAdditionalInfo().add("CARDNAME", Jsonmessage.get("CARDNAME").asString());
						
						Player player = playerList.get(index);
						memoryAction.put(player.getUUID(), action);
						
						System.out.println("INIZIO CHECK: ");
						System.out.println("STATO PRIMA DELL'ESECUZIONE:");
						System.out.println(action);
						System.out.println(player);
			    		if(mv.checkMove(this, player, action, cm)){
			    			System.out.println("check with copy: PASSATO");
			    			makeMove(player, action);
			    			System.out.println("AZIONE ESEGUITA!\n");
							System.out.println("STATO DOPO AZIONE: ");
			    			System.out.println(player);
			    			
			    			// notifiche server
			    			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACTCHKmessage(this, player, action, true));
			    		} else {
		    				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACTCHKmessage(this, player, action, false));
			    		}
		    			break;
					case "TRNEND":
						LOGGER.info("ricevo turn end [GAME]");
						if(!turnManager.isGameEnd()){
							LOGGER.log(Level.INFO, message.getPlayerID()+" has terminated his turn");
							if(turnManager.isRoundEnd()){
								LOGGER.log(Level.INFO, "round end");
								
								if(turnManager.isPeriodEnd()){
									LOGGER.log(Level.INFO, "period "+turnManager.getRoundID()/2+" finished");
								}
								LOGGER.log(Level.INFO, "giving lock to the next player");
								UUID nextPlayer = turnManager.nextPlayer();
								for(int i = 0; i < this.playerList.size(); i++){
									if(!GameRegistry.getInstance().getPlayerFromID(nextPlayer)
																	  .getExcomunicateFlag().contains("SKIPTURN")){
										break;
									}
									nextPlayer = turnManager.nextPlayer();
								}
								setLock(nextPlayer);
								LOGGER.log(Level.INFO, "player "+getLock()+" has the lock");
								// ask action
								MessageManager.getInstance().sendMessge(ServerMessageFactory.buildTRNBGNmessage(this, getLock()));
							}else{
								LOGGER.log(Level.INFO, "game end");
								//stopGame();
							}
							LOGGER.log(Level.INFO, "giving lock to the next player");
							setLock(turnManager.nextPlayer());
							LOGGER.log(Level.INFO, "player "+getLock()+" has the lock");
							// ask action
							MessageManager.getInstance().sendMessge(ServerMessageFactory.buildTRNBGNmessage(this, getLock()));
						} else {
							LOGGER.log(Level.INFO, "game end");
							//stopGame();
						}
						break;
				}
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
	
	public UUID getLock(){
		return this.lock;
	}
	
	public UUID getUUID(){
		return this.gameUUID;
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
	
	public void setLock(UUID player){
		this.lock = player;
	}
	
	private void diceRoll(){
		Random randomGenerator = new Random();
		this.blackDice = 1+randomGenerator.nextInt(6);
		this.orangeDice = 1+randomGenerator.nextInt(6);
		this.whiteDice = 1+randomGenerator.nextInt(6);
		playerList.forEach(player -> {
			player.getFamilyMember()[1].setActionValue(this.blackDice);
			player.getFamilyMember()[2].setActionValue(this.whiteDice);
			player.getFamilyMember()[3].setActionValue(this.orangeDice);
		});
	}
	
	private void checkExcommunication(){
		int excommunicationLevel = 3 + turnManager.getTurnID()/2 -1 ; //calcolo punti fede richiesti 
		playerList.forEach(player -> {
			if(player.getResources().getResource("VICTORY")<=excommunicationLevel){
				LOGGER.info("TIE! beccati la scomunica!");
			}
		});
	}
	
	public void moveFamiliar(Board board, Player player, Action action){
		
		MoveUtils.checkServants(board, player, action); // subtract the servants
		MoveUtils.checkCoinForTribute(board, player, action); // pays the 3 coins if the tower is busy
		
		int pawnID = action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt();
		player.moveFamilyMember(pawnID, action, board); // calls: player's moveFamilyMember and sets the position of this familyMember
															// calls: action's space addFamilyMember and sets this familymember as an occupant.
	}
	
	public void takeCard(Board board, Player player, Action action){
		 // calls: player's moveFamilyMember and sets the position of this familyMember
		MoveUtils.checkCardCost(board, player, action); // pays the cost of the card
		player.takeCard(board, action);													// calls: action's space addFamilyMember and sets this familymember as an occupant.
	}
	
	public void makeMove(Player player, Action action){
		/*if(contextInfoContainer.isEmpty()){
			
			switch(action.getActionType()){
				case "PRODUCTION":
				case "HARVEST":
					LOGGER.info("spedisco context");
					MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
							this,
							player, 
							ContextType.SERVANT, 
							player.getResources().getResource("SERVANTS"),action.getActionType()));
					//sbagliato: non e' vero che tutte le volte che faccio un'azione production apro un context CHANGE
					if(!player.getPersonalBoard().getCardsOfType("BUILDINGCARD").isEmpty()&&action.getActionType().equals("PRODUCTION")){
						LinkedList<DevelopmentCard> activatingCard = new LinkedList<DevelopmentCard>(player.getPersonalBoard().getCardsOfType("BUILDINGCARD"));
						for(DevelopmentCard card : activatingCard){
							if(card.getMinimumActionvalue()>action.getActionValue())
								activatingCard.remove(activatingCard.indexOf(card));
						}
					}
					waitingContextResponseSet.add("SERVANT");
					break;
				case "COUNCIL":
					LOGGER.info("spedisco context");
					MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
							this,
							player,
							ContextType.PRIVILEGE,
							1));
					waitingContextResponseSet.add("PRIVILEGE");
					break;
				case "MARKET":
					if(action.getActionSpaceId() == 2){
						LOGGER.info("spedisco context");
						MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
								this,
								player,
								ContextType.PRIVILEGE,
								2));
						waitingContextResponseSet.add("PRIVILEGE");
					}
					break;	
				default:
					break;
			}
			
			return;
			
		}*/
		
		
		
		System.out.println(contextInfoContainer.isEmpty());
		
		System.out.println("PRIMA DEGLI EFFETTI PERMANENTI:\n" + action);
		MoveUtils.applyEffects(this.board, player, action, cm);
		System.out.println("DOPO GLI EFFETTI PERMANENTI:\n" + action);
		System.out.println("PRIMA DEL BONUS:\n" + player);
		MoveUtils.addActionSpaceBonus(this.board, player, action);
		System.out.println("DOPO DEL BONUS:\n" + player);
		moveFamiliar(this.board, player, action);
		
		switch(action.getActionType()){
			case "PRODUCTION":
			case "HARVEST":
				player.getResources().addResource(player.getPersonalBonusTile().getPersonalProductionBonus()); 
				cm.openContext(ContextType.SERVANT, player, action, null);
				
				JsonValue SERVANTresponse = cm.waitForContextReply();
				action.setActionValue(action.getActionValue() + SERVANTresponse.asObject().get("CHOOSEN_SERVANTS").asInt());
				
				player.getPersonalBoard().getCardsOfType(action.getActionType()=="PRODUCTION" ? "BUILDINGCARD" : "TERRITORYCARD").forEach(card -> {
					if(card.getMinimumActionvalue() <= action.getActionValue())
						card.getPermanentEffect().apply(board, player, action, cm);
				});
				break;
			case "COUNCIL":
				JsonObject councilNumberOfPrivilege = new JsonObject();
				councilNumberOfPrivilege.add("NUMBER", 1);
				cm.openContext(ContextType.PRIVILEGE, player, action, councilNumberOfPrivilege);
				JsonValue COUNCILPRIVILEGEresponse = cm.waitForContextReply();
				
				System.out.println(COUNCILPRIVILEGEresponse.toString());
				
				System.out.println("PRIMA DEL PRIVILEGE:\n" + player);
				player.getResources().addResource("COINS", 1);
				player.getResources().addResource( new ResourceSet(COUNCILPRIVILEGEresponse.asObject()));
				System.out.println("DOPO DEL PRIVILEGE:\n" + player);
				break;
			case "MARKET":
				if(action.getActionSpaceId() == 3){
					JsonObject marketNumberOfPrivilege = new JsonObject();
					marketNumberOfPrivilege.add("NUMBER", 2);
					cm.openContext(ContextType.PRIVILEGE, player, action, marketNumberOfPrivilege);
					JsonValue MARKETPRIVILEGEresponse = cm.waitForContextReply();

					player.getResources().addResource( new ResourceSet(MARKETPRIVILEGEresponse.asObject()));
				}
				break;
			default:
				TowerRegion selectedTower = (TowerRegion)(board.getRegion(action.getActionRegionId()));
				DevelopmentCard card = selectedTower.getTowerLayers()[action.getActionSpaceId()].getCard();
				takeCard(this.board, player, action);
				
				if(card.getType().equals("CHARACTERCARD")){
					if(card.getPermanentEffect()!= null){
						player.addEffect(card.getPermanentEffect());
						System.out.println("AGGIUNTO EFFETTO PERMANENTE");
					}	
				}
				if(card.getInstantEffect()!= null){
					card.getInstantEffect().apply(board, player, action, cm);
				}
				break;
		}
		
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(this, player));
		
	/*	switch(action.getActionType()){
			case "PRODUCTION":{
				player.getResources().addResource(player.getPersonalBonusTile().getPersonalProductionBonus()); 
				action.setActionValue(action.getActionValue() + contextInfoContainer.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
				LinkedList<DevelopmentCard> playerCard = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
				if(contextInfoContainer.containsKey("CHANGE")){
					JsonArray cardlist = contextInfoContainer.get("CHANGE").asObject().get("ID").asArray();
					for( JsonValue json: cardlist){
						playerCard.get(json.asInt()).getPermanentEffect().apply(board, player, action, cm);
					}
				}
				for(DevelopmentCard card : playerCard){
					if(card.getMinimumActionvalue() <= action.getActionValue()){
						card.getPermanentEffect().apply(board, player, action, cm);
					}

				}
				break;
			}	
			case "HARVEST" : {
				player.getResources().addResource(player.getPersonalBonusTile().getPersonalHarvestBonus()); 
				action.setActionValue(action.getActionValue() + contextInfoContainer.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
				LinkedList<DevelopmentCard> playerCard = player.getPersonalBoard().getCardsOfType("TERRITORYCARD");
				for(DevelopmentCard card : playerCard){
					if(card.getMinimumActionvalue() <= action.getActionValue()){
						card.getPermanentEffect().apply(board, player, action, cm);
					}
				}
				break;
			}
			case "COUNCIL" : {
				System.out.println("PRIMA DEL PRIVILEGE:\n" + player);
				player.getResources().addResource("COINS", 1);
				player.getResources().addResource( new ResourceSet(contextInfoContainer.get("PRIVILEGE").asObject()));
				System.out.println("DOPO DEL PRIVILEGE:\n" + player);
				break;
			}
			case "MARKET" : {
				if(action.getActionSpaceId() == 3){
					player.getResources().addResource( new ResourceSet(contextInfoContainer.get("PRIVILEGE").asObject()));
				}
				break;
			} 
			default:{ //case  of a "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE"
				TowerRegion selectedTower = (TowerRegion)(board.getRegion(action.getActionRegionId()));
				DevelopmentCard card = selectedTower.getTowerLayers()[action.getActionSpaceId()].getCard();
				takeCard(this.board, player, action);
				
				if(card.getType().equals("CHARACTERCARD")){
					if(card.getPermanentEffect()!= null){
						player.addEffect(card.getPermanentEffect());
						System.out.println("AGGIUNTO EFFETTO PERMANENTE");
					}	
				}
				if(card.getInstantEffect()!= null){
					card.getInstantEffect().apply(board, player, action, cm);
				}
				break;
			}
		}*/
	}
}
