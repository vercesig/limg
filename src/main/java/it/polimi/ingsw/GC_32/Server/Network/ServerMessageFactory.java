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
import it.polimi.ingsw.GC_32.Server.Game.Card.Card;

/**
 * this class allows to simply generate all the messages used on the server side of the game in their JSON format. ServerMessageFactory offers a multitude of different
 * static method which returns a GameMessage which will be passed to the MessageManager and then send to the correct client.
 * 
 * For more details on the network protocol see the wiki.
 * 
 * @see GameMessage, Game
 *
 */
public class ServerMessageFactory {
	
	private final static Logger LOGGER = Logger.getLogger(ServerMessageFactory.class.getName());
	
	/**
	 * allows to generate a GMSTRT message
	 * 
	 * @param game the game object used to build this GMSTRT message
	 * @return the GameMessage representing this GMSTRT message
	 */
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
			if(action.isBlocked()){
				actionSpace.add("BLOCKFLAG", action.isBlocked());
			}
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
			if(action.isBlocked()){
				actionSpace.add("BLOCKFLAG", action.isBlocked());
			}
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
		if(councilRegion.getCouncilSpace().isBlocked()){
			councilSpace.add("BLOCKFLAG", councilRegion.getCouncilSpace().isBlocked());
		}
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
			if(action.isBlocked()){
				actionSpace.add("BLOCKFLAG", action.isBlocked());
			}
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
				if(towerLayer.getActionSpace().isBlocked()){
					actionSpace.add("BLOCKFLAG", towerLayer.getActionSpace().isBlocked());
				}
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
		
		//Excommunication card JsonArray
		JsonArray excommunicationList = new JsonArray();
		for(int i=0; i<3; i++){
			excommunicationList.add(game.getExcommunicationCard()[i].getName());
		}
		GMSTRT.add("EXCOMMUNICATIONCARDS", excommunicationList);
				
		LOGGER.log(Level.INFO, "packaging game player list...");
		
		// playerList
		game.getPlayerList().forEach(player -> GMSTRTplayers.add(player.getUUID().toString()));
		GMSTRT.add("PLAYERLIST", GMSTRTplayers.toString());
		GMSTRT.add("GAMEUUID", game.getUUID().toString());
		GameMessage GMSTRTmessage = new GameMessage(game.getUUID(), null, "GMSTRT", GMSTRT);
		GMSTRTmessage.setBroadcast();

		LOGGER.log(Level.INFO, "done, GMSTRT ready to be sent");		
		
