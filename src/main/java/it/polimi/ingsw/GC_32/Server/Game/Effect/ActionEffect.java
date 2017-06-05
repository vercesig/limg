package it.polimi.ingsw.GC_32.Server.Game.Effect;


import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class ActionEffect {

	static EffectBuilder actionEffectBuilder = (JsonValue payload) -> {
		
		Effect actionEffect = (Board b, Player p, Action a) -> {
			JsonValue actionPayload = payload;
			p.makeAction(actionPayload);
		};
		return actionEffect;
	};	

	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("ACTION", actionEffectBuilder);
	}
	
}