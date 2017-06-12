package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.ArrayList;
import java.util.Iterator;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class PermanentEffect {
	
	static EffectBuilder bonusPermanentEffect = (JsonValue payload) -> {
		
		String actionType = payload.asObject().get("actionType").asString();
		int actionValueBonus = payload.asObject().get("actionValueBonus").asInt();
		JsonValue flag = payload.asObject().get("flagCost");
		
		ArrayList <ResourceSet> discountList = new ArrayList();
		JsonValue cost = payload.asObject().get("cost");
		
		try{
			cost.asArray().forEach( item -> {
				JsonObject resObject = item.asObject();
				ResourceSet discount = new ResourceSet(resObject);
			//	resObject.iterator().forEachRemaining(resource -> {
			//		discount.addResource(resource.getName(), resource.getValue().asInt());
			//	});
			discountList.add(discount);
			});	
		} catch(NullPointerException e){};
		Effect permanentEffect = (b, p, a) -> {
			if(!a.getActionType().equals(actionType)){ // Action a is not the ActionType of the permanentEffect  
				return;
			}
			a.setActionValue(a.getActionValue() + actionValueBonus);
			try{
				try{
					// int choiceToDrop = p.decideWhichresource(discountList); // ask the client to select Resource to not Discount
					//discountList.remove(choiceToDrop); 
				} catch(NullPointerException e) {};
				for(ResourceSet r : discountList){
					((TowerRegion) b.getRegion(a.getActionRegionId()))
			    			.getTowerLayers()[a.getActionSpaceId()]
			    					.getCard().discountCard(r);;
				}
			} catch(NullPointerException e){};
		};
		return permanentEffect;
	};
	
	public static void loadBuilder() {
		EffectRegistry.getInstance().registerBuilder("PERMANENT", bonusPermanentEffect);
	}	
}
