package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.HashSet;
import java.util.Set;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

/**
 * PrivilegeContext manage council privileges. When a council privilege is gained by the client, server must ask to him what resources he wants take from his privilege.
 * This context is responsable of the client interaction during this choise. Context ends with the sending of a CONTEXTREPLY message.
 * 
 * @see Context
 *
 */

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
			if("n".equals(command) && isCostPrivilege){
				choosedResources.add(command);
				return CONTEXTREPLY.toString();
			}		
			try{
				if(Integer.parseInt(command)>=0&&Integer.parseInt(command)<=4){
					if(!choosedResources.contains(command)){
						CONTEXTREPLYpayloadinfo.add(values[Integer.parseInt(command)]);
						numberOfPrivilege--;
						choosedResources.add(command);
						out.println(numberOfPrivilege+" elapsed");
					}
					else{
						out.println("you can't choose the same resource two times, please enter"
								+ " a different choise");
					}
				}
				else{
					out.println("type a valid command");
				}
				if(numberOfPrivilege==0)
					close();
			}catch(NumberFormatException e){
				out.println("type a valid number");
			}
			
		}
		return CONTEXTREPLY.toString();
	}	
}
