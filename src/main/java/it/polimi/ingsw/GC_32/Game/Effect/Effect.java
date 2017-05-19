package it.polimi.ingsw.GC_32.Game.Effect;

import it.polimi.ingsw.GC_32.Game.Board.Board;
import it.polimi.ingsw.GC_32.Game.Player;
import it.polimi.ingsw.GC_32.Game.Action;

@FunctionalInterface
public interface Effect{
	Boolean apply(Board b, Player p, Action a);
}