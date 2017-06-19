package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.Scanner;

public class IdleContext implements Context,Runnable{

	Scanner in;
	
	public IdleContext(Scanner in){
		this.in = in;	
	}
	
	boolean runFlag = true;
	
	public void run(){
		runFlag = true;
		while(runFlag){
			System.out.println("eccomi");
			String i = in.nextLine();
			if(i.equals("exit")){
				System.out.println("uscendo");
				runFlag=false;
			}
		}
	}
	
	public void close(){
		this.runFlag = false;
	}
	
}
