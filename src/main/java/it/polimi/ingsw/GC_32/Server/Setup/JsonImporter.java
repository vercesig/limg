package it.polimi.ingsw.GC_32.Server.Setup;

import it.polimi.ingsw.GC_32.Common.Utils.Tuple;
import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;
import it.polimi.ingsw.GC_32.Server.Game.Card.*;
import it.polimi.ingsw.GC_32.Server.Game.Effect.*;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * allows to import and parse (from JSON files) external file like card description's file or game configuration files. The utilities here implemented are used
 * from Setup to load cards or configuration file into the GameConfig class
 * 
 * @see Setup
 */


public class JsonImporter {

	private JsonImporter(){}
	
	/**
	 * method which return a list of development card from an external file correctly formatted. The method accept a FileReader object as parameter, 
	 * then parse it and finally returns the list of card (which can be passed to the Deck's constructor to generate an effectively deck structure)
	 * 
	 * @param fileReader  contains the FileReader object relative to the external file to parse
	 * @return the list of the cards generated from the JSON external file
	 * @throws IOException
	 */
	public static List<DevelopmentCard> importDevelopmentCards(Reader fileReader) throws IOException{
		
		ArrayList<DevelopmentCard> cardList = new ArrayList<>();
		
		JsonArray jsonCardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : jsonCardList){
			DevelopmentCard newCard = parseCard(item);
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
	public static List<ExcommunicationCard> importExcommunicationCards(Reader fileReader) throws IOException{
		
		ArrayList<ExcommunicationCard> cardList = new ArrayList<ExcommunicationCard>();
		JsonArray jsonCardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : jsonCardList){
			JsonObject card = item.asObject();

			String name = card.get("name").asString();
			int period = card.get("period").asInt();
			
			ExcommunicationCard newCard = new ExcommunicationCard(name, period);		
			registerCardEffects(newCard, card);
			cardList.add(newCard);
		}
		return cardList;
	}
	
	
	/**
	 * method which return a list of leader cards from an external file correctly formatted. The method accept a FileReader object as parameter, 
	 * then parse it and finally returns the list of card (which can be passed to the Deck's constructor to generate an effectively deck structure)
	 * 
	 * @param fileReader  contains the FileReader object relative to the external file to parse
	 * @return the list of the cards generated from the JSON external file
	 * @throws IOException
	 */
	public static List<LeaderCard> importLeaderCards(Reader fileReader) throws IOException{
		
		ArrayList<LeaderCard> cardList = new ArrayList<LeaderCard>();
		JsonArray jsonCardList = Json.parse(fileReader).asArray();
		
		for(JsonValue item : jsonCardList){
			JsonObject card = item.asObject();

			String name = card.get("name").asString();
			JsonObject requirements =  card.get("requirements").asObject();
			
			LeaderCard newCard = new LeaderCard(name, requirements);
			
			registerCardEffects(newCard, card);
			List<Tuple<JsonValue, JsonValue>> flagEffectList = parseEffect(card.get("flagEffect"),
			                                                               card.get("flagPayload"));
			if(!flagEffectList.isEmpty()){
			    newCard.registerPermanentEffect(getEffectFromRegistry(flagEffectList.get(0)
			                                                                        .getFirstArg()
			                                                                        .asString(),
			                                                                        flagEffectList.get(0).getSecondArg()));
			}
			cardList.add(newCard);
		}
		return cardList;
	}
	
	/**
	 * allows to parse a single card from an external JSON file
	 * 
	 * @param fileReader contains the FileReader object relative to the external file to parse
	 * @param cardName the name of the card to parse
	 * @return the JsonValue representig this card
	 * @throws IOException
	 */
	
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
	
	/**
	 * parse from an external file correctly formatted the information about personalBonusTiles and then return a list of them 
	 * 
	 * @param fileReader contains the FileReader object relative to the external file to parse
	 * @return an ArrayList of PersonalBonusTile
	 * @see PersonalBonusTile
	 * @throws IOException
	 */
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
	 * perform the parsing of the action space bonus' configuration file
	 * 
	 * @param fileReader  contains the FileReader object relative to the external file to parse
	 * @return a JsonArray representing the different bonus of all the action space of the board
	 * @throws IOException 
	 */
	public static JsonArray importBonusSpace(Reader fileReader) throws IOException{
		return Json.parse(fileReader).asArray();
	}
	
