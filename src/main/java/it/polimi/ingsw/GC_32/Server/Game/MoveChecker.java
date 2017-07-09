package it.polimi.ingsw.GC_32.Server.Game;

import com.rits.cloning.Cloner;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Server.Game.MoveUtils;

/**
 * the main clas which perform the simulation of the action and assess if the action is valid or not.
 * 
 * <ul>
 * <li>{@link #cloner}: instance of the cloner which is used for copy the elements of the game, in particular the board, the action and the player status</li>
 * </ul>
 *
 * @see Board, Player, Action, MoveUtilis
 */

public class MoveChecker{
	private Cloner cloner;
	
	/**
	 * initialize the cloner
	 */
    public MoveChecker(){
    	this.cloner = new Cloner();
    	//Effects cannot be deepcopied
    	cloner.dontCloneInstanceOf(Effect.class);
    } 


    /**
     * check if the action contains valid region and action space ID
     * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 * @return true if the check is passed
     */
    public boolean checkValidIDs(Board board, Player player, Action action){
    	return (MoveUtils.checkValidRegionID(board, player, action) &&
    			MoveUtils.checkValidActionSpaceID(board, player, action));
    }
   
    /**
     * check if the family member cam be placed on the indicated action space
     * @param board the board of the game
	 * @param player the player who has performed the action
	 * @param action the action performed by the player
	 * @return true if the check is passed
     */
   public boolean checkFamiliarMove(Game game, Board board, Player player, Action action){
	   if(action.getAdditionalInfo().asObject().get("BONUSFLAG").asBoolean()){
		   return true;
	   }else{
	   return (MoveUtils.familyColor(board, player, action) &&
			   MoveUtils.isFreeSingleSpace(board, player, action) &&
			   !MoveUtils.checkBlockedZone(game.getPlayerList().size(), action));
	   }
   }
   
   /**
    * check if the cost for place the pawn on the indicated action space can be payed
    * @param board the board of the game
	* @param player the player who has performed the action
	* @param action the action performed by the player
	* @return true if the check is passed
    */
   public boolean checkActionSpaceCost(Board board, Player player, Action action){
	   MoveUtils.addActionSpaceBonus(board, player, action);
	   return (MoveUtils.checkServants(board, player, action) &&
			   MoveUtils.checkCoinForTribute(board, player, action) &&
			   MoveUtils.checkNotFoundCard(board, action));// false if there is not a card in towerLayers

   }
   
   /**
    * check if the card can be taken
    * @param board the board of the game
	* @param player the player who has performed the action
	* @param action the action performed by the player
	* @return true if the check is passed
    */
   public boolean checkCardRequirement(Board board, Player player, Action action){
	   if(action.getRegionId() >= 4){
		   return (MoveUtils.checkPersonalBoardRequirement(board, player, action) &&
				   MoveUtils.checkCardCost(board, player, action));
	   }
	   return true;
   }

   /**
    * check if the entire action is valid, calling intermiediate check simulating the action on a copy of the player, board and action
    * @param game the game handled by this MoveChecker instance 
	* @param player the player who has performed the action
	* @param action the action performed by the player
    * @param cm the instance of the context manager, maybe contexts must be opened
    * @return true if the actino is valid, false otherwise
    */
   public boolean checkMove(Game game, Player player, Action action, ContextManager cm){
	   Board cloneBoard = cloner.deepClone(game.getBoard());
	   Player clonePlayer = cloner.deepClone(player);
	   Action cloneAction = cloner.deepClone(action);
	   
	   if(checkValidIDs(cloneBoard, clonePlayer, cloneAction)){
		   System.out.println("checkValidID: true");
		   System.out.println("LISTA EFFETTI: " + player.getEffectList());
		   MoveUtils.cloneApplyEffects(cloneBoard, clonePlayer, player, cloneAction, cm);
		   System.out.println("DOPO AVER APPLICATO GLI EFFETTI");
		   System.out.println(clonePlayer);
		   System.out.println(cloneAction);		   

		   return (cloneAction.isValid() &&
				   checkFamiliarMove(game, cloneBoard, clonePlayer, cloneAction) &&
				   checkActionSpaceCost(cloneBoard, clonePlayer, cloneAction) &&
				   checkCardRequirement(cloneBoard, clonePlayer, cloneAction));
	   } else {
		   return false;
	   }
   }
}
