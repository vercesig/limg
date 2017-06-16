package it.polimi.ingsw.GC_32.Client.Controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import it.polimi.ingsw.GC_32.Client.Network.MsgConnection;
import it.polimi.ingsw.GC_32.Client.Network.NetworkClient;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ClientMessageFactory;

public class MainClient implements ClientInterface{

	private NetworkClient networkClient;
	private String myUUID;
	private String name;
	
	public MainClient(String name){
		networkClient = new NetworkClient();
		this.name = name;
	}
		
	public NetworkClient getNetwork(){
		return this.networkClient;
	}
	
	public static void main(String[] args) throws IOException{
		
		MainClient client = new MainClient("GIANNI");
		MsgConnection network = client.getNetwork().getConnection();
		network.open();
		
			while(true){
				if(network.hasMessage()){
					JsonObject message = Json.parse(network.getMessage()).asObject();
					JsonObject messagePayload = Json.parse(message.get("PAYLOAD").asString()).asObject();
					
					switch(message.get("MESSAGETYPE").asString()){
					case "CONNEST":
						client.myUUID = messagePayload.get("PLAYERID").asString();
						client.networkClient.getPlayers().put(client.myUUID, new ClientPlayer());
						// notifica il proprio nome
						network.sendMessage(ClientMessageFactory.buildCHGNAMEmessage(client.name));
						break;
					case "GMSTRT":
						JsonArray playerList = Json.parse(messagePayload.get("PLAYERLIST").asString()).asArray();
						playerList.forEach(player -> {
							client.networkClient.getPlayers().put(player.asString(), new ClientPlayer());
						});
						System.out.println("[NETWORKCLIENT] added opponents to player list");
						JsonObject board = Json.parse(messagePayload.get("BOARD").asString()).asObject();

						System.out.println("[NETWORKCLIENT] synchronizing board");
						client.networkClient.setClientBoard(new ClientBoard(board));
						System.out.println("[NETWORKCLIENT] board correctly synchronized");
						break;
					case "STATCHNG":
						String playerID = messagePayload.get("PLAYERID").asString();
						if(messagePayload.get("TYPE").asString().equals("RESOURCE")){
							JsonObject addingResources = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							client.networkClient.getPlayers().get(playerID).addResources(new ResourceSet(addingResources));
							System.out.println("[NETWORKCLIENT] player "+playerID+" change resources");
							
							// ************************* ESEMPIO
							System.out.println(client.networkClient.getPlayers().get(client.myUUID).toString());
						}else{
							JsonObject addingCard = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							Iterator<Member> iterable = addingCard.iterator();
							iterable.forEachRemaining(card -> client.networkClient.getPlayers().get(playerID).addCard(card.getName(), card.getValue().asString()));
							System.out.println("[NETWORKCLIENT] add new card to "+playerID);
							
							// ************************* ESEMPIO
							System.out.println(client.networkClient.getPlayers().get(client.myUUID).toString());
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
								client.networkClient.getClientBoard().getRegionList().get(regionID).getActionSpaceList().get(spaceID).setCard(cardName);
							});
							System.out.println(client.networkClient.getClientBoard().toString());
						}
						break;
						
				}
			}
			
		}
	}

	@Override
	public int getScreenId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void openScreen(int screenId, String additionalData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerContextPayloadQueue(ConcurrentLinkedQueue<Object> queue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveMessage(String playerID, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTowerCards(int towerID, String[] cardArray) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTurnOrder(String[] playerIDs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDiceValue(int blackDice, int whiteDice, int orangeDice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableSpace(int regionID, int spaceID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableSpace(int regionID, int spaceID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveFamiliar(int familiar, int regionID, int spaceID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveCardToPlayer(String playerID, int regionID, int spaceID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTrackValue(String playerID, int trackID, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrentPlayer(String playerID) {
		// TODO Auto-generated method stub
		
	}
}
