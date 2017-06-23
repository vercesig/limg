package it.polimi.ingsw.GC_32.Server.Setup;

import it.polimi.ingsw.GC_32.Server.Game.Card.*;
import it.polimi.ingsw.GC_32.Server.Game.Effect.*;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
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
	public static List<DevelopmentCard> importDevelopmentCard(Reader fileReader) throws IOException{
		
		ArrayList<DevelopmentCard> cardList = new ArrayList<DevelopmentCard>();
		
		JsonArray JsonCardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : JsonCardList){
			JsonObject card = item.asObject();

			String name = card.get("name").asString();
			String cardType = card.get("cardType").asString();
			Integer period = card.get("period").asInt();
			Integer actionValue;
			JsonValue action = card.get("minimumActionValue");
	
			if(!action.isNull())
				actionValue = action.asInt();
			else 
				actionValue = 0;
			
			DevelopmentCard newCard = new DevelopmentCard(name, period, cardType, actionValue);
			
			// registrazione costi e requisiti
			JsonValue requirements = card.get("requirements");
			JsonValue resourceCost = card.get("cost");
			if(!resourceCost.isNull()){
				JsonArray resourceArray = new JsonArray();
				if( resourceCost.isObject() ){ // Carta con costo singolo
					resourceArray.add(resourceCost);
				}
				if( resourceCost.isArray() ){ // Carta con costo multiplo
					resourceArray = resourceCost.asArray();
				}
				newCard.registerCost(resourceArray.iterator());
			}
			if(requirements != null && !requirements.isNull())
				newCard.setRequirments(requirements.asObject());
			
			// registrazione effetti
			JsonValue instantEffect = card.get("instantEffect");
			JsonArray instantEffectArray = new JsonArray();
			JsonValue instantPayload = card.get("instantPayload");
			JsonArray instantPayloadArray = new JsonArray();			
			if(!instantEffect.isNull()&&!instantPayload.isNull()){
				if(instantEffect.isArray()){
					instantEffectArray = instantEffect.asArray();
					instantPayloadArray = instantPayload.asArray();
				}else{
					instantEffectArray.add(instantEffect);
					instantPayloadArray.add(instantPayload);
				}
				for(int i=0; i<instantEffectArray.size(); i++){
					newCard.registerInstantEffect(EffectRegistry.getInstance().getEffect(instantEffectArray.get(i).asString(), instantPayloadArray.get(i)));
				}
			}	
			JsonValue permanentEffect = card.get("permanentEffect");
			if(!permanentEffect.isNull()){
				JsonValue permanentPayload = card.get("permanentPayload");
				if(permanentPayload.isNull()){ //effetto custom
					newCard.registerPermanentEffect(EffectRegistry.getInstance().getEffect(permanentEffect.asString())); //effetto permanente custom
				}else{
					newCard.registerPermanentEffect(EffectRegistry.getInstance().getEffect(permanentEffect.asString(), permanentPayload));
				}
			}
			
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
	public static List<ExcommunicationCard> importExcommunicationCard(Reader fileReader) throws IOException{
		
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
	
	public static JsonValue importSingleCard(Reader fileReader, String cardName) throws IOException{
		
		ArrayList<DevelopmentCard> cardList = new ArrayList<DevelopmentCard>();
		JsonArray JsonCardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : JsonCardList){
			JsonObject card = item.asObject();
			if(card.get("name").equals(cardName)){
				return card;
			}
		}
		return null;
	}
	
	/**
	 * perform the parsing of the configuration file
	 * @param fileReader  contains the FileReader object relative to the external file to parse
	 */
	public static void importConfigurationFile(Reader fileReader){
		
	}
	
	
}
