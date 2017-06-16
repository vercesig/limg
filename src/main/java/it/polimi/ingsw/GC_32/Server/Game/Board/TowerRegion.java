package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.logging.Level;

import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class TowerRegion extends Region {

	private TowerLayer[] towerLayers;
	private boolean towerBusy;
	private String typeCard;
	
	public TowerRegion(int regionID, int numberOfLayers){
		super(regionID, numberOfLayers);
		this.towerBusy = false;
		this.towerLayers = new TowerLayer[numberOfLayers];
		for(int i=0; i<numberOfLayers; i++){
			this.towerLayers[i] = new TowerLayer(this.getRegionID(), i);
			this.getTrack()[i] = towerLayers[i].getActionSpace();
		}
	}
	
	public String getTypeCard(){
		return this.typeCard;
	}
	
	public void setTypeCard(String type){
		this.typeCard = type;
	}
	
	public boolean isTowerBusy(){
		for(int i=0; i<this.getTrack().length; i++){
			if(this.getTrack()[i].getPlayers().size()>0){
				return true;
			}
		}
		return false;
	}
	
	public TowerLayer[] getTowerLayers(){
		return this.towerLayers;
	}
	
	public boolean canIPlaceFamilyMember(Player player){
		// c'è già un familiare del mio stesso colore sulla torre
		for(TowerLayer level : towerLayers){
			if(level.getActionSpace().getPlayers().get(0).getUUID() == player.getUUID()){
				return false;
			}
		}
		return true;
	}
	
	public void placeFamilyMember(FamilyMember familyMember, int layer){
		if(canIPlaceFamilyMember(familyMember.getOwner())){
			towerLayers[layer].getActionSpace().addFamilyMember(familyMember);
		}
		if(!this.towerBusy){
			this.towerBusy = true;
		}
	}
	
	// rimuove carte e familiari sulla torre
	public void flushRegion(){
		for(int i=0; i<towerLayers.length; i++){
			towerLayers[i].getActionSpace().flushActionSpace();
		}
	}
		
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		for (TowerLayer level: towerLayers){
			stringBuilder.append(level.getActionSpace().toString());
			stringBuilder.append("\n'''''''''''''''''''''''''''''''''''''''''''''''''\n");
			stringBuilder.append("CARTA: \n");
			try{
				stringBuilder.append(level.getCard().toString());
			}catch(NullPointerException e){
				Logger.getLogger("").log(Level.SEVERE, "context", e);
				stringBuilder.append("nessuna carta presente\n");
			}
			stringBuilder.append("====================================================\n");
		}
		return new String(stringBuilder);
	}
}
