package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

public abstract class Context{
	
	protected Scanner in; // input scanner
	protected ClientCLI client;
	
	protected boolean runFlag; // flag used to stop/start the context
	protected String command; // use this string as a buffer to save in.nextLine output
	
	public Context(ClientCLI client){
		this.in = new Scanner(System.in);
		this.client = client;
	}
	
	public abstract String open(Object object);
	
	protected void close(){
		this.runFlag = false;
	}
	
}
