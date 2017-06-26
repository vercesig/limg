package it.polimi.ingsw.GC_32.Common.Network;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.ActionSpace;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.CouncilRegion;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerLayer;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Setup.JsonImporter;

public class ServerMessageFactory {
	
	private final static Logger LOGGER = Logger.getLogger(ServerMessageFactory.class.getName());
	
	private static InputStreamReader cardFile = new InputStreamReader(ServerMessageFactory.class.getClassLoader().getResourceAsStream("test.json"));
	
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
		
		// Json harvast region
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
		councilSpace.add("BONUS", "#");
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
			actionSpace.add("BONUS", "#");
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
				JsonObject bonus = new JsonObject();
				for(Entry<String,Integer> resource : towerLayer.getActionSpace().getBonus().getResourceSet().entrySet()){
					bonus.add(resource.getKey(), resource.getValue());
				}
				actionSpace.add("BONUS", bonus.toString());
				// single is setted true by default
				tower.add(actionSpace);
			}
			GMSTRTboard.add("TOWERREGION", tower.toString());
		}
		GMSTRT.add("BOARD", GMSTRTboard.toString());
		
		LOGGER.log(Level.INFO, "packaging game player list...");
		
		// playerList
		game.getPlayerList().forEach(player -> GMSTRTplayers.add(player.getUUID()));
		GMSTRT.add("PLAYERLIST", GMSTRTplayers.toString());
		GameMessage GMSTRTmessage = new GameMessage(null, "GMSTRT", GMSTRT.toString());
		GMSTRTmessage.setAsBroadcastMessage();
		
		LOGGER.log(Level.INFO, "done, GMSTRT ready to be sent");		
		
		return GMSTRTmessage;
	}
	
	public static GameMessage buildSTATCHNGmessage(Player player){
		JsonObject STATCHNG = new JsonObject();
 		JsonObject STATCHNGCardpayload = new JsonObject();
 		
 		STATCHNG.add("RESOURCE", player.getResources().toJson().toString());
 		STATCHNG.add("PLAYERID", player.getUUID());
 		
 		player.getPersonalBoard().getCards().forEach((type,cardList)->{
 			JsonArray tmpCardArray = new JsonArray();
 			cardList.forEach(card -> {
 				tmpCardArray.add(card.getName());
 			});
 			STATCHNGCardpayload.add(type, tmpCardArray);
 		});
 		
		STATCHNG.add("BONUSTILE", player.getPersonalBonusTile().toString());
 		STATCHNG.add("PAYLOAD", STATCHNGCardpayload.toString());		
 		GameMessage STATCHNGmessage = new GameMessage(player.getUUID(), "STATCHNG", STATCHNG.toString());
 		STATCHNGmessage.setAsBroadcastMessage();
 		return STATCHNGmessage;
}
	
	public static GameMessage buildNAMECHGmessage(String playerUUID, String name){
		JsonObject NAMECHG = new JsonObject();
		NAMECHG.add("PLAYERID",playerUUID);
		NAMECHG.add("NAME", name);
		GameMessage NAMECHGmessage = new GameMessage(null,"NAMECHG",NAMECHG.toString());
		NAMECHGmessage.setAsBroadcastMessage();
		return NAMECHGmessage;
	}
	
	
	public static GameMessage buildCHGBOARDSTATmessage(Board board){
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
 		
 		GameMessage CHGBOARDSTATmessage = new GameMessage(null, "CHGBOARDSTAT", CHGBOARDSTAT.toString());
 		CHGBOARDSTATmessage.setAsBroadcastMessage();		
 		return CHGBOARDSTATmessage;
}
		
	public static GameMessage buildCONTEXTmessage(String playerUUID, ContextType type, Object...payload){
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
			List<DevelopmentCard> changeCards = (LinkedList<DevelopmentCard>) payload[0];
			
			JsonArray CONTEXTchangeArray = new JsonArray();			
			
			for(DevelopmentCard card : changeCards){
				JsonObject jsonCard = null;
				try {
					jsonCard = JsonImporter.importSingleCard(cardFile, card.getName()).asObject();
				}catch(IOException e) {}
				
				JsonValue permanentPayload = jsonCard.get("permanentPayload");
				JsonArray tmp = new JsonArray();
				
				if(permanentPayload.isArray())
					tmp = (JsonArray) permanentPayload;
				else
					tmp.add(permanentPayload);
				
				JsonArray cardPacket = new JsonArray();
				cardPacket.add(changeCards.indexOf(card)); // indice carta nell'array
				
				int effectID = 0;
				for(JsonValue v : permanentPayload.asArray()){
					Iterator<Member> changePayload = v.asObject().iterator();
					
					JsonObject cost = new JsonObject();
					JsonObject benefit = new JsonObject();
					
					while(changePayload.hasNext()){
						Member item = changePayload.next();
						if(item.getValue().asInt()<0)
							cost.add(item.getName(), item.getValue().asInt());
						else
							benefit.add(item.getName(), item.getValue().asInt());
					}
					cardPacket.add(effectID);
					cardPacket.add(cost.toString());
					cardPacket.add(benefit.toString());
					
					cardPacket.add(card.getName());
					
					CONTEXTchangeArray.add(cardPacket);
					effectID++;
				}
			}
			CONTEXTpayload.add("CHANGEARRAY", CONTEXTchangeArray.toString());
			break;
		}	
		CONTEXT.add("PAYLOAD", CONTEXTpayload);
		return new GameMessage(playerUUID, "CONTEXT", CONTEXT.toString());
	}
	
	public static GameMessage buildACKCONTEXTMessage(String playerUUID) {					
		return new GameMessage(playerUUID, "ACKCONTEXT", "CONTEXT OPERATION SUCCEDED!"); 
	} 
	
	
	public static GameMessage buildDICEROLLmessage(int blackDice, int whiteDice, int orangeDice){
		JsonObject DICEROLL = new JsonObject();
		DICEROLL.add("BLACKDICE", blackDice);
		DICEROLL.add("WHITEDICE",whiteDice);
		DICEROLL.add("ORANGEDICE", orangeDice);
		
		GameMessage DICEROLLmessage = new GameMessage(null, "DICEROLL", DICEROLL.toString());
		DICEROLLmessage.setAsBroadcastMessage();
		return DICEROLLmessage;
	}
	
	public static GameMessage buildTRNBGNmessage(String playerUUID){
		JsonObject TRNBGN = new JsonObject();
		TRNBGN.add("PLAYERID", playerUUID);
		GameMessage TRNBGNmessage = new GameMessage(null, "TRNBGN", TRNBGN.toString());
		TRNBGNmessage.setAsBroadcastMessage();
		return TRNBGNmessage;
	}
}
