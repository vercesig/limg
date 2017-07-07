package it.polimi.ingsw.GC_32.Server.Game.Effect;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class UniqueEffect {

	// annulla il bonus. Da usare su board deepCopiata e non dovra' sostituire la board Originale.
	static Effect noTowerActionSpaceBonus = (b, p, a, cm) ->{
		if(a.getRegionId() != b.getMarketRegion().getRegionID()){
			ResourceSet bonus =b.getRegion(a.getRegionId()).getActionSpace(a.getActionSpaceId()).getBonus();
			if(bonus!=null) //la carta è su un actionSpace con bonus
				p.getResources().subResource(bonus);
		}
	};
	// Excommunicate card 2-5
	static Effect noMarketAction = (b, p, a, cm) -> {
		if(a.getRegionId() == b.getMarketRegion().getRegionID()){
			a.invalidate();
		}
	};
	
	//Excommunicate card 1-7
		static Effect lessDice = (b, p, a, cm) -> {
			if(a.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt() != 0){
				a.setActionValue(a.getActionValue() - 1);
			}
		};
		
		//Sigismondo
		static Effect greyFamily = (b, p, a, cm) -> {
			if(a.getAdditionalInfo().asObject().get("FAMILYMEMBER_ID").asInt() == 0){
				a.setActionValue(a.getActionValue() + 3);
			}
		};
		
		//Ludovico il Moro
		static Effect familyColodDice = (b, p, a, cm) -> {
			if(a.getAdditionalInfo().asObject().get("FAMILYMEMBER_ID").asInt() != 0){
				a.setActionValue(5);
			}
		};
		
		//Lucrezia Borgia
		static Effect familyColorBonus = (b, p, a, cm)->{
			if(a.getAdditionalInfo().asObject().get("FAMILYMEMBER_ID").asInt() != 0){
				a.setActionValue(a.getActionValue() + 2);
			}
		};
		
		//Lorenzo de Medici
		static Effect jolly = (b, p, a, cm)->{ // BALZA
		};
		
		//Federico Montefeltro
		static Effect familySixValor = (b, p, a, cm)->{  //BALZA
		};
		

		public static void loadBuilder(){
			EffectRegistry.getInstance().registerEffect("NO_TOWER_BONUS", noTowerActionSpaceBonus);
			EffectRegistry.getInstance().registerEffect("NO_MARKET_ACTION", noMarketAction);
			EffectRegistry.getInstance().registerEffect("LESSDICE", lessDice);
			
			//Leader
			EffectRegistry.getInstance().registerEffect("GREYFAMILY", greyFamily);
			EffectRegistry.getInstance().registerEffect("FAMILYCOLORDICE", familyColodDice);
			EffectRegistry.getInstance().registerEffect("FAMILYCOLORBONUS", familyColorBonus);
			
			EffectRegistry.getInstance().registerEffect("JOLLT", jolly);
			EffectRegistry.getInstance().registerEffect("FAMILYSIXVALOR", familySixValor);

		}
}
