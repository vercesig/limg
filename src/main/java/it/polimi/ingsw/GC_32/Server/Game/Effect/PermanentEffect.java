package it.polimi.ingsw.GC_32.Server.Game.Effect;

import java.util.ArrayList;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class PermanentEffect {
	
	static EffectBuilder bonusPermanentEffect = (JsonValue payload) -> {
		
		String actionType = payload.asObject().get("TYPE").asString();
		int regionID = payload.asObject().get("REGIONID").asInt();
		
		int actionValueBonus = payload.asObject().getInt("BONUSACTIONVALUE", 0);
		ResourceSet bonusDiscount;
		boolean flag;
		
		if(!payload.asObject().get("BONUSRESOURCE").isNull()){
			bonusDiscount = new ResourceSet(payload.asObject().get("BONUSRESOURCE").asObject());
		}
		else
			bonusDiscount = null;
		if(!payload.asObject().get("EXCLUSIVEBONUS").isNull()){
			flag = true;
		}
		else
			flag = false;
		
		Effect permanentEffect = (b, p, a) -> {
			
			if((a.getActionType().equals(actionType) && a.getActionRegionId()==regionID)) { // Action a is not the ActionType of the permanentEffect  
				System.out.println("ATTIVATO EFFETTO PERMANENTE");
				System.out.println(a.getActionValue() + " + " + actionValueBonus);
				a.setActionValue(a.getActionValue() + actionValueBonus);
				System.out.println(a.getActionValue());

				if(!flag && bonusDiscount !=null){
					DevelopmentCard card =((TowerRegion) b.getRegion(a.getActionRegionId()))
							.getTowerLayers()[a.getActionSpaceId()]
									.getCard();
					card.discountCard(bonusDiscount);
				}
				//open a context if flag == true
			}
			else 
				return;
		};
		return permanentEffect;
	};
	
	public static void loadBuilder() {
		EffectRegistry.getInstance().registerBuilder("PERMANENT", bonusPermanentEffect);
	}	
}
