package it.polimi.ingsw.GC_32.Server.Game.Effect;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

@FunctionalInterface
public interface Effect{
	boolean apply(Board b, Player p, Action a);
}