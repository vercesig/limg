package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;

public class SocketSentinel implements Runnable{

	private SocketListener socketListener;
	
	public SocketSentinel(SocketListener target){
		this.socketListener = target;
	}
	
	public void run(){
		while(true){		
			// controllo messaggi in ricezione dall'inputBuffer dei socket connessi
			for(String player : socketListener.getSocketPlayerRegistry().keySet()){
				try {
					if(socketListener.getSocketPlayerRegistry().get(player).getInputStream().available()!=0){
						Scanner tmpScanner = new Scanner(socketListener.getSocketPlayerRegistry().get(player).getInputStream());
							GameMessage tmpMessage = new GameMessage(player,tmpScanner.nextLine());
							MessageManager.getInstance().putRecivedMessage(tmpMessage);
							System.out.println("nuovo messaggio per te :"+player);
						tmpScanner.close();
					}
				} catch (IOException e) {
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
							System.out.println("messaggio inviato a :"+message.getPlayerID());
						tmpPrinter.close();
					} catch (IOException e) {
						break;
					}
				}
			}
		}
	}	
}
