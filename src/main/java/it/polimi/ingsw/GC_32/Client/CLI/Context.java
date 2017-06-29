package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

public abstract class Context{
	
	protected Scanner in; // input scanner
	protected ConcurrentLinkedQueue<String> sendQueue; // use this queue if you want to send message from context
	
	protected boolean runFlag; // flag used to stop/start the context
	protected String command; // use this string as a buffer to save in.nextLine output
	protected boolean actionRunningGameFlag; // flag indicating if the action timeout is going on
	
	protected String gameUUID; // the gameUUID
	protected String playerUUID; // the playerUUID
	
	protected ClientPlayer clientPlayerReference; // used only by ChangeEffectContext, it is needed to access to client's resources
	
	public Context(){
		this.in = new Scanner(System.in);
	}
	
	public void registerSendQueue(ConcurrentLinkedQueue<String> sendQueue){
		this.sendQueue = sendQueue;
	}
		
	public void registerGameUUID(String gameUUID){
		this.gameUUID = gameUUID;
	}
	
	public void registerPlayerUUID(String playerUUID){
		this.playerUUID = playerUUID;
	}
	
	public void registerActionRunningGameFlag(boolean flag){
		this.actionRunningGameFlag = flag;
	}
	
	public void registerClientPlayer(ClientPlayer clientPlayer){
		this.clientPlayerReference = clientPlayer;
	}
	
	public abstract void open(Object object);
	
	protected void close(){
		this.runFlag = false;
	}
	
}
