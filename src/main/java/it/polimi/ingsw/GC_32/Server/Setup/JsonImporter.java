package it.polimi.ingsw.GC_32.Server.Setup;

import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.*;
import it.polimi.ingsw.GC_32.Server.Game.Effect.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * allows to import and parse (from JSON files) external file like card description's file or game configuration file.
 * @author alessandro
 */

public class JsonImporter {
	
	/**
	 * method which return a list of development card from an external file correctly formatted. The method accept a FileReader object as parameter, 
	 * then parse it and finally returns the list of card (which can be passed to the Deck's constructor to generate an effectively deck structure)
	 * 
	 * @param fileReader  contains the FileReader object relative to the external file to parse
	 * @return the list of the cards generated from the JSON external file
	 * @throws IOException
	 */
	public static List<DevelopmentCard> importDevelopmentCard(FileReader fileReader) throws IOException{
		
		ArrayList<DevelopmentCard> cardList = new ArrayList<DevelopmentCard>();
		
		JsonArray JsonCardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : JsonCardList){
			JsonObject card = item.asObject();
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
		    JsonObject payload = card.get("instantPayload").asObject();		
			newCard.registerInstantEffect(EffectRegistry.getInstance().getEffect(instantEffect,payload));
			newCard.registerPermanentEffect(EffectRegistry.getInstance().getEffect(permanentEffect));
			
			cardList.add(newCard);
		
			cardList.add(newCard);
		}
		return cardList;
	}
	
	/**
	 * method which return a list of excommunication card from an external file correctly formatted. The method accept a FileReader object as parameter, 
	 * then parse it and finally returns the list of card (which can be passed to the Deck's constructor to generate an effectively deck structure)
	 * 
	 * @param fileReader  contains the FileReader object relative to the external file to parse
	 * @return the list of the cards generated from the JSON external file
	 * @throws IOException
	 */
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
	/**
	 * perform the parsing of the configuration file
	 * @param fileReader  contains the FileReader object relative to the external file to parse
	 */
	public static void importConfigurationFile(FileReader fileReader){
		
	}	
}
