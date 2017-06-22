package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

public class ServantContext extends Context{

	public void open(Object object){
		JsonObject JsonPayload = (JsonObject) object;
		int numberOfServants = JsonPayload.get("NUMBER_SERVANTS").asInt();
		String actionType = JsonPayload.get("ACTIONTYPE").asString();
		
		JsonObject response = new JsonObject();
		
		System.out.println("you have "+numberOfServants+" servants to spend to increase your "+actionType+" action"
				+ "\nDo you want to spend any of these?? type 0 if you don't want spend any servant, "
				+ "else type the number of servants you want to spend");
		runFlag = true;
		while(runFlag){
			command = in.nextLine();
			if(Integer.parseInt(command)<=numberOfServants){
				response.add("CHOOSEN_SERVANTS", Integer.parseInt(command));
				sendQueue.add(response.toString());
				close();
			}else{
				System.out.println("type a valid quantity");
			}
		}
		
	}
	
}
