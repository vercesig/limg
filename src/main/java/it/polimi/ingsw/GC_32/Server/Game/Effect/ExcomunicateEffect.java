package it.polimi.ingsw.GC_32.Server.Game.Effect;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

/**
 * contains effect which are used to map excommuncation tile effects. This class contains two excommunication type effect:
 * 
 * <ul>
 * <li>{@link ExcomunicateEffect#lessEffectBuilder}: impelements all the effect of first period which represent a penalty on resource gained</li>
 * <li>{@link ExcomunicateEffect#flagEffect}: impelements particular effects which label the player with a special flag, in this way when the player
 * perform an action his flag will change the normal flow of the action check</li>
 * </ul>
 * 
 * @see Effect, EffectBuilder
 *
 */
public class ExcomunicateEffect {
	
		/**
		 * add a "LESSRESOURCE" flag to the player specifing what resource will be influenced by the effect (for example a "STONE" LESSRESOURCE effect will remove
		 * one STONE every time the player takes a STONE resource)
		 */
		static EffectBuilder lessEffectBuilder = (JsonValue json) -> {
			
			Effect e = (Board b, Player p, Action a, ContextManager cm) -> {
				p.getFlags().put("LESSRESOURCE", json.asArray()); 
				System.out.println("ATTIVATO EFFETTO LESSRESOURCE");
			};
			return e;
		};
		
		/**
		 * add a general flag, contained into the JSON description of the card, which will be added to the Flag list of the player affected by the excommuncation
		 */
		static EffectBuilder flagEffect = (JsonValue json) -> {
			Effect e = (Board b, Player p, Action a, ContextManager cm) -> {
				p.getFlags().put(json.asString(), null);
				System.out.println("ATTIVATO EFFETTO FLAG");
			};
			return e;
		};
		
		/**
		 * load the builder into the rgistry, lessEffectBuilder
		 */
		public static void loadBuilder(){
			EffectRegistry.getInstance().registerBuilder("LESSRESOURCE", lessEffectBuilder);
			EffectRegistry.getInstance().registerBuilder("FLAG", flagEffect);

		}
}
