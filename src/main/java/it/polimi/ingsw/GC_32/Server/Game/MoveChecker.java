package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class MoveChecker{

    private MoveChecker(){}

    public static boolean checkMove(Board b, Player p, Action a){
        boolean cando = true;
        for( Effect e : p.getEffectList() ){
            if(! e.apply(b,p,a) ){
                return false;
            }
        }

        if( a.cost.compareTo(p.getResources()) > 0 && cando ){
        	return true;
            //for( Effect e : p.getEffectList() ){
            //    e.apply(b,p,a);
            //}
            //p.resource -= a.resourceCost;
        } else {
        	return false;
        }
    }


}
