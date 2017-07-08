package it.polimi.ingsw.GC_32.Server.Game.Effect;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

/**
 * generate an ACTION effect. ACTION effect allow the player to perform an extra action. To do this an ACTION effect open a context on the screen of the client
 * 
 * @see EffectRegistry
 */
public class ActionEffect {

	/**
	 * builder used to generate ACTION type effects. ActionEffectBuilder are loaded into the EffectRegistry under the flag "ACTION"
	 * 
	 * @param JsonValue representing the effect 
	 * @return the effect obtained by the payload passed as argument
	 */
	static EffectBuilder actionEffectBuilder = (JsonValue payload) -> {
		
		Effect actionEffect = (Board b, Player p, Action a, ContextManager cm) -> {
			cm.openContext(ContextType.ACTION, p, a, (JsonObject) payload);
		};
		return actionEffect;
	};	

	/**
	 * load the builder into the EffectRegistry with flag "ACTION"
	 */
	public static void loadBuilder(){
		EffectRegistry.getInstance().registerBuilder("ACTION", actionEffectBuilder);
	}
	
}
