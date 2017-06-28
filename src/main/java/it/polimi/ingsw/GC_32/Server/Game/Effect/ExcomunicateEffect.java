package it.polimi.ingsw.GC_32.Server.Game.Effect;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class ExcomunicateEffect {
	
	// Excommunication card 1-1, 1-2, 1-3, 1-4
	static EffectBuilder lessEffectBuilder = (JsonValue json) -> {
		
		String excommunicateFlag = json.asObject().get("RESOURCE").asString();
		Effect e = (Board b, Player p, Action a, ContextManager cm) -> {
			p.getExcomunicateFlag().add(excommunicateFlag);
		};
		return e;
	};

	// Excommunication card 3-1, 3-2, 3-3, 3-4, 3-5, 3-6, 3-7
	static EffectBuilder noEndPoint = (JsonValue json) -> {
		
		String excommunicateFlag = json.asObject().get("FLAG").asString();
		Effect e = (Board b, Player p, Action a, ContextManager cm) -> {
			p.getExcomunicateFlag().add(excommunicateFlag);
		};
		return e;
	};
	
	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("LESSRESOURCE", lessEffectBuilder);
		EffectRegistry.getInstance().registerBuilder("NOENDPOINTS", noEndPoint);

	}
}
