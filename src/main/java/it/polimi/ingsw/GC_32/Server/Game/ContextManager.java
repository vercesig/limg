package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.HashMap;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

public class ContextManager{

	private ArrayList<String> waitingContextResponseSet;
	private HashMap<String, JsonValue> contextInfoContainer;
	private Game game;
	
	public ContextManager(Game game){
		this.contextInfoContainer = new HashMap<String,JsonValue>();
		this.waitingContextResponseSet = new ArrayList<String>();
		this.game = game;
	}
	
	public void openContext(ContextType context, Player player, Action action, Object...extraInfo){
		switch(context){
		case SERVANT:
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
					this.game,
					player, 
					ContextType.SERVANT, 
					player.getResources().getResource("SERVANTS"),action.getActionType()));
			waitingContextResponseSet.add("SERVANT");
			break;
		case CHANGE:
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
					this.game,
					player,
					ContextType.CHANGE,
					action.getAdditionalInfo().get("CARDNAME").asString(), (JsonArray)extraInfo[0], (JsonArray)extraInfo[1] // see ServerMessageFactory
					));
			waitingContextResponseSet.add("CHANGE");
			break;
		case PRIVILEGE:
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
					this.game,
					player,
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
