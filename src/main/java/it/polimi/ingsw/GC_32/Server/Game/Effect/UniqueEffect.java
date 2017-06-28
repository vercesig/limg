package it.polimi.ingsw.GC_32.Server.Game.Effect;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class UniqueEffect {

	// annulla il bonus. Da usare su board deepCopiata e non dovra' sostituire la board Originale.
	static Effect noTowerActionSpaceBonus = (b, p, a, cm) ->{
		if(a.getActionRegionId() != b.getMarketRegion().getRegionID()){
			ResourceSet bonus =b.getRegion(a.getActionRegionId()).getActionSpace(a.getActionSpaceId())
			.getBonus();
			p.getResources().subResource(bonus);
		}
	};
	// Excommunicate card 2-5
	static Effect noMarketAction = (b, p, a, cm) -> {
		if(a.getActionRegionId() == b.getMarketRegion().getRegionID()){
			a.invalidate();
		}
	};
	
	//Excommunicate card 1-7
	static Effect lessDice = (b, p, a, cm) -> {
		if(a.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt() != 0){
			a.setActionValue(a.getActionValue() - 1);
		}
	};
	
	//Excommunicate card 2-6
	static Effect doubleServant = (b, p, a, cm) -> {
		p.getExcomunicateFlag().add("DOUBLESERVANTS");
	};
	
	//Excommunicate card 2-7
	static Effect skipTurn = (b, p, a, cm) -> {
		p.getExcomunicateFlag().add("SKIPTURN");
	};
	
	public static void loadBuilder(){
		EffectRegistry.getInstance().registerEffect("NO_TOWER_BONUS", noTowerActionSpaceBonus);
		EffectRegistry.getInstance().registerEffect("NO_MARKET_ACTION", noMarketAction);
	
	}
}
