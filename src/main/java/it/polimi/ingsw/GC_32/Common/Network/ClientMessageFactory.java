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


	
	
	
}
