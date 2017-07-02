package it.polimi.ingsw.GC_32.Server.Game;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerLayer;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class MoveUtils {
	public MoveUtils(){}
	
	static public boolean checkValidRegionID(Board board, Player player, Action action){	
		if(board.getRegion(action.getRegionId()) == null){		
			return false;
		}
		return true;
	}

	static public boolean checkValidActionSpaceID(Board board, Player player, Action action){
		if(board.getRegion(action.getRegionId())
				.getActionSpace(action.getActionSpaceId()) == null){
			return false;
		}
		return true;
	}

    /** checks if the action value is greater than or equal
     * to the actionspace value
     * @param board
     * @param player
     * @param action
     * @return
     */
    static public boolean checkActionValue(Board board, Action action){
    	return (board.getRegion(action.getRegionId())
    				 .getActionSpace(action.getActionSpaceId())
    				 .getActionValue() <= action.getActionValue());
    }
 
    /** checks if the region is already occupied by a familiar of the same color
     * @param board
     * @param player
     * @param action
     * @return
     */
    public static boolean familyColor(Board board, Player player, Action action){
    	if(action.getRegionId() == 2 || action.getRegionId() == 3){
    		return true;
    	}
    	JsonValue familyMemberId = action.getAdditionalInfo().get("FAMILYMEMBER_ID");
    	if(familyMemberId != null && familyMemberId.asInt() == 0){
    			return true;
    	}
      	for (FamilyMember f : player.getFamilyMember()){
      		/*System.out.println(f.toString());
      		System.out.println(f.getPosition().getRegionID());
      		System.out.println(board.getRegion(action.getRegionId()));
      		System.out.println(action.getRegionId());
      		System.out.println(f.getColor());*/
			if(f.getPosition()!=null && f.getPosition().getRegionID() == action.getRegionId() && !f.getColor().equals(DiceColor.GREY)){
				System.out.println("COLOR RULE NON RISPETTATA");
				return false;
			}
    	}
      	return true;
    }

    /** Checks if the actionspace is free
     * @param board
     * @param player
     * @param action
     * @return
     */
    public static boolean isFreeSingleSpace(Board board, Player player, Action action){
    	if(player.isFlagged("FAMILYOCCUPY")){
    		return true;
    	}
    	return !(board.getRegion(action.getRegionId()).getActionSpace(action.getActionSpaceId()).isSingleActionSpace() &&
    			 board.getRegion(action.getRegionId()).getActionSpace(action.getActionSpaceId()).isBusy());
    }
    
    /** CheckBlockedSpace: checks player count and the presence of additional
     *  production/harvest/market spaces
     * @param numberOfPlayer
     * @param action
     * @return
     */
    public static boolean checkBlockedZone(int numberOfPlayer, Action action){	
    	// Can't access to: Harvest-1,Production-1 and Market 2-3
    	if(numberOfPlayer < 3){
    		if((action.getRegionId() <=1 && action.getActionSpaceId()==1) ||
    				(action.getRegionId() == 3 && action.getActionSpaceId() >= 2)){
    	    	System.out.println("ZONA BLOCCATA");
    			return true;
    		}
    	}
    	//  Can't access to: Market 2-3
    	if(numberOfPlayer <4){
    		if(action.getRegionId() == 3 && action.getActionSpaceId() >= 2){
    	    	System.out.println("ZONA BLOCCATA");
    			return true;
    		}
    	}
    	return false;
	}
    
    public static boolean checkNotFoundCard(Board board, Action action){
    	if(action.getRegionId() < 4){
    		return true;
    	}
    	TowerLayer layer =((TowerRegion) board.getRegion(action.getRegionId()))
				  							  .getTowerLayers()[action.getActionSpaceId()];
    	return (layer.getCard() != null);
    }
    /** checks if the player has enough servants to meet the actionspace requirements
     * @param board
     * @param player
     * @param action
     * @return
     */
    public static boolean checkServants(Board board, Player player, Action action){ // change the state
    	if(checkActionValue(board, action)){
    		return true;
    	}  	
    	System.out.println("PROVO A PAGARE IN SERVITORI LA DIFFERENZA: ");
    	int actionDiff = action.getActionValue() -
    					 board.getRegion(action.getRegionId()).getActionSpace(action.getActionSpaceId()).getActionValue();
    	if(player.isFlagged("DOUBLESERVANTS")){ // excommunicate flag
        	System.out.println("EFFETTO DOUBLESERVANTS EXCOMMUNICATE");
    		player.getResources().addResource("SERVANTS", 2*actionDiff);
    	}
    	else
    		player.getResources().addResource("SERVANTS", actionDiff);
    	System.out.println(" " + player.getResources().isValid());
    	return player.getResources().isValid();
    }
    
    /** checks if the tower is already occupied and if so, if the player can pay the 3 coin tribute
     */
    public static boolean checkCoinForTribute(Board board, Player player, Action action){  // change the state
    	if(action.getRegionId() < 4 || 										   	//Not a tower action
    	   !(board.getTowerRegion()[action.getRegionId() - 4].isTowerBusy()) || //Tower is empty
    	   player.isFlagged("NOTRIBUTE")){										// Leader Card
    		return true;
    	}
    	player.getResources().subResource("COINS", 3);
    	return player.getResources().isValid();
    }
    
    /** checks if the card can be inserted into the personalBoard
     */
    public static boolean checkPersonalBoardRequirement(Board board, Player player, Action action){
    	DevelopmentCard card =((TowerRegion) board.getRegion(action.getRegionId()))
    											  .getTowerLayers()[action.getActionSpaceId()]
    											  .getCard();
    	String cardType = card.getType();
    	if(player.getPersonalBoard().getCardsOfType(cardType).size() == 6){
    			return false;
    	}
    	
    	if(card.getType().equals("TERRITORYCARD")){
    		
    		if(player.isFlagged("NOMILITARYRULE")){ // Leader Card
    			return true;
    		}
    		
    		int milPoints = player.getResources().getResource("MILITARY_POINTS");
    		switch(player.getPersonalBoard().getCardsOfType("TERRITORYCARD").size()){
    		case 2: 
    			return milPoints >= 3; 
    		case 3:
    			return milPoints >= 7;
    		case 4:
    			return milPoints >= 12;
    		case 5:
    			return milPoints >= 18;
    		default:
    			return true;
    		}
    	} 
    	return true;	
    }
    
    /** Checks if the player can buy the card
     * @param board
     * @param player
     * @param action
     * @return
     */
    public static boolean checkCardCost(Board board, Player player, Action action){ 	// change the state
    	if(action.getRegionId()<4)
    		return true;
    	DevelopmentCard card =((TowerRegion) board.getRegion(action.getRegionId()))
    	                                          .getTowerLayers()[action.getActionSpaceId()]
    	                                          .getCard();

		ResourceSet requirements = card.getRequirments();
		if(requirements != null && player.getResources().compareTo(requirements) < 0){
			System.out.println("NO REQUIREMENTS RISPETTATI");
			return false;
		} else {
		    System.out.println("CARTA SENZA REQUIREMENTS");
		}	
    	
		JsonValue costIndex = action.getAdditionalInfo().get("COSTINDEX");
		if(costIndex == null){
		    costIndex = Json.value(0);
		}
		if(costIndex.asInt() < card.getCost().size()){
		    ResourceSet cost = card.getCost().get(costIndex.asInt());
		    System.out.println("PLAYER RESOURCES: " + player.getResources());
		    System.out.println("COST: " + cost);
		    
		    if(action.getAdditionalInfo().asObject().get("BONUSACTIONVALUE")!=null){ // only for ACTION effect
		    	ResourceSet bonusResource = new ResourceSet(action.getAdditionalInfo().asObject().get("BONUSACTIONVALUE").asObject());
		    	if(card.getCost().get(costIndex.asInt()).contains(bonusResource)){
		    		cost.subResource(bonusResource);
		    	}
		    }
		    player.getResources().subResource(cost);
		    return player.getResources().isValid();
		} else {
    		System.out.println("CARTA SENZA COSTO");
    		return true;
		}
    }
    
    public static void applyEffects(Board board, Player player, Action action, ContextManager cm){
    	for(Effect buff : player.getEffectList()){
			buff.apply(board, player, action, cm);
    	}
	}
	
	public static void cloneApplyEffects(Board board, Player playerCopy, Player player, Action action, ContextManager cm){
		for(Effect buff : player.getEffectList()){
			buff.apply(board, playerCopy, action, cm);
		}
	}
	
	public static void addActionSpaceBonus(Board board, Player player, Action action){
		ResourceSet bonus = board.getRegion(action.getRegionId())
				  				 .getActionSpace(action.getActionSpaceId())
				  				 .getBonus();
		if(bonus != null){
			//Excommunication Effect debuff
			if(player.isFlagged("LESSRESOURCE")){
				 JsonArray malusResource = player.getFlags().get("LESSRESOURCE").asArray();
				 for(String key: bonus.getResourceSet().keySet()){
					 malusResource.forEach(member ->{
						 if(member.asString().equals(key)){
							 player.getResources().addResource(key, -1);
						 }
					 });
				 }	 
			}	
			player.getResources().addResource(bonus);
		}
	}
}
