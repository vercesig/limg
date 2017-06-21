package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.rits.cloning.Cloner;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.ActionHandler.MakeAction;
import it.polimi.ingsw.GC_32.Server.Game.ActionHandler.makeHarvest;
import it.polimi.ingsw.GC_32.Server.Game.ActionHandler.makeProduction;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.*;

public class MoveChecker{
	Logger log;
	
    public MoveChecker(){
    	this.log = Logger.getLogger("MoveCheckerLogger");
    }
    
  //*******************************************************************************//  
    
    //FamilyColorCheck: check if the rule only one family member color is allowed
    private boolean checkFamilyColor(Board board, Player player, Action action){
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
    
    //Free Action Space: is this actionSpace free?
    boolean checkIsFreeSingleSpace(Board board, Player player, Action action){
    	if(board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId())
    			.isSingleActionSpace() && board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId())
    			.isBusy()){
    		return false;
    	}else 
    		return true;
    }
    
    // 3CoinsCheck: has the player 3 coins to pay the tribute if the tower is busy?
    private boolean checkCoinForTribute(Board board, Player player, Action action){
    	ResourceSet coins = new ResourceSet();
    	coins.addResource("COINS", 3);
    	if (((TowerRegion) board.getRegion(action.getActionRegionId())).isTowerBusy() &&
    			player.getResources().compareTo(coins) < 0){
    		return false;
    	} 
    	return true;
    }
    
    //ValueActionCheck: FamilyMember actionValue > actionSpaceValue?
    private boolean checkActionValue(Board board, Player player, Action action){
    	if(board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId())
    			.getActionValue() <= action.getActionValue())
    		return true;
    	else 
    		return false;
    }
    
    //CostCheck: can the player pay the cost of the card?
    private boolean checkCost(Board board, Player player, Action action){
    	DevelopmentCard card =((TowerRegion) board.getRegion(action.getActionRegionId()))
    			.getTowerLayers()[action.getActionSpaceId()]
    					.getCard();
    	
    	ArrayList<ResourceSet> costCard = card.getCost();
    	ResourceSet requirements = card.getRequirments();
    	if(!requirements.getResourceSet().isEmpty()){
    		if(player.getResources().compareTo(requirements)>=0){
    			System.out.println("Player ha requirements!");
    			return true;
    		}
    		for (ResourceSet cost: costCard){
    			if(cost.getResourceSet().keySet().containsAll(requirements.getResourceSet().keySet())){
    				costCard.remove(cost);
    			}
    		}	
    	}
    	for(ResourceSet cost: costCard){
    	   if(player.getResources().compareTo(cost)>=0){
    		   return true;
    	   }
    	}
    	return false;
    }   
    
    //Territory check: has the player enough military points?
    private boolean checkTerrytoryRequirement(Board board, Player player, Action action){
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
    
    //CheckMaxCard: is the player trying to take a seventh card?
    private boolean checkMaxCard(Board board, Player player, Action action){
        String cardType = ((TowerRegion) board.getRegion(action.getActionRegionId()))
        		.getTowerLayers()[action.getActionSpaceId()].getCard().getType();
        if(player.getPersonalBoard().getCardsOfType(cardType).size() < 6){
        	return true;
        }
        return false;
    }
    
	//CheckBlockedSpace: can the player access to that actionSpace?
    private boolean checkBlockedZone(int numberOfPlayer, Action action){	
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
	
    //Check on Servants: if player doesn't have enough Action value he uses all his servants to get it
    private boolean useServants(Board board, Player player, Action action){
    	if(checkActionValue(board, player, action)){
    		return true;
    	}
    	while(!checkActionValue(board, player, action)){
    		if(player.getResources().getResource("SERVANTS") <= 0){
    			return false;
    		}
    		player.getResources().addResource("SERVANTS", -1);
    		action.setActionValue(action.getActionValue() + 1);
    	}
    	return true;
    }
    
  //*******************************************************************************//  
    
    //StandardCheck: valid parameters
    private boolean checkValidRegionID(Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId()) == null){
			return false;
		}
		return true;
	}
    
    private boolean checkValidActionSpaceID(Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId())
				.getActionSpace(action.getActionSpaceId()) == null){
			return false;
		}
		return true;
	}
    
    private boolean checkValidActionType(Board board, Player player, Action action){	
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
    public boolean firstCheck(Board board, Player player, Action action){
    	boolean result = true;
    	while(result){
			result = checkValidRegionID(board,player, action); // region ID valida
			log.info("/******** CHECK: is a valid region id?\n" + result + "\n");
			if(!result){
				break;
			}
			result = checkValidActionSpaceID(board,player, action); // actionId valido
			log.info("/******** CHECK: is a valid actionSpace id?\n" + result + "\n");
			if(!result){
				break;
			}
			result = checkValidActionType(board,player, action);
			log.info("/******** CHECK: is a valid action type?\n" + result + "\n");
			break;
    	} 
    	return result;
    }
	
    private boolean checkStandardMove(Board board, Player player, Action action){ 
    		boolean result = true;
    		while(result){
    			result = firstCheck(board,player, action);
    			if(!result){
    				break;
    			}
    			result = checkIsFreeSingleSpace(board,player, action); // family member su free SingleSpace
    			log.info("/******** CHECK: is a free actionSpace?"+ result + "\n");
    			if(!result){
    				break;
    			}
    			result = checkActionValue(board,player, action); // actionValue azione >= actionValue space
    			log.info("/******** CHECK: is the action value of the action enough?" + result + "\n");
    			break;
    	} 
    		return result;
    }

    private boolean checkTowerMove(Board board, Player player, Action action){ 
		boolean result = true;
		while(result){
			result = checkFamilyColor(board,player, action); // region ID valida
			log.info("/******** CHECK: is the rule of Only one Family color respected?" + result + "\n");
			if(!result)
				break;
			result = checkCoinForTribute(board,player, action);
			log.info("/******** CHECK: has the player three coins because the tower is busy?"+ result + "\n");
			if(!result)
				break;
			result = checkIsFreeSingleSpace(board,player, action); // actionId valido
			log.info("/******** CHECK: is a free actionSpace?"+ result + "\n");
			if(!result)
				break;
			result = checkMaxCard(board,player, action);
			log.info("/******** CHECK: has the player six card of the same type of the card he wants to buy?"+ result + "\n");
			if(!result)
				break;
			result = checkTerrytoryRequirement(board,player, action); // family member su free SingleSpace
			log.info("/******** CHECK: has the player enough military points?"+ result + "\n");
			if(!result)
				break;
			result = checkCost(board,player, action); // actionValue azione >= actionValue space
			log.info("/******** CHECK: can the player pay the cost of the card?"+ result + "\n");
			break;
		}
		return result;
    }
    
    private boolean checkProductionHarvestMove(Board board, Player player, Action action){
    	boolean result = true;
    	while(result){
			result = checkFamilyColor(board,player, action); // region ID valida
			log.info("/******** CHECK: is the rule of Only one Family color respected?" + result + "\n");
			break;
    	}
    	return result;
    }
    
  //*******************************************************************************//  
    
    public boolean checkMove(Board board, Player player, Action action){
    	log.info("/******** STARTING THE CHECK\n");
    	boolean result = checkStandardMove(board,player, action);
    	log.info("/******** STANDARD CHECK result :\n" + result + "\n");
		if(action.getActionRegionId()<=1){
			log.info("/******** STARTING CHECK for a Production/Harvest action\n");
			result = result && checkProductionHarvestMove(board,player, action);
		} if(action.getActionRegionId()>=4){
			log.info("/******** STARTING CHECK for a Tower action\n");
			result = result && checkTowerMove(board,player, action);
		}
		log.info("/******** END OF THE CHECK: " + result + "\n"); 
		return result; 
    }
    
    //*******************************************************************************//  

    public Board getCopy(Board board){
    	Cloner cloner = new Cloner();
    	cloner.dontCloneInstanceOf(Effect.class); // Effetti non possono essere deepCopiati dalla libreria cloning
    	return cloner.deepClone(board);
    }
    	
    public Player getCopy(Player player){
    	Cloner cloner = new Cloner();
    	cloner.dontCloneInstanceOf(Effect.class); // Effetti non possono essere deepCopiati dalla libreria cloning
    	return cloner.deepClone(player);
    }
    
    public Action getCopy(Action action){
    	Cloner cloner = new Cloner();
    	cloner.dontCloneInstanceOf(Effect.class); // Effetti non possono essere deepCopiati dalla libreria cloning
    	return cloner.deepClone(action);
    }
    
    //*******************************************************************************//      
    
    // SimulateWithCopy: Try the Action with Copies of Board, Player, action. It returns a boolean and it does not change the state of the game
    public boolean simulateWithCopy(Game game, Board board, Player clonePlayer,Player player,Action action){
    		
    		// Check: this is a BlockedSpace?
    		if(checkBlockedZone(game.getPlayerList().size(), action)){
    			return false;
    		}
    		
    		//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
    		if(!MakeAction.usePermamentEffect(board, clonePlayer, player, action)){
    			return false;
    		}
    		
    		// uses the servants if the player can't pass the check on ActionValue
    		if(!useServants(board, clonePlayer, action)){
    			return false;
    		}
    		
    		//prendo il bonus dell'actionSpace
    		clonePlayer.getResources().addResource(board.getRegion(action.getActionRegionId())
    				.getActionSpace(action.getActionSpaceId()).getBonus());
    		
    		// check l'azione
    		if(!checkMove(board, clonePlayer, action)){
    			return false;
    		}
    			//effettuo l'azione
    		switch(action.getActionType()){
    			
    			case "PRODUCTION" : {
    				
    				return makeProduction.tryMake(board, player, action);
    				}
    			case "HARVEST" : {
    				
    				return makeHarvest.tryMake(board, clonePlayer, action);
    			}
    			case "COUNCIL" : {}
    			case "MARKET" : {}
    			//case  of a "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
    			default : {return false;}
    			
    			}
    			//sostituisco le copie con le originali
    			//game.setPlayer(clonePlayer);
    			//game.setBoard(cloneBoard);
    }

    //*******************************************************************************//      
    
    //Simulate: SimulateWithCopy version using the original objects. It changes the state of the game.
    public void simulate(Game game, Board board, Player player,Action action){
    	
		//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
		MakeAction.usePermamentEffect(board, player, action);
		
		// uses the servants if the player can't pass the check on ActionValue
		useServants(board, player, action);
		
		//prendo il bonus dell'actionSpace
		player.getResources().addResource(board.getRegion(action.getActionRegionId())
				.getActionSpace(action.getActionSpaceId()).getBonus());
		
		//effettuo l'azione
		switch(action.getActionType()){
			
			case "PRODUCTION" : {
				
				makeProduction.make(game, board, player, action);
				}
			case "HARVEST" : {
				
				makeHarvest.make(board, player, action);
			}
			case "COUNCIL" : {}
			case "MARKET" : {}
			//case  of a "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
			
			}
			//sostituisco le copie con le originali
			//game.setPlayer(clonePlayer);
			//game.setBoard(cloneBoard);    	
    	
    }
}
