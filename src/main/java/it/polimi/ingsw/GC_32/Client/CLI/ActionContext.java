package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.Scanner;

public class ActionContext implements Context{

	private Scanner in;
	
	public ActionContext(){
		this.in = new Scanner(System.in);
	}
	
	private boolean runFlag;
	
	public void run(){
		System.out.println("minchione");
		runFlag=true;
		while(runFlag){
			String i = in.nextLine();
			System.out.println(i);
			if(i.equals("exit")){
				close();
			}
		}
	}
	
	public void close(){
		runFlag=false;
	}
	
}
