package it.polimi.ingsw.GC_32.Server.Game.Effect;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

/**
 * collection of effects (so not EffectBuilder) which are specific for some cards. Taken from the game rules, we have those effects:
 * 
 *  <ul>
 *  <li>{@link #lessDice}: All your colored Family Members receive a -1 reduction of their value each time you place them.</li>
 *  <li>{@link #noMarketAction}: You can’t place your FamilyMembers in the Market action spaces.</li>
 *  <li>{@link #noTowerActionSpaceBonus}: You don't take action space bonus when you place a family member on action spaces which contains some bonuses</li>
 *  <li>{@link #greyFamily}: Your uncolored Family Member has a bonus of +3 on its value. (Sigismondo Malatesta)</li>
 *  <li>{@link #familyColodDice}: Your colored Family Members has a value of 5, regardless of their related dice. (Ludovico il moro)</li>
 *  <li>{@link #familyColorBonus}: Your colored Family Members have a bonus of +2 on their value. (Lucrezia Borgia)</li>
 *  </ul>
 *
 * @see Effect
 */
public class UniqueEffect {

	
		static Effect noTowerActionSpaceBonus = (b, p, a, cm) ->{
			if(a.getRegionId() != b.getMarketRegion().getRegionID()){
				ResourceSet bonus =b.getRegion(a.getRegionId()).getActionSpace(a.getActionSpaceId()).getBonus();
				if(bonus!=null) 
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
				if(a.getAdditionalInfo().get("FAMILYMEMBER_ID")!=null){ // if effect is triggered by an ACTION effect
					if(a.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt() != 0){
						a.setActionValue(a.getActionValue() - 1);
					}
				}
			};
			
			//Sigismondo
			static Effect greyFamily = (b, p, a, cm) -> {
				if(a.getAdditionalInfo().get("FAMILYMEMBER_ID")!=null){ // if effect is triggered by an ACTION effect
					if(a.getAdditionalInfo().asObject().get("FAMILYMEMBER_ID").asInt() == 0){
						System.out.println("ATTIVO SIGISMONDO");
						a.setActionValue(a.getActionValue() + 3);
					}
				}
			};
			
			//Ludovico il Moro
			static Effect familyColodDice = (b, p, a, cm) -> {
				if(a.getAdditionalInfo().get("FAMILYMEMBER_ID")!=null){ // if effect is triggered by an ACTION effect
					if(a.getAdditionalInfo().asObject().get("FAMILYMEMBER_ID").asInt() != 0){
						System.out.println("ATTIVO MORO");
						a.setActionValue(5);
					}
				}
			};
			
			//Lucrezia Borgia
			static Effect familyColorBonus = (b, p, a, cm)->{
				if(a.getAdditionalInfo().get("FAMILYMEMBER_ID")!=null){ // if effect is triggered by an ACTION effect
					if(a.getAdditionalInfo().asObject().get("FAMILYMEMBER_ID").asInt() != 0){
						System.out.println("LUVREZIA BORGIA");
						a.setActionValue(a.getActionValue() + 2);
					}
				}
			};
			
			//Lorenzo de Medici
			static Effect jolly = (b, p, a, cm)->{ 
				System.out.println("ATTIVO LORENZO MEDICI");

			};
			
			//Federico Montefeltro
			static Effect familySixValor = (b, p, a, cm)->{  
				System.out.println("ATTIVO FEDERICO MONTEFELTRO");
			};
		
		/**
		 * load the custom effects into the EffectRegistry
		 */
		public static void loadBuilder(){
			EffectRegistry.getInstance().registerEffect("NO_TOWER_BONUS", noTowerActionSpaceBonus);
			EffectRegistry.getInstance().registerEffect("NO_MARKET_ACTION", noMarketAction);
			EffectRegistry.getInstance().registerEffect("LESSDICE", lessDice);
			
			//Leader
			EffectRegistry.getInstance().registerEffect("GREYFAMILY", greyFamily);
			EffectRegistry.getInstance().registerEffect("FAMILYCOLORDICE", familyColodDice);
			EffectRegistry.getInstance().registerEffect("FAMILYCOLORBONUS", familyColorBonus);
			
			EffectRegistry.getInstance().registerEffect("JOLLY", jolly);
			EffectRegistry.getInstance().registerEffect("FAMILYSIXVALOR", familySixValor);

		}
}