	/**
	 * perform the parsing of the victory points configuration relative to the faith track.
	 * 
	 * @param fileReader contains the FileReader object relative to the external file to parse 
	 * @return an HashMap which contains, for all the faith points value of the track, the correspondending value of victory points
	 * @throws IOException
	 */
	public static HashMap<Integer, Integer> importExcommunicationTrack(Reader fileReader) throws IOException{
		HashMap<Integer, Integer> track = new HashMap <Integer, Integer> ();
		JsonObject jsonTrack = Json.parse(fileReader).asObject();
		jsonTrack.forEach(member -> 
			track.put(Integer.parseInt((member.getName())), (Integer) member.getValue().asInt()));
		return track;
	}
	
	/**
	 * perform the parsing of the point conversion rules applied for the final score calculation.
	 * 
	 * @param fileReader contains the FileReader object relative to the external file to parse 
	 * @return an HashMap which map for for each rule (for example the number of cards or the victory points to assign to the first and second player on the
	 *  military track) the corresponding value (as a JsonValue)
	 * @throws IOException
	 */
	public static HashMap <String, JsonValue> importPointsConversion(Reader fileReader) throws IOException{
		HashMap<String, JsonValue> pointsConversion = new HashMap <String, JsonValue> ();
		JsonObject jsonPointsConversion = Json.parse(fileReader).asObject();
		jsonPointsConversion.forEach(member -> pointsConversion.put(member.getName(), member.getValue()));
		return pointsConversion;
	}
	
	/**
	 * perform the parsing of a single development card
	 * 
	 * @param jCard the JsonValue representing the card to parse
	 * @see DevelopmentCard, Tuple, EffectRegistry
	 * @return the DevelopmentCard object obtained from its JsonValue representation
	 */
	public static DevelopmentCard parseCard(JsonValue jCard){
	    JsonObject card = jCard.asObject();

        String name = card.get("name").asString();
        String cardType = card.get("cardType").asString();
        Integer period = card.get("period").asInt();
        Integer actionValue;
        
        if("TERRITORYCARD".equals(cardType) || "BUILDINGCARD".equals(cardType)){
            JsonValue action = card.get("minimumActionValue");  
            actionValue = action.asInt();
        } else {
            actionValue = 0;
        }

        DevelopmentCard newCard = new DevelopmentCard(name, period, cardType, actionValue);
                    
        // registrazione costi e requisiti
        JsonValue resourceCost = card.get("cost");
        if(resourceCost != null && !resourceCost.isNull()){
            JsonArray resourceArray = new JsonArray();
            if( resourceCost.isObject() ){ // Carta con costo singolo
                resourceArray.add(resourceCost);
            }
            if( resourceCost.isArray() ){ // Carta con costo multiplo
                resourceArray = resourceCost.asArray();
            }
            newCard.registerCost(resourceArray.iterator());
        }
        
        JsonValue requirements = card.get("requirements");
        if(requirements != null && !requirements.isNull())
            newCard.setRequirments(requirements.asObject());
        
        // registrazione effetti
        JsonValue instantEffect = card.get("instantEffect");
        JsonValue instantPayload = card.get("instantPayload");        
        List<Tuple<JsonValue, JsonValue>> instantEffectList = parseEffect(instantEffect,
                                                                          instantPayload);
        
        for(Tuple<JsonValue, JsonValue> effect: instantEffectList){
            newCard.registerInstantEffect(getEffectFromRegistry(effect.getFirstArg().asString(),
                                                                effect.getSecondArg()));
            newCard.registerInstantEffectType(effect.getFirstArg().asString());
        }

        JsonValue permanentEffect = card.get("permanentEffect");
        JsonValue permanentPayload = card.get("permanentPayload");
        List<Tuple<JsonValue, JsonValue>> permanentEffectList = parseEffect(permanentEffect,
                                                                          permanentPayload);
        for(Tuple<JsonValue, JsonValue> effect : permanentEffectList){
            newCard.registerPermanentEffect(getEffectFromRegistry(effect.getFirstArg().asString(), effect.getSecondArg()));
            newCard.addPayload(effect.getSecondArg()); //used by CHANGE effect for pretty context build
            newCard.registerPermanentEffectType(effect.getFirstArg().asString());
        }
        return newCard;
	}
	
