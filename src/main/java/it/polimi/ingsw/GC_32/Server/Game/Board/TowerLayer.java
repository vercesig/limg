package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class TowerLayer {

	private DevelopmentCard card;
	private ActionSpace actionSpace;
	
	private TowerLayer(int regionID, int actionSpaceID){
		this.actionSpace = new ActionSpace(null, 0, false, regionID, actionSpaceID);
	}
	
	public static TowerLayer create(int regionID, int actionSpaceID){
		return new TowerLayer(regionID, actionSpaceID);
	}
	
	public DevelopmentCard getCard(){
		return this.card;
	}
	
	public ActionSpace getActionSpace(){
		return this.actionSpace;
	}
	
	public void setCard(DevelopmentCard card){
		this.card = card;
	}
	
	
}
