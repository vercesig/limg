package it.polimi.ingsw.GC_32.Client.GUI;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.CLI.Context;
import it.polimi.ingsw.GC_32.Client.GUI.Screen.GameScreen;
import it.polimi.ingsw.GC_32.Client.GUI.Screen.GameUtils;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientGUI extends Application implements ClientInterface {
	
		// game management
			static ClientBoard boardReference;
			static HashMap<String, ClientPlayer> playerListReference;
			static String playerUUID;
			static String gameUUID;
			
			// network management
			private ConcurrentLinkedQueue<Object> contextQueue;
			private ConcurrentLinkedQueue<String> messageQueue;
			
			static ConcurrentLinkedQueue<String> sendQueue;
			
			private ConcurrentLinkedQueue<String> clientsendQueue;
			
			// context management
			private Context[] contextList;
			private Thread queueHandler; 
			 
			//JavaFx
			private GameScreen game;
			
	public ClientGUI() throws Exception {
	}

		
	private void networkInit(){
		contextQueue = new ConcurrentLinkedQueue<Object>();
		messageQueue = new ConcurrentLinkedQueue<String>();
		clientsendQueue = new ConcurrentLinkedQueue<String>();


	}
	
	private void javafxInit(Stage stage){
		Scene scene = new Scene(this.game, 1200.0, 680.0);
	    stage.setTitle("Lorenzo il Magnifico");
	    stage.setScene(scene);
	    stage.show();
	   
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		
		this.game = new GameScreen(this);

		this.networkInit();
		
		this.javafxInit(stage);
	}

	@Override
	public void run() {
		 launch();
		 
	}


	@Override
	public void openContext(JsonObject contextPayload) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void receiveMessage(String playerID, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayMessage(String message) {
	}

	//registers
	public void registerBoard(ClientBoard board){
		if(this.game != null){
			System.out.println("registro su game");
			this.game.getClient().registerBoard(board);
			
		}
		
	boardReference = board;
	}
	
	public void registerPlayers(HashMap<String,ClientPlayer> playerList){
		if(this.game != null){
			this.game.getClient().playerListReference = playerList;
			System.out.println("registro su game");
		}
		playerListReference = playerList;
	}
	
	public void registerGameUUID(String UUID){
		if(this.game != null){
			System.out.println("registro su game");

			this.game.getClient().gameUUID = UUID;
		}
		gameUUID = UUID;
	}
	
	public void registerPlayerUUID(String UUID){
		if(this.game!= null){
			this.game.getClient().playerUUID = UUID;
			System.out.println("registro su game");

		}	
		playerUUID = UUID;
	}

	public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue) {
		System.out.println("Register SendMessageQueue: " + queue);

		if(this.game != null){
			this.game.getClient().sendQueue = queue;		
			System.out.println("Assegnato dentro this.game.getClient()");
		}	
		System.out.println("Assegnato dentro this.ClinetGUI");
		this.sendQueue = queue;
	}

	
	//getters
	public ClientBoard getBoard(){
		return ClientGUI.boardReference;
	}
	
	public String getPlayerUUID(){
		return this.playerUUID;
	}
	
	public String getGameUUID(){
		return this.gameUUID;
	}
	
	public HashMap<String, ClientPlayer> getPlayerList(){
		return this.playerListReference;
	}

	public ConcurrentLinkedQueue<String> getSendQueue(){
		return this.sendQueue;
	}
	
	@Override
	public void leaderStartPhaseEnd() {
		// TODO Auto-generated method stub
	}


	@Override
	public void waitTurn(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		GameUtils.update(this.game);
		
	}
}
