package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.HashMap;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.ServerMessageFactory;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;

public class ContextManager{

	private ArrayList<String> waitingContextResponseSet;
	private HashMap<String, JsonValue> contextInfoContainer;
	
	public ContextManager(){
		this.contextInfoContainer = new HashMap<String,JsonValue>();
		this.waitingContextResponseSet = new ArrayList<String>();
	}
	
	public void openContext(ContextType context, Player player, Action action, Object...extraInfo){
		switch(context){
		case SERVANT:
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
					player.getUUID(), 
					ContextType.SERVANT, 
					player.getResources().getResource("SERVANTS"),action.getActionType()));
			waitingContextResponseSet.add("SERVANT");
			break;
		case CHANGE:
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
					player.getUUID(),
					ContextType.CHANGE,
					action.getAdditionalInfo().get("CARDNAME").asString(), (JsonArray)extraInfo[0], (JsonArray)extraInfo[1] // see ServerMessageFactory
					));
			waitingContextResponseSet.add("CHANGE");
			break;
		case PRIVILEGE:
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
					player.getUUID(),
					ContextType.PRIVILEGE,
					extraInfo[0])); // number of privilege to consume
			waitingContextResponseSet.add("PRIVILEGE");
			break;
		default:
			break;
		}
	}
	
	public boolean isThereAnyOpenContext(){
		return !waitingContextResponseSet.isEmpty();
	}
	
	
	
	
}
