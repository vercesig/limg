package it.polimi.ingsw.GC_32.Server.Game.Board;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class TowerLayer {

	private DevelopmentCard card;
	private ActionSpace actionSpace;
	
	public TowerLayer(int regionID, int actionSpaceID){
		
		ResourceSet bonus = new ResourceSet(towerBonus(regionID, actionSpaceID));
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
	
	private JsonObject towerBonus(int regionID, int actionSpaceID){
		JsonObject bonusJs = new JsonObject();
		
		if(actionSpaceID == 2){ // penultimo livello
			switch (regionID){
			case 4 : { bonusJs.add("WOOD", 1); break;}
			case 5 : { bonusJs.add("STONE", 1); break;}
			case 6 : { bonusJs.add("MILITARY_POINTS", 1); break;}
			case 7 : { bonusJs.add("COINS", 1); break;}
			default : break;
			}
		}
		
		if(actionSpaceID == 3){ // ultimo livello
			switch (regionID){
			case 4 : { bonusJs.add("WOOD", 2); break;}
			case 5 : { bonusJs.add("STONE", 2); break;}
			case 6 : { bonusJs.add("MILITARY_POINTS", 2); break;}
			case 7 : { bonusJs.add("COINS", 2); break;}
			default : break;
			}
		} return bonusJs;
	}
}
