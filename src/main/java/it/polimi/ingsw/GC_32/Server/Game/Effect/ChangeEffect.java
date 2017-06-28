package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.ArrayList;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class ChangeEffect {

	static EffectBuilder changeEffectBuilder = (JsonValue payload) -> {

		JsonArray payloadList = new JsonArray();
		
		if(payload.isArray()){
			payloadList = payload.asArray();
		}else{
			payloadList.add(payload);
		}
		
		JsonArray resourceInArray = new JsonArray();
		JsonArray resourceOutArray = new JsonArray();
		
		payloadList.forEach(item -> {
			JsonObject obj = item.asObject();
			JsonObject resourceIn = obj.get("RESOURCEIN").asObject();
			JsonObject resourceOut = obj.get("RESOURCEOUT").asObject();
			resourceInArray.add(resourceIn);
			resourceOutArray.add(resourceOut);
		});
		
		Effect changeEffect = (Board b, Player p, Action a, ContextManager cm) -> {
				JsonArray cmPayload = new JsonArray();
				cmPayload.add(resourceInArray);
				cmPayload.add(resourceOutArray);
				System.out.println("++++++++++++++++++ change effect apre context");
				cm.openContext(ContextType.CHANGE,p,a,cmPayload);
				
				JsonObject contextResponse = cm.waitForContextReply().asObject();
				int index = contextResponse.get("CHANGEID").asInt();
				
				p.getResources().addResource(new ResourceSet(cmPayload.get(index).asObject()));
				p.getResources().subResource(new ResourceSet(cmPayload.get(index).asObject()));
			};	
		return changeEffect;
	};
	
	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("CHANGE", changeEffectBuilder);
	}
	
}
