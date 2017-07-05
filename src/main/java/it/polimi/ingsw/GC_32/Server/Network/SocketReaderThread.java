package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;

public class SocketReaderThread implements Runnable{

	private final static Logger LOGGER = Logger.getLogger(SocketReaderThread.class.getName());
	
	private SocketListener socketListener;
	
	public SocketReaderThread(SocketListener target){
		this.socketListener = target;
		LOGGER.log(Level.INFO, "start");
	}
	
	public void run(){
		LOGGER.log(Level.INFO, "ready to recive and send message");
		while(true){		
			
			try{ // send packet in order and without losses
				Thread.sleep(100);
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
			}
			
			// se ci sono messaggi in coda li spedisco sul relativo socket
			while(!MessageManager.getInstance().getSocketSendQueue().isEmpty()){
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
