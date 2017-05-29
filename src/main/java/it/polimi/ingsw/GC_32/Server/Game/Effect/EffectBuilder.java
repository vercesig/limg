package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.function.Function;
import com.eclipsesource.json.JsonValue;

/**
 * interface for EffectBuilder. an EffectBuilder ia a Function which take as argument a JSON object, called payload, into whom it is contained all the information
 * necessary to build the effect. EffectBuilder returns an Effect (which will be associated to the card which has generated such effect).
 * 
 * @extends Function<JsonValue, Effect>
 * @author alessandro
 *
 */
public interface EffectBuilder extends Function<JsonValue,Effect>{}