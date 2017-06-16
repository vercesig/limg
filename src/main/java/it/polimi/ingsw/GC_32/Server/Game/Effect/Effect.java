package it.polimi.ingsw.GC_32.Server.Game.Effect;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
/**
 * functional interface representing an Effect. Each effect has a method "apply" reciving as parameter the board, the player to whom the effect will be applied, and 
 * the action which has triggered the effect.
 * @author alessandro
 *
 */
@FunctionalInterface
public interface Effect{
	void apply(Board b, Player p, Action a) throws ImpossibleMoveException;
}