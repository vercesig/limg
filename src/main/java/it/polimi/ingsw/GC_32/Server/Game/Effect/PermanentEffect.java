package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.ArrayList;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;

public class PermanentEffect {
	
	static EffectBuilder bonusPermanentEffect = (JsonValue payload) -> {
		
		String actionType = payload.asObject().get("TYPE").asString();
		int regionID = payload.asObject().get("REGIONID").asInt();
		int actionValueBonus = payload.asObject().get("BONUSACTIONVALUE").asInt();
		JsonValue flag = payload.asObject().get("EXCLUSIVEBONUS");
		
		ArrayList<ResourceSet> discountList = new ArrayList<>();
		JsonValue cost = payload.asObject().get("BONUSRESOURCE");
		JsonArray costArray = new JsonArray();
		if(cost.isArray())
			costArray = cost.asArray();
		else
			costArray.add(cost);
		
		if(!cost.isNull()){
			costArray.forEach( item -> {
				JsonObject resObject = item.asObject();
				ResourceSet discount = new ResourceSet(resObject);
			//	resObject.iterator().forEachRemaining(resource -> {
			//		discount.addResource(resource.getName(), resource.getValue().asInt());
			//	});
			discountList.add(discount);
			});	
		}
		Effect permanentEffect = (b, p, a) -> {
			
			if(!(a.getActionType().equals(actionType)&& a.getActionRegionId()==regionID)) { // Action a is not the ActionType of the permanentEffect  
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
