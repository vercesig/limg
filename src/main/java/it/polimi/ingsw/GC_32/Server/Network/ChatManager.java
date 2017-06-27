package it.polimi.ingsw.GC_32.Server.Network;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;

public class ChatManager implements Runnable{
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private ConcurrentLinkedQueue<GameMessage> queue;
	private boolean stop;
	
	public ChatManager(ConcurrentLinkedQueue<GameMessage> queue){
		this.queue = queue;
		this.stop = false;
	}
	
	public void run(){
		while(!stop){
			for(GameMessage message: queue){
				switch(message.getOpcode()){
					case "CHGNAME":
						GameRegistry.getInstance()
									  .getPlayerFromID(message.getPlayerUUID())
									  .setPlayerName(message.getMessage().asString());
						message.setBroadcast();
						MessageManager.getInstance().sendMessge(message);
						LOGGER.log(Level.INFO, "player "+message.getPlayerID()+
											   " changed name to "+message.getMessage().asString());
						break;
					case "SMSG":
						message.setBroadcast();
						MessageManager.getInstance().sendMessge(message);
				}
			}
		}
	}
}