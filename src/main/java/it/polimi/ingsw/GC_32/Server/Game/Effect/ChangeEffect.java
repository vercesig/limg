package it.polimi.ingsw.GC_32.Server.Game.Effect;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class ChangeEffect {

	static EffectBuilder changeEffectBuilder = (JsonValue payload) -> {

		
		Effect changeEffect = (Board b, Player p, Action a, ContextManager cm) -> {
			
				JsonValue indexResponse = a.getAdditionalInfo().get("CHANGEID");				
				JsonArray effectArray = new JsonArray();
				
				if(payload.isArray())
					effectArray = payload.asArray();
				else
					effectArray.add(payload.asObject());
				
				if(indexResponse.isNumber()){
					p.getResources().addResource(new ResourceSet(effectArray.get(indexResponse.asInt()).asObject().get("RESOURCEOUT").asObject()));
					p.getResources().subResource(new ResourceSet(effectArray.get(indexResponse.asInt()).asObject().get("RESOURCEIN").asObject()));
				}
			};	
		return changeEffect;
	};
	
	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("CHANGE", changeEffectBuilder);
	}
	
}
