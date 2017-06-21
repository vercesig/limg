package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class Check {
	public Check(){}

  //*******************************************************************************//  	
	//--------------Correct Parameters Check -------------------
    
	//1)
	static public boolean checkValidRegionID(Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId()) == null){
			return false;
		}
		return true;
	}
    
	//2)
	static public boolean checkValidActionSpaceID(Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId())
				.getActionSpace(action.getActionSpaceId()) == null){
			return false;
		}
		return true;
	}
    
	//3)
    static public boolean checkValidActionType(Board board, Player player, Action action){	
		switch (action.getActionType()){
		case "PRODUCTION" : {if(action.getActionRegionId()!=0){return false;} else {return true;}}
		case "HARVEST" : {if(action.getActionRegionId()!=1){return false;} else {return true;}}
		case "COUNCIL" : {if(action.getActionRegionId()!=2){return false;} else {return true;}}
		case "MARKET" : {if(action.getActionRegionId()!=3){return false;} else {return true;}}
		case "TOWER_GREEN" : {if(action.getActionRegionId()!=4){return false;} else {return true;}}
		case "TOWER_BLUE" : {if(action.getActionRegionId()!=5){return false;} else {return true;}}
		case "TOWER_YELLOW" : {if(action.getActionRegionId()!=6){return false;} else {return true;}}
		case "TOWER_PURPLE" : {if(action.getActionRegionId()!=7){return false;} else {return true;}}
		default : return false;
		}
	} 

    //*******************************************************************************//  
  //--------------Fundamental Check -------------------
    
    //1) ValueActionCheck: FamilyMember actionValue > actionSpaceValue?
    static public boolean checkActionValue(Board board, Player player, Action action){
    	if(board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId())
    			.getActionValue() <= action.getActionValue())
    		return true;
    	else 
    		return false;
    }
 
