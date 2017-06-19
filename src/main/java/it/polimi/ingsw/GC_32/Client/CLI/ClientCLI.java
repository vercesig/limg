package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

public class ClientCLI implements ClientInterface{

	private ClientBoard boardReference;
	private HashMap<String, ClientPlayer> playerListReference;
	private String UUID;
	
	private Scanner in;
	private PrintWriter out;
	
	private ConcurrentLinkedQueue<Object> contextQueue;
	
	private Context[] contextList;
	
	public ClientCLI(){
		in = new Scanner(System.in);
		out  = new PrintWriter(System.out);
		contextQueue = new ConcurrentLinkedQueue<Object>();
		
		this.contextList = new Context[5];
		contextList[0] = new IdleContext(in); // idle
		contextList[1] = new ActionContext(); // actioncontext
		
	}
	
	public void registerBoard(ClientBoard board){
		this.boardReference = board;
	}
	
	public void registerPlayers(HashMap<String,ClientPlayer> playerList){
		this.playerListReference = playerList;
	}
	
	public void registerUUID(String UUID){
		this.UUID = UUID;
	}
		
	Thread contextThread;
	boolean idleRun = false;
	
	public void run(){
		
		while(true){
			if(!idleRun){
				idleRun=true;
				contextThread = new Thread((Runnable) contextList[0]);
				contextThread.start();
				System.out.println("apro idle");
			}
			if(!contextQueue.isEmpty()){
				System.out.println("chiduo idle");
				contextList[0].close();
				JsonObject contextMessage = (JsonObject) contextQueue.poll();
				contextList[contextMessage.get("CONTEXTID").asInt()].run();
				idleRun=false;
				// avvia il context indicato nel contextID del messaggio in testa alla coda
			}
		}
		
	}

	public void displayMessage(String message){
		out.println(message);
		out.flush();
	}
	
	@Override
	public int getScreenId() {
		// TODO Auto-generated method stub
		return 0;
	}

	// change context
	@Override
	public void openScreen(int screenId, String additionalData) {
		
	}

	public void openScreen(JsonObject contextMessage){
		this.contextQueue.add(contextMessage);
	}
	
	
	@Override
	public void registerContextPayloadQueue(ConcurrentLinkedQueue<Object> queue) {
		this.contextQueue = queue;
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
