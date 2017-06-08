package it.polimi.ingsw.GC_32.Server.Network;

import java.util.ArrayList;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.MoveChecker;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;

public class GameMessageFilter implements Runnable {

	private Game game;
	private ArrayList<GameMessage> filteredMessage;
	private boolean runFlag = true;
	
	
	public GameMessageFilter(Game game){
		this.game = game;
		this.filteredMessage = new ArrayList<GameMessage>();
	}
	
	public void stop(){
		this.runFlag = false;
	}
	
	public ArrayList<GameMessage> getFilteredMessage(){
		return this.filteredMessage;
	}
	
	public void run(){
		
		while(runFlag){
			
			filteredMessage.forEach(message -> {
				JsonObject Jsonmessage = Json.parse(message.getMessage()).asObject();
				switch(Jsonmessage.get("MESSAGETYPE").toString()){
				case "ASKACT":
					int pawnID = Jsonmessage.get("PAWNID").asInt();
					int actionValue = PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID()).getFamilyMember()[pawnID].getActionValue();
					
					int regionID = Jsonmessage.get("REGIONID").asInt();
					int spaceID = Jsonmessage.get("SPACEID").asInt();
					String actionType = Jsonmessage.get("ACTIONTYPE").asString();
					
					Action action = new Action(actionType,actionValue,regionID,spaceID);
					
					// verifica se l'azione è valida
					//MoveChecker.checkMove(game.getBoard(),PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID()),action);
					//game.moveFamiliar(PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID()), pawnID, regionID, spaceID);
					break;
				case "SMSG":
					//printa su chat
					break;
				case "CHGNAME":
					String name = Jsonmessage.get("NAME").asString();
					PlayerRegistry.getInstance().getPlayerFromID(message.getPlayerID()).setPlayerName(name);
					break;
				case "TRNEND":
					break;
				}
			});
			
		}
		
	}
	
}
