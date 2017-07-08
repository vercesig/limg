package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonObject;

public class ServantContext extends Context{

	public ServantContext(ClientCLI client){
		super(client);
	}
	
	public String open(Object object){
		JsonObject JsonPayload = (JsonObject) object;
		int numberOfServants = JsonPayload.get("NUMBER_SERVANTS").asInt();
		String actionType = JsonPayload.get("ACTIONTYPE").asString();
		
		JsonObject CONTEXTREPLY = new JsonObject();
		CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
		JsonObject CONTEXTREPLYpayload = new JsonObject();
		JsonObject CONTEXTREPLYpayloadinfo = new JsonObject();
		CONTEXTREPLYpayload.add("PAYLOAD", CONTEXTREPLYpayloadinfo);
		CONTEXTREPLYpayload.add("CONTEXT_TYPE", "SERVANT");
		
		out.println("you have "+numberOfServants+" servants to spend to increase your "+actionType+" action"
				+ "\nDo you want to spend any of these?? type 0 if you don't want spend any servant, "
				+ "else type the number of servants you want to spend");
		
		while(true){
			command = in.nextLine();
			try{
				if(Integer.parseInt(command)<=numberOfServants){
					CONTEXTREPLYpayloadinfo.add("CHOSEN_SERVANTS", Integer.parseInt(command));
					CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);
					return CONTEXTREPLY.toString();
				}else{
					out.println("type a valid quantity");
				}
			}catch(NumberFormatException e){
				out.println("type a valid number");
			}
		}
	}
	
}
