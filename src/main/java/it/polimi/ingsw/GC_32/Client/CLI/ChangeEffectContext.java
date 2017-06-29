package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.ArrayList;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ChangeEffectContext extends Context{

	
	public void open(Object object) {
		
		runFlag = true;
		JsonObject JsonPayload = (JsonObject) object;
		
		JsonArray CHANGEcardName = JsonPayload.get("NAME").asArray();
		JsonArray CHANGEresourcePayload = JsonPayload.get("RESOURCE").asArray();
		
		System.out.println("you have some change effect to apply, for each card choose what exchange you want perform:");
		
		JsonObject CONTEXTREPLY = new JsonObject();
		CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
		JsonObject CONTEXTREPLYpayload = new JsonObject();
		CONTEXTREPLYpayload.add("CONTEXT_TYPE", "CHANGE");
		JsonObject CONTEXTREPLYpayloadinfo = new JsonObject();
		CONTEXTREPLYpayload.add("PAYLOAD", CONTEXTREPLYpayloadinfo);
		
		JsonArray indexArray = new JsonArray();
		CONTEXTREPLYpayloadinfo.add("CHANGEIDARRAY", indexArray);
		
		int i=0;
		
		while(runFlag){
			
			boolean actionFlag = true;
			
			System.out.println("--------------------  "+CHANGEcardName.get(i).asString()+"  --------------------\n");
			
			JsonValue item = CHANGEresourcePayload.get(i);
			if(item.isObject()){ // CHANGE singolo
				System.out.println("this card offer only this exchange:");
				System.out.println(new ResourceSet(item.asObject().get("RESOURCEIN").asObject()).toString()+" -> "
								  +new ResourceSet(item.asObject().get("RESOURCEOUT").asObject()).toString()+"\n"
								  + "type 'y' if you want apply this effect, otherwise type 'n'");

				while(actionFlag){
					command = in.nextLine();
					switch(command){
					case "y":
						indexArray.add(0);
						actionFlag=false;
						i++;
						break;
					case "n":
						indexArray.add("N");
						actionFlag=false;
						i++;
						break;
					default:
						System.out.println("type a valid argument");
					}
				}	
			}else{ // CHANGE esclusivo
				System.out.println("select what exchange you want apply. please type the corresponding ID of the effect you want perform\n"
						+ "type 'n' if you don't want to apply this effect");
				for(int j=0; j<item.asArray().size(); j++){
					System.out.println("["+j+"]  "+new ResourceSet(item.asArray().get(j).asObject().get("RESOURCEIN").asObject()).toString()+" -> "
												  +new ResourceSet(item.asArray().get(j).asObject().get("RESOURCEOUT").asObject()).toString()+"\n");
				}
				while(actionFlag){
					System.out.println("dentro while sotto else");
					command = in.nextLine();
					try{
						if(Integer.parseInt(command)<item.asArray().size()&&Integer.parseInt(command)>=0){
							indexArray.add(Integer.parseInt(command));
							actionFlag = false;
							i++;
						}else
							System.out.println("type a valid number");
						if(command.equals("n")){
							indexArray.add(command);
							actionFlag = false;
							i++;
						}else
							System.out.println("type a valid number");
					}catch(NumberFormatException e){
						System.out.println("type a valid number");
					}
				}
			}
			
			if(i==CHANGEcardName.size()){
				System.out.println("chiudo context "+CHANGEcardName.size());
				CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);	
				sendQueue.add(CONTEXTREPLY.toString());
				close();
			}
		}
	}
}
