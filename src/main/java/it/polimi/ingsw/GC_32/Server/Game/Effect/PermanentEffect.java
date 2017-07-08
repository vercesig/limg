package it.polimi.ingsw.GC_32.Server.Game.Effect;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

/**
 * Permanent effect are used to increase the action value of a specific action when the permanent effect of the card is activated. This class contains the 
 * effectbuilder relative to this type of effect
 * 
 * @see EffectBuilder, Effect
 *
 */
public class PermanentEffect {
	
	/**
	 * the builder of this effect. Given a JsonObject, taken from the JSON representation of the card, the builder generates the correct permanent effect which
	 * will be loaded into the Card object
	 */
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
		
		Effect e = (Board b, Player p, Action a, ContextManager cm) -> {
						
			if((a.getActionType().equals(actionType) && a.getRegionId()==regionID)) { // Action a is not the ActionType of the permanentEffect  
				System.out.println("ATTIVATO EFFETTO PERMANENTE");
				System.out.println(a.getActionValue() + " + " + actionValueBonus);
				a.setActionValue(a.getActionValue() + actionValueBonus);
				System.out.println(a.getActionValue());

				if(!flag && bonusDiscount !=null){
					DevelopmentCard card =((TowerRegion) b.getRegion(a.getRegionId()))
							.getTowerLayers()[a.getActionSpaceId()]
									.getCard();
					card.discountCard(bonusDiscount);
				}
			}
			else 
				return;
		};
		return e;
	};
	
	/**
	 * load the builder into the EffectRegistry with key "PERMANENT"
	 */
	public static void loadBuilder() {
		EffectRegistry.getInstance().registerBuilder("PERMANENT", bonusPermanentEffect);
	}	
}
