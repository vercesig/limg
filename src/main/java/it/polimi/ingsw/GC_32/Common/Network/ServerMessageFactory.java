package it.polimi.ingsw.GC_32.Common.Network;



import java.util.List;
import java.util.Map.Entry;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.ActionSpace;
import it.polimi.ingsw.GC_32.Server.Game.Board.CouncilRegion;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerLayer;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class ServerMessageFactory {

	public static GameMessage buildGMSTRTmessage(Game game){
		JsonObject GMSTRT = new JsonObject();
		JsonArray GMSTRTplayers = new JsonArray();
		JsonObject GMSTRTboard = new JsonObject();
		
		System.out.println("[GAME->SERVERMESSAGEFACTORY] creating JSON for inititial board configuration...");
		
		// ***************************** BOARD JSON		
		JsonObject bonus = null;
		
		// JSON towers
		for(TowerRegion towerRegion : game.getBoard().getTowerRegion()){
			JsonArray tower = new JsonArray();
			for(TowerLayer towerLayer : towerRegion.getTowerLayers()){
				JsonObject actionSpace = new JsonObject();
				actionSpace.add("ACTIONVALUE", towerLayer.getActionSpace().getActionValue());
				actionSpace.add("REGIONID", towerLayer.getActionSpace().getRegionID());
				actionSpace.add("SPACEID", towerLayer.getActionSpace().getActionSpaceID());
				bonus = new JsonObject();
				for(Entry<String,Integer> resource : towerLayer.getActionSpace().getBonus().getResourceSet().entrySet()){
					bonus.add(resource.getKey(), resource.getValue());
				}
				actionSpace.add("BONUS", bonus.toString());
				// single is setted true by default
				tower.add(actionSpace);
			}
			GMSTRTboard.add("TOWERREGION", tower.toString());
		}
		
		// Json production region
		JsonArray boardProductionRegion = new JsonArray();
		for(ActionSpace action : game.getBoard().getProductionRegion().getTrack()){
			JsonObject actionSpace = new JsonObject();
			actionSpace.add("BONUS", bonus.toString());
			actionSpace.add("REGIONID", action.getRegionID());
			actionSpace.add("SPACEID", action.getActionSpaceID());
			// actionvalue is setted by default to 1
			actionSpace.add("SINGLE", action.isSingleActionSpace());
			boardProductionRegion.add(actionSpace);
		}
		GMSTRTboard.add("PRODUCTIONREGION", boardProductionRegion.toString());
		
		// Json production region
		JsonArray boardHarvastRegion = new JsonArray();
		for(ActionSpace action : game.getBoard().getHarvestRegion().getTrack()){
			JsonObject actionSpace = new JsonObject();
			actionSpace.add("BONUS", bonus.toString());
			actionSpace.add("REGIONID", action.getRegionID());
			actionSpace.add("SPACEID", action.getActionSpaceID());
			// actionvalue is setted by default to 1
			actionSpace.add("SINGLE", action.isSingleActionSpace());
			boardHarvastRegion.add(actionSpace);
		}
		GMSTRTboard.add("HARVASTREGION", boardHarvastRegion.toString());
		
		// Json council region
		JsonArray boardCouncilRegion = new JsonArray();
		CouncilRegion councilRegion = game.getBoard().getCouncilRegion();
		JsonObject councilSpace = new JsonObject();
		councilSpace.add("BONUS", bonus.toString());
		councilSpace.add("REGIONID", councilRegion.getCouncilSpace().getRegionID());
		councilSpace.add("SPACEID",  councilRegion.getCouncilSpace().getActionSpaceID());
		councilSpace.add("SINGLE", councilRegion.getCouncilSpace().isSingleActionSpace());
		// actionvalue is setted by default to 1
		boardCouncilRegion.add(councilSpace);
		GMSTRTboard.add("COUNCILREGION", boardCouncilRegion.toString());
		
		// Json market region
		JsonArray boardMarketRegion = new JsonArray();
		for(ActionSpace action : game.getBoard().getMarketRegion().getTrack()){
			JsonObject actionSpace = new JsonObject();
			actionSpace.add("BONUS", bonus.toString());
			actionSpace.add("REGIONID", action.getRegionID());
			actionSpace.add("SPACEID", action.getActionSpaceID());
			// actionvalue is setted by default to 1
			// single is setted true by default
			actionSpace.add("SINGLE", action.isSingleActionSpace());
			boardMarketRegion.add(actionSpace);
		}
		GMSTRTboard.add("MARKETREGION", boardMarketRegion.toString());
		
		GMSTRT.add("BOARD", GMSTRTboard.toString());
		
		System.out.println("[GAME->SERVERMESSAGEFACTORY] packaging game player list...");
		
		// playerList
		game.getPlayerList().forEach(player -> GMSTRTplayers.add(player.getUUID()));
		GMSTRT.add("PLAYERLIST", GMSTRTplayers.toString());
		GameMessage GMSTRTmessage = new GameMessage(null, "GMSTRT", GMSTRT.toString());
		GMSTRTmessage.setAsBroadcastMessage();
		
		System.out.println("[GAME->SERVERMESSAGEFACTORY] done, GMSTRT ready to be sent");		
		
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
