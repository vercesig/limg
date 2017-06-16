package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class TowerLayer {

	private DevelopmentCard card;
	private ActionSpace actionSpace;
	
	public TowerLayer(int regionID, int actionSpaceID){
		ResourceSet bonus = new ResourceSet();
		bonus.setResource("COINS", 1);
		boolean single = true;
		int actionValue = 2*actionSpaceID + 1;
		this.actionSpace = new ActionSpace(bonus, actionValue, single, regionID, actionSpaceID);
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
	
	public DevelopmentCard takeCard(){
		DevelopmentCard takenCard = this.card;
		this.card = null;
		return takenCard;
	}
	
	public void flushTowerLayer(){
		this.card = null;
		actionSpace.flushActionSpace();
	}
	
}
