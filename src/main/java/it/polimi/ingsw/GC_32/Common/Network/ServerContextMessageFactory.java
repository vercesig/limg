package it.polimi.ingsw.GC_32.Common.Network;

import java.util.LinkedList;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class ServerContextMessageFactory {
	
	public static JsonObject buildServantMessage(Player p) {
		JsonObject INFORMATION = new JsonObject();
		INFORMATION.add("OPCODE", "SERVANTS").add("NUMBER", p.getResources().getResource("SERVANTS"));
		
		return INFORMATION; 
	} 
	
	public static JsonObject buildPrivilegeMessage(int number) {
		JsonObject INFORMATION = new JsonObject();
		INFORMATION.add("OPCODE", "PRIVILEGE").add("NUMBER", number);
		
		return INFORMATION; 
	} 
	
	public static JsonObject buildExcommunicationMessage(Player p, int faithNeeded){
		JsonObject INFORMATION = new JsonObject();
		INFORMATION.add("OPCODE", "EXCOMMUNICATION").add("PLAYER_FAITH", p.getResources().getResource("FAITH"))
			.add("FAITH_NEEDED", faithNeeded);
		return INFORMATION;
	}
	
	public static JsonObject buildChangeMessage(LinkedList<DevelopmentCard> cardlist){
		JsonObject INFORMATION = new JsonObject();
		/*INFORMATION.add("OPCODE", "CHANGE").add("RESOURCE_IN", in.toJson().toString())
			.add("RESOURCE_OUT", out.toJson().toString()); */
		return INFORMATION;	
	}
	
	public static JsonObject buildActionMessage(Action a){
		JsonObject INFORMATION = new JsonObject();
		INFORMATION.add("OPCODE", "ACTION").add("REGION_ID", a.getActionRegionId());
		return INFORMATION;
	}
}
