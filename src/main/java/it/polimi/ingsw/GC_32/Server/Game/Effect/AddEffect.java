package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.Iterator;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

/**
 * ResourceAdder is kind of EffectBuilder which generate an effect of type ADD (this kind of effect (this kind of effect sum a specific quantity of resources, 
 * depending of the specific card, to the resources owned by the player). Every Effect which are a ADD-type effect must be generated by the BonusEffect builder 
 * referencing to it with the ADD opcode
 * @author alessandro
 *
 */
public class AddEffect {
	
	static EffectBuilder buildAddResource = (JsonValue payload) -> {
		
		Iterator<Member> singleItem = payload.asObject().iterator();				
		ResourceSet resourceSet = new ResourceSet();
		
		while(singleItem.hasNext()){
			String resourceType = singleItem.next().getName();
			int value = payload.asObject().get(resourceType).asInt();
			resourceSet.setResource(resourceType, value);			
		}
		Effect e = (Board b, Player p, Action a, ContextManager cm) ->	{
			
			// Excomunicate Negative buff: 
			// se ho addEffect di Wood devo perdere una Stone e un Wood;
			// se ho addEffect di Stone devo perdere una Stone e un Wood;
			// se ho addEffect di Wood e Stone?  perdo due Stone e due Wood;
			if(p.isFlagged("LESSRESOURCE")){ // DA TESTARE
				 JsonArray malusResource = p.getFlags().get("LESSRESOURCE").asArray();
					for (String key: resourceSet.getResourceSet().keySet()){
						malusResource.forEach(member -> {
							if(member.asString().equals(key)){
								p.getResources().addResource(key, -1);
							}
						});
					}
				}
			System.out.println(resourceSet.toString());
			p.getResources().addResource(resourceSet);
			
			//Santa Rita
			if(p.isFlagged("DOUBLE")){ // DA TESTARE
				resourceSet.getResourceSet().remove("FAITH_POINTS");
				resourceSet.getResourceSet().remove("VICTORY_POINTS");
				resourceSet.getResourceSet().remove("MILITARY_POINTS");
				
				p.getResources().addResource(resourceSet); // doppie risorse!!!
			}
		};
		return e;
	};
	
	public static void loadBuilder() {
		EffectRegistry.getInstance().registerBuilder("ADD", buildAddResource);
	}
}