package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class CardCli {
	
	public CardCli() {};
	
	private static void fillWith(StringBuilder stringBuilder, int howManyTimes, String string){
		for(int i=0; i<howManyTimes; i++){
			stringBuilder.append(string);
		}
	}
	
	private static void loadEffect(ArrayList<String> array, String key, JsonObject json){
		try{
			if(json.get(key).isArray()){
				for(JsonValue js : json.get(key).asArray()){
					if(js.isObject()){
						js.asObject().forEach(item -> {
							array.add(item.toString() + ',');
						});
					}
					if(js.isString()){
						array.add(key + ": " + js.asString());
				
					}
				}	
			}
			if(json.get(key).isObject()){
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
		catch(NullPointerException e){array.add(key + ": none");}
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
	
	public static String print(JsonObject json){
		
		StringBuilder card = new StringBuilder();
		String name = "name: " + json.get("name").asString();
		
		ArrayList <String> cost = new <String> ArrayList();
		
		try{
			for(JsonValue js : json.get("cost").asArray()){
				cost.add(new ResourceSet(js.asObject()).toString());
			}
		} catch(NullPointerException e){
			cost.add("cost: none");
		}
		
		String requirements;
		try{
			requirements = "requirements: " + new ResourceSet(json.get("requirements").asObject()).toString();
		} catch(NullPointerException e){
			requirements = "requirements: none";
		}
		
		ArrayList <String> effectInstant = new <String> ArrayList();
		ArrayList <String> InstantPayload = new <String> ArrayList();
		ArrayList <String> effectPermanent = new <String> ArrayList();
		ArrayList <String> PermanentPayload = new <String> ArrayList();
		
		loadEffect(effectInstant, "instantEffect", json);
		loadEffect(InstantPayload, "instantPayload", json);
		loadEffect(effectPermanent, "permanentEffect", json);
		loadEffect(PermanentPayload, "permanentPayload", json);
		
		String minimunActionValue = "Minimum Action Value: " + json.getInt("minimumActionValue", 0);
		
		int width = 30;
		int height = 10;
		
		//top
		fillWith(card, width, "-");
		card.append("\n");
		
		printLine(card, width, name);
		concat(card, width, cost);
		printLine(card, width, requirements);
		
		//mid
		printImage(card, width, height);
		
		//bot
		concat(card, width, effectInstant);
		printLine(card, width, "");
		concat(card, width, InstantPayload);
		fillWith(card, width, "-");
		
		card.append("\n");
		
		concat(card, width, effectPermanent);
		concat(card, width, PermanentPayload);
		fillWith(card,width, " ");
		card.append("\n");
		printLine(card, width, minimunActionValue);

		fillWith(card,width, "-");
		card.append("\n");
		
		return new String(card);
	}
}