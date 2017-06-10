package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

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
					System.out.println("[NETWORKCLIENT] recived message from server");
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
						
				}
			}
			
		}
	}
	
}
