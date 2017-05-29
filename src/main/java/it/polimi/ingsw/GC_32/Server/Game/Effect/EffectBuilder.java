package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.function.Function;
import com.eclipsesource.json.JsonValue;

public interface EffectBuilder extends Function<JsonValue,Effect>{}