		return GMSTRTmessage;
	}
	
	/**
	 * allows to generate a STATCHNG message relative to a specific player
	 * 
	 * @param game the game object used to build this STATCHNG message
	 * @param player the player which will be described by this STATCHNG message
	 * @return the GameMessage representing this STATCHNG message
	 */
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
 		JsonArray leaderCardArray = new JsonArray();
 		player.getPersonalBoard().getLeaderCards().forEach(leader -> leaderCardArray.add(leader.getName()));
 		STATCHNGCardpayload.add("LEADER", leaderCardArray);
 		
 		JsonArray familyStatus = new JsonArray();
 		for(int i=0; i<player.getFamilyMember().length; i++){
 			if(player.getFamilyMember()[i].isBusy()){
 				familyStatus.add(true);
 			}else{
 				familyStatus.add(false);
 			}
 		}
 		STATCHNG.add("FAMILYSTATUS", familyStatus.toString());
		STATCHNG.add("BONUSTILE", player.getPersonalBonusTile().toString());
 		STATCHNG.add("PAYLOAD", STATCHNGCardpayload.toString());		
 		GameMessage STATCHNGmessage = new GameMessage(game.getUUID(), player.getUUID(), "STATCHNG", STATCHNG);
 		STATCHNGmessage.setBroadcast();
 		return STATCHNGmessage;
	}
	
	/**
	 * allows to generate a NAMECHG message for the 
	 * 
	 * @param game the game object used to build this NAMECHG message
	 * @param playerUUID the UUID of the player
	 * @param name the new name of this player
	 * @return the GameMessage representing this NAMECHG message
	 */
	public static GameMessage buildNAMECHGmessage(Game game, String playerUUID, String name){
		JsonObject NAMECHG = new JsonObject();
		NAMECHG.add("PLAYERID",playerUUID);
		NAMECHG.add("NAME", name);
		GameMessage NAMECHGmessage = new GameMessage(game.getUUID(), null,"NAMECHG", NAMECHG);
		NAMECHGmessage.setBroadcast();
		return NAMECHGmessage;
	}
	
	/**
	 * allows to generate a ASKLDRACK message
	 * 
	 * @param game the game object used to build this ASKLDRACK message
	 * @param player the recipient of the message
	 * @param leaderCard the name of the leader card used in the leader action
	 * @param decision the type of action which specify this leader action
	 * @param result the result of the action, this flag show to the client if the action has been assessed as valid or not
	 * @return the GameMessage representing this ASKLDRACK message
	 */
	public static GameMessage buildASKLDRACKmessage(Game game, Player player, String leaderCard, String decision, boolean result){

		JsonObject ASKLDRACK = new JsonObject();
		JsonObject ASKLDRACKPayload = new JsonObject();
		if(result){
			ASKLDRACKPayload.add("RESULT", true);
		}
		else
			ASKLDRACKPayload.add("RESULT", false);
		ASKLDRACKPayload.add("LEADERCARD", leaderCard);
		ASKLDRACKPayload.add("DECISION", decision);
		ASKLDRACK.add("TYPE", "ASKLDRACK");
		ASKLDRACK.add("PAYLOAD", ASKLDRACKPayload.toString());
		
		GameMessage ASKLDRACKmessage = new GameMessage(game.getUUID(), player.getUUID(),"ASKLDRACK", ASKLDRACK);
		return ASKLDRACKmessage;
	}
	
	/**
	 * allows to generate a CHGBOARDSTAT message, describing a variation of the entire board (the card layout)
	 * 
	 * @param game the game object used to build this CHGBOARDSTAT message
	 * @param board the board which will be represented into this packet
	 * @see Board
	 * @return the GameMessage representing this CHGBOARDSTAT message
	 */
	public static GameMessage buildCHGBOARDSTATmessage(Game game, Board board){
 		JsonObject CHGBOARDSTAT = new JsonObject();
 		CHGBOARDSTAT.add("TYPE", "BOARD");
 		JsonArray CHGBOARDSTATpayload = new JsonArray();
 		for(TowerRegion towerRegion : board.getTowerRegion()){
 			for(TowerLayer towerLayer : towerRegion.getTowerLayers()){
 				JsonObject jCard = new JsonObject();
 				Card tCard = towerLayer.getCard();
 				if(tCard != null){
 				    jCard.add("NAME", tCard.getName());
 				} else {
 					jCard.add("NAME", "EMPTY"); // se la carta e' stata presa
 				}				
 				jCard.add("REGIONID", towerLayer.getActionSpace().getRegionID());
 				jCard.add("SPACEID", towerLayer.getActionSpace().getActionSpaceID()); 				
 				CHGBOARDSTATpayload.add(jCard);
 			}
 		}
 		CHGBOARDSTAT.add("PAYLOAD", CHGBOARDSTATpayload.toString());
 		
 		GameMessage CHGBOARDSTATmessage = new GameMessage(game.getUUID(), null, "CHGBOARDSTAT", CHGBOARDSTAT);
 		CHGBOARDSTATmessage.setBroadcast();		
 		return CHGBOARDSTATmessage;
	}
	
	/**
	 * allows to generate a CHGBOARDSTAT message, describing a variation of a family member position on the board
	 * 
	 * @param game the game object used to build this CHGBOARDSTAT message
	 * @param playerUUID the player that has made the action
	 * @param action the action which has caused the variation of family member position
	 * @see Action
	 * @return the GameMessage representing this CHGBOARDSTAT message
	 */
	public static GameMessage buildCHGBOARDSTATmessage(Game game, String playerUUID, Action action){
		JsonObject CHGBOARDSTAT = new JsonObject();
		CHGBOARDSTAT.add("TYPE", "FAMILY");
		JsonObject CHGBOARDSTATpayload = new JsonObject();
		CHGBOARDSTATpayload.add("REGIONID", action.getRegionId());
		CHGBOARDSTATpayload.add("SPACEID", action.getActionSpaceId());
		CHGBOARDSTATpayload.add("PLAYERID", playerUUID);
		CHGBOARDSTATpayload.add("FAMILYMEMBER_ID", action.getAdditionalInfo().asObject().get("FAMILYMEMBER_ID").asInt());
		CHGBOARDSTAT.add("PAYLOAD", CHGBOARDSTATpayload);
		
		GameMessage CHGBOARDSTATmessage = new GameMessage(game.getUUID(), null, "CHGBOARDSTAT", CHGBOARDSTAT);
		CHGBOARDSTATmessage.setBroadcast();
		return CHGBOARDSTATmessage;
	}
	
	/**
	 * allows to generate a CHGBOARDSTAT message, telling the client to flush his board
	 * 
	 * @param game the game object used to build this CHGBOARDSTAT message
	 * @param turnEndFlag if settet true, the client must flush his board
	 * @return the GameMessage representing this CHGBOARDSTAT message
	 */
	public static GameMessage buildCHGBOARDSTATmessage(Game game, boolean turnEndFlag){
		JsonObject CHGBOARDSTAT = new JsonObject();
		CHGBOARDSTAT.add("TYPE", "FLUSHFAMILY");
		JsonObject CHGBOARDSTATpayload = new JsonObject();
		CHGBOARDSTATpayload.add("TURNENDFLAG", turnEndFlag);
		CHGBOARDSTAT.add("PAYLOAD", CHGBOARDSTATpayload);
		
		GameMessage CHGBOARDSTATmessage = new GameMessage(game.getUUID(), null, "CHGBOARDSTAT", CHGBOARDSTAT);
		CHGBOARDSTATmessage.setBroadcast();
		return CHGBOARDSTATmessage;
	}
	
	/**
	 * allows to generate a CONTEXT message
	 * 
	 * @param game the game object used to build this CONTEXT message
	 * @param player the recipient of the message
	 * @param type the type of context to open
	 * @param payload the additional information used to customize the context
	 * @return the GameMessage representing this CONTEXT message
	 */
	public static GameMessage buildCONTEXTmessage(Game game, Player player, ContextType type, Object...payload){
		JsonObject CONTEXT = new JsonObject();
		CONTEXT.add("CONTEXTID", type.getContextID());	
		JsonObject CONTEXTpayload = new JsonObject();
		switch(type){
		case PRIVILEGE:
			int numberOfPrivilege = (int) payload[0];
			CONTEXTpayload.add("NUMBER", numberOfPrivilege);
			if(payload.length>1){ // privilege with cost
				JsonObject cost = (JsonObject) payload[1];
				CONTEXTpayload.add("COST", cost);
			}
			break;
		case SERVANT:
			CONTEXTpayload.add("NUMBER_SERVANTS", (int) payload[0]);
			CONTEXTpayload.add("ACTIONTYPE", (String) payload[1]);
			break;
		case EXCOMMUNICATION:
			CONTEXTpayload.add("FAITH_NEEDED", (int) payload[0]);
			CONTEXTpayload.add("PLAYER_FAITH", (int) payload[1]);
			CONTEXTpayload.add("PLAYERID", (String) player.getUUID().toString());
			break;
		case LEADERSET:
			CONTEXTpayload.add("LIST", (JsonArray) payload[0]);
			break;
		case CHANGE:
			CONTEXTpayload.add("NAME", (JsonArray) payload[0]);
			CONTEXTpayload.add("RESOURCE", (JsonArray) payload[1]);
			break;
		case ACTION:
			CONTEXTpayload.add("PAYLOAD", (JsonObject) payload[0]);
		}	
		CONTEXT.add("PAYLOAD", CONTEXTpayload);
		GameMessage CONTEXTmessage = new GameMessage(game.getUUID(), player.getUUID(), "CONTEXT", CONTEXT);
		if(type.equals(ContextType.EXCOMMUNICATION)){
			CONTEXTmessage.setBroadcast();
		}
		return CONTEXTmessage;			
	}
	
	/**
	 * allows to build a CONTEXTACK message
	 * 
	 * @param game the game object used to build this CONTEXTACK message
	 * @param player the recipient of the message
	 * @param accepted flag used to tell the client if the response followed by a CONTEXT is valid or not
	 * @return the GameMessage representing this CONTEXTACK message
	 */
	public static GameMessage buildCONTEXTACKMessage(Game game, Player player, boolean accepted) {
		JsonObject CONTEXTACK = new JsonObject();
		CONTEXTACK.add("ACCEPTED", Json.value(accepted));		
		return new GameMessage(game.getUUID(), player.getUUID(), "CONTEXTACK", CONTEXTACK); 
	} 
	
	
	/**
	 * allows to build a ACTCHK message
	 * 
	 * @param game the game object used to build this CONTEXTACK message
	 * @param player the recipient of the message
	 * @param action the action which has been applied
	 * @param result the result of the action, this flag tells the client if his action has been applied or not
	 * @return the GameMessage representing this ACTCHK message
	 */
	public static GameMessage buildACTCHKmessage(Game game, Player player, Action action, boolean result) {
		JsonObject payload = new JsonObject();
		if(result){
			payload.add("RESULT", true);
			payload.add("REGIONID", action.getRegionId());
			payload.add("SPACEID", action.getActionSpaceId());
			payload.add("BONUSACTION", action.getAdditionalInfo().get("BONUSFLAG").asBoolean());
			payload.add("ACTIONTYPE", action.getActionType());
		}
		else
			payload.add("RESULT", false);
		return new GameMessage(game.getUUID(), player.getUUID(), "ACTCHK", payload);
	} 
	
	/**
	 * allows to build a DECIROLL message
	 * 
	 * @param game the game object used to build this DICEROLL message
	 * @param blackDice the value of the black dice
	 * @param whiteDice the value of the white dice
	 * @param orangeDice the value of the orange dice
	 * @return the GameMessage representing this DICEROLL message
	 */
	public static GameMessage buildDICEROLLmessage(Game game, int blackDice, int whiteDice, int orangeDice){
		JsonObject DICEROLL = new JsonObject();
		DICEROLL.add("BLACKDICE", blackDice);
		DICEROLL.add("WHITEDICE",whiteDice);
		DICEROLL.add("ORANGEDICE", orangeDice);
		
		GameMessage DICEROLLmessage = new GameMessage(game.getUUID(), null, "DICEROLL", DICEROLL);
		DICEROLLmessage.setBroadcast();
		return DICEROLLmessage;
	}
	
	/**
	 * allows to build a TRNBGN message
	 * 
	 * @param game the game object used to build this TRNBGN message
	 * @param uuid the player which has the lock. i.e. the player which must perform the action
	 * @return the GameMessage representing this TRNBGN message
	 */
	public static GameMessage buildTRNBGNmessage(Game game, UUID uuid){
		JsonObject TRNBGN = new JsonObject();
		TRNBGN.add("PLAYERID", uuid.toString());
		GameMessage TRNBGNmessage = new GameMessage(game.getUUID(), null, "TRNBGN", TRNBGN);
		TRNBGNmessage.setBroadcast();
		return TRNBGNmessage;
	}
	
	/**
	 * allows to build a ENDGAME message
	 * 
	 * @param game the game object used to build this ENDGAME message
	 * @param ENDGAME the JsonObject containing all the info on the final score of the game
	 * @return the GameMessage representing this ENDGAME message
	 */
	public static GameMessage buildENDGAMEmessage(Game game, JsonObject ENDGAME){
		GameMessage ENDGAMEmessage = new GameMessage(game.getUUID(), null, "ENDGAME", ENDGAME);
		ENDGAMEmessage.setBroadcast();
		return ENDGAMEmessage;
	}
	/**
	 * allows to build a CONNEST message
	 * 
	 * @param playerId the player UUID of the client connected
	 * @return the GameMessage representing this CONNEST message
	 */
	public static GameMessage buildCONNESTmessage(UUID playerId){
		JsonObject CONNEST = new JsonObject();
		CONNEST.add("PLAYERID", playerId.toString());
		GameMessage CONNESTmessage = new GameMessage(null, playerId,"CONNEST", CONNEST);
		return CONNESTmessage;
	}
}
