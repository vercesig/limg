package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;

public class ExcommunicationContext extends Context{

	public ExcommunicationContext(ClientCLI client){
		super(client);
	}
	
	public String open(Object object){
			
		JsonObject JsonPayload = (JsonObject) object;
		int playerFaithPoints = JsonPayload.get("PLAYER_FAITH").asInt();
		int faithPointsNeeded = JsonPayload.get("FAITH_NEEDED").asInt();
		
		System.out.println("\n >You have "+playerFaithPoints+ " faith points.\nFor this period you need to spend "+faithPointsNeeded+" to support the Church.\n"
				+ "you can choose if support it, and spend all your faith points, or to not support it. In this case you whill suffer the excommunication"
				+ " of this period.\n");
		
		while(true){
			System.out.println("Type 'y' if you want to support the church, 'n' if you don't want to support it");			
			command = in.nextLine();
			switch(command){
			case "y":
				if(playerFaithPoints<faithPointsNeeded){
					System.out.println("\n>Sorry, but unfortunately you don't have enough faith points.\n"
							+ "You should type: n\nI hope the excommunicate card won't be a serious problem for you...");
					return ClientMessageFactory.buldSENDPOPEmessage(client.getGameUUID(), client.getPlayerUUID(), true, faithPointsNeeded);
				}
				System.out.println("\n>The Pope is really happy with you.\n May God bless you!\n");
				return ClientMessageFactory.buldSENDPOPEmessage(client.getGameUUID(), client.getPlayerUUID(), false, faithPointsNeeded);
			case "n":
				System.out.println("\n>The Pope is really angry with you.\n\nYOU HAVE BEEN EXCOMMUNICATED!!!");
				return ClientMessageFactory.buldSENDPOPEmessage(client.getGameUUID(), client.getPlayerUUID(), true, faithPointsNeeded);
			default:
				System.out.println("type a valid command");
				break;
			}		
		}	
	}
}
