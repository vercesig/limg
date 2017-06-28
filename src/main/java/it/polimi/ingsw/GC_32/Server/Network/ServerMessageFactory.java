package it.polimi.ingsw.GC_32.Server.Network;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.Json;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.ActionSpace;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.CouncilRegion;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerLayer;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;

public class ServerMessageFactory {
	
	private final static Logger LOGGER = Logger.getLogger(ServerMessageFactory.class.getName());
	
	public static GameMessage buildGMSTRTmessage(Game game){
		JsonObject GMSTRT = new JsonObject();
		JsonArray GMSTRTplayers = new JsonArray();
		JsonObject GMSTRTboard = new JsonObject();
		
		LOGGER.log(Level.INFO, "creating JSON for inititial board configuration...");
		
		// ***************************** BOARD JSON	(l'ordine con cui vengono impilate le region è fondamentale)		
		
		// Json production region
		JsonArray boardProductionRegion = new JsonArray();
		for(ActionSpace action : game.getBoard().getProductionRegion().getTrack()){
			JsonObject actionSpace = new JsonObject();
			actionSpace.add("BONUS", "#");
			actionSpace.add("REGIONID", action.getRegionID());
			actionSpace.add("SPACEID", action.getActionSpaceID());
			// actionvalue is setted by default to 1
			actionSpace.add("SINGLE", action.isSingleActionSpace());
			boardProductionRegion.add(actionSpace);
		}
		GMSTRTboard.add("PRODUCTIONREGION", boardProductionRegion.toString());
		
		// Json harvest region
		JsonArray boardHarvastRegion = new JsonArray();
		for(ActionSpace action : game.getBoard().getHarvestRegion().getTrack()){
			JsonObject actionSpace = new JsonObject();
			actionSpace.add("BONUS", "#");
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
		councilSpace.add("BONUS", "PRIVILEGE");
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
			if(action.getBonus()!= null){
				actionSpace.add("BONUS", action.getBonus().toJson());
			}
			else
				actionSpace.add("BONUS", "PRIVILEGE");
			actionSpace.add("REGIONID", action.getRegionID());
			actionSpace.add("SPACEID", action.getActionSpaceID());
			// actionvalue is setted by default to 1
			// single is setted true by default
			actionSpace.add("SINGLE", action.isSingleActionSpace());
			boardMarketRegion.add(actionSpace);
		}
		GMSTRTboard.add("MARKETREGION", boardMarketRegion.toString());
		
		// JSON towers
		for(TowerRegion towerRegion : game.getBoard().getTowerRegion()){
			JsonArray tower = new JsonArray();
			for(TowerLayer towerLayer : towerRegion.getTowerLayers()){
				JsonObject actionSpace = new JsonObject();
				actionSpace.add("ACTIONVALUE", towerLayer.getActionSpace().getActionValue());
				actionSpace.add("REGIONID", towerLayer.getActionSpace().getRegionID());
				actionSpace.add("SPACEID", towerLayer.getActionSpace().getActionSpaceID());
				/*JsonObject bonus = new JsonObject();		
				for(Entry<String,Integer> resource : towerLayer.getActionSpace().getBonus().getResourceSet().entrySet()){
					bonus.add(resource.getKey(), resource.getValue());
				}*/
				if(towerLayer.getActionSpace().getBonus()!= null){
					actionSpace.add("BONUS", towerLayer.getActionSpace().getBonus().toJson());
				}
				else
					actionSpace.add("BONUS", "#");
				// single is setted true by default
				tower.add(actionSpace);
			}
			GMSTRTboard.add("TOWERREGION", tower.toString());
		}
		GMSTRT.add("BOARD", GMSTRTboard.toString());
		
		LOGGER.log(Level.INFO, "packaging game player list...");
		
		// playerList
		game.getPlayerList().forEach(player -> GMSTRTplayers.add(player.getUUID().toString()));
		GMSTRT.add("PLAYERLIST", GMSTRTplayers.toString());
		GameMessage GMSTRTmessage = new GameMessage(game.getUUID(), null, "GMSTRT", GMSTRT);
		GMSTRTmessage.setBroadcast();
		
		LOGGER.log(Level.INFO, "done, GMSTRT ready to be sent");		
		
		return GMSTRTmessage;
	}
	
	public static GameMessage buildSTATCHNGmessage(Game game, Player player){
		JsonObject STATCHNG = new JsonObject();
 		JsonObject STATCHNGCardpayload = new JsonObject();
 		
 		STATCHNG.add("RESOURCE", player.getResources().toJson().toString());
 		STATCHNG.add("PLAYERID", player.getUUID().toString());
 		
 		player.getPersonalBoard().getCards().forEach((type,cardList)->{
 			JsonArray tmpCardArray = new JsonArray();
 			cardList.forEach(card -> {
 				tmpCardArray.add(card.getName());
 			});
 			STATCHNGCardpayload.add(type, tmpCardArray);
 		});
 		
		STATCHNG.add("BONUSTILE", player.getPersonalBonusTile().toString());
 		STATCHNG.add("PAYLOAD", STATCHNGCardpayload.toString());		
 		GameMessage STATCHNGmessage = new GameMessage(game.getUUID(), player.getUUID(), "STATCHNG", STATCHNG);
 		STATCHNGmessage.setBroadcast();
 		return STATCHNGmessage;
}
	
