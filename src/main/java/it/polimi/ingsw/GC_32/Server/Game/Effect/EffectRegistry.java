package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.HashMap;
import java.util.function.Function;

public class EffectRegistry{

    private static EffectRegistry instance;
    private HashMap<String, Effect> registry;
    private HashMap<String, Function> builderEffect;

    private EffectRegistry(){
        this.registry = new HashMap<String, Effect>();
        this.builderEffect = new HashMap<String, Function>();
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
    
    // Builder
    public void registerBuilder(String effectCode, Function builder){ 
        this.builderEffect.put(effectCode, builder);
    }
    public Function getBuilder(String effectCode){	
        return this.builderEffect.get(effectCode);
    }
}
