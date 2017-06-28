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
		
		System.out.println("\n >You have "+playerFaithPoints+ " faith points.\nFor this period "+faithPointsNeeded+" are required to support the Church.\n"
				+ "you can choose if support it, and spend all your faith points, or to not support it. In this case you whill suffer the excommunication"
				+ " of this period.\nType 'y' if you want to support the church, 'n' if you don't want to support it");
		
		while(runFlag){
			command = in.nextLine();
			switch(command){
			case "y":
				if(playerFaithPoints<faithPointsNeeded){
					System.out.println("\n>Sorry, but unfortunately you don't have enough faith points.\n"
							+ "You should type: n\nI hope the excommunicate card won't be a serious problem for you...");
					break;
				}
				System.out.println("\n>The Pope is really happy with you.\n May God bless you!\n");
				sendQueue.add(ClientMessageFactory.buldSENDPOPEmessage(gameUUID, playerUUID, false, faithPointsNeeded));
				runFlag = false;
				break;
			case "n":
				System.out.println("\n>The Pope is really angry with you.\n\nYOU HAVE BEEN EXCOMMUNICATED!!!");
				sendQueue.add(ClientMessageFactory.buldSENDPOPEmessage(gameUUID, playerUUID, true, faithPointsNeeded));
				runFlag = false;
				break;
			default:
				System.out.println("type a valid command");
				break;
			}		
		}
	}
}
