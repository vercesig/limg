package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Context{
	
	protected Scanner in;
	protected ConcurrentLinkedQueue<String> sendQueue;
	protected boolean runFlag;
	protected String command;
	protected String gameUUID;
	protected boolean actionRunningGameFlag;
	
	public Context(){
		this.in = new Scanner(System.in);
	}
	
	public void registerSendQueue(ConcurrentLinkedQueue<String> sendQueue){
		this.sendQueue = sendQueue;
	}
	
	public void registerGameUUID(String gameUUID){
		this.gameUUID = gameUUID;
	}
	
	public void registerActionRunningGameFlag(boolean flag){
		this.actionRunningGameFlag = flag;
	}
	
	public abstract void open(Object object);
	
	protected void close(){
		this.runFlag = false;
	}
	
}
