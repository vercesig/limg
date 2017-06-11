package it.polimi.ingsw.GC_32.Client.Network;

import java.util.ArrayList;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;

public class SlimRegion {

	private String type;
	private ArrayList<SlimActionSpace> actionSpaces;
	
	
	public SlimRegion(String type, JsonArray actionSpaces){
		this.type = type;
		this.actionSpaces = new ArrayList<SlimActionSpace>();
		
		actionSpaces.forEach(actionSpace -> {
			JsonObject slimActionSpace = actionSpace.asObject();
			ResourceSet bonus = null;
			if(!slimActionSpace.get("BONUS").isNull())
				bonus = new ResourceSet(Json.parse(slimActionSpace.get("BONUS").asString()).asObject());			
			int actionValue = slimActionSpace.getInt("ACTIONVALUE", 1);
			boolean single = slimActionSpace.getBoolean("SINGLE", true);
			int regionID = slimActionSpace.get("REGIONID").asInt();
			int actionspaceID = slimActionSpace.get("SPACEID").asInt();
			this.actionSpaces.add(new SlimActionSpace(bonus,actionValue,single,regionID,actionspaceID));
		});
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("************************** "+type.toUpperCase()+"\n");
		actionSpaces.forEach(actionSpace -> tmp.append(actionSpace.toString()));
		return new String(tmp);
	}
}