	public static GameMessage buildNAMECHGmessage(Game game, String playerUUID, String name){
		JsonObject NAMECHG = new JsonObject();
		NAMECHG.add("PLAYERID",playerUUID);
		NAMECHG.add("NAME", name);
		GameMessage NAMECHGmessage = new GameMessage(game.getUUID(), null,"NAMECHG", NAMECHG);
		NAMECHGmessage.setBroadcast();
		return NAMECHGmessage;
	}
	
	
	public static GameMessage buildCHGBOARDSTATmessage(Game game, Board board){
 		JsonObject CHGBOARDSTAT = new JsonObject();
 		CHGBOARDSTAT.add("TYPE", "BOARD");
 		JsonArray CHGBOARDSTATpayload = new JsonArray();
 		for(TowerRegion towerRegion : board.getTowerRegion()){
 			for(TowerLayer towerLayer : towerRegion.getTowerLayers()){
 				JsonObject card = new JsonObject();
 				try{
 				card.add("NAME", towerLayer.getCard().getName());
 				}
 				catch(NullPointerException e){
 					card.add("NAME", "empty"); // se ;a carta e' stata presa
 				}
 				
 				card.add("REGIONID", towerLayer.getActionSpace().getRegionID());
 				card.add("SPACEID", towerLayer.getActionSpace().getActionSpaceID());
 				CHGBOARDSTATpayload.add(card);
 			}
 		}
 		CHGBOARDSTAT.add("PAYLOAD", CHGBOARDSTATpayload.toString());
 		
 		GameMessage CHGBOARDSTATmessage = new GameMessage(game.getUUID(), null, "CHGBOARDSTAT", CHGBOARDSTAT);
 		CHGBOARDSTATmessage.setBroadcast();		
 		return CHGBOARDSTATmessage;
}
		
	public static GameMessage buildCONTEXTmessage(Game game, Player player, ContextType type, Object...payload){
		JsonObject CONTEXT = new JsonObject();
		CONTEXT.add("CONTEXTID", type.getContextID());	
		JsonObject CONTEXTpayload = new JsonObject();
		switch(type){
		case PRIVILEGE:
			int numberOfPrivilege = (int) payload[0];
			CONTEXTpayload.add("NUMBER", numberOfPrivilege);
			break;
		case SERVANT:
			CONTEXTpayload.add("NUMBER_SERVANTS", (int) payload[0]);
			CONTEXTpayload.add("ACTIONTYPE", (String) payload[1]);
			break;
		case EXCOMMUNICATION:
			CONTEXTpayload.add("PLAYER_FAITH", (int) payload[0]);
			CONTEXTpayload.add("FAITH_NEEDED", (int) payload[1]);
			break;
		case CHANGE:
			CONTEXTpayload.add("NAME", (String) payload[0]);
			
			CONTEXTpayload.add("RESOURCEIN", (JsonArray) payload[1]);
			CONTEXTpayload.add("RESOURCEOUT", (JsonArray) payload[2]);
			break;
		}	
		CONTEXT.add("PAYLOAD", CONTEXTpayload);
		return new GameMessage(game.getUUID(), player.getUUID(), "CONTEXT", CONTEXT);
	}
	
	public static GameMessage buildCONTEXTACKMessage(Game game, Player player, boolean accepted) {					
		return new GameMessage(game.getUUID(), player.getUUID(), "CONTEXTACK", Json.value(accepted)); 
	} 
	
	public static GameMessage buildACTCHKmessage(Game game, Player player, Action action, boolean result) {
		JsonObject payload = new JsonObject();
		if(result){
			payload.add("RESULT", true);
			payload.add("REGIONID", action.getActionRegionId());
			payload.add("SPACEID", action.getActionSpaceId());
			payload.add("FAMILYMEMBER_ID", action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt());
			payload.add("ACTIONTYPE", action.getActionType());
		}
		else
			payload.add("RESULT", false);
		return new GameMessage(game.getUUID(), player.getUUID(), "ACTCHK", payload);
	} 
	
	
	public static GameMessage buildDICEROLLmessage(Game game, int blackDice, int whiteDice, int orangeDice){
		JsonObject DICEROLL = new JsonObject();
		DICEROLL.add("BLACKDICE", blackDice);
		DICEROLL.add("WHITEDICE",whiteDice);
		DICEROLL.add("ORANGEDICE", orangeDice);
		
		GameMessage DICEROLLmessage = new GameMessage(game.getUUID(), null, "DICEROLL", DICEROLL);
		DICEROLLmessage.setBroadcast();
		return DICEROLLmessage;
	}
	
	public static GameMessage buildTRNBGNmessage(Game game, UUID uuid){
		JsonObject TRNBGN = new JsonObject();
		TRNBGN.add("PLAYERID", uuid.toString());
		GameMessage TRNBGNmessage = new GameMessage(game.getUUID(), null, "TRNBGN", TRNBGN);
		TRNBGNmessage.setBroadcast();
		return TRNBGNmessage;
	}
}
