package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.ServerMessageFactory;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;

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
	private MoveChecker mv;
	
	// context management
	//private HashMap<ContextType , Object[]> contextQueue;
	private HashSet<String> waitingContextResponseSet;
	private HashMap<String, JsonValue> contextInfoContainer;
	private HashMap<String, Action> memoryAction;
	
	private boolean runGameFlag = true;
	
	public Game(ArrayList<Player> players){
		
		this.mv = new MoveChecker();
		//this.contextQueue = new HashMap<ContextType, Object[]>();
		this.memoryAction = new HashMap<String, Action>();
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
		setLock(turnManager.nextPlayer());
		LOGGER.log(Level.INFO, "player "+getLock()+" has the lock");
		
		// ask action
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildTRNBGNmessage(getLock()));
		
		//MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTMessage(getLock(), null));
		
		while(runGameFlag){
			
			// controllo se ci sono context da aprire, in caso positivo li apro
			/*if(!this.contextQueue.isEmpty()){
				for(Entry<ContextType, Object[]> context : contextQueue.entrySet()){
					waitingContextResponseSet.add(context.getKey().toString());
					MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(getLock(), context.getKey(), context.getValue()));
				}
				contextQueue.clear();
			}*/
			
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
							int index = playerList.indexOf(PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID())); 
							int pawnID = Jsonmessage.get("FAMILYMEMBER_ID").asInt();
							int actionValue = PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID()).getFamilyMember()[pawnID].getActionValue();

							int regionID = Jsonmessage.get("REGIONID").asInt();
							int spaceID = Jsonmessage.get("SPACEID").asInt();
							String actionType = Jsonmessage.get("ACTIONTYPE").asString();

							Action action = new Action(actionType,actionValue,regionID,spaceID);
							action.setAdditionalInfo(new JsonObject().add("FAMILYMEMBER_ID", Jsonmessage.get("FAMILYMEMBER_ID").asInt()));
							action.getAdditionalInfo().add("COSTINDEX", Jsonmessage.get("COSTINDEX").asInt()); // Cost Index

							Player player = playerList.get(index);
							memoryAction.put(player.getUUID(), action);
							
							System.out.println("INIZIO CHECK: ");
							System.out.println("STATO PRIMA DELL'ESECUZIONE:");
							System.out.println(action);
							System.out.println(player);
				    		if(mv.checkMove(this, player, action)){
				    			System.out.println("check with copy: PASSATO");
				    			makeMove(player, action);
				    			System.out.println("AZIONE ESEGUITA!\n");
								System.out.println("STATO DOPO AZIONE: ");
				    			System.out.println(player);
				    			
				    			// notifiche server
				    			playerList.forEach(p -> {
				    				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(p));
				    			});
				    			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCHGBOARDSTATmessage(getBoard()));
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
								}
								LOGGER.log(Level.INFO, "giving lock to the next player");
								setLock(turnManager.nextPlayer());
								LOGGER.log(Level.INFO, "player "+getLock()+" has the lock");
								// ask action
								MessageManager.getInstance().sendMessge(ServerMessageFactory.buildTRNBGNmessage(getLock()));
							}else{
								LOGGER.log(Level.INFO, "game end");
								//stopGame();
							}
							break;
						case "CONTEXTREPLY" :{
							JsonValue contextReply = Json.parse(message.getMessage());
							
							contextInfoContainer.put(contextReply.asObject().get("CONTEXT_TYPE").asString(), contextReply.asObject().get("PAYLOAD"));
							waitingContextResponseSet.remove(contextReply.asObject().get("CONTEXT_TYPE").asString());						
							
							if(waitingContextResponseSet.isEmpty()){
								
								Player playerReply = PlayerRegistry.getInstance().getPlayerFromID(getLock());
								Action actionReply = memoryAction.get(getLock());
								
								System.out.println("CONTEXT: retry simulateWithCopy");
								System.out.println("STATO PRIMA DELL'ESECUZIONE:");
								System.out.println(playerReply);
								
								if(mv.checkMove(this, playerReply, actionReply)){
					    			makeMove(playerReply, actionReply);
									System.out.println("AZIONE ESEGUITA!\n");
									System.out.println("STATO DOPO AZIONE: ");
									System.out.println(playerReply);
									
									
									playerList.forEach(p -> {
					    				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(p));
					    			});
					    			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCHGBOARDSTATmessage(getBoard()));	
					    		}
							}
							memoryAction.remove(getLock());
							
							/*MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACKCONTEXTMessage(message.getPlayerID()));*/
						}
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
		if(contextInfoContainer.isEmpty()){
			
			switch(action.getActionType()){
				case "PRODUCTION":
				case "HARVEST":
					LOGGER.info("spedisco context");
					MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
							player.getUUID(), 
							ContextType.SERVANT, 
							player.getResources().getResource("SERVANTS"),action.getActionType()));
					//sbagliato: non e' vero che tutte le volte che faccio un'azione production apro un context CHANGE
					if(!player.getPersonalBoard().getCardsOfType("BUILDINGCARD").isEmpty()&&action.getActionType().equals("PRODUCTION")){
						LinkedList<DevelopmentCard> activatingCard = new LinkedList<DevelopmentCard>(player.getPersonalBoard().getCardsOfType("BUILDINGCARD"));
						for(DevelopmentCard card : activatingCard){
							if(card.getMinimumActionvalue()>action.getActionValue())
								activatingCard.remove(activatingCard.indexOf(card));
						}
				//		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
				//				player.getUUID(),
				//				ContextType.CHANGE,
				//				activatingCard
				//				));
				//		waitingContextResponseSet.add("CHANGE");
					}
					waitingContextResponseSet.add("SERVANT");
					break;
				case "COUNCIL":
					LOGGER.info("spedisco context");
					MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
							player.getUUID(),
							ContextType.PRIVILEGE,
							1));
					waitingContextResponseSet.add("PRIVILEGE");
					break;
				case "MARKET":
					if(action.getActionSpaceId() == 2){
						LOGGER.info("spedisco context");
						MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
								player.getUUID(),
								ContextType.PRIVILEGE,
								2));
						waitingContextResponseSet.add("PRIVILEGE");
					}
					break;	
				default:
					break;
			}
			
			return;
			
		}
		
		System.out.println(contextInfoContainer.isEmpty());
		
		System.out.println("PRIMA DEGLI EFFETTI PERMANENTI:\n" + action);
		MoveUtils.applyEffects(this.board, player, action);
		System.out.println("DOPO GLI EFFETTI PERMANENTI:\n" + action);
		System.out.println("PRIMA DEL BONUS:\n" + player);
		MoveUtils.addActionSpaceBonus(this.board, player, action);
		System.out.println("DOPO DEL BONUS:\n" + player);
		moveFamiliar(this.board, player, action);
		switch(action.getActionType()){
			case "PRODUCTION":{
				player.getResources().addResource(player.getPersonalBonusTile().getPersonalProductionBonus()); 
				action.setActionValue(action.getActionValue() + contextInfoContainer.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
				LinkedList<DevelopmentCard> playerCard = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
				if(contextInfoContainer.containsKey("CHANGE")){
					JsonArray cardlist = contextInfoContainer.get("CHANGE").asObject().get("ID").asArray();
					for( JsonValue json: cardlist){
						playerCard.get(json.asInt()).getPermanentEffect().apply(board, player, action);
					}
				}
				for(DevelopmentCard card : playerCard){
					if(card.getMinimumActionvalue() <= action.getActionValue()){
						card.getPermanentEffect().apply(board, player, action);
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
						card.getPermanentEffect().apply(board, player, action);
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
					card.getInstantEffect().apply(board, player, action);
				}
				break;
			}
		}
	}
}
