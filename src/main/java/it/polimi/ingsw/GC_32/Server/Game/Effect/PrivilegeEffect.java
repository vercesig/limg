package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.ArrayList;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class PrivilegeEffect {

	static EffectBuilder buildPrivilege = (JsonValue payload) -> {
			int number = payload.asObject().get("NUMBER").asInt();
					 
			Effect e = (Board b, Player p, Action a, ContextManager cm) ->	{
				
				boolean isCostPrivilege = false;
				ResourceSet cost = null;
				JsonValue jCost = payload.asObject().get("COST");
				if(jCost != null){
					cost = new ResourceSet(jCost.asObject());
					isCostPrivilege = true;
				}
				
				if(isCostPrivilege){
					JsonArray privilegePayload = new JsonArray();				
						privilegePayload.add(number);
						privilegePayload.add(payload.asObject().get("COST").asObject());
					cm.openContext(ContextType.PRIVILEGE, p, a, privilegePayload);
				}else{				
					cm.openContext(ContextType.PRIVILEGE, p, a, Json.value(number));
				}

				while(true){
					JsonArray response = cm.waitForContextReply().asArray();
					System.out.println(response.toString());
					if(response.size() != number || !isSet(response)){
						cm.setContextAck(false, p);
						return;
					} else {
						for(JsonValue val: response){
							System.out.println("+++++++++++ PRIVILEGE: "+val.toString());
							p.getResources().addResource(new ResourceSet(Json.parse(val.asString()).asObject()));
						}
						if(isCostPrivilege){
							System.out.println("+++++++++++++++++++ PRIVILEGE COST: "+cost.toString());
							p.getResources().subResource(cost);
						}
						cm.setContextAck(true, p);
						return;
					}
				}
			};
			return e;
		};
		
	public static void loadBuilder() {
		EffectRegistry.getInstance().registerBuilder("PRIVILEGE", buildPrivilege);
	}
		
	private static boolean isSet(JsonArray array){
		ArrayList<JsonObject> nums = new ArrayList<>();
		for(JsonValue val: array){
			if(nums.contains(Json.parse(val.asString()).asObject())){
				return false;
			}
			nums.add(Json.parse(val.asString()).asObject());
		}
		return true;
	}
}