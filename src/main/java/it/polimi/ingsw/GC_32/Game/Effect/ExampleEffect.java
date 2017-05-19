package it.polimi.ingsw.GC_32.Game.Effect;

import it.polimi.ingsw.GC_32.Game.Board.Board;
import it.polimi.ingsw.GC_32.Game.Action;
import it.polimi.ingsw.GC_32.Game.Player;

public class ExampleEffect{
    
    Effect example = (Board b, Player p, Action a) -> {
    	return true;
    };

    public ExampleEffect(){
        //EffectRegistry.getInstance().registerEffect("MUCH_RICH", ExampleEffect::example);
    }
}
