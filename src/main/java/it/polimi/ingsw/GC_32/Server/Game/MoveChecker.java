package it.polimi.ingsw.GC_32.Server.Game;


import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.ActionHandler.MakeAction;
import it.polimi.ingsw.GC_32.Server.Game.ActionHandler.MakeHarvest;
import it.polimi.ingsw.GC_32.Server.Game.ActionHandler.MakeProduction;
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
    public boolean firstCheck(Board board, Player player, Action action){
    	
    	log.info("/******** CHECK: is a valid region id?\n" + Check.checkValidRegionID(board, player, action) + "\n");
    	if(!Check.checkValidRegionID(board, player, action)){
			return false;
		}
    	
		log.info("/******** CHECK: is a valid actionSpace id?\n" + Check.checkValidActionSpaceID(board, player, action) + "\n");
		if(!Check.checkValidActionSpaceID(board, player, action)){
			return false;
		}
		
		log.info("/******** CHECK: is a valid action type?\n" + Check.checkValidActionType(board,player, action) + "\n");
		if(!Check.checkValidActionType(board,player, action)){
			return false;
    	} 
    	return true;
    }
    
   public boolean moveFamilyMember(Game game, Board board, Player player, Action action){
	    
	   log.info("/******** CHECK: is the rule of Only one Family color respected?" + Check.familyColor(board, player, action) + "\n");
   	    if(!Check.familyColor(board, player, action)){
			return false;
		}
   	
   	    log.info("/******** CHECK: is a free actionSpace?"+ Check.isFreeSingleSpace(board, player, action) + "\n");
		if(!Check.isFreeSingleSpace(board, player, action)){
			return false;
		}
   	
		log.info("/******** CHECK: is a space blocked?"+ Check.checkBlockedZone(game.getPlayerList().size(), action) + "\n");
		if(!Check.checkBlockedZone(game.getPlayerList().size(), action)){
			return false;
		}
		
		log.info("/******** CHECK: can the player use servants to positionate the family member?"+ Check.useServants(board, player, action) + "\n");
		if(!Check.useServants(board, player, action)){ // change the state of the game
			return false;
		}
		
		log.info("/******** CHECK: has the player three coins because the tower is busy?"+ Check.checkCoinForTribute(board, player, action) + "\n");
		if(!Check.checkCoinForTribute(board, player, action)){ // change the state of the game
			return false;
		}
	
	int pawnID = action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt();
	game.moveFamiliar(player, board,  pawnID, action); // change the state of the game
   	return true;
   }
        
   public boolean takeCard(Game game, Board board, Player player, Action action){
	   
	   log.info("/******** CHECK: has the player enough military points?"+ Check.checkTerrytoryRequirement(board, player, action) + "\n");
	   if(!Check.checkTerrytoryRequirement(board, player, action)){
		   return false;
	   }
	   
	   log.info("/******** CHECK: has the player six card of the same type of the card he wants to buy?"+ Check.checkMaxCard(board, player, action) + "\n");
		if(!Check.checkMaxCard(board, player, action)){
			return false;
		}
		
		log.info("/******** CHECK: can the player pay the cost of the card?"+ Check.checkCost(board, player, action) + "\n");
		if(!Check.checkCost(board, player, action)){ //change the state of the game
			return false;
		}
		
		game.takeCard(player, board, action); // change the state of the game
		return true;
   }
   
//*******************************************************************************//  
    
   // SimulateWithCopy: Try the Action with Copies of Board, Player, action. It returns a boolean and it does not change the state of the game
     // Everything with Effect will produce an exception with copies.
   
    public boolean simulateWithCopy(Game game, Board cloneBoard, Player clonePlayer, Player player, Action cloneAction){
    		
    		// are valid arguments?
    		if(!firstCheck(cloneBoard, clonePlayer, cloneAction)){
    			return false;
    		}
    		//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
    		if(!MakeAction.usePermamentEffect(cloneBoard, clonePlayer, player, cloneAction)){
    			return false;
    		}
    		
    		if(!moveFamilyMember(game, cloneBoard, clonePlayer, cloneAction)){
    			return false;
    		}
    		//prendo il bonus dell'actionSpace
    		clonePlayer.getResources().addResource(cloneBoard.getRegion(cloneAction.getActionRegionId())
    				.getActionSpace(cloneAction.getActionSpaceId()).getBonus());
    		
    		switch(cloneAction.getActionType()){	
    			
    			case "PRODUCTION" : {
    				
    				return MakeProduction.tryMake(cloneBoard, player, cloneAction);
    				}
    			case "HARVEST" : {
    				
    				return MakeHarvest.tryMake(cloneBoard, clonePlayer, cloneAction);
    			}
    			case "COUNCIL" : {
    				return true;
    			}
    			case "MARKET" : {
    				return true;
    			}
    			
    			//case  of a "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
    			default : {
    				
    				return takeCard(game, cloneBoard, clonePlayer, cloneAction);
    				}
    			
    			}
    }

    //Simulate: SimulateWithCopy version using the original objects. It changes the state of the game.
    public void simulate(Game game, Board board, Player player,Action action){
    	
		//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
		MakeAction.usePermamentEffect(board, player, action);
		
		// uses the servants if the player can't pass the check on ActionValue
		moveFamilyMember(game, board, player, action);
		
		//prendo il bonus dell'actionSpace
		player.getResources().addResource(board.getRegion(action.getActionRegionId())
				.getActionSpace(action.getActionSpaceId()).getBonus());
		
		//effettuo l'azione
		switch(action.getActionType()){
			
			case "PRODUCTION" : {
				
				MakeProduction.make(game, board, player, action);
				}
			case "HARVEST" : {
				
				MakeHarvest.make(game, board, player, action);
			}
			case "COUNCIL" : {}
			case "MARKET" : {}
			//case  of a "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
			default:{
				
				TowerRegion selectedTower = (TowerRegion)(board.getRegion(action.getActionRegionId()));
		    	DevelopmentCard card = selectedTower.getTowerLayers()[action.getActionSpaceId()].getCard();
		    	
		    	takeCard(game, board, player, action);
		    	player.addEffect(card.getPermanentEffect());
		    	try {
		    		card.getInstantEffect().apply(board, player, action);
		    	} catch (ImpossibleMoveException e) {
					log.severe("ERRORE!");
		    	}
			}
		}
    }
}
