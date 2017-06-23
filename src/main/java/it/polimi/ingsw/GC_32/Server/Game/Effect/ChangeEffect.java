package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.ArrayList;
import java.util.logging.Level;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class ChangeEffect {

	static EffectBuilder changeEffectBuilder = (JsonValue payload) -> {

		JsonArray payloadList = new JsonArray();
		ArrayList<ResourceSet> chanches = new ArrayList<ResourceSet>();
		
		if(payload.isArray()){
			payloadList = payload.asArray();
		}else{
			payloadList.add(payload);
		}
		
		payloadList.forEach(item -> {
			JsonObject obj = item.asObject();
			ResourceSet resourceSet = new ResourceSet();
			
			obj.iterator().forEachRemaining(resource -> {
				resourceSet.setResource(resource.getName(), resource.getValue().asInt());
			});
			chanches.add(resourceSet);
		});
		
		Effect changeEffect = (Board b, Player p, Action a) -> {
				ArrayList<ResourceSet> changeList = chanches;
				try{
					p.getResources().addResource(changeList.get(a.getAdditionalInfo().get("INDEX_EFFECT").asInt()));
					if(p.getResources().hasNegativeValue()){
						a.invalidate();
					}
				}catch(NullPointerException e){
					Logger.getLogger("").log(Level.SEVERE, "context", e);
				}
			};	
		return changeEffect;
	};
	
	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("CHANGE", changeEffectBuilder);
	}
	
}
