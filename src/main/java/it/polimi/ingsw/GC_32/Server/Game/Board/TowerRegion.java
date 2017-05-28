package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class TowerRegion implements Region {

	private TowerLayer[] towerLayers;
	private boolean towerBusy;
	private final int regionID;
	private String typeCard;
	
	public TowerRegion(int regionID, int numberOfLayers){
		this.regionID = regionID;
		this.towerBusy = false;
		this.towerLayers = new TowerLayer[numberOfLayers];
		for(int i=0; i<numberOfLayers; i++){
			this.towerLayers[i] = new TowerLayer(this.regionID, i);
		}
	}
	
	public String getTypeCard(){
		return this.typeCard;
	}
	
	public void setTypeCard(String type){
		this.typeCard = type;
	}
	
	public boolean isTowerBusy(){
		return this.towerBusy;
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
			// ....
		}
	}
	
	// rimuove carte e familiari sulla torre
	public void flushTowerRegion(){
		
	}
	
	public void placeCards(){
		
	}
	// implementazioni Metodi Astratti Region
		public int getRegionID(){
			return this.regionID;
		}
		
		public String toString(){
			StringBuilder stringBuilder = new StringBuilder();
			for (TowerLayer level: towerLayers){
				stringBuilder.append(level.getActionSpace().toString());
				stringBuilder.append("\n'''''''''''''''''''''''''''''''''''''''''''''''''\n");
				stringBuilder.append("CARTA: \n");
				stringBuilder.append(level.getCard().toString());
				stringBuilder.append("====================================================\n");
			}
			return new String(stringBuilder);
		}
		
		public ActionSpace getActionSpace(int id){
			try{
				for (TowerLayer level: towerLayers){
					if(level.getActionSpace().getActionSpaceID() == id)
						return level.getActionSpace();
				} return null;
			}catch(NullPointerException e){
				return null;
			}
		}
}
