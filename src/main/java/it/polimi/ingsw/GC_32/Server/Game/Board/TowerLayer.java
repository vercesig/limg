package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class TowerLayer {

	private DevelopmentCard card;
	private ActionSpace actionSpace;
	
	public TowerLayer(int regionID, int actionSpaceID){
		ResourceSet bonus = new ResourceSet();
		boolean single = true;
		int actionValue = 1;
		
		switch(actionSpaceID){		// BRUTTO_ CI VUOLE UNA NOVA SOLUZIONE
			case 0:
				actionValue = 1;
				break;
			case 1:
				actionValue = 3;
				break;
			case 2:
				actionValue = 5;
				break;
			case 3:
				actionValue = 7;
				break;
		}
		
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
	
	
}
