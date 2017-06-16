package it.polimi.ingsw.GC_32.Client.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import it.polimi.ingsw.GC_32.Client.Network.MsgConnection;
import it.polimi.ingsw.GC_32.Client.Network.SocketMsgConnection;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ClientMessageFactory;

public class MainClient{

	private MsgConnection network;
	private ClientInterface graphicInterface;
	
	private HashMap<String,ClientPlayer> players;
	private ClientBoard clientBoard;
	
	private String myUUID;
	private String name = "pippo";
	
	public MainClient(){
		this.network = (MsgConnection) new SocketMsgConnection();
		this.players = new HashMap<String, ClientPlayer>();
	}
		
	public MsgConnection getNetwork(){
		return this.network;
	}
	
	public ClientBoard getBoard(){
		return this.clientBoard;
	}
	
	public HashMap<String,ClientPlayer> getPlayers(){
		return this.players;
	}
	
	private void setClientBoard(ClientBoard board){
		this.clientBoard = board;
		//graphicInterface.registerBoard(clientBoard);
	}
		
	public static void main(String[] args) throws IOException{
		
		MainClient client = new MainClient();
		MsgConnection network = client.getNetwork();
		network.open();
		
			while(true){
				if(network.hasMessage()){
					JsonObject message = Json.parse(network.getMessage()).asObject();
					JsonObject messagePayload = Json.parse(message.get("PAYLOAD").asString()).asObject();
					
					String playerID;
					
					switch(message.get("MESSAGETYPE").asString()){					
					case "NAMECHG":
						playerID = messagePayload.get("PLAYERID").asString();
						String name = messagePayload.get("NAME").asString();
						client.getPlayers().get(playerID).setName(name);
						System.out.println("[MAINCLIENT] player "+playerID+" changed his name to "+name);
						break;
					case "CONNEST":
						client.myUUID = messagePayload.get("PLAYERID").asString();
						client.getPlayers().put(client.myUUID, new ClientPlayer());
						// notifica il proprio nome
						network.sendMessage(ClientMessageFactory.buildCHGNAMEmessage(client.name));
						break;
					case "GMSTRT":
						JsonArray playerList = Json.parse(messagePayload.get("PLAYERLIST").asString()).asArray();
						playerList.forEach(player -> {
							client.getPlayers().put(player.asString(), new ClientPlayer());
						});
						System.out.println("[MAINCLIENT] added opponents to player list");
						JsonObject board = Json.parse(messagePayload.get("BOARD").asString()).asObject();

						System.out.println("[MAINCLIENT] synchronizing board");
						client.setClientBoard(new ClientBoard(board));
						System.out.println("[MAINCLIENT] board correctly synchronized");
						break;
					case "STATCHNG":
						playerID = messagePayload.get("PLAYERID").asString();
						if(messagePayload.get("TYPE").asString().equals("RESOURCE")){
							JsonObject addingResources = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							client.getPlayers().get(playerID).addResources(new ResourceSet(addingResources));
							System.out.println("[MAINCLIENT] player "+playerID+" change resources");
							
							// ************************* ESEMPIO
							System.out.println(client.getPlayers().get(client.myUUID).toString());
						}else{
							JsonObject addingCard = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							Iterator<Member> iterable = addingCard.iterator();
							iterable.forEachRemaining(card -> client.getPlayers().get(playerID).addCard(card.getName(), card.getValue().asString()));
							System.out.println("[MAINCLIENT] add new card to "+playerID);
							
							// ************************* ESEMPIO
							System.out.println(client.getPlayers().get(client.myUUID).toString());
						}
						break;
					case "CHGBOARDSTAT":
						// notifica cambiamento dell'intera board (quando si svuota la board e si inseriscono tutte le carte nuove)
						if(messagePayload.get("TYPE").asString().equals("BOARD")){
							JsonArray cardLayout = Json.parse(messagePayload.get("PAYLOAD").asString()).asArray();
							cardLayout.forEach(JSONcard -> {
								JsonObject card = JSONcard.asObject();
								int regionID = card.get("REGIONID").asInt();
								int spaceID = card.get("SPACEID").asInt();
								String cardName = card.get("NAME").asString();
								client.getBoard().getRegionList().get(regionID).getActionSpaceList().get(spaceID).setCard(cardName);
							});
							System.out.println(client.getBoard().toString());
						}
						break;
						
				}
			}
			
		}
	}
}
