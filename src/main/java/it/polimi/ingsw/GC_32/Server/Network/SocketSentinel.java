package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Scanner;
import java.util.logging.Level;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;

public class SocketSentinel implements Runnable{

	private SocketListener socketListener;
	
	public SocketSentinel(SocketListener target){
		this.socketListener = target;
		System.out.println("[SOCKETSENTINEL] start");
	}
	
	public void run(){
		System.out.println("[SOCKETSENTINEL] ready to recive and send message");
		while(true){		
			
			// se ci sono messaggi in coda li spedisco sul relativo socket
			if(!MessageManager.getInstance().getSocketSendQueue().isEmpty()){
				for(GameMessage message : MessageManager.getInstance().getSocketSendQueue()){
					JsonObject finalMessage = new JsonObject();
					finalMessage.add("MESSAGETYPE", message.getOpcode());
					finalMessage.add("PAYLOAD", message.getMessage());
					if(message.isBroadcastMessage()){
						socketListener.getSocketPlayerRegistry().forEach((playerID,socketInfoContainer) -> {
							PrintWriter tmpPrinter = socketInfoContainer.getPrinterOut();
							tmpPrinter.println(finalMessage.toString());
							tmpPrinter.flush();
						});
						System.out.println("[SOCKETSENTINEL] send message in broadcast");
					}else{
						PrintWriter tmpPrinter = socketListener.getSocketPlayerRegistry().get(message.getPlayerID()).getPrinterOut();
						tmpPrinter.println(finalMessage.toString());
						tmpPrinter.flush();
						System.out.println("[SOCKETSENTINEL] message sent to :"+message.getPlayerID());
					}
					MessageManager.getInstance().getSocketSendQueue().remove();
				}
			}
			
			// controllo messaggi in ricezione dall'inputBuffer dei socket connessi
			for(String player : socketListener.getSocketPlayerRegistry().keySet()){
				try {
					if(socketListener.getSocketPlayerRegistry().get(player).getSocket().getInputStream().available()!=0){
							Scanner tmpScanner = socketListener.getSocketPlayerRegistry().get(player).getScannerIn();
							JsonObject parsedMessage = Json.parse(tmpScanner.nextLine()).asObject();						
							GameMessage tmpMessage = new GameMessage(player,parsedMessage.get("MESSAGETYPE").asString(),parsedMessage.get("PAYLOAD").toString());
							System.out.println("[SOCKETSENTINEL] catched new message for "+tmpMessage.getPlayerID());
							MessageManager.getInstance().putRecivedMessage(tmpMessage);
					}
				} catch (IOException e) {
					System.out.println("eccezione in lettura");
					e.printStackTrace();
					Logger.getLogger("").log(Level.SEVERE, "context", e);
					break;
				}
			}
			
		}
	}	
}
