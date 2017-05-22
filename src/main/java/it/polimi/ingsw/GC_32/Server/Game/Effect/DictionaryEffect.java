package it.polimi.ingsw.GC_32.Server.Game.Effect;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;

/* Piccola CLasse dove sono definiti due funzioni da usare come checker!
 * */

public class DictionaryEffect{
    
	
	public static boolean checkValidRegionID (Board board, Player player, Action action){
		if( board.getRegionMap().containsKey(action.getActionRegionId())){
    		return true;
    	} return false;
	}
	public static boolean checkValidActionSpaceID (Board board, Player player, Action action){
		if( board.getRegion(action.getActionRegionId()).contains(action.getActionSpaceId())){
    		return true;
    	} return false;
	}
	
    public void DictionaryEffect(){
    	
        EffectRegistry.getInstance().registerEffect("checkValidRegionID", DictionaryEffect :: checkValidRegionID);
        EffectRegistry.getInstance().registerEffect("checkValidActionSpaceID", DictionaryEffect:: checkValidActionSpaceID);   
      
    }
}
