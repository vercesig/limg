package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Effect.DictionaryEffect;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Server.Game.Effect.EffectRegistry;

public class MoveChecker{
	
    public MoveChecker(){}

   /* check standard per ogni tipologia di azione. Valuto se:
   *	- ActionSpaceID dell'azione e' valida;
   *	- RegionID dell'azione e' valida; 
   * @TO-DO: e se venisse caricato direttamente un Array di Effetti dalla Registry?
   */
        public static boolean checkStandardMove(Board board, Player player, Action a){
            
        	ArrayList<Effect> checkStandardEffect = new ArrayList<Effect>();
        
        // @TO-DO: 
        // prendere il riferimento dalla Registry non funziona e viene lanciata 
        // una NullPointerException
        //	Effect checkValidRegionID = EffectRegistry.getInstance().getEffect("checkValidRegionID");
        //	Effect checkValidActionSpaceID = EffectRegistry.getInstance().getEffect("checkValidActionSpaceID");
            
        	Effect checkValidActionSpaceID = DictionaryEffect::checkValidActionSpaceID;
        	Effect checkValidRegionID = DictionaryEffect::checkValidRegionID;
        
        	checkStandardEffect.add(checkValidActionSpaceID);
            checkStandardEffect.add(checkValidRegionID);
          
        	for( Effect effect : checkStandardEffect){
                if(! effect.apply(board, player, a))
                	return false;
            } return true;
        }
   }