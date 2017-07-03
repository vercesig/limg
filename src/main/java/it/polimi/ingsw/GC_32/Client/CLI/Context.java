package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.PrintWriter;
import java.util.Scanner;

public abstract class Context{
	
	protected Scanner in; // input scanner
	protected PrintWriter out;
	protected ClientCLI client;
	
	protected boolean runFlag; // flag used to stop/start the context
	protected String command; // use this string as a buffer to save in.nextLine output
	
	public Context(ClientCLI client){
		this.in = new Scanner(System.in);
		this.out = new PrintWriter(System.out);
		this.client = client;
	}
	
	public abstract String open(Object object);
	
	protected void close(){
		this.runFlag = false;
	}
	
}
