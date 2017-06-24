package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import it.polimi.ingsw.GC_32.Client.Network.MsgConnection;

public class SocketMsgConnection implements MsgConnection{

	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private boolean stop;
	
	public void open() throws IOException{
		socket = new Socket("localhost",9500);
		//System.out.println("[SOCKETMSGCONNECTION] connected to server");
		in = new Scanner(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream());
	}
	
	public void close() throws IOException{
		socket.close();
		in.close();
		out.close();
	}
	
	public void sendMessage(String message){
		out.println(message);
		out.flush();
	}
	
	public String getMessage(){
		return in.nextLine();
	}
	
	public boolean hasMessage(){
		return in.getInputStream().available()!=0;
	}
			
}
