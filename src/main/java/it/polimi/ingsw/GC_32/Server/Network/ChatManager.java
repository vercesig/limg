package it.polimi.ingsw.GC_32.Server.Network;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Utils.KillableRunnable;

public class ChatManager implements KillableRunnable{
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private LinkedBlockingQueue<GameMessage> queue;
	private boolean stop;
	
	public ChatManager(LinkedBlockingQueue<GameMessage> queue){
		this.queue = queue;
		this.stop = false;
	}
	
	public void run(){
		while(!stop){
			GameMessage message = null;
			try{
				message = queue.take();
			} catch(InterruptedException e){
			    Thread.currentThread().interrupt();
			}
			if(message != null){
				switch(message.getOpcode()){
					case "CHGNAME":
						manageChangeName(message);
						break;
					case "MSG":
						message.setBroadcast();
						MessageManager.getInstance().sendMessge(message);
						break;
				}
			}
		}
	}
	
	public void kill(){
		this.stop = true;
	}
	
	private void manageChangeName(GameMessage message){
	    GameRegistry.getInstance()
                    .getPlayerFromID(message.getPlayerUUID())
                    .setPlayerName(message.getMessage().asObject().get("NAME").asString());
	    message.setBroadcast();
	    MessageManager.getInstance().sendMessge(message);
	    LOGGER.log(Level.INFO, "player {} changed name to {}",
	                           new Object[]{message.getPlayerID(),
	                                        message.getMessage().asObject()
	                                               .get("NAME").asString()});
	}
}