package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

public class MoveChecker{
	
    public MoveChecker(){}
    
    public static boolean checkValidRegionID(Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId()) == null){
			return false;
		}
		return true;
	}
	
	public static boolean checkValidActionSpaceID(Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId())
				.getActionSpace(action.getActionSpaceId()) == null){
			return false;
		}
		return true;
	}
		
    public static boolean checkStandardMove(Board board, Player player, Action action){
            
    		boolean checkValidRegionID = checkValidRegionID(board, player, action);
    		boolean checkValidActionSpaceID = checkValidActionSpaceID (board, player, action);
        	return checkValidRegionID && checkValidActionSpaceID;
    }
    
    public static boolean checkMove(Board board, Player player, Action actionToCheck){
    	return true;
    }
    
}