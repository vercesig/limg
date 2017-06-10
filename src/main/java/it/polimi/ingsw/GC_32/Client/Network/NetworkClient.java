package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;

public class NetworkClient{

	private MsgConnection network;
	private String myUUID;
	private String name  ="pippo";
	private SlimPlayer I;
	private HashMap<String,SlimPlayer> players;
	
	
	public NetworkClient(){
		network = new SocketMsgConnection();
		players = new HashMap<String, SlimPlayer>();
		I = new SlimPlayer();
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
						JsonObject response = new JsonObject();
						JsonObject responsePayload = new JsonObject();
						response.add("MESSAGETYPE", "CHGNAME");
						responsePayload.add("NAME", client.name);
						response.add("PAYLOAD", responsePayload);
						client.getNetwork().sendMessage(response.toString());
						break;
					case "GMSTRT":
						JsonArray opponents = Json.parse(messagePayload.get("PLAYERLIST").asString()).asArray();
						opponents.forEach(opponent -> {
							if(!opponent.asString().equals(client.myUUID))
								client.players.put(opponent.asString(), new SlimPlayer());
						});
						System.out.println("[NETWORKCLIENT] added opponents to player list");
						break;
					case "STATCHNG":
						if(messagePayload.get("TYPE").asString().equals("RESOURCE")){
							JsonObject startResources = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							client.I.addResources(new ResourceSet(startResources));
							System.out.println("[NETWORKCLIENT] change resources");
							
							// ************************* ESEMPIO
							System.out.println(client.I.toString());
						}else{
							System.out.println("[NETWORKCLIENT] add new card");
							JsonObject startResources = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							Iterator<Member> iterable = startResources.iterator();
							iterable.forEachRemaining(card -> client.I.addCard(card.getName(), card.getValue().asString()));
							
							// ************************* ESEMPIO
							System.out.println(client.I.toString());
						}
						break;
						
				}
			}
			
		}
	}
	
}
