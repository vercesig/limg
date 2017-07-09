package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * context are the way the server can ask to the client more information needed for complete an action. When a CONTEXT message has been received, a conext is open.
 * The context remains open until the needed information hasn't been caugth. After that a message is sent and the context which has been opened can be close.
 *
 * <ul>
 * <li>{@link #in}: scanner which handles the input stream</li>
 * <li>{@link #out}: printwriter which handles the output stream</li>
 * <li>{@link #client}: reference to the main CLI class</li>
 * <li>{@link #reader}: BufferedReader which wraps an InputStreamReader, usefull when the input stream mustn't be blocked by a blocking call like scanner nextLine</li>
 * <li>{@link #runFlag}: use this flag to stop the context</li>
 * <li>{@link #command}: string used to handle user input</li>
 * </ul>
 *
 * @see ClientCli
 */

public abstract class Context{
	
	protected Scanner in; // input scanner
	protected PrintWriter out;
	protected ClientCLI client;
	
	protected BufferedReader reader;
	
	protected boolean runFlag; // flag used to stop/start the context
	protected String command; // use this string as a buffer to save in.nextLine output
	
	/**
	 * inizialize the context
	 * @param client the client the context has to handle
	 */
	public Context(ClientCLI client){
		reader = new BufferedReader(new InputStreamReader(System.in));
		this.in = client.getIn();
		this.out = client.getOut();
		this.client = client;
	}
	
	/**
	 * open a context
	 * @param object every information which can be usefull to customize the context
	 * @return the message which will be sent to the server
	 */
	public abstract String open(Object object);
	
	/**
	 * close this context
	 */
	public void close(){
		this.runFlag = false;
	}
	
}
