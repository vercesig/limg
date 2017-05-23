package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.List;
import it.polimi.ingsw.GC_32.Server.Game.Player;



public class Board {

	private TowerRegion[] towerRegion;
	private ProductionRegion productionRegion;
	private HarvestRegion harvestRegion;
	private CouncilRegion councilRegion;
		
	public Board(){		
		this.towerRegion = new TowerRegion[3]; //rendere scalabile		
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

	public List<Player> getPlayerOrder() {
		return null;
	}
	
}
