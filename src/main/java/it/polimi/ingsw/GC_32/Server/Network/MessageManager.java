package it.polimi.ingsw.GC_32.Server.Network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;

/**
 * MessageManager is a singleton class which handle all the messages of the server. Given a message sent by a Game instance (or by another class of the program),
 * MessageManager take this message ensuring that the message is inserted in the correct queue. MessageManager handles to type of sendQueue, one for each connection mode 
 * socket and RMI). It handles two queue for recived message as well, one for game-specfic game message and the other for messages that are common to all the instance
 * of Game (i.e. for all the games)
 * 
 * <ul>
 * <li>{@link #instance}: the instance of this singleton class</li>
 * <li>{@link #commonReceiveQueue}: the recived queue used for game messages which are independent from the game</li>
 * <li>{@link #gameReceiveQueueList}: HashMap which map each game with its received message queue</li>
 * <li>{@link #RMISendQueue}: queue used to send messages throught RMI</li>
 * <li>{@link #socketSendQueue}: queue used to send messages throught socket</li>
 * <li>{@link #chatManager}: used to handle general message which doesn't influence the game, like chat mesasges</li>
 * <li>{@link #chatMessageTypeSet}: a Set which is used to filter messages in order to allow chatManagerThread to handle them, only messages which must be
 * handled by chatManagerThread are filtered</li>
 * <li>{@link #chatManagerThread}: the thread which perform the action handled by chatManager</li>
 * </ul>
 *
 *
 * @see GameMessage, ChatManager, SocketListener, SocketReaderThread, RmiServer, RmiListener
 */
public class MessageManager {

	private final static Logger LOGGER = Logger.getLogger(MessageManager.class.getName());
	
	private static MessageManager instance;
	private LinkedBlockingQueue<GameMessage> commonReceiveQueue;
	private HashMap<UUID,LinkedBlockingQueue<GameMessage>> gameReceiveQueueList;
	private ConcurrentLinkedQueue<GameMessage> RMISendQueue;
	private ConcurrentLinkedQueue<GameMessage> socketSendQueue;
	
	protected ChatManager chatManager;
	protected Set<String> chatMessageTypeSet;

    protected Thread chatManagerThread;
	
    /**
     * inizialize MessageManager (inizialize all the queues and chatMessageTypeSet values, starts chatManager thread so it can handle chatMessageTypeSet messages)
     */
	private MessageManager(){
		this.commonReceiveQueue = new LinkedBlockingQueue<>();
		this.gameReceiveQueueList = new HashMap<>();
		this.RMISendQueue = new ConcurrentLinkedQueue<>();
		this.socketSendQueue = new ConcurrentLinkedQueue<>();
		this.chatMessageTypeSet = new HashSet<>();
		this.chatMessageTypeSet.add("MSG");
		this.chatMessageTypeSet.add("CHGNAME");
		
		this.chatManager = new ChatManager(commonReceiveQueue);
		this.chatManagerThread = new Thread(chatManager);
		this.chatManagerThread.start();
	}
	
	/**
	 * allows to retrive the instance of this singleton class
	 * @return the MessageManager instance
	 */
	public static MessageManager getInstance(){
		if(instance==null){
			instance = new MessageManager();
		}
		return instance;
	}
	/**
	 * put a given message into the correct sendQueue
	 * 
	 * @param message the message to put into the receivedQueue
	 */
	synchronized public void putRecivedMessage(GameMessage message){
		if(chatMessageTypeSet.contains(message.getOpcode())){
			message.setBroadcast();
			commonReceiveQueue.add(message);
			LOGGER.log(Level.INFO, "add new message ("+message.getOpcode()+") to recivedQueue");
			return;
		}		
		if("CONTEXTREPLY".equals(message.getOpcode()) && "EXCOMMUNICATION".equals(message.getMessage().asObject().get("CONTEXT_TYPE").asString())){ // excommunication messages are lock independent
			this.gameReceiveQueueList.get(message.getGameID()).add(message);
		} else {
		    if(message.getPlayerUUID().equals(GameRegistry.getInstance()
		                                                  .getGame(message.getGameID())
		                                                  .getLock())){			
    			this.gameReceiveQueueList.get(message.getGameID()).add(message);
    			LOGGER.log(Level.INFO, "add new message ("+message.getOpcode()+") to recivedQueue");
		    }
		}
	}
	
	/**
	 * put a given message into the correct send queue looking to the client connection mode. if the message is sent in broadcast the message is put in both the queue
	 * 
	 * @param gameMessage the message to send
	 */
	public void sendMessge(GameMessage gameMessage){
		if(gameMessage.isBroadcast()){
			socketSendQueue.add(gameMessage);
			LOGGER.log(Level.INFO, "add new message ("+gameMessage.getOpcode()+") to socket sendQueue");
			RMISendQueue.add(gameMessage);
			LOGGER.log(Level.INFO, "add new message ("+gameMessage.getOpcode()+") to RMI sendQueue");
		}else{
			if(GameRegistry.getInstance().getConnectionMode(gameMessage.getPlayerUUID()) == ConnectionType.SOCKET){
				socketSendQueue.add(gameMessage);
				LOGGER.log(Level.INFO, "add new message ("+gameMessage.getOpcode()+") to socket sendQueue");
			}else{
				RMISendQueue.add(gameMessage);
			}	
		}
	}
	
	/**
	 * allows to get the socket send queue used to send message on socket
	 * 
	 * @return the socketSendQueue
	 */
	public ConcurrentLinkedQueue<GameMessage> getSocketSendQueue(){
		return this.socketSendQueue;
	}
	
	/**
	 * allows to get the RMI send queue used to send message on RMI
	 * 
	 * @return the RMIsendQueue
	 */
	public ConcurrentLinkedQueue<GameMessage> getRMISendQueue(){
		return this.RMISendQueue;
	}
	
	/**
	 * given a game, registerGame register that game instantiating a new entry in the gameReceivedQueueList, in this way all the GameMessage with the gameUUID of that
	 * game will be put in the correct queue
	 * 
	 */
	public void registerGame(Game game){
		this.gameReceiveQueueList.put(game.getUUID(), new LinkedBlockingQueue<>());
	}
	
	/**
	 * allows to retrive the queue of a specific game given his UUID
	 * 
	 * @param uuid the UUID of the game  of which you want get the queue
	 * @return the queue of this game
	 */
	public LinkedBlockingQueue<GameMessage> getQueueForGame(UUID uuid){
		return this.gameReceiveQueueList.get(uuid);
	}
	
	/**
	 * allows to retrive the common message queue
	 * 
	 * @return the commonReciveQueue
	 */
	protected LinkedBlockingQueue<GameMessage> getCommonReceiveQueue(){
		return this.commonReceiveQueue;
	}
}