//*******************************************************************************//    
    //--------------MoveFamilyMember Check -------------------
	
	// 1) FamilyColorCheck: check if the rule only one family member color is allowed
    public static boolean familyColor(Board board, Player player, Action action){
    	
    	// Non si applica per azioni Council e Market
    	if(action.getActionRegionId() == 2 || action.getActionRegionId() == 3){
    		return true;
    	}
    
    	try{
    		if(action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt() == 0){
    			return true;
    		}
    	} 
    	catch(NullPointerException e){};
      	for (FamilyMember f : player.getFamilyMember()){
    		try{
    			if(f.getPosition().getRegionID() == action.getActionRegionId() && !f.getColor().equals(DiceColor.GREY)){
    				return false;
    			}
    		}
    		catch(NullPointerException e){};	
    	}
      	return true;
    }

    // 2) Free Action Space: is this actionSpace free?
    public static boolean isFreeSingleSpace(Board board, Player player, Action action){
    	if(board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId())
    			.isSingleActionSpace() && board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId())
    			.isBusy()){
    		return false;
    	}else 
    		return true;
    }
    
    // 3) CheckBlockedSpace: can the player access to that actionSpace?
    public static boolean checkBlockedZone(int numberOfPlayer, Action action){	
    	// Can't access to: Harvest-1,Production-1 and Market 2-3
    	if(numberOfPlayer < 3){
    		if((action.getActionRegionId() <=1 && action.getActionSpaceId()==1) ||
    				(action.getActionRegionId() == 3 && action.getActionSpaceId() >= 2)){
    			return true;
    		}
    	}
    	//  Can't access to: Market 2-3
    	if(numberOfPlayer <4){
    		if(action.getActionRegionId() == 3 && action.getActionSpaceId() >= 2){
    			return true;
    		}
    	}
    	return false;
	}
    
    // 4) Check on Servants: if player doesn't have enough Action value he uses all his servants to get it
    public static boolean useServants(Board board, Player player, Action action){
    	if(checkActionValue(board, player, action)){
    		return true;
    	}
    	while(!checkActionValue(board, player, action)){
    		if(player.getResources().getResource("SERVANTS") < 0){
    			return false;
    		}
    		player.getResources().addResource("SERVANTS", -1);
    		action.setActionValue(action.getActionValue() + 1);
    	}
    	return true;
    }
    
    // 5) 3CoinsCheck: has the player 3 coins to pay the tribute if the tower is busy?
    public static boolean checkCoinForTribute(Board board, Player player, Action action){
    	ResourceSet coins = new ResourceSet();
    	coins.addResource("COINS", 3);
    	
    	// non si tratta di un'azione tower
    	if(action.getActionRegionId() < 4){
    		return true;
    	}
    	
    	if (((TowerRegion) board.getRegion(action.getActionRegionId())).isTowerBusy() &&
    			player.getResources().compareTo(coins) < 0){
    		return false;	
    	} 
    	player.getResources().addResource("COINS", -3); // Change the State of the Game
    	return true;
    }
    
  //*******************************************************************************//  
  	//--------------Take Card Check -------------------

    // 1) Territory check: has the player enough military points?
    public static boolean checkTerrytoryRequirement(Board board, Player player, Action action){
    	DevelopmentCard card =((TowerRegion) board.getRegion(action.getActionRegionId()))
    			.getTowerLayers()[action.getActionSpaceId()]
    					.getCard();
    	if(card.getType().equals("TERRITORYCARD")){
    		int point = player.getResources().getResource("MILITARY_POINTS");
    		switch(player.getPersonalBoard().getCardsOfType("TERRITORYCARD").size()){
    		case 2 : if(point >= 3){return true;}  else {return false;} 
    		case 3 : if(point >= 7){return true;}  else {return false;} 
    		case 4 : if(point >= 12){return true;} else {return false;}  
    		case 5 : if(point >= 18){return true;} else {return false;}  	
    		default : return true;
    		}
    	} 
    	return true;	
    }

    // 2) CheckMaxCard: is the player trying to take a seventh card?
    public static boolean checkMaxCard(Board board, Player player, Action action){
        String cardType = ((TowerRegion) board.getRegion(action.getActionRegionId()))
        		.getTowerLayers()[action.getActionSpaceId()].getCard().getType();
        if(player.getPersonalBoard().getCardsOfType(cardType).size() < 6){
        	return true;
        }
        return false;
    }
    
  // 3) CostCheck: can the player pay the cost of the card?
    public static boolean checkCost(Board board, Player player, Action action){
    	
      	ResourceSet cost = new ResourceSet(action.getAdditionalInfo());
    	if(player.getResources().compareTo(cost) >=0 ){
    		player.getResources().subResource(cost);
    		return true;
    	}
    	return false;
    	
    /*	DevelopmentCard card =((TowerRegion) board.getRegion(action.getActionRegionId()))
    			.getTowerLayers()[action.getActionSpaceId()]
    					.getCard();
    	
     	ArrayList<ResourceSet> costCard = card.getCost();
 		ResourceSet requirements = card.getRequirments();
    	if(!requirements.getResourceSet().isEmpty()){
    		if(player.getResources().compareTo(requirements)>=0){
    		}
    		else { // non contiene i requirements
    			for (ResourceSet cost: costCard){
    				if(cost.getResourceSet().keySet().containsAll(requirements.getResourceSet().keySet())){
    					costCard.remove(cost);
    				}
    			}
    		}	
    	}	
    	for(ResourceSet cost: costCard){
     	   if(player.getResources().compareTo(cost)<0){
     		   costCard.remove(cost);
     	   }
     	}
    	
    	switch (costCard.size()){
    	case 0: 
    		return false; // can't pay any cost
    	
    	case 1:
    		player.getResources().subResource(costCard.get(0)); // Change the State of the Game
    		return true;
    	default:
    		break;
    	}
    	return false;*/
    }   
}
