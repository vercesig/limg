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
import it.polimi.ingsw.GC_32.Client.Network.SocketMsgConnection;
import it.polimi.ingsw.GC_32.Client.Network.RMIMsgConnection;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.MsgConnection;

public class MainClient{

	private MsgConnection network;
	private ClientInterface graphicInterface;
	private ConcurrentLinkedQueue<String> sendQueue;
	
	private HashMap<String,ClientPlayer> players;
	private ClientBoard clientBoard;
	
	private String myUUID;
	private String gameUUID;
	
	// timer management
	private int ACTIONTIMEOUT = 60000;
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
	
	private boolean setNetwork(String type) throws IOException{
		switch(type){
		case "s":
			this.network = new SocketMsgConnection();
			this.network.open("localhost", 9500);
			return true;
		case "r":
			this.network = new RMIMsgConnection();
			this.network.open("localhost", 1099);
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
		
		System.out.println("ok, now we are ready to play");		
				
			while(true){
				
				if(client.startTimeout + client.ACTIONTIMEOUT < System.currentTimeMillis()&&client.actionRunningFlag){
					System.out.println("[!] YOU HAVE BEEN DISCONETTED FROM THE SERVER!");
					client.getSendQueue().add(ClientMessageFactory.buildTRNENDmessage(client.gameUUID, client.getPlayers().get(client.getUUID()).getName()));
					client.actionRunningFlag=false;
				}
				
				if(!client.getSendQueue().isEmpty()){
					String message = client.getSendQueue().poll();				
					JsonObject JsonMessage = Json.parse(message).asObject();					
					JsonMessage.add("GameID", client.gameUUID);
					
					if(JsonMessage.get("MESSAGETYPE").asString().equals("ASKACT")){
						client.actionRunningFlag=false;
					}
					
					client.network.sendMessage(JsonMessage.toString());
				}	
				
				// elabora messaggi in entrata
				if(network.hasMessage()){
					JsonObject message = Json.parse(network.getMessage()).asObject();
					JsonObject messagePayload = Json.parse(message.get("PAYLOAD").asString()).asObject();
					String playerID;

					switch(message.get("MESSAGETYPE").asString()){					
					case "CHGNAME":
						playerID = messagePayload.get("PLAYERID").asString();
						String name = messagePayload.get("NAME").asString();
						client.getPlayers().get(playerID).setName(name);
						break;
					case "CONNEST":
						client.myUUID = messagePayload.get("PLAYERID").asString();

						client.getPlayers().put(client.myUUID, new ClientPlayer());
						client.getPlayers().get(client.myUUID).setName(myName);
						
						client.graphicInterface.registerPlayerUUID(client.getUUID());
						client.graphicInterface.registerSendMessageQueue(client.getSendQueue());
						break;
					case "GMSTRT":
						JsonArray playerList = Json.parse(messagePayload.get("PLAYERLIST").asString()).asArray();
						// registrazione gameUUID
						client.gameUUID = messagePayload.get("GAMEUUID").asString();
						client.getClientInterface().registerGameUUID(client.gameUUID);
						
						client.getSendQueue().add(ClientMessageFactory.buildCHGNAMEmessage(client.myUUID, 
																					 	   client.getPlayers().get(client.getUUID()).getName()));
						
						playerList.forEach(player -> {
							client.getPlayers().put(player.asString(), new ClientPlayer());
						});
						JsonObject board = Json.parse(messagePayload.get("BOARD").asString()).asObject();
						client.setClientBoard(new ClientBoard(board));
						
						//set excommunication cards
						client.getBoard().setExcommunicationCards(messagePayload.get("EXCOMMUNICATIONCARDS"));
						
						client.graphicInterface.registerBoard(client.getBoard());
						client.graphicInterface.registerPlayers(client.getPlayers());						
						client.graphicInterface.displayMessage("game start, "+client.getPlayers().size()+" players connected");
						
						Thread clientInterfaceThread = new Thread(client.getClientInterface());
						clientInterfaceThread.start();
						
						break;
					case "STATCHNG":
						playerID = messagePayload.get("PLAYERID").asString();
						
						// update resources
 						JsonObject newResources = Json.parse(messagePayload.get("RESOURCE").asString()).asObject();
 						client.getPlayers().get(playerID).getPlayerResources().replaceResourceSet(new ResourceSet(newResources));	
						
 						// updating card
 						JsonObject cardList = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject();
 						Iterator<Member> cardListIterator = cardList.iterator();
 						client.getPlayers().get(playerID).getCards().clear();
 						
 						cardListIterator.forEachRemaining(cards -> {
 							JsonArray cardListArray = cards.getValue().asArray();
 							if(!cardListArray.isNull())
 								cardListArray.forEach(card -> client.getPlayers().get(playerID).addCard(cards.getName(), card.asString()));
 						});
						client.getPlayers().get(playerID).setPersonalBonusTile(messagePayload.get("BONUSTILE").asString());		
						
						// update familyMEmberStatus
						JsonArray familyStatus = Json.parse(messagePayload.get("FAMILYSTATUS").asString()).asArray();
						for(int i=0; i<familyStatus.size(); i++){
							client.getPlayers().get(playerID).getFamilyMembers()[i].setBusyFlag(familyStatus.get(i).asBoolean());
						}
						
						for(int k=0; k<client.getPlayers().get(playerID).getTrack().length; k++){
							client.getPlayers().get(playerID).getTrack()[k].addScore(client.getPlayers().get(playerID).getPlayerResources());
						}
						break;
					case "CHGBOARDSTAT":
						// notifica cambiamento board
						if("BOARD".equals(messagePayload.get("TYPE").asString())){
							JsonArray cardLayout = Json.parse(messagePayload.get("PAYLOAD").asString()).asArray();
							cardLayout.forEach(JSONcard -> {
								JsonObject card = JSONcard.asObject();
								int regionID = card.get("REGIONID").asInt();
								int spaceID = card.get("SPACEID").asInt();
								String cardName = card.get("NAME").asString();
								client.getBoard().getRegionList().get(regionID).getActionSpaceList().get(spaceID).setCard(cardName);
							});
						}
						if("FAMILY".equals(messagePayload.get("TYPE").asString())){							
							int regionID = messagePayload.get("PAYLOAD").asObject().get("REGIONID").asInt();
							int spaceID = messagePayload.get("PAYLOAD").asObject().get("SPACEID").asInt();
							int familyMemberID = messagePayload.get("PAYLOAD").asObject().get("FAMILYMEMBER_ID").asInt();
							client.getBoard().getRegionList().get(regionID).getActionSpaceList().get(spaceID).addFamilyMember(
									client.getPlayers().get(messagePayload.get("PAYLOAD").asObject().get("PLAYERID").asString())
									.getFamilyMembers()[familyMemberID]);							
						}
						if("FLUSHFAMILY".equals(messagePayload.get("TYPE").asString())){
							if(messagePayload.get("PAYLOAD").asObject().get("TURNENDFLAG").asBoolean())
								client.getBoard().flushFamilyMember();
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
						break;
					case "TRNBGN":
						client.getClientInterface().leaderStartPhaseEnd();
						String playerUUID = messagePayload.get("PLAYERID").asString();
						System.out.println("TRNBGN");
						System.out.println(playerUUID.equals(client.getUUID()));
						System.out.println(playerUUID);

						System.out.println(client.getUUID().equals(playerUUID));
						if(playerUUID.equals(client.getUUID())){
							// timer inizialization
							client.startTimeout = System.currentTimeMillis();
							client.actionRunningFlag = true;
							
							client.graphicInterface.waitTurn(false);
							client.getClientInterface().displayMessage("your turn is start, make an action\n"
																	 + "> type ENTER to update your state");					
						}else{
							//timer stop
							client.actionRunningFlag = false;
							
							client.graphicInterface.waitTurn(true);
							client.getClientInterface().displayMessage("now is "+client.getPlayers().get(playerUUID).getName()+"'s turn");
						}						
						break;
						
					case "ACTCHK":
						boolean result = messagePayload.get("RESULT").asBoolean();
						if(!result){
							client.graphicInterface.displayMessage("> THE ACTION IS NOT VALID!\n "
												+ "please type a valid action.");
						}
						else{
							client.graphicInterface.displayMessage("> THE ACTION IS VALID!\n");

							for(int k=0; k<client.getPlayers().get(client.getUUID()).getTrack().length; k++){
								client.getPlayers().get(client.getUUID()).getTrack()[k].addScore(
										client.getPlayers().get(client.getUUID()).getPlayerResources());
							}
							client.graphicInterface.waitTurn(true);
						}	
						break;
					case "CONTEXT":
						client.getClientInterface().openContext(messagePayload);
						break;
					
					case "ASKLDRACK": //ACK AZIONE LEADER
						JsonObject payload = Json.parse(messagePayload.get("PAYLOAD").asString()).asObject(); // non so come mai ma va parsato il payload
						String decision = payload.get("DECISION").asString();
						String cardName = payload.get("LEADERCARD").asString();
						
						if(payload.get("RESULT").asBoolean()){
							client.getClientInterface().displayMessage("Leader "+ decision + 
									" action performed.\nYou "+ decision+"ed your leader card "+
									cardName);
							if("DISCARD".equals(decision)){
								System.out.println("PRIMA DEL DISCARD: " + client.getPlayers().get(client.getUUID()).getCards().get("LEADER").toString());
								client.getPlayers().get(client.getUUID()).getCards().get("LEADER").remove(cardName);
								System.out.println("DOPO DEL DISCARD: " + client.getPlayers().get(client.getUUID()).getCards().get("LEADER").toString());
							}
						} if(!payload.get("RESULT").asBoolean()){
							client.getClientInterface().displayMessage("Impossible to perform the action " + decision + " .\n" 
							+ "You don't have the requirements to play the card or it's on the game.\n");
						}
						break;
						
					case "MSG":
						if(!messagePayload.get("FLAG").asBoolean()){
							if(!client.getUUID().equals(messagePayload.get("RECEIVER").asString())){
								break; // non sei tu il destinatario
							}
						}
						String sender = messagePayload.get("SENDER").asString();
						String text = messagePayload.get("MESSAGE").asString();
						client.getClientInterface().receiveMessage(client.getPlayers().get(sender).getName(), text);
						break;
						
					case "ENDGAME":
						client.getClientInterface().displayMessage("END OF THE GAME!\nthe final score is:\n");
						StringBuilder display = new StringBuilder();
						for( String key: client.getPlayers().keySet()){
							display.append(client.getPlayers().get(key).getName() +": " +
											messagePayload.get(key).asInt());
						}	
						client.getClientInterface().displayMessage(new String(display));
						
						in.close();
						break;
				}
			}
		}
	}
}
