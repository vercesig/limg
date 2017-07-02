package it.polimi.ingsw.GC_32.Server.Game.Effect;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class ActionEffect {

	static EffectBuilder actionEffectBuilder = (JsonValue payload) -> {
		
		Effect actionEffect = (Board b, Player p, Action a, ContextManager cm) -> {
			/*payload.asObject().add("ACTIONID", a.getRegionId());
			payload.asObject().add("SPACEID", a.getActionSpaceId());*/
			cm.openContext(ContextType.ACTION, p, a, (JsonObject) payload);
		};
		return actionEffect;
	};	

	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("ACTION", actionEffectBuilder);
	}
	
}
