package it.polimi.ingsw.GC_32.Server.Setup;

import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;
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
 			
			try{
				JsonValue action = card.get("minimumActionValue");	
				actionValue = action.asInt();
				actionValue = 0;
			}catch(NullPointerException e){
				actionValue = 0;
			}
			DevelopmentCard newCard = new DevelopmentCard(name, period, cardType, actionValue);
			
			// registrazione costi e requisiti
			try{
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
			}catch(NullPointerException e){}
			
			try{
				JsonValue requirements = card.get("requirements");
				if(requirements != null && !requirements.isNull())
					newCard.setRequirments(requirements.asObject());
			}
			catch(NullPointerException e){}
			
			
			// registrazione effetti
			try{
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
						newCard.registerInstantEffect(EffectRegistry.getInstance()
																	.getEffect(instantEffectArray.get(i).asString(),
																			   instantPayloadArray.get(i)));
					}
				}	
			}catch(NullPointerException e){}
			try{
				JsonValue permanentEffect = card.get("permanentEffect");
				JsonArray permanentEffectArray = new JsonArray();
				JsonValue permanentPayload = card.get("permanentPayload");
				JsonArray permanentPayloadArray = new JsonArray();
				if(!permanentEffect.isNull()&&!permanentPayload.isNull()){
					if(permanentEffect.isArray()){
						permanentEffectArray = permanentEffect.asArray();
						permanentPayloadArray = permanentPayload.asArray();
					}else{
						permanentEffectArray.add(permanentEffect);
						permanentPayloadArray.add(permanentPayload);
					}
					for(int i=0; i<permanentEffectArray.size(); i++){
						newCard.registerPermanentEffect(EffectRegistry.getInstance()
																	  .getEffect(permanentEffectArray.get(i).asString(),
																			     permanentPayloadArray.get(i)));
					}
				}
			}catch(NullPointerException e){
				if(name.equals("Preacher")){ // Preacher unica carta con un Effetto permanente senza payload
					System.out.println("REGISTRO PREACHER");
					JsonValue permanentEffect = card.get("permanentEffect");
					newCard.registerPermanentEffect(EffectRegistry.getInstance()
							  .getEffect(permanentEffect.asString()));
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
			if(card.get("name").asString().equals(cardName)){
				return card;
			}
		}
		return null;
	}
	
	public static ArrayList<PersonalBonusTile> importPersonalBonusTile(Reader fileReader) throws IOException{
		
			JsonArray personalBonusTileList = Json.parse(fileReader).asArray();
			ArrayList<PersonalBonusTile> tmpList = new ArrayList<PersonalBonusTile>();
			
			for(JsonValue item : personalBonusTileList){
					JsonObject bonusTile = item.asObject();
					
				PersonalBonusTile personalBonus = new PersonalBonusTile(bonusTile.get("PRODUCTION").asObject(),
																		bonusTile.get("HARVEST").asObject(),
																		false);
				tmpList.add(personalBonus);			
			}
			return tmpList;		
		}
	
	/**
	 * perform the parsing of the configuration file
	 * @param fileReader  contains the FileReader object relative to the external file to parse
	 * @throws IOException 
	 */
	public static JsonArray importBonusSpace(Reader fileReader) throws IOException{
		
		return Json.parse(fileReader).asArray();
	}
}
