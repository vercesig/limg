package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import it.polimi.ingsw.GC_32.Client.Network.MsgConnection;

public class SocketMsgConnection implements MsgConnection{

	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	
	public void open() throws IOException{
		socket = new Socket("localhost",9500);
		
		System.out.println("connected to server");
		
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
		return in.hasNextLine();
	}
	
/*	public static void main(String[] args) throws IOException{
		SocketMsgConnection connection = new SocketMsgConnection();
		connection.open();
		
		while(true){
			if(connection.hasMessage()){
				System.out.println("messaggio\n");
				System.out.println(connection.getMessage());
			}
		}
	}*/
	
}
