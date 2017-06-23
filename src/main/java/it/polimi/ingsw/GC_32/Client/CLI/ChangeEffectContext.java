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
		JsonObject jsonObject = (JsonObject) object;
		JsonArray JsonPayload = Json.parse(jsonObject.get("CHANGEARRAY").asString()).asArray();
		
		System.out.println("you have some change effect to apply. For each card you can choose the effect you want to apply just typing the corresponding "
				+ "number\nType 'n' if you don't want to apply the change for one specific card");
		
		ArrayList<StringBuilder> tmpList = new ArrayList<StringBuilder>();
		ArrayList<Integer> maxIndexChangeEffect = new ArrayList<Integer>();
		
		
		int cardIndex = -1;
		
		for(JsonValue value : JsonPayload){
			JsonArray valueArray = value.asArray();
			
			if(cardIndex!=valueArray.get(0).asInt()){
				maxIndexChangeEffect.add(valueArray.get(1).asInt());				
				tmpList.add(new StringBuilder());
				tmpList.get(valueArray.get(0).asInt()).append("---------------- "+valueArray.get(4).asString()+" ----------------\n ");
			}
			tmpList.get(valueArray.get(0).asInt()).append(valueArray.get(1).asInt()+" "); // card name
			tmpList.get(valueArray.get(0).asInt()).append(new ResourceSet(Json.parse(valueArray.get(2).asString()).asObject()).toString()+" -> "); // cost
			tmpList.get(valueArray.get(0).asInt()).append(new ResourceSet(Json.parse(valueArray.get(3).asString()).asObject()).toString()+"\n\n"); // benefit
			
			cardIndex = valueArray.get(0).asInt();
			
			if(maxIndexChangeEffect.get(cardIndex)<valueArray.get(1).asInt()){
				maxIndexChangeEffect.set(cardIndex, valueArray.get(1).asInt());
			}
			
		}
		
		JsonObject CONTEXTREPLY = new JsonObject();
		CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
		JsonObject CONTEXTREPLYpayload = new JsonObject();
		CONTEXTREPLYpayload.add("CONTEXT_TYPE", "CHANGE");
		JsonArray CONTEXTREPLYpayloadArray = new JsonArray();
				
		while(runFlag){
			tmpList.forEach(changeString -> {
				System.out.println(new String(changeString));
				
				command = in.nextLine();
				while(!(Integer.parseInt(command)<=maxIndexChangeEffect.get(tmpList.indexOf(changeString)))&&!command.equals("n")){
					System.out.println("enter a valid index for this card, or type 'n' if you don't want to apply any changes");
					command = in.nextLine();
				}
				if(!command.equals("n"))
					CONTEXTREPLYpayloadArray.add(JsonPayload.get(tmpList.indexOf(changeString)+Integer.parseInt(command)));
			});
			
			CONTEXTREPLYpayload.add("RESOURCEARRAY", CONTEXTREPLYpayloadArray);
			CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);
			
			sendQueue.add(CONTEXTREPLY.toString());
			close();
		}
	}
}
