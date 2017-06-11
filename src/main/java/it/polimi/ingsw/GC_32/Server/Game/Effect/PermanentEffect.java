package it.polimi.ingsw.GC_32.Server.Game.Effect;

import com.eclipsesource.json.JsonValue;

public class PermanentEffect {
	
	static EffectBuilder bonusPermanentEffect = (JsonValue payload) -> {
		JsonValue type = payload.asObject().get("TYPE");
		JsonValue forEach = payload.asObject().get("FOREACH");
		JsonValue quantity = payload.asObject().get("QUANTITY");
		JsonValue increase = payload.asObject().get("INCREASE");
		JsonValue increasingQuantity = payload.asObject().get("INCREASINGQUANTITY");
		return null;
	};
}
