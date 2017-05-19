package it.polimi.ingsw.GC_32.Game;

import it.polimi.ingsw.GC_32.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Game.Board.Board;

public class MoveChecker{

    private MoveChecker(){}

    public static boolean checkMove(Board b, Player p, Action a){
        boolean cando = true;
        for( Effect e : p.effectList ){
            if(! e.apply(b,p,a) ){
                cando = false;
                break;
            }
        }

        if( a.resourceCost < p.resources && cando ){
            for( Effect e : p.effectList ){
                e.apply(b,p,a);
            }
            p.resource -= a.resourceCost;
        }
    }


}
