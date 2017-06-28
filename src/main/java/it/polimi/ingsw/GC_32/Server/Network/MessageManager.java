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

public class MessageManager {

	private final static Logger LOGGER = Logger.getLogger(MessageManager.class.getName());
	
	private static MessageManager instance;
	private LinkedBlockingQueue<GameMessage> commonReceiveQueue;
	private HashMap<UUID,LinkedBlockingQueue<GameMessage>> gameReceiveQueueList;
	private ConcurrentLinkedQueue<GameMessage> RMISendQueue;
	private ConcurrentLinkedQueue<GameMessage> socketSendQueue;
	
	protected Set<String> chatMessageTypeSet;
	
	private MessageManager(){
		this.commonReceiveQueue = new LinkedBlockingQueue<>();
		this.gameReceiveQueueList = new HashMap<>();
		this.RMISendQueue = new ConcurrentLinkedQueue<>();
		this.socketSendQueue = new ConcurrentLinkedQueue<>();
		this.chatMessageTypeSet = new HashSet<>();
		this.chatMessageTypeSet.add("SMSG");
		this.chatMessageTypeSet.add("CHGNAME");
	}
	
	public static MessageManager getInstance(){
		if(instance==null){
			instance = new MessageManager();
		}
		return instance;
	}
	
	public void putRecivedMessage(GameMessage message){
		if(chatMessageTypeSet.contains(message.getOpcode())){
			message.setBroadcast();
			commonReceiveQueue.add(message);
			LOGGER.log(Level.INFO, "add new message ("+message.getOpcode()+") to recivedQueue");
			return;
		}
		if(message.getPlayerUUID().equals(GameRegistry.getInstance()
													  .getGame(message.getGameID())
													  .getLock())){
			this.gameReceiveQueueList.get(message.getGameID()).add(message);
			LOGGER.log(Level.INFO, "add new message ("+message.getOpcode()+") to recivedQueue");
		}
	}
	
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
	
	public ConcurrentLinkedQueue<GameMessage> getSocketSendQueue(){
		return this.socketSendQueue;
	}
	
	public ConcurrentLinkedQueue<GameMessage> getRMISendQueue(){
		return this.RMISendQueue;
	}
	
	public void registerGame(Game game){
		this.gameReceiveQueueList.put(game.getUUID(), new LinkedBlockingQueue<>());
	}
	
	public LinkedBlockingQueue<GameMessage> getQueueForGame(UUID uuid){
		return this.gameReceiveQueueList.get(uuid);
	}
	
	public LinkedBlockingQueue<GameMessage> getCommonReceiveQueue(){
		return this.commonReceiveQueue;
	}
}
