package it.polimi.ingsw.GC_32.Client.Network;

import com.eclipsesource.json.JsonObject;

public class ClientMessageFactory {

	public static String buildCHGNAMEmessage(String playerUUID, String name){
		JsonObject response = new JsonObject();
		JsonObject responsePayload = new JsonObject();
		response.add("MESSAGETYPE", "CHGNAME");
		responsePayload.add("NAME", name);
		responsePayload.add("PLAYERID", playerUUID);
		response.add("PAYLOAD", responsePayload);
		return response.toString();
	}
	
	public static String buildMSGmessage(String message){
		JsonObject MSG = new JsonObject();
		JsonObject MSGPayload = new JsonObject();
		MSG.add("MESSAGETYPE", "MSG");
		MSGPayload.add("MESSAGE", message);
		MSG.add("PAYLOAD", MSGPayload);
		return MSG.toString();
	}
	
	public static String buildASKACTmessage(String actionType, int pawnID, int regionID, int spaceID, int indexCost, String cardName){
		JsonObject ASKACT = new JsonObject();
		JsonObject ASKACTpayload = new JsonObject();
		ASKACTpayload.add("ACTIONTYPE", actionType);
		ASKACTpayload.add("FAMILYMEMBER_ID", pawnID);
		ASKACTpayload.add("REGIONID", regionID);
		ASKACTpayload.add("SPACEID", spaceID);
		ASKACTpayload.add("COSTINDEX", indexCost);
		ASKACTpayload.add("CARDNAME", cardName==null ? "" : cardName);
		ASKACT.add("MESSAGETYPE", "ASKACT");
		ASKACT.add("PAYLOAD", ASKACTpayload);
		return ASKACT.toString();
	}
	
	public static String buildTRNENDmessage(String gameUUID, String name){
		JsonObject TRNEND = new JsonObject();
		JsonObject TRNENDPayload = new JsonObject();
		TRNEND.add("MESSAGETYPE", "TRNEND");
		TRNENDPayload.add("NAME", name);
		TRNEND.add("PAYLOAD", TRNENDPayload);
		TRNEND.add("GameID", gameUUID);
		return TRNEND.toString();
	}
	
}
