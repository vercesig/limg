package it.polimi.ingsw.GC_32.Server.Network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;

public class MessageManager {

	private final static Logger LOGGER = Logger.getLogger(MessageManager.class.getName());
	
	private static MessageManager instance;
	private ConcurrentLinkedQueue<GameMessage> commonReceiveQueue;
	private HashMap<UUID,ConcurrentLinkedQueue<GameMessage>> gameReceiveQueueList;
	private ConcurrentLinkedQueue<GameMessage> RMISendQueue;
	private ConcurrentLinkedQueue<GameMessage> socketSendQueue;
	
	protected Set<String> chatMessageTypeSet;
	
	private MessageManager(){
		this.commonReceiveQueue = new ConcurrentLinkedQueue<>();
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
		this.gameReceiveQueueList.put(game.getUUID(), new ConcurrentLinkedQueue<>());
	}
	
	public ConcurrentLinkedQueue<GameMessage> getQueueForGame(UUID uuid){
		return this.gameReceiveQueueList.get(uuid);
	}
	
	public ConcurrentLinkedQueue<GameMessage> getCommonReceiveQueue(){
		return this.commonReceiveQueue;
	}
	
	public static GameMessage parsePacker(String packet, UUID playerID){
		JsonObject parsedMessage = Json.parse(packet).asObject();						
		return new GameMessage(UUID.fromString(parsedMessage.get("GameID").asString()), 
											   playerID,
											   parsedMessage.get("MESSAGETYPE").asString(),
											   parsedMessage.get("PAYLOAD"));
	}
}
