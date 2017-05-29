package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.Iterator;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class ResourceAdder {
	
	EffectBuilder buildAddResource = (JsonValue payload) -> {
		
		Iterator<Member> singleItem = payload.asObject().iterator();				
		ResourceSet resourceSet = new ResourceSet();
		
		while(singleItem.hasNext()){
			String resourceType = singleItem.next().getName();
			int value = payload.asObject().get(resourceType).asInt();
			resourceSet.setResource(resourceType, value);			
		}
		Effect e = (Board b, Player p, Action a) ->	
			p.getResources().addResource(resourceSet);
		return e;
	};
	
	private ResourceAdder() {
		EffectRegistry.getInstance().registerBuilder("ADD", buildAddResource);
	}
}