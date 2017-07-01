package it.polimi.ingsw.GC_32.Client.Network;

import com.eclipsesource.json.JsonObject;

public class ClientMessageFactory {

	public static String buildCHGNAMEmessage(String playerUUID, String name){
		JsonObject CHGNAME = new JsonObject();
		JsonObject CHGNAMEPayload = new JsonObject();
		CHGNAME.add("MESSAGETYPE", "CHGNAME");
		CHGNAMEPayload.add("NAME", name);
		CHGNAMEPayload.add("PLAYERID", playerUUID);
		CHGNAME.add("PAYLOAD", CHGNAMEPayload);
		return CHGNAME.toString();
	}
	
	public static String buildMSGmessage(String playerUUID, String message, String destination, boolean allFlag){
		JsonObject MSG = new JsonObject();
		JsonObject MSGPayload = new JsonObject();
		MSG.add("MESSAGETYPE", "MSG");
		MSGPayload.add("FLAG", allFlag);
		MSGPayload.add("RECEIVER", destination);
		MSGPayload.add("SENDER", playerUUID);
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
	
	// only for ACTION effect
	public static String buildASKACTmessage(String actionType, int regionID, int spaceID, JsonObject payload){
		JsonObject ASKACT = new JsonObject();
		JsonObject ASKACTpayload = new JsonObject();
		ASKACTpayload.add("ACTIONTYPE", actionType);
		ASKACTpayload.add("REGIONID", regionID);
		ASKACTpayload.add("SPACEID", spaceID);
		ASKACTpayload.add("JSONPAYLOAD", payload);
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
	
	public static String buldSENDPOPEmessage(String gameUUID, String name, boolean answer, int faith){
		JsonObject SENDPOPE = new JsonObject();
		JsonObject SENDPOPEPayload = new JsonObject();
		SENDPOPEPayload.add("ANSWER", answer);
		SENDPOPEPayload.add("FAITH_NEEDED", faith);
		SENDPOPEPayload.add("PLAYERID", name);
		SENDPOPE.add("MESSAGETYPE", "SENDPOPE");
		SENDPOPE.add("PAYLOAD", SENDPOPEPayload);
		SENDPOPE.add("GameID", gameUUID);
		return SENDPOPE.toString();
	}
}
