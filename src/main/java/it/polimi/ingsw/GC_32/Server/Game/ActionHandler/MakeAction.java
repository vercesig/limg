package it.polimi.ingsw.GC_32.Server.Game.ActionHandler;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Server.Game.Effect.ImpossibleMoveException;

public class MakeAction {

	public MakeAction(){}
	
	public static boolean usePermamentEffect(Board board, Player player, Action action){
		try{
			for(Effect buff : player.getEffectList()){
				buff.apply(board, player, action);
			}
		}
		catch(ImpossibleMoveException e){return false;}
	return true;
	}
	
	public static boolean usePermamentEffect(Board board, Player playerCopy, Player player, Action action){
		try{
			for(Effect buff : player.getEffectList()){
				buff.apply(board, playerCopy, action);
			}
		}
		catch(ImpossibleMoveException e){return false;}
	return true;
	}
}
