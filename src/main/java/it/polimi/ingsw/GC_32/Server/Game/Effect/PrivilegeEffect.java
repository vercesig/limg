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
				boolean isValid = true;
				ResourceSet cost = null;											
				try{
					cost = new ResourceSet(payload.asObject().get("COST").asObject());
					isCostPrivilege = true;
				}catch(NullPointerException ecc){}
				
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
					if(response.size() != number || !isSet(response)){
						cm.setContextAck(false, p);
					} else {
						for(JsonValue val: response){
							if(val.isNumber()) // to check if the client has type 'n' (only for privilege with cost)
								p.getResources().addResource(new ResourceSet(Json.parse(val.asString()).asObject()));
							else
								isValid = false; // cost privilege effect has been cancelled
						}
						if(isCostPrivilege&&isValid){
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