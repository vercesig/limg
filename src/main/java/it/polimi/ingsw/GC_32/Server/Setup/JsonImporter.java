package it.polimi.ingsw.GC_32.Server.Setup;

import it.polimi.ingsw.GC_32.Server.Game.*;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.*;
import it.polimi.ingsw.GC_32.Server.Game.Effect.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

public class JsonImporter {
	
	public static List<DevelopmentCard> importDevelopmentCard(FileReader fileReader) throws IOException{
		
		ArrayList<DevelopmentCard> tmp = new ArrayList<DevelopmentCard>();
		
		JsonArray cardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : cardList){
			JsonObject card = item.asObject();
			
			JsonValue name = card.get("name");
			JsonValue period = card.get("period");
			JsonValue cardType = card.get("cardType");
			
			JsonArray instantEffectList = card.get("instantEffectList").asArray();
			JsonArray permanentEffectList = card.get("permanentEffectList").asArray();
			
			JsonObject JsonresourceSet = card.get("resourceSet").asObject();
			Iterator<Member> singleItem = JsonresourceSet.iterator();				
			ResourceSet resourceSet = new ResourceSet();		
			while(singleItem.hasNext()){
				String resourceType = singleItem.next().getName();
				int value = JsonresourceSet.get(resourceType).asInt();
				resourceSet.setResource(resourceType, value);
			}
			
			DevelopmentCard newCard = new DevelopmentCard(name.asString(), resourceSet, period.asInt(), cardType.asString());
			instantEffectList.forEach(effectOPCODE -> newCard.addInstantEffect(EffectRegistry.getInstance().getEffect(effectOPCODE.asString())));
			permanentEffectList.forEach(effectOPCODE -> newCard.addPermanentEffect(EffectRegistry.getInstance().getEffect(effectOPCODE.asString())));
									
			tmp.add(newCard);
		}
		return tmp;		
	}
	
	public static List<ExcommunicationCard> importExcommunicationCard(FileReader fileReader) throws IOException{
		
		ArrayList<ExcommunicationCard> tmp = new ArrayList<ExcommunicationCard>();
		
		JsonArray cardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : cardList){
			JsonObject card = item.asObject();
			JsonValue name = card.get("name");
			JsonValue period = card.get("period");
	
			JsonArray instantEffectList = card.get("instantEffectList").asArray();
			JsonArray permanentEffectList = card.get("permanentEffectList").asArray();
			
			ExcommunicationCard newCard = new ExcommunicationCard(name.asString(),period.asInt());
			instantEffectList.forEach(effectOPCODE -> newCard.addInstantEffect(EffectRegistry.getInstance().getEffect(effectOPCODE.asString())));
			permanentEffectList.forEach(effectOPCODE -> newCard.addPermanentEffect(EffectRegistry.getInstance().getEffect(effectOPCODE.asString())));
									
			tmp.add(newCard);		
		}
		
		return tmp;
	}
	
	public static void importConfigurationFile(FileReader fileReader){
		
	}
	
	public static void main(String[] args) throws IOException{
		
		
		FileReader developmentCard = new FileReader("/home/alessandro/Scrivania/testscomunica.json");
		Deck<ExcommunicationCard> list = new Deck(JsonImporter.importExcommunicationCard(developmentCard));
		
		System.out.println(list.toString());
		
		list.shuffleDeck();
		
		System.out.println(list.drawRandomElement().toString());
		
	}
	
}
