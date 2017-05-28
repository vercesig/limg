package it.polimi.ingsw.GC_32.Server.Setup;

import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.*;
import it.polimi.ingsw.GC_32.Server.Game.Effect.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class JsonImporter {
	
	public static List<DevelopmentCard> importDevelopmentCard(FileReader fileReader) throws IOException{
		
		ArrayList<DevelopmentCard> cardList = new ArrayList<DevelopmentCard>();
		
		JsonArray JsonCardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : JsonCardList){
			JsonObject card = item.asObject();
<<<<<<< HEAD
			
			JsonValue name = card.get("name");
			JsonValue period = card.get("period");
			JsonValue cardType = card.get("cardType");
			
			// effetto instantaneo da passare al builder
			JsonValue instantEffect = card.get("instantEffect");
			JsonObject instantPayload = card.get("instantPayload").asObject();
			// OPCODE relativo all'effetto custom istantaneo
			JsonValue customIstantEffect = card.get("customIstantEffect");
			
			// effetto permanente da passare al builder
			JsonValue permanentEffect = card.get("permanentEffect");
			JsonObject permanentPayload = card.get("permanentPayload").asObject();
			
			// costo della carta
			JsonObject JsonresourceSet = card.get("cost").asObject();
			Iterator<Member> singleItem = JsonresourceSet.iterator();				
			ResourceSet resourceSet = new ResourceSet();		
			while(singleItem.hasNext()){
				String resourceType = singleItem.next().getName();
				int value = JsonresourceSet.get(resourceType).asInt();
				resourceSet.setResource(resourceType, value);
			}
			
			DevelopmentCard newCard = new DevelopmentCard(name.asString(), resourceSet, period.asInt(), cardType.asString());
			// binding degli effetti alla carta
			if(!(customIstantEffect==null))
				newCard.addInstantEffect(EffectRegistry.getInstance().getEffect(customIstantEffect.asString())); // effetto custom
			
									
			tmp.add(newCard);
=======
		
			try{
				String name = card.get("name").asString();
				Integer period = card.get("period").asInt();
				String cardType = card.get("cardType").asString();
				
				String instantEffect = card.get("instantEffect").asString();
				String permanentEffect = card.get("permanentEffect").asString();
				
				JsonValue resourceCost = card.get("resourceCost");
				JsonArray resourceArray = new JsonArray();
				if( resourceCost.isObject() ){ // Carta con costo singolo
					 resourceArray = new JsonArray();
					resourceArray.add(resourceCost);
				}
				if( resourceCost.isArray() ){ // Carta con costo multiplo
					resourceArray = resourceCost.asArray();
				}
					
				DevelopmentCard newCard = new DevelopmentCard(name, period, cardType);
				newCard.registerCost(resourceArray.iterator());
				newCard.registerInstantEffect(EffectRegistry.getInstance().getEffect(instantEffect));
				newCard.registerPermanentEffect(EffectRegistry.getInstance().getEffect(permanentEffect));
				
				cardList.add(newCard);
			} catch (UnsupportedOperationException e){
				e.printStackTrace();
			}
>>>>>>> 4647c49517b769421ed97e650f6d01432dcd68ce
		}
		return cardList;
	}
	
	public static List<ExcommunicationCard> importExcommunicationCard(FileReader fileReader) throws IOException{
		
		ArrayList<ExcommunicationCard> cardList = new ArrayList<ExcommunicationCard>();
		
		JsonArray jsonCardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : jsonCardList){
			JsonObject card = item.asObject();
			JsonValue name = card.get("name");
			JsonValue period = card.get("period");
	
			String instantEffect = card.get("instantEffect").asString();
			String permanentEffect = card.get("permanentEffect").asString();
			
			ExcommunicationCard newCard = new ExcommunicationCard(name.asString(),period.asInt());
			newCard.registerInstantEffect(EffectRegistry.getInstance().getEffect(instantEffect));
			newCard.registerPermanentEffect(EffectRegistry.getInstance().getEffect(permanentEffect));
									
			cardList.add(newCard);
		}
		
		return cardList;
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
