package it.polimi.ingsw.GC_32.Server.Game.ActionHandler;

import java.util.LinkedList;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.ImpossibleMoveException;

public class MakeHarvest {
	
	public MakeHarvest(){}
	
	public static boolean tryMake(Board board, Player player, Action action){
		
		// sommo i bonus della tile 
		player.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus());
		
		LinkedList<DevelopmentCard> cardlist = new LinkedList<DevelopmentCard>();
		cardlist = player.getPersonalBoard().getCardsOfType("TERRITORYCARD");
		for(DevelopmentCard c : cardlist){
			try {
				c.getInstantEffect()
				.apply(board, player, action);
			} catch (ImpossibleMoveException e) {
				return false;
			}
		}
		//ultimo check
		if(player.getResources().hasNegativeValue()){
			return false;
		}
		return true;
	}
	
	// same logic as tryMake. This is Used for the Original
	public static void make(Board board, Player player, Action action){
		player.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus());
		
		LinkedList<DevelopmentCard> cardlist = new LinkedList<DevelopmentCard>();
		cardlist = player.getPersonalBoard().getCardsOfType("TERRITORYCARD");
		for(DevelopmentCard c : cardlist){
			try {
				c.getInstantEffect()
				.apply(board, player, action);
			} 
			catch (ImpossibleMoveException e) {}
		}
	}
}
