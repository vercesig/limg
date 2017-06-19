package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.Scanner;

public class ZeroLevelContext implements Context,Runnable{

	Scanner in;
	ClientCLI CLI;
	
	public ZeroLevelContext(ClientCLI CLI){
		this.in = new Scanner(System.in);
		this.CLI = CLI;
	}
	
	boolean runFlag = true;
	String command;
	
	
	public void run(){
		open();
	}
	
	public void open(){
		runFlag = true;
		while(runFlag){
			System.out.println("type a command:\n- board: display the board status\n- players: display players' status"
					+ "\n- action: make an action (if isn't your turn your requests won't be applied)");
			command = in.nextLine();
			switch(command){
			case "board":
				System.out.println(this.CLI.getBoard().toString());
				break;
			case "players":
				this.CLI.getPlayerList().forEach((UUID, client) -> System.out.println(client.toString()));
				break;
				
			}
		}
	}
	
	public void close(){
		this.runFlag = false;
	}
	
}
