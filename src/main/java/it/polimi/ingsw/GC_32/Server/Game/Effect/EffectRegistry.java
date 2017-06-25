package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.HashMap;
import com.eclipsesource.json.JsonValue;

/**
 * EffectRegistry is a singleton class which contains all the references to all the effect of the game. It contains custom effect (i.e. effect which are 
 * effectively written into the code and than loaded into the registry) and effect builders (i.e. functions which can dinamically generate effect 
 * by parsing JSON card description file, the effect generated is then associated to the correct card)
 * <ul>
 *  <li> {@link EffectRegistry#instance}: the unique instance of the EffectRegistry
 *  <li> {@link #registry}: an hashmap which map each effect with a specific OPCODE.
 *  <li> {@link #builderRegistry}: an hashmap which map each effectBuilder with a specific OPCODE. 
 * </ul>
 * @author alessandro
 *
 */
public class EffectRegistry{

    private static EffectRegistry instance;
    private HashMap<String, Effect> registry;
    private HashMap<String, EffectBuilder> builderRegistry;

    private EffectRegistry(){
        this.registry = new HashMap<String, Effect>();
        this.builderRegistry = new HashMap<String, EffectBuilder>();
    }

    /**
     * allows to obtain the unique instance of the registry
     * @return the instance of the registry
     */
    public static EffectRegistry getInstance(){
        if( instance == null ){
            instance = new EffectRegistry();
            AddEffect.loadBuilder();
            BonusEffect.loadBuilder();
            ChangeEffect.loadBuilder();
            ActionEffect.loadBuilder();
            PermanentEffect.loadBuilder();
            UniqueEffect.loadBuilder();
            PrivilegeEffect.loadBuilder();
        }
        return instance;
    }

    /**
     * allows to associate a specific effect to a specific opcode and then load it into the registry.
     * @param effectCode	the code which represent the effect
     * @param effect		the effect to load
     */
    protected void registerEffect(String effectCode, Effect effect){
        this.registry.put(effectCode, effect);
    }

    /**
     * given the opcode return the effect associated with it.
     * @param effectCode	the code which represent the effect
     * @return	the effect into the registry associated with the specific opcode passed as parameter
     */
    public Effect getEffect(String effectCode){
        return this.registry.get(effectCode);
    }
    
    
    /**
     * given the opcode of the effectBuilder and the payload associated with the effect return the correct method build on the information inside the payload
     * @param effectCode	the opcode which represent the effectBuilder
     * @param payload	the payload which represent the specific method
     * @return the method generated by the builder
     */
    public Effect getEffect(String effectCode, JsonValue payload){
    	return this.builderRegistry.get(effectCode).apply(payload);
    }

    /**
     * allows to associate a builder with a specific opcode, in this way the builder will be simply called by its opcode
     * @param effectCode	the code which represent the builder
     * @param builder	the implementation of the builder
     */
    public void registerBuilder(String effectCode, EffectBuilder builder){
    	this.builderRegistry.put(effectCode, builder);
    }
}
