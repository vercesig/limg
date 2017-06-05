package it.polimi.ingsw.GC_32.Server.Network;

import java.util.concurrent.ConcurrentLinkedQueue;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;

public class MessageManager {

	private static MessageManager instance;
	private ConcurrentLinkedQueue<GameMessage> reciveQueue;
	private ConcurrentLinkedQueue<GameMessage> RMISendQueue;
	private ConcurrentLinkedQueue<GameMessage> socketSendQueue;
	
	private MessageManager(){
		this.reciveQueue = new ConcurrentLinkedQueue<GameMessage>();
		this.RMISendQueue = new ConcurrentLinkedQueue<GameMessage>();
		this.socketSendQueue = new ConcurrentLinkedQueue<GameMessage>();
	}
	
	public static MessageManager getInstance(){
		if(instance==null){
			instance = new MessageManager();
		}
		return instance;
	}
	
	public void putRecivedMessage(GameMessage message){
		reciveQueue.add(message);
	}
	
	public void sendMessge(GameMessage message){
		if(PlayerRegistry.getInstance().getConnectionMode(message.getPlayerID()) == ConnectionType.SOCKET){
			socketSendQueue.add(message);
			System.out.println("messaggio inserito nella coda");
		}else{
			RMISendQueue.add(message);
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
	
}
