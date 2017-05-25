package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.List;
import it.polimi.ingsw.GC_32.Server.Game.Player;



public class Board {

	private TowerRegion[] towerRegion;
	private ProductionRegion productionRegion;
	private HarvestRegion harvestRegion;
	private CouncilRegion councilRegion;
		
	public Board(){		
		this.towerRegion = new TowerRegion[4]; //rendere scalabile
		for(int i=0; i<4; i++){
			towerRegion[i] = new TowerRegion(i,4);
		}
		this.productionRegion = new ProductionRegion(0);
		this.harvestRegion = new HarvestRegion(1);
		this.councilRegion = new CouncilRegion(2);
	}
	
	public TowerRegion[] getTowerRegion(){
		return this.towerRegion;
	}
	
	public ProductionRegion getProductionRegion(){
		return this.productionRegion;
	}
	
	public HarvestRegion getHarvastRegion(){
		return this.harvestRegion;
	}
	
	public CouncilRegion getCouncilRegion(){
		return this.councilRegion;
	}
	
	public void flushBoard(){
		
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		for(int i=0; i<towerRegion.length; i++){
			tmp.append("--------------------------------------------torre numero: "+i+"\n");
			for(int j=0; j<towerRegion[i].getTowerLayers().length; j++){
				tmp.append("-------------- carta al livello: "+j+"\n");
				tmp.append(towerRegion[i].getTowerLayers()[j].getCard().toString());
			}
		}
		return new String(tmp);
	}
	
}
