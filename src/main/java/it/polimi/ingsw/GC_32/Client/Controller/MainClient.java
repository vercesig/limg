package it.polimi.ingsw.GC_32.Client.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.CLI.ClientCLI;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import it.polimi.ingsw.GC_32.Client.Network.MsgConnection;
import it.polimi.ingsw.GC_32.Client.Network.SocketMsgConnection;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ClientMessageFactory;

public class MainClient{

	private MsgConnection network;
	private ClientInterface graphicInterface;
	private ConcurrentLinkedQueue<String> sendQueue;
	
	private HashMap<String,ClientPlayer> players;
	private ClientBoard clientBoard;
	
	private String myUUID;
	
	public MainClient(){
		this.players = new HashMap<String, ClientPlayer>();		
		this.sendQueue = new ConcurrentLinkedQueue<String>();
	}
		
	public MsgConnection getNetwork(){
		return this.network;
	}
	
	public ClientInterface getClientInterface(){
		return this.graphicInterface;
	}
	
	public ClientBoard getBoard(){
		return this.clientBoard;
	}
	
	public HashMap<String,ClientPlayer> getPlayers(){
		return this.players;
	}
	
	public String getUUID(){
		return this.myUUID;
	}
	
	private boolean setNetwork(String type){
		switch(type){
		case "s":
			this.network = (MsgConnection) new SocketMsgConnection();
			return true;
		case "r":
			// RMI
			return true;
		default:
			return false;
		}
	}
	
	private boolean setClientInterface(String type){
		switch(type){
		case "c":
			this.graphicInterface = new ClientCLI();
			return true;
		case "g":
			// javaFX
			return true;
		default: return false;
		
		}
	}
	
	private void setClientBoard(ClientBoard board){
		this.clientBoard = board;
	}
	
	private ConcurrentLinkedQueue<String> getSendQueue(){
		return this.sendQueue;
	}
		
	public static void main(String[] args) throws IOException{
		
		MainClient client = new MainClient();
		Scanner in = new Scanner(System.in);
		
		
		System.out.println("welcome in LORENZO IL MAGNIFICO\n");
		String clientInterfaceType = "";
		while(!client.setClientInterface(clientInterfaceType)){
			System.out.println("before start, choose the graphic interface you want use, type 'c' for Command Line Interface, type 'g' for Graphical User Interface");
			clientInterfaceType = in.nextLine();
		}
		System.out.println("please enter your name");
		String myName = in.nextLine();
		String networkType = "";
		while(!client.setNetwork(networkType)){
			System.out.println(myName+" choose the network tecnology you want use: type 's' for SOCKET connection, 'r' for RMI connection");
			networkType = in.nextLine();
		}
		
		MsgConnection network = client.getNetwork();
		network.open();
		
		System.out.println("ok, now we are ready to play");		
				
			while(true){
							
				if(!client.getSendQueue().isEmpty()){
					client.network.sendMessage(client.getSendQueue().poll());
				}	
				
				// elabora messaggi in entrata
				if(network.hasMessage()){
					JsonObject message = Json.parse(network.getMessage()).asObject();
					JsonObject messagePayload = Json.parse(message.get("PAYLOAD").asString()).asObject();
					
					String playerID;
					
					switch(message.get("MESSAGETYPE").asString()){					
					case "NAMECHG":
						playerID = messagePayload.get("PLAYERID").asString();
						String name = messagePayload.get("NAME").asString();
						client.getPlayers().get(playerID).setName(name);
						//System.out.println("[MAINCLIENT] player "+playerID+" changed his name to "+name);
						break;
					case "CONNEST":
						client.myUUID = messagePayload.get("PLAYERID").asString();
						client.getPlayers().put(client.myUUID, new ClientPlayer());
						
						client.getPlayers().get(client.getUUID()).setName(myName);
						network.sendMessage(ClientMessageFactory.buildCHGNAMEmessage(client.getPlayers().get(client.getUUID()).getName()));
						
						client.graphicInterface.registerUUID(client.getUUID());
						client.graphicInterface.registerSendMessageQueue(client.getSendQueue());
						break;
					case "GMSTRT":
						JsonArray playerList = Json.parse(messagePayload.get("PLAYERLIST").asString()).asArray();
						playerList.forEach(player -> {
							client.getPlayers().put(player.asString(), new ClientPlayer());
						});
						//System.out.println("[MAINCLIENT] added opponents to player list");
						JsonObject board = Json.parse(messagePayload.get("BOARD").asString()).asObject();

						//System.out.println("[MAINCLIENT] synchronizing board");
						client.setClientBoard(new ClientBoard(board));
						
						client.graphicInterface.registerBoard(client.getBoard());
						client.graphicInterface.registerPlayers(client.getPlayers());
						client.graphicInterface.displayMessage("game start, "+client.getPlayers().size()+" players connected");
						
						client.graphicInterface.openContext(0, null);
						//System.out.println("[MAINCLIENT] board correctly synchronized");
						
						Thread clientInterfaceThread = new Thread(client.getClientInterface());
						clientInterfaceThread.start();
						
						break;
					case "STATCHNG":
						playerID = messagePayload.get("PLAYERID").asString();
						if(messagePayload.get("TYPE").asString().equals("RESOURCE")){
							JsonObject newResources = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							client.getPlayers().get(playerID).getPlayerResources().replaceResourceSet(new ResourceSet(newResources));
							System.out.println("[MAINCLIENT] player "+playerID+" change resources");
							
							// ************************* ESEMPIO
							//System.out.println(client.getPlayers().get(client.myUUID).toString());
						}else{
							JsonObject addingCard = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
							Iterator<Member> iterable = addingCard.iterator();
							iterable.forEachRemaining(card -> client.getPlayers().get(playerID).addCard(card.getName(), card.getValue().asString()));
							//System.out.println("[MAINCLIENT] add new card to "+playerID);
							
							// ************************ ESEMPIO
							//System.out.println(client.getPlayers().get(client.myUUID).toString());
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
							//System.out.println(client.getBoard().toString());
						}
						break;
					case "DICEROLL":
						int blackDice = messagePayload.get("BLACKDICE").asInt();
						int whiteDice = messagePayload.get("WHITEDICE").asInt();
						int orangeDice = messagePayload.get("ORANGEDICE").asInt();
						client.getBoard().setDiceValue(blackDice, whiteDice, orangeDice);
						client.getPlayers().forEach((UUID,player)->{
							player.getFamilyMembers()[1].setActionValue(blackDice);
							player.getFamilyMembers()[2].setActionValue(whiteDice);
							player.getFamilyMembers()[3].setActionValue(orangeDice);
						});
						//System.out.println("[MAINCLIENT] set dice value to ["+blackDice+","+whiteDice+","+orangeDice+"]");
						break;
					case "TRNBGN":
						String playerUUID = messagePayload.get("PLAYERID").asString();
						if(playerUUID.equals(client.getUUID())){
							client.getClientInterface().displayMessage("your turn is start, make an action");
						}
						else{
							client.getClientInterface().displayMessage("now is "+client.getPlayers().get(playerUUID).getName()+"'s turn");
						}
						break;
					case "CONTEXT":
						client.getClientInterface().openContext(messagePayload);
						break;
				}
			}
		}
	}
}
