package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class MoveChecker{
	
	ArrayList <Check> checkList;
	
    public MoveChecker(){
    	this.checkList = new <Check> ArrayList();
    	checkList.add(checkValidActionSpaceID);
    	checkList.add(checkValidRegionID);
    }
    
   /* public static boolean checkValidRegionID (Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId()) == null){
			return false;
		} return true;
	}
	
	public static boolean checkValidActionSpaceID (Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId())
				.getActionSpace(action.getActionSpaceId()) == null){
			return false;
		} return true;
	}
		
    public static boolean checkStandardMove(Board board, Player player, Action action){
            
    		boolean checkValidRegionID = checkValidRegionID(board, player, action);
    		boolean checkValidActionSpaceID = checkValidActionSpaceID (board, player, action);
        	return checkValidRegionID && checkValidActionSpaceID;
    	
        }
   */
   
 /*  Check Cardcost = (Board b, Player p, Action a) -> {	   
	   ArrayList <ResourceSet> cardCost =  b.getTowerRegion()[a.getActionRegionId()]
			   .getTowerLayers()[a.getActionSpaceId()].getCard().getCost();
	   for(ResourceSet c: cardCost){
		   if(p.getResources().compareTo(c) >= 0){ // player puo' pagare almeno un costo
			   return true;
		   }
	   }
	   return false;
   };
   */ 
    
   Check territoryRequirements = (Board b, Player p, Action a) -> {
	   switch (p.getPersonalBoard().getCardsOfType("TERRITORYCARD").size()){
	   case 2:
		   if(p.getResources().getResouce(" MILITARY_POINTS") < 3){
			   return false;
		   }else return true;
	   case 3:
		   if(p.getResources().getResouce("MILITARY_POINTS") < 8){
			   return false;
		   }else return true;
	   case 4:
		   if(p.getResources().getResouce("MILITARY_POINTS") < 12){
			   return false;
		   }else return true;   
	   case 5:
		   if(p.getResources().getResouce("MILITARY_POINTS") < 18){
			   return false;
		   }else return true; 
	   }
	return false;
   };
    
   // Standard Check
   Check checkValidActionSpaceID = (Board b, Player p, Action a) -> {
    		if(b.getRegion(a.getActionRegionId()).getActionSpace(a.getActionSpaceId()) == null){
    			return false;
    		} return true;
    };
    Check checkValidRegionID = (Board b, Player p, Action a) -> {
    	if(b.getRegion(a.getActionRegionId()) == null){
			return false;
		} return true;
    };
    
    public boolean checkMove(Board board, Player player, Action action){
    	
    	Boolean isValid;
    	for (Check c: this.checkList){
    		isValid = c.apply(board, player, action);
    		if(isValid.equals(false)){
    			return false;
    		}
    	}
    	return true;
    }
    
   }