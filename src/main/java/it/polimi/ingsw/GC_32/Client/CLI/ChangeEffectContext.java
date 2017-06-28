package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.ArrayList;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ChangeEffectContext extends Context{

	
	public void open(Object object) {
		
		runFlag = true;
		JsonObject JsonPayload = (JsonObject) object;
		
		String cardName = JsonPayload.get("NAME").asString();
		
		JsonArray resourceIn = JsonPayload.get("RESOURCEIN").asArray();
		JsonArray resourceOut = JsonPayload.get("RESOURCEOUT").asArray();
		
		System.out.println("you have a change from "+cardName+" card. Choose what change you want perform just typing the corresponding number, \n"
				+ "type 'n' if you don't want to apply the effect");
		
		for(int i=0; i<resourceIn.size(); i++){
			System.out.println("["+i+"]  "+resourceIn.get(i).asString()+" -> "+resourceOut.get(i).asString()+"\n");
		}
		
		JsonObject CONTEXTREPLY = new JsonObject();
		CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
		JsonObject CONTEXTREPLYpayload = new JsonObject();
		CONTEXTREPLYpayload.add("CONTEXT_TYPE", "CHANGE");
		JsonObject CONTEXTREPLYpayloadinfo = new JsonObject();
		CONTEXTREPLYpayload.add("PAYLOAD", CONTEXTREPLYpayloadinfo);
				
		while(runFlag){
				
				command = in.nextLine();
				while(!(Integer.parseInt(command)<=resourceIn.size()&&!command.equals("n"))){
					System.out.println("enter a valid index for this card, or type 'n' if you don't want to apply any changes");
					command = in.nextLine();
				}
				if(!command.equals("n"))
					CONTEXTREPLYpayloadinfo.add("CHANGEID", Integer.parseInt(command));
			
			
			CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);
			
			sendQueue.add(CONTEXTREPLY.toString());
			close();
		}
	}
}
