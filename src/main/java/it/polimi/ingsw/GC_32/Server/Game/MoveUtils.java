package it.polimi.ingsw.GC_32.Server.Game;

import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerLayer;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

/**
 * this class contains utilities used during the check to check the validity of the action
 *
 */
public class MoveUtils {
    private static Logger LOGGER = Logger.getLogger(MoveUtils.class.toString());
    
	private MoveUtils(){}
	
	/**
	 * check if the region ID contained into the action is a valid region ID
	 * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 * @return true if the check is passed
	 */
	static public boolean checkValidRegionID(Board board, Player player, Action action){	
		if(board.getRegion(action.getRegionId()) == null){		
			return false;
		}
		return true;
	}

	/**
	 * check if the action space ID contained into the action is a valid action space ID
	 * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 * @return true if the check is passed
	 */
	static public boolean checkValidActionSpaceID(Board board, Player player, Action action){
		if(board.getRegion(action.getRegionId())
				.getActionSpace(action.getActionSpaceId()) == null){
			return false;
		}
		return true;
	}

    /** checks if the action value is greater than or equal to the actionspace value
	 * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 * @return true if the check is passed
     */
    static public boolean checkActionValue(Board board, Action action){
    	return (board.getRegion(action.getRegionId())
    				 .getActionSpace(action.getActionSpaceId())
    				 .getActionValue() <= action.getActionValue());
    }
 
    /** checks if the region is already occupied by a familiar of the same color
     * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 * @return false if the region has a same-color familiar, false otherwise
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
			if(f.getPosition()!=null && f.getPosition().getRegionID() == action.getRegionId() && !f.getColor().equals(DiceColor.GREY)){
				System.out.println("COLOR RULE NON RISPETTATA");
				return false;
			}
    	}
      	return true;
    }

    /** checks if the actionspace is free
	 * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 * @return true if the check is passed
     */
    public static boolean isFreeSingleSpace(Board board, Player player, Action action){
    	if(player.isFlagged("FAMILYOCCUPY")){
    		return true;
    	}
    	return !(board.getRegion(action.getRegionId()).getActionSpace(action.getActionSpaceId()).isSingleActionSpace() &&
    			 board.getRegion(action.getRegionId()).getActionSpace(action.getActionSpaceId()).isBusy());
    }
    
    /** checks player count and the presence of additional production/harvest/market spaces
	 * @param numberofPlayers the number of players registered to the game
	 * @param action the action performed by the player
     * @return true if the action is on a blocked area, false otherwise
     */
    public static boolean checkBlockedZone(int numberOfPlayer, Action action){	
    	// Can't access to: Harvest-1,Production-1 and Market 2-3
    	if(numberOfPlayer < 3){
    		if(action.getRegionId() <= 1 && action.getActionSpaceId() == 1 ||     //Deny Harvest and production regions
    		   action.getRegionId() == 3 && action.getActionSpaceId() >= 2){      //Deny Extra market spots
    	    	System.out.println("ZONA BLOCCATA");
    			return true;
    		}
    	}
    	//  Can't access to: Market 2-3
    	if(numberOfPlayer < 4){
    		if(action.getRegionId() == 3 && action.getActionSpaceId() >= 2){
    	    	System.out.println("ZONA BLOCCATA");
    			return true;
    		}
    	}
    	return false;
	}

    /**
     * Checks that the actionspace actually has a card
     * @param board the board of the game
	 * @param action the action performed by the player
     * @return false if the actionspace should have a card and doesn't, true otherwise
     */
    public static boolean checkNotFoundCard(Board board, Action action){
    	if(action.getRegionId() < 4){
    		return true;
    	}
    	TowerLayer layer =((TowerRegion) board.getRegion(action.getRegionId()))
				  							  .getTowerLayers()[action.getActionSpaceId()];
    	return (layer.getCard() != null);
    }

    /** checks if the player has enough servants to meet the actionspace requirements
     * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
     * @return true if the player has enough servants, false otherwise
     */
    public static boolean checkServants(Board board, Player player, Action action){ // change the state
    	if(checkActionValue(board, action)){
    		return true;
    	}
    	LOGGER.info("PROVO A PAGARE IN SERVITORI LA DIFFERENZA: ");
    	int actionDiff = action.getActionValue() -
    					 board.getRegion(action.getRegionId())
    					      .getActionSpace(action.getActionSpaceId()).getActionValue();
    	if(player.isFlagged("DOUBLESERVANTS")){ // excommunicate flag
        	LOGGER.info("EFFETTO DOUBLESERVANTS EXCOMMUNICATE");
    		player.getResources().addResource("SERVANTS", 2*actionDiff);
    	} else {
    		player.getResources().addResource("SERVANTS", actionDiff);
    	}
    	LOGGER.log(Level.INFO, " %s", player.getResources().isValid());
    	return player.getResources().isValid();
    }
    
