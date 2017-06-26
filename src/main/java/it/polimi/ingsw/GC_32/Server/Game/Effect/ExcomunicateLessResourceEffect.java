package it.polimi.ingsw.GC_32.Server.Game.Effect;

import com.eclipsesource.json.JsonValue;

public class ExcomunicateLessResourceEffect {
	static EffectBuilder lessEffectBuilder = (JsonValue json) -> {
		
		String excommunicateFlag = json.asObject().get("RESOURCE").asString();
		Effect e = (b, p, a) -> {
			p.getExcomunicateFlag().add(excommunicateFlag);
		};
		return e;
	};

	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("LESSRESOURCE", lessEffectBuilder);
	}
}
