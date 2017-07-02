package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class CardCli {
	
	public CardCli() {}
	
	private static void fillWith(StringBuilder stringBuilder, int howManyTimes, String string){
		for(int i=0; i<howManyTimes; i++){
			stringBuilder.append(string);
		}
	}
	
	private static void loadEffect(ArrayList<String> array, String key, JsonObject json){
		
		if(json.get(key)!= null){
			if(json.get(key).isArray()){  // example [{"COINS":1},{"WOOD":1}]
				for(JsonValue js : json.get(key).asArray()){  
					if(js.isObject()){
						for(Member m : js.asObject()){
							if(m.getValue().isNumber()){
								array.add(m.getName() +": " + m.getValue().asInt());
							}
							if(m.getValue().isString()){
								array.add(m.getName() +": " + m.getValue().asString());
							}
						}
					}
					if(js.isString()){
						array.add(key + ": " + js.asString());
					}
				}	
			}
			if(json.get(key).isObject()){ // example {"COINS":1, "FAITH_POINT":1}
				for(Member m : json.get(key).asObject()){
					if(m.getValue().isNumber()){
						array.add(m.getName() +": " + m.getValue().asInt());
					}
					if(m.getValue().isString()){
						array.add(m.getName() +": " + m.getValue().asString());
					}
				}
			}
			if(json.get(key).isString()){
				array.add(key + ": " + json.get(key).asString());
			}
		} 
		else
			array.add(key + ": none");
	}
	
	private static void printImage(StringBuilder stringBuilder, int length, int height){
		
		int emptyX = (length - length/5 )/2;
		int emptyY = (height - height/5 )/2;
		
		
		for(int i = 0; i < emptyY; i++){
			printLine(stringBuilder, length, "");
		}	
		
		for(int i = 0; i < (height - height/5); i++){
			stringBuilder.append("|");
			fillWith(stringBuilder, emptyX -2, " ");
			stringBuilder.append("||");
			fillWith(stringBuilder, length - 2*(emptyX), "=");
			stringBuilder.append("||");
			fillWith(stringBuilder, emptyX -2, " ");
			stringBuilder.append("|\n");
		}
		for(int i = 0; i < emptyY; i++){
			printLine(stringBuilder, length, "");
		}	
	}
	
	private static void printLine( StringBuilder stringBuilder, int length, String field){
	
		stringBuilder.append("|");
		fillWith(stringBuilder, (length - field.length())/2, " ");
		
		if(length - (length - field.length())/2 >= field.length()){
			stringBuilder.append(field);
			fillWith(stringBuilder, (length - (length - field.length())/2 - field.length()), " ");
			stringBuilder.append("|\n");
			return;
		} // devo andare a capo
		fillWith(stringBuilder, (length - (length - field.length())/2 ), " ");
		stringBuilder.append("\n|");
		stringBuilder.append(field);
		fillWith(stringBuilder, length - field.length(), " ");
		stringBuilder.append("|\n");
	}

	private static void concat( StringBuilder stringBuilder, int width,  ArrayList<String> string){
		for(String s : string){
			printLine(stringBuilder, width, s);
		}
	}
	
	public static String print(String name, JsonObject json){
	
		StringBuilder card = new StringBuilder();
		
		if(json.isNull()){
			card.append("EMPTY");
			return new String(card);
		}
		
		//String name = "name: " + json.get("name").asString();
		
		ArrayList<String> cost = new ArrayList<>();
		
		if(json.get("cost")!= null){
			for(JsonValue js : json.get("cost").asArray()){
				cost.add(new ResourceSet(js.asObject()).toString());
			}
		} else
			cost.add("cost: none");
		
		String requirements;
		if(json.get("requirements")!= null){
		JsonObject req = json.get("requirements").asObject();
			if(json.get("cost")== null){ // Carta Leader
				StringBuilder tmp = new StringBuilder();
				if(req.get("RESOURCE")!= null){
					tmp.append(new ResourceSet(req.get("RESOURCE").asObject()).toString());
				}
				if(req.get("CARDTYPE")!= null){
					for (Member item: req.get("CARDTYPE").asObject()){
						tmp.append(item.getName() + ": " + item.getValue().asInt());
					}
				}
				requirements = "requirements: " + new String(tmp);
			}
			else
				requirements = "requirements:" + (new ResourceSet(req)).toString();
		}
		else
			requirements = "requirements: none";
		
		ArrayList<String> effectInstant = new ArrayList<>();
		ArrayList<String> InstantPayload = new ArrayList<>();
		ArrayList<String> effectPermanent = new ArrayList<>();
		ArrayList<String> PermanentPayload = new ArrayList<>();
		
		loadEffect(effectInstant, "instantEffect", json);
		loadEffect(InstantPayload, "instantPayload", json);
		loadEffect(effectPermanent, "permanentEffect", json);
		loadEffect(PermanentPayload, "permanentPayload", json);
		
		String minimunActionValue = "Minimum Action Value: " + json.getInt("minimumActionValue", 0);
		
		int width = 40;
		int height = 10;
		
		//top
		fillWith(card, width+2, "-");
		card.append("\n");
		
		printLine(card, width, name);
		concat(card, width, cost);
		printLine(card, width, requirements);
		
		//mid
		printImage(card, width, height);
		
		//bot
		fillWith(card, width+2, "-");
		card.append("\n");
		concat(card, width, effectInstant);
		printLine(card, width, "");
		concat(card, width, InstantPayload);
		fillWith(card, width+2, "-");
		
		card.append("\n");
		
		concat(card, width, effectPermanent);
		concat(card, width, PermanentPayload);
		fillWith(card,width, " ");
		card.append("\n");
		printLine(card, width, minimunActionValue);

		fillWith(card,width+2, "-");
		card.append("\n");
		
		return new String(card);
	}
}
