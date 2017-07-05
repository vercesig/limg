package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public abstract class Context{
	
	protected Scanner in; // input scanner
	protected PrintWriter out;
	protected ClientCLI client;
	
	protected BufferedReader reader;
	
	protected boolean runFlag; // flag used to stop/start the context
	protected String command; // use this string as a buffer to save in.nextLine output
	
	public Context(ClientCLI client){
		reader = new BufferedReader(new InputStreamReader(System.in));
		this.in = client.getIn();
		this.out = client.getOut();
		this.client = client;
	}
	
	public abstract String open(Object object);
	
	public void close(){
		this.runFlag = false;
	}
	
}
