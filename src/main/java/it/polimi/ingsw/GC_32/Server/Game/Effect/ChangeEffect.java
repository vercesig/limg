package it.polimi.ingsw.GC_32.Server.Game.Effect;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

/**
 * generate an CHANGE effect. CHANGE effect allow the player to change some resources for another type of resources. To do this, change effect take an index from
 * an extra field into the action passed as argument to the apply function, choosed by the player throught a context precedently opened, and then perform the change
 * 
 * @see EffectRegistry
 */
public class ChangeEffect {

	/**
	 * generate a CHANGE effect. CHANGE effect allow the player to change resources
	 * 
	 * @see EffectRegistry
	 */
	static EffectBuilder changeEffectBuilder = (JsonValue payload) -> {

		Effect changeEffect = (Board b, Player p, Action a, ContextManager cm) -> {
			
				JsonValue indexResponse = a.getAdditionalInfo().get("CHANGEID");				
				JsonArray effectArray = new JsonArray();
				
				if(payload.isArray())
					effectArray = payload.asArray();
				else
					effectArray.add(payload.asObject());
				
				if(indexResponse.isNumber()){
					JsonObject selectedChange = effectArray.get(indexResponse.asInt()).asObject();
					p.getResources().subResource(new ResourceSet(selectedChange.get("RESOURCEIN").asObject()));
					p.getResources().addResource(new ResourceSet(selectedChange.get("RESOURCEOUT").asObject()));
				}
			};	
		return changeEffect;
	};
	/**
	 * load the builder into the EffectRegistry with flag "CHANGE"
	 */
	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("CHANGE", changeEffectBuilder);
	}
	
}
