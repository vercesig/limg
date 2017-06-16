package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;
import java.util.logging.Level;

import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.CardRegistry;
import it.polimi.ingsw.GC_32.Server.Game.Game;



public class Board {

	private ArrayList <Region> region;
	private TowerRegion[] towerRegion;
	private ProductionRegion productionRegion;
	private HarvestRegion harvestRegion;
	private CouncilRegion councilRegion;
	private MarketRegion marketRegion;
		
	public Board(){
		this.region = new ArrayList <Region>();
		region.add(0, new ProductionRegion(0)); 
		region.add(1, new HarvestRegion(1));
		region.add(2, new CouncilRegion(2)); 
		region.add(3, new MarketRegion(3));
		
		// setup delle torri
		String[] cardTypes = CardRegistry.getInstance().getAllCardType().toArray(new String[CardRegistry.getInstance().getAllCardType().size()]);
		this.towerRegion =new TowerRegion[cardTypes.length];
		for(int i=0; i<cardTypes.length; i++){
			towerRegion[i] = new TowerRegion(i + 4,4);
			region.add(4 + i, towerRegion[i]);
			towerRegion[i].setTypeCard(cardTypes[i]);
		}
		System.out.println("[GAME->BOARD] board succesfully inizialized");
	}
	
	public Region getRegion(int idRegion){	// NUOVO METODO
		try{
			return this.region.get(idRegion);
		} catch (IndexOutOfBoundsException e){
			Logger.getLogger("").log(Level.SEVERE, "context", e);
			return null;
		}
	}
		
	public TowerRegion[] getTowerRegion(){
		return this.towerRegion;
	}
	
	public ArrayList<Region> getRegionMap(){
		return this.region;
	}
	
	public ProductionRegion getProductionRegion(){
		return (ProductionRegion) this.region.get(0);
	}
	
	public HarvestRegion getHarvestRegion(){
		return (HarvestRegion) this.region.get(1);
	}
	
	public CouncilRegion getCouncilRegion(){
		return (CouncilRegion) this.region.get(2);
	}
	public MarketRegion getMarketRegion(){
		return (MarketRegion) this.region.get(3);
	}
	
	public void placeCards(Game game){
		System.out.println("[BOARD] placing cards on tower regions...");
		for(TowerRegion towerRegion : this.getTowerRegion()){
			for(TowerLayer towerLayer : towerRegion.getTowerLayers()){
				towerLayer.setCard(game.getDeck(towerRegion.getTypeCard()).drawElement());
			}
		}	
	}
	
	public void flushBoard(){
		System.out.println("[BOARD] flushing board");
		region.forEach(region -> region.flushRegion());
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
