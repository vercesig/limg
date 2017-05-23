package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.HashMap;

public class EffectRegistry{

    private static EffectRegistry instance;
    private HashMap<String, Effect> registry;

    private EffectRegistry(){
        this.registry = new HashMap<String, Effect>();
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
}