	/**
	 * parse the effects of a single card
	 * 
	 * @param code the OPCODE representing the general effect type
	 * @param payload the JsonValue describing the specific effect
	 * @see Effect
	 * @return a list of tuple <effectCode, effectPayload>
	 */
	private static List<Tuple<JsonValue, JsonValue>> parseEffect(JsonValue code, JsonValue payload){
	    ArrayList<Tuple<JsonValue, JsonValue>> result = new ArrayList<>();
	    if(code != null && !code.isNull()){
	        if(code.isString()){
	            if(payload != null && !payload.isNull()){
	                result.add(new Tuple<>(code, payload));
	            } else {
	                result.add(new Tuple<>(code, null));
	            }
	        } else {
	            addEffectArray(result, code.asArray(), (payload != null)? payload.asArray() : null);
	        }
	    }
	    return result;
	}
	
	/**
	 * given a JsonArray of effect OPCODE of the card and a JsonArray of payload which describes these effects, builds a list of tuple <effectCode, effectPayload> 
	 * which can be used in the parsing
	 * 
	 * @param list a list of tuple <effectCode, effectPayload>
	 * @param code a JsonArray of effect OPCODE
	 * @param payload a JsonArray of payload
	 */
	private static void addEffectArray(List<Tuple<JsonValue, JsonValue>> list, JsonArray code, JsonArray payload){
	    if(payload != null){
	        for(int i = 0; i < code.size(); i++){
	            list.add(new Tuple<JsonValue, JsonValue>(code.get(i), payload.get(i)));
	        }
	    } else {
	        for(JsonValue singleCode : code){
	            list.add(new Tuple<JsonValue, JsonValue>(singleCode, null));
	        }
	    }
	}
	
	/**
	 * 
	 * allows to retrive from the effect registry the specific effect of the card which is under parsing. The OPCODE is needed to call the correct effect builder, while
	 * the effectPayload will be used from the effect builder to build the exactly effect describing the specific card
	 * 
	 * @param effectCode the OPCODE of the effect to load
	 * @param effectPayload the JsonValue representing the spcific effect to get from the registry
	 * @see Effect, EffectBuilder
	 * @return the Effect build on the information passed as parameter
	 */
	private static Effect getEffectFromRegistry(String effectCode, JsonValue effectPayload){
	    Effect regEffect;
	    if(effectPayload != null){
            regEffect = EffectRegistry.getInstance()
                                             .getEffect(effectCode,
                                                        effectPayload);
        } else {
            regEffect = EffectRegistry.getInstance()
                                      .getEffect(effectCode);
        }
	    return regEffect;
	}
	
	/**
	 * 
	 * this method is responsable of loading the effect into the Card class, given its JSON representation (used to build the effects)
	 * 
	 * @see Card, Effect
	 * @param newCard the card into which the effect must be registered
	 * @param card the JsonObject representing this card
	 * 
	 */
	
	private static void registerCardEffects(Card newCard, JsonObject card){
	    List<Tuple<JsonValue, JsonValue>> instantEffectList = parseEffect(card.get("instantEffect"),
                                                                          card.get("instantPayload"));
	    if(!instantEffectList.isEmpty()){
	        Tuple<JsonValue, JsonValue> instantEffect = instantEffectList.get(0);
	        newCard.registerInstantEffect(getEffectFromRegistry(instantEffect.getFirstArg().asString(),
	                                                            instantEffect.getSecondArg()));
	    }

	    List<Tuple<JsonValue, JsonValue>> permanentEffectList = parseEffect(card.get("permanentEffect"),
                                                                            card.get("permanentPayload"));
	    if(!permanentEffectList.isEmpty()){
            Tuple<JsonValue, JsonValue> permanentEffect = permanentEffectList.get(0);
            newCard.registerPermanentEffect(getEffectFromRegistry(permanentEffect.getFirstArg().asString(),
	                                                              permanentEffect.getSecondArg()));
	    }
	}
}
