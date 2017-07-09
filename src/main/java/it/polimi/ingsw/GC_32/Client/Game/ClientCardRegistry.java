package it.polimi.ingsw.GC_32.Client.Game;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * singleton class which contains all the information about cards. Different from the CardRegistry server-side class, this registry doesn't contains any Card instance. 
 * Instead it contains only a map between card name and its JSON representation. Classes which uses ClientCardRegistry will perform their parsing logic on the JsonObject
 * returned by this class
 * 
 * <ul>
 * <li>{@link #instance}: the instance of this singleton class</li>
 * <li>{@link #cardRegistry}: HashMap which map each card name to its JSON representation, directly taken from extrenal file</li>
 * </ul>
 */

public class ClientCardRegistry {
	
	private static ClientCardRegistry instance;
	private HashMap<String, JsonObject> cardRegistry;
	
	/**
	 * initialize the data structure to support the registry
	 */
	private ClientCardRegistry(){
		this.cardRegistry = new HashMap<>();
	}
	
	/**
	 * return the instance of the card registry
	 * @return the instance ot this singleton class
	 */
	public static ClientCardRegistry getInstance(){
		if(instance==null){
			instance = new ClientCardRegistry();
		}
		return instance;
	}
	
	/**
	 * load cards from JSON external files
	 * @throws IOException
	 */
	public void init() throws IOException{
	    loadJson("cards.json");
	    loadJson("excom_cards.json");
	    loadJson("leader_cards.json");
	}
	
	/**
	 * given the specified path, this method simply load into the registry the card's JsonObject
	 * @param path the path of the external file to load
	 * @throws IOException
	 */
	private void loadJson(String path) throws IOException{
	    JsonArray cardList = Json.parse(getReader(path)).asArray();
	    cardList.forEach(item -> addCard(item));
	}

	private Reader getReader(String path){
		return  new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(path));
	}
	
	/**
	 * add card into the HashMap, referring every JsonObject to the card name
	 * @param item the JsonValue to add into the registry
	 */
	private void addCard(JsonValue item){
		String name = item.asObject().get("name").asString();
		JsonObject details = item.asObject().remove("name");
		this.cardRegistry.put(name, details);
	}
	
	/**
	 * allows to retrive the card details. I.e. the JsonObject representing the card
	 * @param cardName the name of the card to get
	 * @return the JsonObject representing the searched card
	 */
	public JsonObject getDetails(String cardName){
		return this.cardRegistry.get(cardName);
	}
}
