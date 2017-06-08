package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Network.MsgConnection;

public class SocketMsgConnection implements MsgConnection{

	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private boolean stop;
	
	public void open() throws IOException{
		socket = new Socket("localhost",9500);
		System.out.println("[SOCKETMSGCONNECTION] connected to server");
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
	
	public void kill(){
		this.stop = true;
	}
	
	public static void main(String[] args) throws IOException{
		SocketMsgConnection connection = new SocketMsgConnection();
		connection.open();
		
		while(true){
			if(connection.hasMessage()){
				System.out.println("[SOCKETMSGCONNECTION] recived message from server");
				JsonObject message = Json.parse(connection.getMessage()).asObject();
				switch(message.get("MESSAGETYPE").asString()){
				case "TURNBGN":
					System.out.println("[SOCKETMSGCONNECTION] message type "+message.get("MESSAGETYPE").toString());
					/*JsonObject sendMessage = new JsonObject();
					sendMessage.add("MESSAGETYPE", "TRNEND");
					JsonObject payload = new JsonObject();
					payload.add("TYPE", "TOWER");
					sendMessage.add("PAYLOAD", payload);
					System.out.println("[SOCKETMSGCONNECTION] sending response to server");
					connection.sendMessage(sendMessage.toString());*/
					break;
				}
			}
		}
	}
	
}
