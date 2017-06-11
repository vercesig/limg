package it.polimi.ingsw.GC_32.Common.Network;



import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class ServerMessageFactory {

	public static GameMessage buildGMSTRTmessage(Game game){
		JsonObject GMSTRT = new JsonObject();
		JsonArray GMSTRTplayers = new JsonArray();
		game.getPlayerList().forEach(player -> GMSTRTplayers.add(player.getUUID()));
		GMSTRT.add("PLAYERLIST", GMSTRTplayers.toString());
		GameMessage GMSTRTmessage = new GameMessage(null, "GMSTRT", GMSTRT.toString());
		GMSTRTmessage.setAsBroadcastMessage();
		
		return GMSTRTmessage;
	}
	
	public static GameMessage buildSTATCHNGmessage(Player player){
		JsonObject STATCHNG = new JsonObject();
		JsonObject STATCHNGpayload = new JsonObject();
		STATCHNG.add("TYPE", "RESOURCE");
		STATCHNGpayload.add("WOOD", 2);
		STATCHNGpayload.add("STONE", 2);
		STATCHNGpayload.add("SERVANTS", 3);
		STATCHNGpayload.add("FAITH", 0);
		STATCHNGpayload.add("MILITARY", 0);
		STATCHNGpayload.add("VICTORY", 0);
		STATCHNGpayload.add("COINS", player.getResources().getResouce("COINS"));
		STATCHNG.add("PLAYERID", player.getUUID());
		STATCHNG.add("PAYLOAD", STATCHNGpayload.toString());		
		return new GameMessage(player.getUUID(), "STATCHNG", STATCHNG.toString());
	}
	
	public static GameMessage buildSTATCHNGmessage(String playerUUID, ResourceSet addingResources){
		JsonObject STATCHNG = new JsonObject();
		JsonObject STATCHNGpayload = new JsonObject();
		STATCHNG.add("TYPE", "RESOURCE");
		addingResources.getResourceSet().forEach((resourceName,resourceQuantity)->{
			STATCHNG.add(resourceName, resourceQuantity);
		});
		STATCHNG.add("PLAYERID", playerUUID);
		STATCHNG.add("PAYLOAD", STATCHNGpayload.toString());
		return new GameMessage(playerUUID, "STATCHNG", STATCHNG.toString());		
	}
	
	public static GameMessage buildSTATCHNGmessage(String playerUUID, List<DevelopmentCard> cards){
		JsonObject STATCHNG = new JsonObject();
		JsonObject STATCHNGpayload = new JsonObject();
		STATCHNG.add("TYPE", "CARD");
		cards.forEach(card -> STATCHNGpayload.add(card.getType(),card.getName()));
		STATCHNG.add("PAYLOAD", STATCHNGpayload.toString());
		STATCHNG.add("PLAYERID", playerUUID);
		return new GameMessage(playerUUID, "STATCHNG", STATCHNG.toString());	
	}
	
	public static GameMessage buildNAMECHGmessage(String playerUUID, String name){
		JsonObject NAMECHG = new JsonObject();
		NAMECHG.add("PLAYERID",playerUUID);
		NAMECHG.add("NAME", name);
		GameMessage NAMECHGmessage = new GameMessage(null,"NAMECHG",NAMECHG.toString());
		NAMECHGmessage.setAsBroadcastMessage();
		return NAMECHGmessage;
	}
	
	
	
	
}
