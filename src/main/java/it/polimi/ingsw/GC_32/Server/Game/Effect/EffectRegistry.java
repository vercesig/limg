package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.HashMap;
import com.eclipsesource.json.JsonValue;

public class EffectRegistry{

    private static EffectRegistry instance;
    private HashMap<String, Effect> registry;
    private HashMap<String, EffectBuilder> builderRegistry;

    private EffectRegistry(){
        this.registry = new HashMap<String, Effect>();
        this.builderRegistry = new HashMap<String, EffectBuilder>();
    }

    public static EffectRegistry getInstance(){
        if( instance == null )
            instance = new EffectRegistry();
        return instance;
    }

    protected void registerEffect(String effectCode, Effect effect){
        this.registry.put(effectCode, effect);
    }

    public Effect getEffect(String effectCode){
        return this.registry.get(effectCode);
    }
    
    public Effect getEffect(String effectCode, JsonValue payload){
    	return this.builderRegistry.get(effectCode).apply(payload);
    }

    public void registerBuilder(String effectCode, EffectBuilder builder){
    	this.builderRegistry.put(effectCode, builder);
    }
}
