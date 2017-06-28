package it.polimi.ingsw.GC_32.Client.Network;

import com.eclipsesource.json.JsonObject;

public class ClientMessageFactory {

	public static String buildCHGNAMEmessage(String gameUUID, String name){
		JsonObject response = new JsonObject();
		JsonObject responsePayload = new JsonObject();
		response.add("GameID", gameUUID);
		response.add("MESSAGETYPE", "CHGNAME");
		responsePayload.add("NAME", name);
		response.add("PAYLOAD", responsePayload);
		return response.toString();
	}
	
	public static String buildASKACTmessage(String gameUUID, String actionType, int pawnID, int regionID, int spaceID, int indexCost, String cardName){
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
		ASKACT.add("GameID", gameUUID);
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
