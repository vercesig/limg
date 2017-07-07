package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;
import it.polimi.ingsw.GC_32.Server.Game.Card.CardRegistry;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;


public class Game implements Runnable{

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	private ArrayList<Player> playerList;
	private Board board;
	
	private HashMap<String, Deck<DevelopmentCard>> decks;	
	private ExcommunicationCard[] excommunicationCards;
	
	private int blackDice;
	private int whiteDice;
	private int orangeDice;
		
	private UUID lock;
	
	private LeaderHandler leaderHandler;
	private TurnManager turnManager;
	private MoveChecker mv;
	private ContextManager cm;
	private MessageHandler messageHandler;
	private ActionHandler actionHandler;
    private EndPhaseHandler endPhaseHandler;
	
	// context management
	private HashMap<String, JsonValue> contextInfoContainer;

	private final UUID gameUUID;
	
	private boolean runGameFlag = true;
	
	public Game(ArrayList<Player> players, UUID uuid){
		this.gameUUID = uuid;
		this.mv = new MoveChecker();
		this.cm = new ContextManager(this);
		MessageManager.getInstance().registerGame(this);
		
		contextInfoContainer = new HashMap<String, JsonValue>();
		
		LOGGER.log(Level.INFO, "setting up game...");
		this.playerList = players;
		this.board = new Board();
		board.blockZone(playerList.size());
		this.turnManager = new TurnManager(this);
		LOGGER.log(Level.INFO, "loading cards...");
		this.decks = new HashMap<String, Deck<DevelopmentCard>>(CardRegistry.getInstance().getDevelopmentDecks());
		this.excommunicationCards = new ExcommunicationCard[3];	
		for(int i=0; i<3; i++){
			this.excommunicationCards[i] = CardRegistry.getInstance().getDeck(i+1).drawRandomElement();
		}
	    
		this.messageHandler = new MessageHandler(this);
	    this.actionHandler = new ActionHandler(this);
	    this.leaderHandler = new LeaderHandler(this);
	    this.endPhaseHandler = new EndPhaseHandler(this);
		LOGGER.log(Level.INFO, "decks succesfprivateully loaded");
		
		LOGGER.log(Level.INFO, "setting up players resources");

		// assegnazione casuale bonusTile
		ArrayList<PersonalBonusTile> bonusTile = GameConfig.getInstance().getBonusTileList();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int k=0; k<bonusTile.size(); k++){
			list.add(k);
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
	
	@Override
	public void run(){
		LOGGER.log(Level.INFO, "notifying connected players on game settings...");
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildGMSTRTmessage(this));
		
		// do tempo ai thread di rete di spedire i messaggi in coda
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		}
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
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		}
		
	////-------------------------LEADER DISTRIBUTION ----------------////////
			LOGGER.log(Level.INFO, "giving lock to the first player...");
			if(true){  // da settare se si vuole giocare con le carte Leader
				boolean flag = true;
				while(leaderHandler.getRunning()){ // da settare se si vuole giocare con le carte Leader
					if(flag){
						LOGGER.log(Level.INFO, "leader rule activated. Distribuiting card");
						Player firstPlayer = this.playerList.get(0);
						setLock(firstPlayer.getUUID());
						leaderHandler.leaderPhase(firstPlayer);
						flag = false; // blocca l'accesso al primo send
					}
					//ricezione messaggi
					GameMessage message = null;
					
					try{
						message = MessageManager.getInstance().getQueueForGame(this.gameUUID).take();
					} catch(InterruptedException e){
						Thread.currentThread().interrupt();
						LOGGER.log(Level.FINEST, "InterruptedException when taking packet", e);
					}
			
					if(message != null){
						JsonObject jsonMessage = message.getMessage().asObject();
						switch(message.getOpcode()){
						case "LDRSET":
							JsonArray json = jsonMessage.get("LIST").asArray();
							//setto la lista
							leaderHandler.setList(GameRegistry.getInstance().getPlayerFromID(message.getPlayerUUID()), json);
							int index = leaderHandler.getIndex(GameRegistry.getInstance().getPlayerFromID(message.getPlayerUUID()))+1;
							LOGGER.log(Level.INFO, "TURNO: %d\nPlayerIndice: %d", new Object[]{leaderHandler.getTurn(), (index -1)});
							
							if(index < playerList.size()){   						///STACK 1
								setLock(playerList.get(index).getUUID());
								leaderHandler.leaderPhase(playerList.get(index));
								break;
							}
							if(index == playerList.size() && leaderHandler.getTurn() < 3 ){   /// STACK 2
								leaderHandler.addTurn();
								setLock(playerList.get(0).getUUID());
								leaderHandler.leaderPhase(playerList.get(0)); // ricomincia con il primo
								break;
							}
							if(index == playerList.size() && leaderHandler.getTurn() == 3){  // sono 4 turni per 4 carte
								leaderHandler.setInactive();
								break;
							}
							break;
						default:
						    break;
						}
					}
				}
			}
			
			playerList.forEach(player -> MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(this, player)));
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
			}
			
			setLock(turnManager.nextPlayer());
			LOGGER.log(Level.INFO, "player "+getLock()+" has the lock");
			
		///----------------------FINE LEADER DISTRIBUTION--------------------------------------///////////
		
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
				messageHandler.handleMessage(message);
			}
		}
	}
	
	public void makeMove(Player player, Action action){
		LOGGER.info(() -> Boolean.toString(contextInfoContainer.isEmpty()));
		
		LOGGER.log(Level.INFO, "PRIMA DEGLI EFFETTI PERMANENTI:\n%s", action.toString());
		System.out.println("prima effetti permanenti\n"+action.toString());
		MoveUtils.applyEffects(this.board, player, action, cm);
		LOGGER.log(Level.INFO, "DOPO GLI EFFETTI PERMANENTI:\n%s", action.toString());
		System.out.println("dopo effetti permanenti\n"+action.toString());
		LOGGER.log(Level.INFO, "PRIMA DEL BONUS:\n%s", player.toString());
		System.out.println("PRIMA DEL BONUS:\n"+ player.toString());
		MoveUtils.addActionSpaceBonus(this.board, player, action);
		LOGGER.log(Level.INFO, "DOPO DEL BONUS:\n%s", player.toString());
		System.out.println("dopo DEL BONUS:\n"+ player.toString());
		if(!action.getAdditionalInfo().asObject().get("BONUSFLAG").asBoolean()) // da attivare solo per azioni non bonus
			moveFamiliar(this.board, player, action);
		
		switch(action.getActionType()){
			case "PRODUCTION":
				actionHandler.handleProduction(player, action);
				break;
			case "HARVEST":				
				actionHandler.handleHarvest(player, action);
				break;
			case "COUNCIL":
				actionHandler.handleCouncil(player, action);
				break;
			case "MARKET":
				actionHandler.handleMarket(player, action);
				break;
			default:
				actionHandler.handleTower(player, action);
				break;
		}
		
		System.out.println("dopo azione:\n"+ player.toString());
		
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
	
	public ExcommunicationCard[] getExcommunicationCard(){
		return this.excommunicationCards;
	}
	
	public void setLock(UUID player){
		this.lock = player;
	}
	
	protected void diceRoll(){
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
	
	protected ContextManager getContextManager(){
	    return this.cm;
	}
	
	protected MoveChecker getMoveChecker(){
	    return this.mv;
	}
	
	protected EndPhaseHandler getEndPhaseHandler(){
	    return this.endPhaseHandler;
	}
	
	protected void sendDICEROLL(){
	    MessageManager.getInstance().sendMessge(ServerMessageFactory.buildDICEROLLmessage(this, blackDice, whiteDice, orangeDice));
	}
}
