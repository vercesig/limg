package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketInfoContainer {

	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	
	public SocketInfoContainer(Socket socket) throws IOException{
		this.socket = socket;
		this.in = new Scanner(this.socket.getInputStream());
		this.out = new PrintWriter(this.socket.getOutputStream());
	}
	
	public Socket getSocket(){
		return this.socket;
	}
	
	public Scanner getScannerIn(){
		return this.in;
	}
	
	public PrintWriter getPrinterOut(){
		return this.out;
	}
	
	public void close() throws IOException{
		this.socket.close();
		this.in.close();
		this.out.close();
	}
	
}
