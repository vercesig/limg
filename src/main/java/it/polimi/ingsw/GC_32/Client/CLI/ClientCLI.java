package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

public class ClientCLI implements ClientInterface{

	private ClientBoard boardReference;
	private HashMap<String, ClientPlayer> playerListReference;
	private String UUID;
	
	private Scanner in;
	private PrintWriter out;
	
	public ClientCLI(){
		in = new Scanner(System.in);
		out  = new PrintWriter(System.out);
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
	
	public void run(){
			
		while(true){
			System.out.println("type a command");
			String command = in.nextLine();
			if(command.equals("board")){
				System.out.println(this.boardReference.toString());
			}
			if(command.equals("players")){
				playerListReference.forEach((UUID, player)->{
					System.out.println(player.toString());
				});
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
