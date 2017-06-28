package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;

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
			case "yes":
				if(playerFaithPoints<faithPointsNeeded){
					System.out.println("Sorry, but unfortunately you don't have enough faith points.\n"
							+ "You should type: n\nI hope the excommunicate card won't be a serious problem for you...");
					break;
				}
				sendQueue.add(ClientMessageFactory.buldSENDPOPEmessage(gameUUID, playerUUID, false, faithPointsNeeded));
				close();
				break;
			case "no":
				System.out.println("The Pope is really angry with you.\nYOU HAVE BEEN EXCOMMUNICATED!");
				sendQueue.add(ClientMessageFactory.buldSENDPOPEmessage(gameUUID, playerUUID, true, faithPointsNeeded));
				close();
				break;
			default:
				System.out.println("type a valid command");
			}		
		}
	}
}
