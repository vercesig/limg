package it.polimi.ingsw.GC_32.Client.GUI;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.CLI.ClientCLI;
import it.polimi.ingsw.GC_32.Client.CLI.Context;
import it.polimi.ingsw.GC_32.Client.GUI.Screen.GameScreen;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientGUI extends Application implements ClientInterface{
	
	private static ClientGUI clientGui;
	private  ClientCLI client;
		//JavaFx
	private  GameScreen game;
	
	public ClientGUI(){
		this.client = new ClientCLI();
	}

	//registers
	public void registerBoard(ClientBoard board){
		clientGui.getClient().registerBoard(board);
		if(game != null){
			System.out.println("registro su game");
			game.getClient().registerBoard(board); //this ClientCLi is the instance 	
		}		
	}
	
	public void registerPlayers(HashMap<String,ClientPlayer> playerList){
		clientGui.getClient().registerPlayers(playerList);
		if(game != null){
			game.getClient().registerPlayers(playerList);
			System.out.println("registro su game");
		}
	}
	
	public void registerGameUUID(String UUID){
		clientGui.getClient().registerGameUUID(UUID);
		if(game != null){
			System.out.println("registro su game");
			game.getClient().registerGameUUID(UUID);
		}
	 }
	
	public void registerPlayerUUID(String UUID){
		clientGui.getClient().registerPlayerUUID(UUID);
		if(game!= null){
			game.getClient().registerPlayerUUID(UUID);
			System.out.println("registro su game");
		}	
	}
	
	//getters
	public ClientCLI getClient(){
		return this.client;
	}
	
	public GameScreen getGame(){
		return this.game;
	}

	public static ClientGUI getClientGui(){
		return clientGui;
	}
	
	//setters
	public void setGame(GameScreen game){
		this.game = game;
	}
	
	@Override
	public void openContext(JsonObject contextPayload) {
		clientGui.getClient().openContext(contextPayload);
	}

	@Override
	public void receiveMessage(String playerID, String message) {
		clientGui.getClient().receiveMessage(playerID, message);
	}

	@Override
	public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue) {
		clientGui.getClient().registerSendMessageQueue(queue);
	}

	@Override
	public void displayMessage(String message) {
		clientGui.getClient().displayMessage(message);
	}

	@Override
	public void leaderStartPhaseEnd() {
		clientGui.getClient().leaderStartPhaseEnd();
	}

	@Override
	public Context[] getContextList() {
		return clientGui.getClient().getContextList();
	}

	@Override
	public void setIdleRun(boolean idleRunFlag) {
		clientGui.getClient().setIdleRun(idleRunFlag);
	}

	@Override
	public void waitTurn(boolean flag) {
		clientGui.getClient().waitTurn(flag);
	}
	
	//static
	
	public void run() {
		System.out.println("BOARd CLIEnt:\n" +  client.getBoard().toString());
		Thread cliThread = new Thread(clientGui.getClient());
		cliThread.start();
		launch();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		System.out.println("BOARd CLIEnt:\n" +  clientGui.getClient());
		clientGui.setGame(new GameScreen(clientGui.getClient()));	
		javafxInit(new Stage(), clientGui);
	}
	
	private void javafxInit(Stage stage, ClientGUI clientGui){
		Scene scene = new Scene(clientGui.getGame(), 1200.0, 680.0);
	    stage.setTitle("Lorenzo il Magnifico");
	    stage.setScene(scene);
	    stage.show();
	}
	
	/*public static void main(String[] args){
		launch();
	}*/

	public static void initClientGui(){
		clientGui = new ClientGUI();
	}
}