    /** checks if the tower is already occupied and if so, if the player can pay the 3 coin tribute
     * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 * @return true if the check is passed
     */
    public static boolean checkCoinForTribute(Board board, Player player, Action action){  // change the state
    	if(action.getRegionId() < 4 || 										   	//Not a tower action
    	   !(board.getTowerRegion()[action.getRegionId() - 4].isTowerBusy()) || //Tower is empty
    	   player.isFlagged("NOTRIBUTE")){// Leader Card
    		return true;
    	}
    	player.getResources().subResource("COINS", 3);
    	return player.getResources().isValid();
    }
    
    /** checks if the card can be inserted into the personalBoard
     * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 * @return true if the check is passed
     */
    public static boolean checkPersonalBoardRequirement(Board board, Player player, Action action){
    	DevelopmentCard card =((TowerRegion) board.getRegion(action.getRegionId()))
    											  .getTowerLayers()[action.getActionSpaceId()]
    											  .getCard();
    	String cardType = card.getType();
    	if(player.getPersonalBoard().getCardsOfType(cardType).size() >= 6){
    			return false;
    	}
    	
    	if("TERRITORYCARD".equals(card.getType())){
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
     * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
     * @return true if the card can be bought, false if not
     */
    public static boolean checkCardCost(Board board, Player player, Action action){
    	if(action.getRegionId()<4)
    		return true;
    	DevelopmentCard card =((TowerRegion) board.getRegion(action.getRegionId()))
    	                                          .getTowerLayers()[action.getActionSpaceId()]
    	                                          .getCard();

		ResourceSet requirements = card.getRequirments();
		if(requirements != null && player.getResources().compareTo(requirements) < 0){
			LOGGER.info("NO REQUIREMENTS RISPETTATI");
			return false;
		} else {
		    LOGGER.info("CARTA SENZA REQUIREMENTS");
		}
		
		if(card.getCost().isEmpty()){
		    LOGGER.info("CARTA SENZA COSTO");
            return true;
		}
    	
		JsonValue costIndex = action.getAdditionalInfo().get("COSTINDEX");
		if(costIndex == null){
		    costIndex = Json.value(0);
		}
		if(costIndex.asInt() < card.getCost().size()){
		    ResourceSet cost = card.getCost().get(costIndex.asInt());
		    LOGGER.log(Level.INFO, "PLAYER RESOURCES: %s", player.getResources());
		    LOGGER.log(Level.INFO, "COST: %s", cost);
		    
		    if(action.getAdditionalInfo() != null &&
		       action.getAdditionalInfo().asObject().get("BONUSRESOURCE") != null){ // only for ACTION effect
		    	ResourceSet bonusResource = new ResourceSet(action.getAdditionalInfo().asObject()
		    	                                                  .get("BONUSRESOURCE").asObject());
		    	
		    	for(Entry<String,Integer> resource : bonusResource.getResourceSet().entrySet()){
		    		if(cost.hasResource(resource.getKey())){
		    			cost.subResource(resource.getKey(), resource.getValue());
		    		}
		    	}
		    }
		    player.getResources().subResource(cost);
		    return player.getResources().isValid();
		}
		return false;
    }
    
    /**
     * apply the effect contained in the effect list of the player
     * @param board the board of the game
     * @param player the player who has performed the action
     * @param action the action performed by the player
     * @param cm instance of the context manager, maybe effects can open contexts
     */
    public static void applyEffects(Board board, Player player, Action action, ContextManager cm){
    	for(Effect buff : player.getEffectList()){
			buff.apply(board, player, action, cm);
    	}
	}
	
    /**
     * apply the effect contained in the effect list of the player, in the simultation of the move
     * @param board the board of the game
     * @param playerCopy the copy of the player who has performed the action
     * @param player the player who has performed the action
     * @param action the action performed by the player
     * @param cm instance of the context manager, maybe effects can open contexts
     */
	public static void cloneApplyEffects(Board board, Player playerCopy, Player player, Action action, ContextManager cm){
		for(Effect buff : player.getEffectList()){
			buff.apply(board, playerCopy, action, cm);
		}
	}
	
	/**
	 * add the bonus contained into the action space to the resources of the player. If the player is flagged by a LESSRESOURCE excommunication effect, the effect its
	 * applied for every ind of resource taken
     * @param board the board of the game
     * @param player the player who has performed the action
     * @param action the action performed by the player
	 */
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
