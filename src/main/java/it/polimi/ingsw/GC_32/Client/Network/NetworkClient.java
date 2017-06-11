package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

import it.polimi.ingsw.GC_32.Common.Network.ClientMessageFactory;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;

public class NetworkClient{

	private MsgConnection network;
	private String myUUID;
	private String name  ="pippo";
	//private SlimPlayer I;
	private HashMap<String,SlimPlayer> players;
	private SlimBoard slimBoard;
	
	
	public NetworkClient(){
		network = new SocketMsgConnection();
		players = new HashMap<String, SlimPlayer>();
	}
	
	public MsgConnection getNetwork(){
		return this.network;
	}
	
	public static void main(String[] args) throws IOException{
		
		NetworkClient client = new NetworkClient();
		client.getNetwork().open();
		
			while(true){
				if(client.getNetwork().hasMessage()){
					JsonObject message = Json.parse(client.getNetwork().getMessage()).asObject();
					JsonObject messagePayload = Json.parse(message.get("PAYLOAD").asString()).asObject();
					
					switch(message.get("MESSAGETYPE").asString()){
					case "CONNEST":
						client.myUUID = messagePayload.get("PLAYERID").asString();
						client.players.put(client.myUUID, new SlimPlayer());
						// notifica il proprio nome
						client.getNetwork().sendMessage(ClientMessageFactory.buildCHGNAMEmessage(client.name));
						break;
					case "GMSTRT":
						JsonArray playerList = Json.parse(messagePayload.get("PLAYERLIST").asString()).asArray();
						playerList.forEach(player -> {
								client.players.put(player.asString(), new SlimPlayer());
						});
						System.out.println("[NETWORKCLIENT] added opponents to player list");
						JsonObject board = Json.parse(messagePayload.get("BOARD").asString()).asObject();

						System.out.println("[NETWORKCLIENT] synchronizing board");
						client.slimBoard = new SlimBoard(board);
						System.out.println("[NETWORKCLIENT] board correctly synchronized");
						System.out.println(client.slimBoard.toString());
						break;
					case "STATCHNG":
						String playerID = messagePayload.get("PLAYERID").asString();
						if(messagePayload.get("TYPE").asString().equals("RESOURCE")){
							JsonObject addingResources = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							client.players.get(playerID).addResources(new ResourceSet(addingResources));
							System.out.println("[NETWORKCLIENT] player "+playerID+" change resources");
							
							// ************************* ESEMPIO
							System.out.println(client.players.get(client.myUUID).toString());
						}else{
							JsonObject addingCard = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							Iterator<Member> iterable = addingCard.iterator();
							iterable.forEachRemaining(card -> client.players.get(playerID).addCard(card.getName(), card.getValue().asString()));
							System.out.println("[NETWORKCLIENT] add new card to "+playerID);
							
							// ************************* ESEMPIO
							System.out.println(client.players.get(client.myUUID).toString());
						}
						break;
						
				}
			}
			
		}
	}
	
}
