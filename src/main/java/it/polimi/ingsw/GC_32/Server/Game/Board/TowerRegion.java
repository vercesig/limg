package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class TowerRegion {

	private TowerLayer[] towerLayers;
	private boolean towerBusy;
	private final int regionID;
	
	public TowerRegion(int regionID, int numberOfLayers){
		this.regionID = regionID;
		this.towerBusy = false;
		this.towerLayers = new TowerLayer[numberOfLayers];
		for(int i=0; i<numberOfLayers; i++){
			this.towerLayers[i] = TowerLayer.create(this.regionID,i);
		}
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
