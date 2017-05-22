package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class TowerRegion extends Region {

	private TowerLayer[] towerLayers;
	private boolean towerBusy;
	//private final int regionID;
	
	public TowerRegion(int id, int numberOfLayers){
		super(id);
		this.towerBusy = false;
		this.towerLayers = new TowerLayer[numberOfLayers];
		for(int i=0; i<numberOfLayers; i++){
			this.towerLayers[i] = TowerLayer.create(this.getRegionID(),i);
		}
	}
	
	public boolean isTowerBusy(){
		return this.towerBusy;
	}
	
/*	public boolean canIPlaceFamilyMember(Player player){
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
	}*/
	
	// rimuove carte e familiari sulla torre
	
/*	public int getTowerRegionID(){
		return this.regionID;
	}*/
	
	public void flushTowerRegion(){
		
	}
	
	public void placeCards(){
		
	}
	// metodo che verifica se e' presente un actionSpace in quella regione con id passato
	@Override
	public boolean contains(int id){
		for (TowerLayer level: towerLayers){
			if(level.getActionSpace().getActionSpaceID() == id)
				return true;
		} return false;
	}
	
	@Override
	public void print(){
		for (TowerLayer level: towerLayers){
			level.getActionSpace().print();
		}
	}
	
}

