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
import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;
import it.polimi.ingsw.GC_32.Client.Network.MsgConnection;
import it.polimi.ingsw.GC_32.Client.Network.SocketMsgConnection;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class MainClient{

	private MsgConnection network;
	private ClientInterface graphicInterface;
	private ConcurrentLinkedQueue<String> sendQueue;
	
	private HashMap<String,ClientPlayer> players;
	private ClientBoard clientBoard;
	
	private String myUUID;
	private String gameUUID;
	
	// timer management
	private int ACTIONTIMEOUT = 30000;
	private long startTimeout = 0;
	private boolean actionRunningFlag = false;
	
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
				
				if(client.startTimeout + client.ACTIONTIMEOUT < System.currentTimeMillis()&&client.actionRunningFlag){
					System.out.println("[!] YOU HAVE BEEN DISCONETTED FROM THE SERVER!");
					client.actionRunningFlag=false;
				}
				
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
						client.getPlayers().get(client.myUUID).setName(myName);
						
						client.graphicInterface.registerUUID(client.getUUID());
						client.graphicInterface.registerSendMessageQueue(client.getSendQueue());
						break;
					case "GMSTRT":
						JsonArray playerList = Json.parse(messagePayload.get("PLAYERLIST").asString()).asArray();
						// registrazione gameUUID
						client.gameUUID = messagePayload.get("GAMEUUID").asString();
						client.getClientInterface().registerGameUUID(client.gameUUID);
						
						network.sendMessage(ClientMessageFactory.buildCHGNAMEmessage(client.gameUUID, client.getPlayers().get(client.getUUID()).getName()));
						
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
						
						client.graphicInterface.unlockZone(client.getPlayers().size());
						client.graphicInterface.openContext(0, null);
						//System.out.println("[MAINCLIENT] board correctly synchronized");
						
						Thread clientInterfaceThread = new Thread(client.getClientInterface());
						clientInterfaceThread.start();
						
						break;
					case "STATCHNG":
						playerID = messagePayload.get("PLAYERID").asString();
 						JsonObject newResources = Json.parse(messagePayload.get("RESOURCE").asString()).asObject();
 						client.getPlayers().get(playerID).getPlayerResources().replaceResourceSet(new ResourceSet(newResources));	
												
 						JsonObject cardList = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
 						Iterator<Member> cardListIterator = cardList.iterator();
 						cardListIterator.forEachRemaining(cards -> {
 							JsonArray cardListArray = cards.getValue().asArray();
 							if(!cardListArray.isNull())
 								cardListArray.forEach(card -> client.getPlayers().get(playerID).addCard(cards.getName(), card.asString()));
 						});
						client.getPlayers().get(playerID).setPersonalBonusTile(messagePayload.get("BONUSTILE").asString());						
						client.graphicInterface.setTrackValue(playerID, 0);
						client.graphicInterface.setTrackValue(playerID, 1);
						client.graphicInterface.setTrackValue(playerID, 2);
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
								client.graphicInterface.setTowerCards(regionID, spaceID, cardName);
							});
							//System.out.println(client.getBoard().toString());
						}
			 			break;
					case "DICEROLL":
						int blackDice = messagePayload.get("BLACKDICE").asInt();
						int whiteDice = messagePayload.get("WHITEDICE").asInt();
						int orangeDice = messagePayload.get("ORANGEDICE").asInt();
						client.graphicInterface.setDiceValue(blackDice, whiteDice, orangeDice);		
						break;
					case "TRNBGN":
						String playerUUID = messagePayload.get("PLAYERID").asString();
						if(playerUUID.equals(client.getUUID())){
							// timer inizialization
							client.startTimeout = System.currentTimeMillis();
							client.actionRunningFlag=true;
							
							client.graphicInterface.waitTurn(false);
							client.getClientInterface().displayMessage("your turn is start, make an action");
							client.getClientInterface().displayMessage("> type ENTER to update your state");
							
						}
						else{
							client.graphicInterface.waitTurn(true);
							client.getClientInterface().displayMessage("now is "+client.getPlayers().get(playerUUID).getName()+"'s turn");
						}						
						break;
						
					case "ACTCHK":
						boolean result = messagePayload.get("RESULT").asBoolean();
						if(!result){
							client.graphicInterface.displayMessage("> THE ACTION IS NOT VALID!\n "
												+ "please type a valid action.");
							break;
						}
						else{
							client.graphicInterface.displayMessage("> THE ACTION IS VALID!\n");
							int regionId = messagePayload.get("REGIONID").asInt();
							int spaceId = messagePayload.get("SPACEID").asInt();
							int indexFamily = messagePayload.get("FAMILYMEMBER_ID").asInt();
							String actionType = messagePayload.get("ACTIONTYPE").asString();
							// update state
							client.graphicInterface.moveCardToPlayer(client.getUUID(), regionId, spaceId);
							client.graphicInterface.moveFamiliar(indexFamily, regionId, spaceId);
							client.graphicInterface.setTrackValue(client.getUUID(), 0);
							client.graphicInterface.setTrackValue(client.getUUID(), 1);
							client.graphicInterface.setTrackValue(client.getUUID(), 2);
							client.graphicInterface.waitTurn(true);
							
							network.sendMessage(ClientMessageFactory.buildTRNENDmessage(client.gameUUID, client.getPlayers().get(client.getUUID()).getName()));
							break;
						}	
					case "CONTEXT":
						client.getClientInterface().openContext(messagePayload);
						break;
				}
			}
		}
	}
}
