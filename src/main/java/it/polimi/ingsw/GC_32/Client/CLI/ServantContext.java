package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

public class ServantContext extends Context{

	public void open(Object object){
		JsonObject JsonPayload = (JsonObject) object;
		int numberOfServants = JsonPayload.get("NUMBER_SERVANTS").asInt();
		String actionType = JsonPayload.get("ACTIONTYPE").asString();
		
		JsonObject CONTEXTREPLY = new JsonObject();
		CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
		JsonObject CONTEXTREPLYpayload = new JsonObject();
		CONTEXTREPLYpayload.add("MESSAGETYPE", "SERVANT");
		
		System.out.println("you have "+numberOfServants+" servants to spend to increase your "+actionType+" action"
				+ "\nDo you want to spend any of these?? type 0 if you don't want spend any servant, "
				+ "else type the number of servants you want to spend");
		runFlag = true;
		while(runFlag){
			command = in.nextLine();
			try{
				if(Integer.parseInt(command)<=numberOfServants){
					CONTEXTREPLYpayload.add("CHOOSEN_SERVANTS", Integer.parseInt(command));
					
					CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);
					sendQueue.add(CONTEXTREPLY.toString());
					close();
				}else{
					System.out.println("type a valid quantity");
				}
			}catch(NumberFormatException e){
				System.out.println("type a valid number");
			}
		}
		
	}
	
}
