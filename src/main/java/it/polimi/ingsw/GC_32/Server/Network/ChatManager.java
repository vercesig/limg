package it.polimi.ingsw.GC_32.Server.Network;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Utils.KillableRunnable;

/**
 * thread which handle the processing of all the message-types contained in chatMessageTypeSet into MessageManager. Messages are then echoed in broadcast to all the client
 * interested
 *
 *<ul>
 *<li> {@link #queue}: queue used to send messages</li>
 *<li> {@link #stop}: flag used to kill this thread</li>
 *</ul>
 *
 */
public class ChatManager implements KillableRunnable{
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private LinkedBlockingQueue<GameMessage> queue;
	private boolean stop;
	
	/**
	 * inzialize the chat manager with the queue used by external classes to send messages. MessageManager inizialize ChatManager whit his commonReceiveQueue.
	 * 
	 * @see MessageManager
	 * @param queue the queue used by this thread to send messages
	 */
	public ChatManager(LinkedBlockingQueue<GameMessage> queue){
		this.queue = queue;
		this.stop = false;
	}
	
	/**
	 * undle the processing of chatMessageTypeSet messages, i.e. CHGNAME messages and MSG messages (general messages used in chat)
	 */
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
					default:
					    break;
				}
			}
		}
	}
	
	/**
	 * use this method to kill this thread
	 */
	public void kill(){
		this.stop = true;
	}
	
	/**
	 * given a CHGNAME message, this method take the correct Player object changing his name according to the message content
	 * @param message the CHGNAME messsage to manage
	 */
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