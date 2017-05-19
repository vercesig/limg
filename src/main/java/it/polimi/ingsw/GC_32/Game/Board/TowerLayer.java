package it.polimi.ingsw.GC_32.Game.Board;

import it.polimi.ingsw.GC_32.Game.DevelopmentCard;

public class TowerLayer {

	private DevelopmentCard card;
	private ActionSpace actionSpace;
	
	private TowerLayer(DevelopmentCard card, ActionSpace actionSpace){
		this.card = card;
		this.actionSpace = actionSpace;
	}
	
	public static TowerLayer create(DevelopmentCard card, ActionSpace actionSpace){
		return new TowerLayer(card, actionSpace);
	}
	
	public DevelopmentCard getCard(){
		return this.card;
	}
	
	public ActionSpace getActionSpace(){
		return this.actionSpace;
	}
	
	
}
