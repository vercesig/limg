package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.HashSet;
import java.util.Set;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class PrivilegeContext extends Context{
	
	public PrivilegeContext(ClientCLI client){
		super(client);
	}
	
	private String[] values = { "{\"WOOD\": 1, \"STONE\": 1}",
								"{\"SERVANTS\": 2}",
								"{\"COINS\": 2}",
								"{\"MILITARY_POINTS\": 2}",
								"{\"FAITH_POINTS\": 1}"};
	
	public String open(Object payload){
		
		runFlag=true;
		
		JsonObject JsonPayload = (JsonObject) payload;
		
		int numberOfPrivilege = JsonPayload.get("NUMBER").asInt();
		ResourceSet cost = null;
		boolean isCostPrivilege = false;
		
		if(JsonPayload.get("COST")!=null){
			cost = new ResourceSet(JsonPayload.get("COST").asObject());
			isCostPrivilege = true;
		}
		
		JsonObject CONTEXTREPLY = new JsonObject();
		CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
		JsonObject CONTEXTREPLYpayload = new JsonObject();
		CONTEXTREPLYpayload.add("CONTEXT_TYPE", "PRIVILEGE");
		JsonArray CONTEXTREPLYpayloadinfo = new JsonArray();
		CONTEXTREPLYpayload.add("PAYLOAD", CONTEXTREPLYpayloadinfo);
		
		CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);
		
		if(!isCostPrivilege){
			out.println("you have "+numberOfPrivilege+" privilege to spend. Each privilege could "
					+ "be transformed into:\n- (0) 1 WOOD and 1 STONE\n- (1) 2 SERVANTS\n- (2) 2 COINS\n"
					+ "- (3) 2 MILITARY POINTS\n- (4) 1 FAITH POINT\ntype the number corrisponding "
					+ "to the resource you want to exchange with your privilege");
		}else{
			out.println("you have this effect to consume :\n"+cost.toString()+" -> PRIVILEGE :"+numberOfPrivilege+"\nEach privilege could "
					+ "be transformed into:\n- (0) 1 WOOD and 1 STONE\n- (1) 2 SERVANTS\n- (2) 2 COINS\n"
					+ "- (3) 2 MILITARY POINTS\n- (4) 1 FAITH POINT\ntype the number corrisponding "
					+ "to the resource you want to exchange with your privilege. type 'n' if you don't want to apply the effect");
		}		
		Set<String> choosedResources = new HashSet<String>();
		while(runFlag){
			command = in.nextLine();
			if("n".equals(command)&&isCostPrivilege){
				choosedResources.add(command);
				return CONTEXTREPLY.toString();
			}		
			try{
				switch(Integer.parseInt(command)){
				case 0:
					CONTEXTREPLYpayloadinfo.add(values[0]);
					break;
				case 1:
					CONTEXTREPLYpayloadinfo.add(values[1]);
					break;
				case 2:
					CONTEXTREPLYpayloadinfo.add(values[2]);
					break;
				case 3:
					CONTEXTREPLYpayloadinfo.add(values[3]);
					break;
				case 4:
					CONTEXTREPLYpayloadinfo.add(values[4]);
					break;
				default:
					System.out.println("type a valid number");
					break;
				}
			}catch(NumberFormatException e){
				out.println("type a valid number");
			}
			if(!choosedResources.contains(command)){
				numberOfPrivilege--;
				choosedResources.add(command);
				out.println(numberOfPrivilege+" elapsed");
			}
			else{
				out.println("you can't choose the same resource two times, please enter"
						+ " a different choise");
			}
			if(numberOfPrivilege==0)
				close();
		}
		return CONTEXTREPLY.toString();
	}	
}
