package it.polimi.ingsw.GC_32.Server.Game.Effect;

public class UniqueEffect {

	// annulla il bonus. Da usare su board deepCopiata e non dovra' sostituire la board Originale.
	static Effect noTowerActionSpaceBonus = (b, p, a) ->{
		if(a.getActionRegionId() != b.getMarketRegion().getRegionID()){
			b.getRegion(a.getActionRegionId()).getActionSpace(a.getActionSpaceId())
			.getBonus().getResourceSet().replaceAll((k,i) -> i=0);
		}
	};
	// No Market Action
	static Effect noMarketAction = (b, p, a) -> {
		if(a.getActionRegionId() == b.getMarketRegion().getRegionID()){
			a.invalidate();
		}
	};
	
	public static void loadBuilder(){
		EffectRegistry.getInstance().registerEffect("NO_TOWER_BONUS", noTowerActionSpaceBonus);
		EffectRegistry.getInstance().registerEffect("NO_MARKET_ACTION", noMarketAction);
	
	}
}
