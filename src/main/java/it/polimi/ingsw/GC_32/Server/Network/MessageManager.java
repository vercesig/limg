package it.polimi.ingsw.GC_32.Server.Network;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;

public class MessageManager {

	private static MessageManager instance;
	private ConcurrentLinkedQueue<GameMessage> reciveQueue;
	private ConcurrentLinkedQueue<GameMessage> RMISendQueue;
	private ConcurrentLinkedQueue<GameMessage> socketSendQueue;
	
	private Game game;
	
	private Set<String> filterMessageTypeSet;
	
	private MessageManager(){
		this.reciveQueue = new ConcurrentLinkedQueue<GameMessage>();
		this.RMISendQueue = new ConcurrentLinkedQueue<GameMessage>();
		this.socketSendQueue = new ConcurrentLinkedQueue<GameMessage>();
		this.filterMessageTypeSet = new HashSet<String>();
		this.filterMessageTypeSet.add("SMSG");
		this.filterMessageTypeSet.add("CHGNAME");
	}
	
	public static MessageManager getInstance(){
		if(instance==null){
			instance = new MessageManager();
		}
		return instance;
	}
	
	public void putRecivedMessage(GameMessage message){
		if(filterMessageTypeSet.contains(message.getOpcode())||message.getPlayerID().equals(game.getLock())){
			reciveQueue.add(message);
			System.out.println("[MESSAGEMANAGER] add new message to recivedQueue");
		}
	}
	
	public void sendMessge(GameMessage gameMessage){
		if(PlayerRegistry.getInstance().getConnectionMode(gameMessage.getPlayerID()) == ConnectionType.SOCKET){
			socketSendQueue.add(gameMessage);
			System.out.println("[MESSAGEMANAGER] add new message to sendQueue");
		}else{
			RMISendQueue.add(gameMessage);
		}
	}
	
	public ConcurrentLinkedQueue<GameMessage> getSocketSendQueue(){
		return this.socketSendQueue;
	}
	
	public ConcurrentLinkedQueue<GameMessage> getRMISendQueue(){
		return this.RMISendQueue;
	}
	
	public ConcurrentLinkedQueue<GameMessage> getRecivedQueue(){
		return this.reciveQueue;
	}
	
	public boolean hasMessage(){
		return !reciveQueue.isEmpty();
	}
	
	public void registerGame(Game game){
		this.game = game;
	}
	
}
