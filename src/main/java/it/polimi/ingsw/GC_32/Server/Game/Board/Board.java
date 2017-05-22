package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;
import java.util.HashMap;

/*import Model.CouncilRegion;
import Model.HarvestRegion;
import Model.ProductionRegion;
import Model.TowerRegion;*/
import it.polimi.ingsw.GC_32.Server.Game.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class Board {

	private HashMap<Integer, Region> region;	// AGGIUNTO ATTRIBUTO
	private TowerRegion[] towerRegion;
	private ProductionRegion productionRegion;
	private HarvestRegion harvestRegion;
	private CouncilRegion councilRegion;
		
	public Board(){		
		
		this.region = new HashMap<Integer, Region>();
		this.towerRegion = new TowerRegion[4];
		
		for(int i = 0; i<towerRegion.length; i++){
			this.towerRegion[i] = new TowerRegion(i, 4);  // 4 piani
			region.put(i, towerRegion[i]);
		}
		
		this.productionRegion = new ProductionRegion(4); //NUMERI MAGICI
		region.put(4, productionRegion);
		this.harvestRegion = new HarvestRegion(5);
		region.put(5, harvestRegion);
		this.councilRegion = new CouncilRegion(6);
		region.put(6, councilRegion);
	}
	
	public Region getRegion(int idRegion){	// NUOVO METODO
		return this.region.get(idRegion);
	}
	public HashMap<Integer, Region> getRegionMap(){
		return this.region;
	}
/*	public ProductionRegion getProductionRegion(){
		return this.productionRegion;
	}
	
	public HarvestRegion getHarvastRegion(){
		return this.harvestRegion;
	}
	
	public CouncilRegion getCouncilRegion(){
		return this.councilRegion;
	}
	
*/	public void flushBoard(){
		
	}
	
 	// metodo per verificare se l'id della regione e' un id valido
	public boolean isRegion(int id){		// NON PENSO CHE SERVA PIU'
		for(TowerRegion region: towerRegion){
			if(region.getRegionID() == id)
				return true;	
		} return false;
	}

	public void print(){
		
		for(int i=0; i < this.region.size(); i++){
			Region r = this.region.get(i);
			System.out.println("Regione " + r.getRegionID() + " :");
			r.print();
			System.out.println("");
		}
	}
}
