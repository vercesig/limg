package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;

/**
 * 
 * network thread responsable of the management of socket connections. After SocketListener has opened a socket connection, SocketReaderThread perform the sending of 
 * messages to the clients and the reception of messages from the clients on a socket connection
 * 
 * @see SocketListener
 *
 */
public class SocketReaderThread implements Runnable{
	
	private final static Logger LOGGER = Logger.getLogger(SocketReaderThread.class.getName());
	
	private SocketListener socketListener;
	
	/**
	 * 
	 * inizialize the thread in oreder to make available all the socket connection opened
	 * 
	 * @param target the SocketListener this SocketReaderThread must manage, a reference to SocketListener is used to retrive all the open socket connections
	 */
	public SocketReaderThread(SocketListener target){
		this.socketListener = target;
		LOGGER.log(Level.INFO, "start");
	}
	
	/**
	 * run method perform the sending and the reception of messages on all the opened socket connections. It also perform broadcast sending message, sending a specific
	 * message on all the socket connection relative to a specific game session. On the reception side, when a message is recived it is memorized into the MessageManger
	 * as a GameMessage.
	 * 
	 * @see GameMessage, MessageManager, GameRegistry
	 */
	public void run(){
		LOGGER.log(Level.INFO, "ready to recive and send message");
		while(true){		
			
			// se ci sono messaggi in coda li spedisco sul relativo socket
			while(!MessageManager.getInstance().getSocketSendQueue().isEmpty()){
								
				try{ // send packet in order and without losses
					Thread.sleep(100);
				}catch(InterruptedException e){
					Thread.currentThread().interrupt();
				}
				
				GameMessage message = MessageManager.getInstance().getSocketSendQueue().poll();
				if(message != null){
					if(message.isBroadcast()){
						socketListener.getSocketPlayerRegistry().forEach((UUID, SocketInfoContainer)->{
							// message are send only to the players connected to the same game
							if(GameRegistry.getInstance().getPlayerFromGameID(message.getGameID()).contains(UUID)){
								PrintWriter tmpPrinter = SocketInfoContainer.getPrinterOut();
								tmpPrinter.println(message.toJson().toString());
								tmpPrinter.flush();
								LOGGER.log(Level.INFO, "message sent in broadcast");
							}
						});
					}
					else{
						PrintWriter tmpPrinter = socketListener.getSocketPlayerRegistry().get(message.getPlayerUUID()).getPrinterOut();
						tmpPrinter.println(message.toJson().toString());
						tmpPrinter.flush();
						LOGGER.log(Level.INFO, "message sent to :"+message.getPlayerID());
					}
				}
			}
			
			// controllo messaggi in ricezione dall'inputBuffer dei socket connessi
			for(UUID playerID : socketListener.getSocketPlayerRegistry().keySet()){
				try {
					if(socketListener.getSocketPlayerRegistry().get(playerID).getSocket().getInputStream().available()!=0){
							Scanner tmpScanner = socketListener.getSocketPlayerRegistry().get(playerID).getScannerIn();
							GameMessage message = new GameMessage(tmpScanner.nextLine(), playerID);
							System.out.println("ricevuto: "+message.toJson().toString());
							LOGGER.log(Level.INFO, "catched new message for "+message.getPlayerID());
							MessageManager.getInstance().putRecivedMessage(message);
					}
				} catch (IOException e) {
					Logger.getLogger("").log(Level.SEVERE, "context", e);
					break;
				}
			}
			
		}
	}	
}
