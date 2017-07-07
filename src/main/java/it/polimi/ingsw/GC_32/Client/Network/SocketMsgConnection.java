package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import it.polimi.ingsw.GC_32.Common.Network.MsgConnection;
import it.polimi.ingsw.GC_32.Common.Utils.KillableRunnable;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;

public class SocketMsgConnection implements MsgConnection, KillableRunnable{
    private Logger LOGGER = Logger.getLogger(this.getClass().getName());
    
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private boolean stop = false;
	
	private ConcurrentLinkedQueue<String> sendMessageQueue;
	private ConcurrentLinkedQueue<String> receivedMessageQueue;
	
	public void open(String ip, int port) throws IOException{
		socket = new Socket(ip, port);
		in = new Scanner(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream());
		
		this.sendMessageQueue = new ConcurrentLinkedQueue<String>();
		this.receivedMessageQueue = new ConcurrentLinkedQueue<String>();
						
	}
	
	public void run(){
		System.out.println("partito");
		while(!stop){
			try {
				if(socket.getInputStream().available()>0){
					receivedMessageQueue.add(in.nextLine());
					//System.out.println("ricevuto");
				}
			} catch (IOException e) {
			    LOGGER.log(Level.INFO, "Connection cloased unexpectedly", e);
				break;
			}
			
			if(!sendMessageQueue.isEmpty()){
				out.println(sendMessageQueue.poll());
				out.flush();
				System.out.println("spedito");
			}
		}
	}
	
	public void close() throws IOException{
		socket.close();
		in.close();
		out.close();
	}
	
	public void sendMessage(String message){
		this.sendMessageQueue.add(message);
		//out.println(message);
		//out.flush();
	}
	
	public String getMessage(){
		return this.receivedMessageQueue.poll();
		//return in.nextLine();
	}
	
	public boolean hasMessage() throws IOException{
		return !this.receivedMessageQueue.isEmpty();
		//return socket.getInputStream().available() > 0;
	}
	
	@Override
	public void kill(){
	    this.stop = true;
	}
}
