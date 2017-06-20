package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonObject;

public class ExcommunicationContext extends Context{

	public void open(Object object){
		
		runFlag = true;
		
		JsonObject JsonPayload = (JsonObject) object;
		int playerFaithPoints = JsonPayload.get("PLAYER_FAITH").asInt();
		int faithPointsNeeded = JsonPayload.get("FAITH_NEEDED").asInt();
		
		JsonObject response = new JsonObject();
		
		System.out.println("you have "+playerFaithPoints+".\nFor this period "+faithPointsNeeded+" are required to support the Church.\n"
				+ "you can choose if support it, and spend all your faith points, or to not support it. In this case you whill suffer the excommunication"
				+ " of this period.\nType 'y' if you want to support the church, 'n' if you don't want to support it");
		
		while(runFlag){
			command = in.nextLine();
			switch(command){
			case "y":
				response.add("APPLYEXCOMMUNICATION", false);
				sendQueue.add(response.toString());
				close();
				break;
			case "n":
				response.add("APPLYEXCOMMUNICATION", true);
				sendQueue.add(response.toString());
				close();
				break;
			default:
				System.out.println("type a valid command");
			}
			
		}
		
		
	}
	
}
