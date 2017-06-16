package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ClientRegion {

	private String type;
	private ArrayList<ClientActionSpace> actionSpaces;
	
	
	public ClientRegion(String type, JsonArray actionSpaces){
		this.type = type;
		this.actionSpaces = new ArrayList<ClientActionSpace>();
		
		actionSpaces.forEach(actionSpace -> {
			JsonObject slimActionSpace = actionSpace.asObject();
			ResourceSet bonus = null;
			if(!slimActionSpace.get("BONUS").asString().equals("#"))
				bonus = new ResourceSet(Json.parse(slimActionSpace.get("BONUS").asString()).asObject());			
			int actionValue = slimActionSpace.getInt("ACTIONVALUE", 1);
			boolean single = slimActionSpace.getBoolean("SINGLE", true);
			int regionID = slimActionSpace.get("REGIONID").asInt();
			int actionspaceID = slimActionSpace.get("SPACEID").asInt();
			this.actionSpaces.add(new ClientActionSpace(bonus,actionValue,single,regionID,actionspaceID));
		});
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("************************** "+type.toUpperCase()+"\n");
		actionSpaces.forEach(actionSpace -> tmp.append(actionSpace.toString()));
		return new String(tmp);
	}
	
	public ArrayList<ClientActionSpace> getActionSpaceList(){
		return this.actionSpaces;
	}
}
