package it.polimi.ingsw.GC_32.Game.Board;

import it.polimi.ingsw.GC_32.Game.FamilyMember;
import it.polimi.ingsw.GC_32.Game.Player;

public class TowerRegion {

	private TowerLayer[] towerLayers;
	private boolean towerBusy;
	
	public TowerRegion(){
		this.towerBusy = false;
		this.towerLayers = new TowerLayer[4]; //rendere livelli scalabili
	}
	
	public boolean isTowerBusy(){
		return this.towerBusy;
	}
	
	public boolean canIPlaceFamilyMember(Player player){
		// c'è già un familiare del mio stesso colore sulla torre
		for(TowerLayer level : towerLayers){
			if(level.getActionSpace().getPlayers().get(0).getColor() == player.getColor()){ //poco elegante, da rivedere...
				return false;
			}
		}
		return true;
	}
	
	public void placeFamilyMember(FamilyMember familyMember, int layer){
		if(canIPlaceFamilyMember(familyMember.getOwner())){
			towerLayers[layer].getActionSpace().addFamilyMember(familyMember);
			// ....
		}
	}
	
	// rimuove carte e familiari sulla torre
	public void flushTowerRegion(){
		
	}
	
	public void placeCards(){
		
	}
	
}
