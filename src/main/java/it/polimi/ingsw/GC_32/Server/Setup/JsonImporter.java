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
			
			if(cardType.equals("TERRITORYCARD") || cardType.equals("BUILDINGCARD")){
				JsonValue action = card.get("minimumActionValue");	
				actionValue = action.asInt();
			} else {
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
						newCard.registerPermanentEffectType(permanentEffectArray.get(i).asString());
						newCard.addPayload(permanentPayloadArray.get(i)); //used by CHANGE effect for pretty context build
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

			String name = card.get("name").asString();
			int period = card.get("period").asInt();
			
			ExcommunicationCard newCard = new ExcommunicationCard(name, period);

			try{
				if(!card.get("instantEffect").isNull() && !card.get("instantPayload").isNull()){
					newCard.registerInstantEffect(EffectRegistry.getInstance()
							.getEffect(card.get("instantEffect").asString(),
									   card.get("instantPayload").asArray()));
				}
			}
			catch(NullPointerException e){}
			
			try{
				if(!card.get("permanentEffect").isNull()){
					try{
						card.get("permanentPayload").isNull();;
					}
					catch(NullPointerException e){	// carte con Payload Permanent nullo
						newCard.registerPermanentEffect(EffectRegistry.getInstance()
						       .getEffect(card.get("permanentEffect").asString()));
					}
					newCard.registerPermanentEffect((EffectRegistry.getInstance()
							.getEffect(card.get("permanentEffect").asString(),
									   card.get("permanentPayload").asObject())));
				}
			}catch(NullPointerException e){}
			
			cardList.add(newCard);
		}
		return cardList;
	}
	
	public static List<LeaderCard> importLeaderCard(Reader fileReader) throws IOException{
		
		ArrayList<LeaderCard> cardList = new ArrayList<LeaderCard>();
		JsonArray jsonCardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : jsonCardList){
			JsonObject card = item.asObject();

			String name = card.get("name").asString();
			JsonObject requirements =  card.get("requirements").asObject();
			
			LeaderCard newCard = new LeaderCard(name, requirements);
			
			try{
				card.get("instantEffect");
				try{
					card.get("instantPayload");
					newCard.registerInstantEffect(EffectRegistry.getInstance()
							.getEffect(card.get("instantEffect").asString(),
									   card.get("instantPayload")));
				}catch(NullPointerException e){ // carte con payload instant nullo
					newCard.registerInstantEffect(EffectRegistry.getInstance()
						    .getEffect(card.get("instantEffect").asString()));
				}
			}catch(NullPointerException e){}
		
			
			try{
				card.get("permanentEffect");
				try{
					card.get("permanentPayload");
					newCard.registerPermanentEffect((EffectRegistry.getInstance()
							.getEffect(card.get("permanentEffect").asString(),
									   card.get("permanentPayload").asObject())));
				}catch(NullPointerException e){
					newCard.registerInstantEffect(EffectRegistry.getInstance()
						    .getEffect(card.get("permanentEffect").asString()));
				}
				
			}
			catch(NullPointerException e){}
			try{
				if(!card.get("flagEffect").isNull() && !card.get("flagPayload").isNull()){
					newCard.registerPermanentEffect(EffectRegistry.getInstance()
						    .getEffect(card.get("flagEffect").asString(), card.get("flagPayload")));
				}
			} catch(NullPointerException e){}	
			
			cardList.add(newCard);
		}
		return cardList;
	}
	
	public static JsonValue importSingleCard(Reader fileReader, String cardName) throws IOException{
		
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
