package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Utils.Utils;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;
import it.polimi.ingsw.GC_32.Server.Game.Card.CardRegistry;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

/**
 * class Game is the core class of all the game. It handles all the game mechanics and process the message received from the network thread. When a Game is started by
 * the GameLobby class the run() method of this class is launched. In this way the game thread can handle all the messages coming from the client who belong to this 
 * particular instance of the Game object. Game is also responsable of all the changes to the state of the model that represent the game in progress
 * following the request of action by the clients which has been assessed valid by the check logic (managed by Game as well). 
 * Game is also responsable of sending the majority of messages to the clients connected to a particular instance of Game
 *
 * <ul>
 * <li>{@link @#playerList}: the list of players connected to this game</li>
 * <li>{@link @#board}: the board of this game</li>
 * <li>{@link @#decks}: the decks of DevelopmentCard, shuffled and loaded according to game rules, of this game</li>
 * <li>{@link @#excommunicationCards}: the excommunication tiles of this game (randomly choosed)</li>
 * <li>{@link @#blackDice}: the value of the black dice</li>
 * <li>{@link @#whiteDice}: the value of the white dice</li>
 * <li>{@link @#orangeDice}: the value of the orange dice</li>
 * <li>{@link @#lock}: the UUID of the player which has the lock, i.e. the player who must perform his action. only ASKACT message of the player who has the lock
 * are processed, the others are automatically dropped</li>
 * <li>{@link @#leaderHandler}: handle all the mechanics related to LeaderCard</li>
 * <li>{@link @#turnManager}: responsable of handling turns, round and period. Called by Game, give the UUID of the next player to who the lock must be assigned</li>
 * <li>{@link @#moveChecker}: implements all the logic of the Game. when an action has been received, moveChecker analize the validity of that action simulating it 
 * on a copy of the actual status of the game. If moveChecker returns true the action is valid and Game effectively apply the action to the real state of the game</li>
 * <li>{@link @#contextManager}: handle the opening and the reception of CONTEXT messages</li>
 * <li>{@link @#messageHandler}: after a message has been received, messageHandler process that message</li>
 * <li>{@link @#actionHandler}: responsable of apply the action, after an ASKACT message has been received</li>
 * <li>{@link @#endPhaseHandler}: the class which manage the final computation of the score</li>
 * <li>{@link @#gameUUID}: the UUID of this instance of Game. Every Game is labeled by its UUID, so GameMessages are filtered in order to ensure that only the messages
 * destinated to the correct game instance are processes by the game which is the effectively target of that message</li>
 * <li>{@link @#runGameFlag}: setted ot false when the game end, stop the Game thread</li>
 * </ul>
 * 
 * @see Player, Board, DevelopmentCard, ExcommunicationCard, LeaderHandler, TurnManager, MoveChecker, ContextManager, MessageHandler, ActionHandler, EndPhaseHandler,
 * MessageManager, CardRegistry, GameConfig, ServerMessageFactory, GameMessage, Action, GameRegistry
 *
 */

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
	private MoveChecker moveChecker;
	private ContextManager contextManager;
	private MessageHandler messageHandler;
	private ActionHandler actionHandler;
    private EndPhaseHandler endPhaseHandler;

	private final UUID gameUUID;
	
	private boolean runGameFlag = true;
	
	/**
	 * inizialize the game with the given player list and the given uuid
	 * @param players the players who belong to this game
	 * @param uuid the UUID of the game
	 */
	public Game(List<Player> players, UUID uuid){
		this.gameUUID = uuid;
		this.moveChecker = new MoveChecker();
		this.contextManager = new ContextManager(this);
		MessageManager.getInstance().registerGame(this);
		
		LOGGER.log(Level.INFO, "setting up game...");
		this.playerList = new ArrayList<Player>();
		this.playerList.addAll(players);
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

		//Bonus tile distribution
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
			playerList.get(i).getResources().setResource("COINS", 5 + i);

			playerList.get(i).getResources().setResource("FAITH_POINTS", 0);
			playerList.get(i).getResources().setResource("VICTORY_POINTS", 0);
			playerList.get(i).getResources().setResource("MILITARY_POINTS", 0);
			playerList.get(i).setPersonalBonusTile(bonusTile.get(list.get(j)));
		}
		LOGGER.log(Level.INFO, "done");
	}
	
	/**
	 * after comunicate the game configuration, player status ,board configuration and the dices value for the first round, run handle the phase of LeaderCard
	 * distribution. finally the lock to the first player is given and the game can effectively start. run execute a while loop until the game isn't end, asking to
	 * MessageManager if there are some messages for this game. If yes, the message is processed by the messageHandler.
	 */
	@Override
	public void run(){
		LOGGER.log(Level.INFO, "notifying connected players on game settings...");
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildGMSTRTmessage(this));
		
		// sleep for threads
		Utils.safeSleep(200);
		LOGGER.log(Level.INFO, "done");
		
		LOGGER.log(Level.INFO, "ready to play");
		this.board.placeCards(this);
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCHGBOARDSTATmessage(this, getBoard()));
		LOGGER.log(Level.INFO, "notified players on card layout");
		
		diceRoll();
		LOGGER.log(Level.INFO, "dice rolled");
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildDICEROLLmessage(this, blackDice, whiteDice, orangeDice));
		
		Utils.safeSleep(200);
		
	////-------------------------LEADER DISTRIBUTION ----------------////////
			LOGGER.log(Level.INFO, "giving lock to the first player...");
			if(true){  
				boolean flag = true;
				while(leaderHandler.getRunning()){ 
					if(flag){
						LOGGER.log(Level.INFO, "leader rule activated. Distribuiting card");
						Player firstPlayer = this.playerList.get(0);
						setLock(firstPlayer.getUUID());
						leaderHandler.leaderPhase(firstPlayer);
						flag = false; // cancel the first try
					}
					
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
			Utils.safeSleep(500);
			
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

			if(message != null && message.getPlayerUUID().equals(getLock())){
				messageHandler.handleMessage(message);
			}
		}
	}
	
	/**
	 * the main method which apply the action, after it has been assessed as a valid action
	 * @param player the player who has done the action
	 * @param action the action performed by the player
	 */
	public void makeMove(Player player, Action action){
		LOGGER.log(Level.INFO, "PRIMA DEGLI EFFETTI PERMANENTI:\n%s", action.toString());
		MoveUtils.applyEffects(this.board, player, action, contextManager);
		LOGGER.log(Level.INFO, "DOPO GLI EFFETTI PERMANENTI:\n%s", action.toString());
		LOGGER.log(Level.INFO, "PRIMA DEL BONUS:\n%s", player.toString());
		MoveUtils.addActionSpaceBonus(this.board, player, action);
		LOGGER.log(Level.INFO, "DOPO DEL BONUS:\n%s", player.toString());
		if(action.getAdditionalInfo().asObject().get("BONUSFLAG") != null &&
		   action.getAdditionalInfo().asObject().get("BONUSFLAG").asBoolean()) // da attivare solo per azioni non bonus
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
		
		LOGGER.log(Level.FINE, "dopo azione: %s\n", player.toString());
		
	}
	
	/**
	 * allows to retrive the TurnManager instance
	 * @return the turnManager of this game
	 */
	public TurnManager getTurnManager(){
		return this.turnManager;
	}
	
	/**
	 * allows to retrive the list of players connected to this game
	 * @return the list of player of this game
	 */
	public ArrayList<Player> getPlayerList(){
		return this.playerList;
	}
	
	/**
	 * allows to retrive the Board instance
	 * @return the board of this game
	 */
	public Board getBoard(){
		return this.board;
	}
	
	/**
	 * allows to retrive the UUID of the player who has the lock
	 * @return the UUID of the player who has the lock when the method was called
	 */
	public UUID getLock(){
		return this.lock;
	}
	
	/**
	 * get the UUID of this game
	 * @return the UUID of the game
	 */
	public UUID getUUID(){
		return this.gameUUID;
	}
	
	/**
	 * allows to retrive a single development card deck, given its card type
	 * @param type the deck type which must be retrived
	 * @return the requested development deck at the state when the method was called
	 */
	public Deck<DevelopmentCard> getDeck(String type){
		return this.decks.get(type);
	}
	
	/**
	 * given the corresponding period, allows to retrive the excommunication tile of the instance of this game
	 * @param period the period of the excommunication tile which has to be retrived
	 * @return the requested excommunication card
	 */
	public ExcommunicationCard getExcommunicationCard(int period){
		return this.excommunicationCards[period-1];
	}
	
	/**
	 * allows to get all the excommunication tiles of this game
	 * @return the array of excommunication tile
	 */
	public ExcommunicationCard[] getExcommunicationCard(){
		return this.excommunicationCards;
	}
	
	/**
	 * allow to pass the lock to the given player
	 * @param player the player to who the lock must be passed
	 */
	public void setLock(UUID player){
		this.lock = player;
	}
	
	/**
	 * perform the dice roll phase and set the action value of all the family members of all player according to the color and value of each dice
	 */
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
	
	/**
	 * move the family member to the position specified into the action argument, after action has passed some preliminary check
	 * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 */
	public void moveFamiliar(Board board, Player player, Action action){
		
		MoveUtils.checkServants(board, player, action);
		MoveUtils.checkCoinForTribute(board, player, action);
		
		int pawnID = action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt();
		player.moveFamilyMember(pawnID, action, board); 
	}
	
	/**
	 * perform the card taking, after the action has passed some preliminary check
	 * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 */
	public void takeCard(Board board, Player player, Action action){
		MoveUtils.checkCardCost(board, player, action); 
		player.takeCard(board, action);					
	}
	
	/**
	 * allows to get the instance of the context manager of this game
	 * @return the context manager of this game
	 */
	protected ContextManager getContextManager(){
	    return this.contextManager;
	}
	
	/**
	 * allows to get the instance of the move checker of this game
	 * @return the move checker of this game
	 */
	protected MoveChecker getMoveChecker(){
	    return this.moveChecker;
	}
	
	/**
	 * allows to get the instance of the EndPhaseHandler of this game
	 * @return the end phase handler of this game
	 */
	protected EndPhaseHandler getEndPhaseHandler(){
	    return this.endPhaseHandler;
	}
	
	/**
	 * responsable of sending the DICEROLL message to the clients after diceRoll() has been called
	 */
	protected void sendDICEROLL(){
	    MessageManager.getInstance().sendMessge(ServerMessageFactory.buildDICEROLLmessage(this, blackDice, whiteDice, orangeDice));
	}
	
	
	/**
	 * allows to get the instance of the message handler of this game
	 * @return the message handler of this game
	 */
	protected MessageHandler getMessageHandler(){
	    return this.messageHandler;
	}
	
	/** 
	 * replaces the current actionHandler with the one provided
	 */
	protected void setActionHandler(ActionHandler actionHandler){
	    this.actionHandler = actionHandler;
	}
}
