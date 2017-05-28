package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.Iterator;
import java.util.function.Function;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class EffectBuilder {
	
	Function <JsonObject, Effect> buildAddResource = (JsonObject payload) -> {
		
		Iterator<Member> singleItem = payload.iterator();				
		ResourceSet resourceSet = new ResourceSet();		
		
		while(singleItem.hasNext()){
			String resourceType = singleItem.next().getName();
			int value = payload.get(resourceType).asInt();
			resourceSet.setResource(resourceType, value);			
		}
		Effect e = (Board b, Player p, Action a) ->	
			p.getResources().addResource(resourceSet);
		return e;
	};
	
	private EffectBuilder() {
		EffectRegistry.getInstance().registerBuilder("Add", buildAddResource);
	}
}
