package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/**
 * ExcommunicationContext handles the excommunication phase, i.e. the phase during which the clients must decide if support the church or not. 
 * Context ends with the sending of a CONTEXTREPLY message.
 * 
 * @see Context
 *
 */

public class ExcommunicationContext extends Context{

	public ExcommunicationContext(ClientCLI client){
		super(client);
	}
	
	public String open(Object object){
		
		runFlag = true;
		
		JsonObject JsonPayload = (JsonObject) object;
		int playerFaithPoints = JsonPayload.get("PLAYER_FAITH").asInt();
		int faithPointsNeeded = JsonPayload.get("FAITH_NEEDED").asInt();
		String playerID = JsonPayload.get("PLAYERID").asString();
		
		JsonObject CONTEXTREPLY = new JsonObject();
		CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
		JsonObject CONTEXTREPLYpayload = new JsonObject();
		CONTEXTREPLYpayload.add("CONTEXT_TYPE", "EXCOMMUNICATION");
		JsonObject CONTEXTREPLYpayloadinfo = new JsonObject();
		CONTEXTREPLYpayload.add("PAYLOAD", CONTEXTREPLYpayloadinfo);
		
		CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);
		CONTEXTREPLYpayloadinfo.add("FAITH_NEEDED", faithPointsNeeded);
		CONTEXTREPLYpayloadinfo.add("PLAYERID", client.getPlayerUUID());
		
		
		if(playerID.equals(client.getPlayerUUID())){
			client.setIdleRun(true);
			out.println("=============================== EXCOMMUNICATION PHASE ==============================="
					+ "\n >You have "+playerFaithPoints+ " faith points.\nFor this period you need to spend "+faithPointsNeeded+" to support the Church.\n"
					+ "you can choose if support it, and spend all your faith points, or to not support it. In this case you whill suffer the excommunication"
					+ " of this period.\nType 'y' if you want to support the church, 'n' if you don't want to support it");
			
			while(true){
				command = in.nextLine();
				switch(command){
    				case "y":
    					if(playerFaithPoints<faithPointsNeeded){
    						out.println(">Sorry, but unfortunately you don't have enough faith points.\n"
    								+ "I hope the excommunication card won't be a serious problem for you...");
    						CONTEXTREPLYpayloadinfo.add("ANSWER", Json.value(true));
    					}else{
    						out.println(">The Pope is really happy with you.\n May God bless you!\n");
    						CONTEXTREPLYpayloadinfo.add("ANSWER", Json.value(false));
    					}
    					System.out.println(CONTEXTREPLY.toString());
    					return CONTEXTREPLY.toString();
    				case "n":
    					out.println(">The Pope is really angry with you.\n\nYOU HAVE BEEN EXCOMMUNICATED!!!");
    					CONTEXTREPLYpayloadinfo.add("ANSWER", Json.value(true));
    					return CONTEXTREPLY.toString();
    				default:
    					out.println("type a valid command");
    					break;
				}		
			}
		}
		else{
			client.setIdleRun(true);
			out.println("=============================== EXCOMMUNICATION PHASE ===============================\n"
					+ "excommunication phase ... waiting "+client.getPlayerList().get(playerID).getName());
			return "";
		}
	}
}