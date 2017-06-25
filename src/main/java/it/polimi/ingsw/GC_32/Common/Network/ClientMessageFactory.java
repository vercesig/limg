package it.polimi.ingsw.GC_32.Common.Network;

import com.eclipsesource.json.JsonObject;

public class ClientMessageFactory {

	public static String buildCHGNAMEmessage(String name){
		JsonObject response = new JsonObject();
		JsonObject responsePayload = new JsonObject();
		response.add("MESSAGETYPE", "CHGNAME");
		responsePayload.add("NAME", name);
		response.add("PAYLOAD", responsePayload);
		return response.toString();
	}
	
	public static String buildASKACTmessage(String actionType, int pawnID, int regionID, int spaceID){
		JsonObject ASKACT = new JsonObject();
		JsonObject ASKACTpayload = new JsonObject();
		ASKACTpayload.add("ACTIONTYPE", actionType);
		ASKACTpayload.add("FAMILYMEMBER_ID", pawnID);
		ASKACTpayload.add("REGIONID", regionID);
		ASKACTpayload.add("SPACEID", spaceID);
		ASKACT.add("MESSAGETYPE", "ASKACT");
		ASKACT.add("PAYLOAD", ASKACTpayload);
		return ASKACT.toString();
	}
	
	
	
}
