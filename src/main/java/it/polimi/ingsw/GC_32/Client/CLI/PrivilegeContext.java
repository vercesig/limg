package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.HashSet;
import java.util.Set;
import com.eclipsesource.json.JsonObject;

public class PrivilegeContext extends Context{
		

	
	public void open(Object payload){
		
		runFlag=true;
		
		JsonObject JsonPayload = (JsonObject) payload;
		//int numberOfPrivilege = JsonPayload.get("NUMBER").asInt();
		int numberOfPrivilege = 2;
		JsonObject response = new JsonObject();
		
		System.out.println("you have "+numberOfPrivilege+" privilege to spend. Each privilege could "
				+ "be transformed into:\n- (a) 1 WOOD and 1 STONE\n- (b) 2 SERVANTS\n- (c) 2 COINS\n"
				+ "- (d) 2 MILITARY POINTS\n- (e) 1 FAITH POINT\ntype the letter corrisponding "
				+ "to the resource you want to exchange with your privilege");
		Set<String> choosedResources = new HashSet<String>();
		while(runFlag){
			command = in.nextLine();
			switch(command){
			case "a":
				response.add("WOOD", 1);
				response.add("STONE", 1);
				break;
			case "b":
				response.add("SERVANTS", 2);
				break;
			case "c":
				response.add("COINS", 2);
				break;
			case "d":
				response.add("MILITARY", 2);
				break;
			case "e":
				response.add("FAITH", 1);
				break;
			}
			if(!choosedResources.contains(command)){
				numberOfPrivilege--;
				choosedResources.add(command);
				System.out.println(numberOfPrivilege+" elapsed");
			}
			else{
				System.out.println("you can't choose the same resource two times, please enter"
						+ " a different choise");
			}
			
			if(numberOfPrivilege==0){
				if(sendQueue==null){
					System.out.println(response.toString());
					sendQueue.add(response.toString());
				}
				close();
			}
		}
	}	
}
