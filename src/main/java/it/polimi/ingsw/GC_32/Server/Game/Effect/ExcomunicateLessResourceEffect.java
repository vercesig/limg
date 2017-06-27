package it.polimi.ingsw.GC_32.Server.Game.Effect;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class ExcomunicateLessResourceEffect {
	static EffectBuilder lessEffectBuilder = (JsonValue json) -> {
		
		String excommunicateFlag = json.asObject().get("RESOURCE").asString();
		Effect e = (Board b, Player p, Action a, ContextManager cm) -> {
			p.getExcomunicateFlag().add(excommunicateFlag);
		};
		return e;
	};

	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("LESSRESOURCE", lessEffectBuilder);
	}
}
