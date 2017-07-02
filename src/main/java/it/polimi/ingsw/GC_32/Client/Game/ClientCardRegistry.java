package it.polimi.ingsw.GC_32.Client.Game;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ClientCardRegistry {
	
	private static ClientCardRegistry instance;
	private HashMap<String, JsonObject> cardRegistry;
	
	private ClientCardRegistry(){
		this.cardRegistry = new HashMap<String, JsonObject>();
		loadJson("cards.json");
		loadJson("excom_cards.json");
		loadJson("leader_cards.json");
	}
	
	public static ClientCardRegistry getInstance(){
		if(instance==null){
			instance = new ClientCardRegistry();
		}
		return instance;
	}
	
	private void loadJson(String path){
		try {
			JsonArray cardList = Json.parse(getReader(path)).asArray();
			cardList.forEach(item -> addCard(item));
		} 
		catch (IOException e) {}
	}
	
	private Reader getReader(String path){
		return  new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(path));
	}
	
	private void addCard(JsonValue item){
		String name = item.asObject().get("name").asString();
		JsonObject details = item.asObject().remove("name");
		this.cardRegistry.put(name, details);
	}
	
	public JsonObject getDetails(String cardName){
		return this.cardRegistry.get(cardName);
	}
}
