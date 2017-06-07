package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;

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
			// controllo messaggi in ricezione dall'inputBuffer dei socket connessi
			for(String player : socketListener.getSocketPlayerRegistry().keySet()){
				try {
					if(socketListener.getSocketPlayerRegistry().get(player).getInputStream().available()!=0){
						Scanner tmpScanner = new Scanner(socketListener.getSocketPlayerRegistry().get(player).getInputStream());
							GameMessage tmpMessage = new GameMessage(player,tmpScanner.nextLine());
							MessageManager.getInstance().putRecivedMessage(tmpMessage);
							System.out.println("[SOCKETSENTINEL] catched new message for "+tmpMessage.getPlayerID());
						tmpScanner.close();
					}
				} catch (IOException e) {
					Logger.getLogger("").log(Level.SEVERE, "context", e);
					break;
				}
			}
			// se ci sono messaggi in coda li spedisco sul relativo socket
			if(!MessageManager.getInstance().getSocketSendQueue().isEmpty()){
				for(GameMessage message : MessageManager.getInstance().getSocketSendQueue()){
					try {
						PrintWriter tmpPrinter = new PrintWriter(socketListener.getSocketPlayerRegistry().get(message.getPlayerID()).getOutputStream());
							tmpPrinter.println(message.getMessage());
							tmpPrinter.flush();
							MessageManager.getInstance().getSocketSendQueue().remove();
							System.out.println("[SOCKETSENTINEL] message sent to :"+message.getPlayerID());
						tmpPrinter.close();
					} catch (IOException e) {
						Logger.getLogger("").log(Level.SEVERE, "context", e);
						break;
					}
				}
			}
		}
	}	
}
