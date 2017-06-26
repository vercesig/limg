package it.polimi.ingsw.GC_32.Server.Game;

import com.rits.cloning.Cloner;

import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class MoveChecker{
	private final Logger LOGGER = Logger.getLogger(this.getClass().toString());
	private Cloner cloner;
	
    public MoveChecker(){
    	this.cloner = new Cloner();
    	//Effects cannot be deepcopied
    	cloner.dontCloneInstanceOf(Effect.class);
    } 


    public boolean checkValidIDs(Board board, Player player, Action action){
    	return (MoveUtils.checkValidRegionID(board, player, action) &&
    			MoveUtils.checkValidActionSpaceID(board, player, action));
    }
    
   public boolean checkFamiliarMove(Game game, Board board, Player player, Action action){
	   return (MoveUtils.familyColor(board, player, action) &&
			   MoveUtils.isFreeSingleSpace(board, player, action) &&
			   !MoveUtils.checkBlockedZone(game.getPlayerList().size(), action));
   }
   
   public boolean checkActionSpaceCost(Board board, Player player, Action action){
	   MoveUtils.addActionSpaceBonus(board, player, action);
	   return (MoveUtils.checkServants(board, player, action) &&
			   MoveUtils.checkCoinForTribute(board, player, action) &&
			   MoveUtils.checkCardCost(board, player, action));
   }
        
   public boolean checkCardRequirement(Board board, Player player, Action action){
	   if(action.getActionRegionId() >= 4){
		   return (MoveUtils.checkPersonalBoardRequirement(board, player, action) &&
				   MoveUtils.checkCardCost(board, player, action));
	   }
	   return true;
   }

   public boolean checkMove(Game game, Player player, Action action){
	   Board cloneBoard = cloner.deepClone(game.getBoard());
	   Player clonePlayer = cloner.deepClone(player);
	   if(checkValidIDs(cloneBoard, clonePlayer, action)){
		   MoveUtils.cloneApplyEffects(cloneBoard, player, clonePlayer, action);
		   return (action.isValid() &&
				   checkFamiliarMove(game, cloneBoard, clonePlayer, action) &&
				   checkActionSpaceCost(cloneBoard, clonePlayer, action) &&
				   checkCardRequirement(cloneBoard, clonePlayer, action));
	   } else {
		   return false;
	   }
   }
}
