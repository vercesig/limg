package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.Scanner;

public class TestContext implements Context{

	private Scanner in;
	
	public TestContext(){
		this.in = new Scanner(System.in);
	}
	
	private boolean runFlag;
	
	String command;
	
	public void open(){
		System.out.println("actionContext");
		runFlag=true;
		while(runFlag){
			command = in.nextLine();
			System.out.println(command);
			if(command.equals("exit")){
				close();
				System.out.println("chiudo actionContext");
			}
		}
	}
	
	public void close(){
		runFlag=false;
	}
	
}
