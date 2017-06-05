package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Player;



public class Board {

	private ArrayList <Region> region;
	private TowerRegion[] towerRegion;
	private ProductionRegion productionRegion;
	private HarvestRegion harvestRegion;
	private CouncilRegion councilRegion;
	private MarketRegion marketRegion;
		
	public Board(){

		this.productionRegion = new ProductionRegion(0);
		this.harvestRegion = new HarvestRegion(1);
		this.councilRegion = new CouncilRegion(2);
		this.marketRegion = new MarketRegion(3);
		
		this.region = new ArrayList <Region>();
		region.add(0, (Region) productionRegion); 
		region.add(1, (Region) harvestRegion);
		region.add(2, (Region) councilRegion); 
		region.add(3, (Region) marketRegion);
	}
	
	public TowerRegion[] getTowerRegion(){
		return this.towerRegion;
	}
	
	public void setTowerRegion(int numberOfTowers){
		this.towerRegion = new TowerRegion[numberOfTowers];
		for(int i=0; i<numberOfTowers; i++){
			towerRegion[i] = new TowerRegion(i + 4,4);
			region.add(4 + i, (Region) towerRegion[i]);
		}
	}
	
	public Region getRegion(int idRegion){	// NUOVO METODO
		try{
			return this.region.get(idRegion);
		} catch (NullPointerException e){
			Logger.getLogger("").log(Level.SEVERE, "context", e);
			return null;
		}
	}
	public ArrayList <Region> getRegionMap(){
		return this.region;
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
	public MarketRegion getMarketRegion(){
		return this.marketRegion;
	}
	
	public void flushBoard(){
		
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		for(int i=0; i< this.region.size(); i++){
			tmp.append("Regione " + this.region.get(i).getRegionID() + " :" + '\n');
			tmp.append(this.region.get(i).toString() + '\n');
			tmp.append("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ " + '\n');
		}
		return new String(tmp);
	}
	
}
