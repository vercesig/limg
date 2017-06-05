package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;

@FunctionalInterface
public interface Check {
	boolean apply(Board b, Player p, Action a);
}
