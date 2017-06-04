package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;

public class GameMessageFilter implements Runnable {

	private Game game;
	private ArrayList<GameMessage> filteredMessage;
	
	
	public GameMessageFilter(Game game){
		this.game = game;
	}
	
	public void run(){
		
		while(true){
			
			// recupero messaggi di interesse dalla coda dei messaggi ricevuti
			if(MessageManager.getInstance().hasMessage()){
				for(GameMessage message : MessageManager.getInstance().getRecivedQueue()){
					
					// recupero messaggi che non interessano il lock
					switch(message.getMessageType()){
					case SMSG:
					case CHGNAME:
						filteredMessage.add(message);
						MessageManager.getInstance().getRecivedQueue().remove(message);
					}
					
					// recupero messaggi relativi al giocatore che ha il lock
					if(message.getPlayerID().equals(game.getLock())){
						filteredMessage.add(message);
					}
				}
				// pulisco la coda dai rimanenti messaggi (ASKACT, ASKLDRACT) non applicabili
				MessageManager.getInstance().getRecivedQueue().clear();
			}
			
			filteredMessage.forEach(message -> {
				switch(message.getMessageType()){
				case ASKACT:
					JsonObject Jsonmessage = Json.parse(message.getMessage()).asObject();
					int pawnID = Jsonmessage.get("PAWNID").asInt();
					int regionID = Jsonmessage.get("REGIONID").asInt();
					int spaceID = Jsonmessage.get("SPACEID").asInt();
					game.moveFamiliar(PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID()), pawnID, regionID, spaceID);
				}
			});
			
		}
		
	}
	
}
