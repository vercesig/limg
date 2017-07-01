package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;
import java.util.logging.Level;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.GameConfig;
import it.polimi.ingsw.GC_32.Server.Game.Card.CardRegistry;


/**
 * Board is a set of all the Region of the Game in which can be placed a FamilyMember.
 * In a Board there are informations about:
 * <ul>
 *  <li> {@link #region}: ArrayList of all the {@link Region} present on the Board.
 *  <li> {@link #towerRegion}: static array of TowerRegion.
 *  <li> {@link #productionRegion}: a {@link ProductionRegion}.
 *  <li> {@link #harvestRegion}: a {@link HarvestRegion}.
 *  <li> {@link #councilRegion}: a {@link CouncilRegion}.
 *  <li> {@link #marketRegion}: a {@link MarketRegion}.
 * </ul>
 * <p>
 * On the Board it is possible to get the reference to all the regions of the game.
 * 
 * @author VaporUser.
 * @see Region, TowerRegion, ProductionRegion, HarverstRegion, CouncilRegion, MarketRegion.
 */

public class Board {
	
	private final static Logger LOGGER = Logger.getLogger(Board.class.getName());

	private ArrayList <Region> region;
	private TowerRegion[] towerRegion;
	
	/**
	 * Board constructor.
	 * <p>
	 * The constructor does not need any parameters. It calls all the region's constructors.
	 * It sets the regionID for all the region on the board:
	 * <ul>
	 *  <li> For {@link #productionRegion} it sets regionID 0.
	 *  <li> For {@link #harvestRegion} it sets regionID 1.
	 *  <li> For {@link #councilRegion} it sets regionID 2.
	 *  <li> For {@link #marketRegion} it sets regionID 3.
	 *  <li> For {@link #towerRegion}s it sets regionID 4, 5, 6, 7.
	 * </ul>
	 * 
	 * @author VaporUser.
	 * @see Region, TowerRegion, ProductionRegion, HarverstRegion, CouncilRegion, MarketRegion.
	 */
	public Board(){
		this.region = new ArrayList<Region>();
		region.add(0, new ProductionRegion(0)); 
		region.add(1, new HarvestRegion(1));
		region.add(2, new CouncilRegion(2)); 
		region.add(3, new MarketRegion(3));
		
		// setup delle torri
		String[] cardTypes = CardRegistry.getInstance()
										 .getAllCardType()
										 .toArray(new String[CardRegistry.getInstance()
										                     			 .getAllCardType()
										                     			 .size()]);
		this.towerRegion = new TowerRegion[cardTypes.length];
		for(int i=0; i<cardTypes.length; i++){
			towerRegion[i] = new TowerRegion(i + 4,4);
			region.add(4 + i, towerRegion[i]);
		}
		
		//paranoie per associare correttamente ad ogni torre ID il giusto CardType
		towerRegion[0].setTypeCard(CardRegistry.getInstance().getDeck("TERRITORYCARD").getDeck().get(0).getType());
		towerRegion[1].setTypeCard(CardRegistry.getInstance().getDeck("CHARACTERCARD").getDeck().get(0).getType());
		towerRegion[2].setTypeCard(CardRegistry.getInstance().getDeck("BUILDINGCARD").getDeck().get(0).getType());
		towerRegion[3].setTypeCard(CardRegistry.getInstance().getDeck("VENTURECARD").getDeck().get(0).getType());
		setupBonus();
		LOGGER.log(Level.INFO, "board succesfully inizialized");
	}
	
	/**
	 * Get method for a generic {@link Region} in the Board.
	 * <p>
	 * It is helpful to get informations about the Region in the Board.
	 * If the number passed is not a RegionId valid, returns null.
	 * 
	 * @author VaporUser.
	 * @param idRegion integer of the Region to get the reference.
	 * @return Region in Board which has {@link Region#RegionID} == idRegion.
	 * @see Board, Region.
	 */
	public Region getRegion(int regionId){
		if(regionId < this.region.size()){
			return this.region.get(regionId);
		} else {
			return null;
		}
	}
		
	public TowerRegion[] getTowerRegion(){
		return this.towerRegion;
	}
	
	/**
	 * Get method for the {@link #region}.
	 * <p>
	 * It is helpful to get informations about the Region in the Board.
	 * 
	 * @author VaporUser.
	 * @return this {@link #region}.
	 * @see Board, Region.
	 */
	public ArrayList<Region> getRegionMap(){
		return this.region;
	}
	
	/**
	 * Get method for the {@link #productionRegion}.
	 * <p>
	 * It is helpful to get informations about the ProductionRegion in the Board.
	 * 
	 * @author VaporUser.
	 * @return this {@link #productionRegion}.
	 * @see Board, ProductionRegion.
	 */
	public ProductionRegion getProductionRegion(){
		return (ProductionRegion) this.region.get(0);
	}
	
	/**
	 * Get method for the {@link #harvestRegion}.
	 * <p>
	 * It is helpful to get informations about the HarvestRegion in the Board.
	 * 
	 * @author VaporUser.
	 * @return this {@link #harvestRegion}.
	 * @see Board, HarvestRegion.
	 */
	public HarvestRegion getHarvestRegion(){
		return (HarvestRegion) this.region.get(1);
	}
	
	/**
	 * Get method for the {@link #councilRegion}.
	 * <p>
	 * It is helpful to get informations about the CouncilRegion in the Board.
	 * 
	 * @author VaporUser.
	 * @return this {@link #councilRegion}.
	 * @see Board, CouncilRegion.
	 */
	public CouncilRegion getCouncilRegion(){
		return (CouncilRegion) this.region.get(2);
	}
	
	/**
	 * Get method for the {@link #markerRegion}.
	 * <p>
	 * It is helpful to get informations about the MarketRegion in the Board.
	 * 
	 * @author VaporUser.
	 * @return this {@link #marketRegion}.
	 * @see Board, MarketRegion.
	 */
	public MarketRegion getMarketRegion(){
		return (MarketRegion) this.region.get(3);
	}
	
	public void placeCards(Game game){
		LOGGER.log(Level.INFO, "placing cards on tower regions...");
		for(TowerRegion towerRegion : this.getTowerRegion()){
			for(TowerLayer towerLayer : towerRegion.getTowerLayers()){
				towerLayer.setCard(game.getDeck(towerRegion.getTypeCard()).drawElement());
			}
		}	
	}
	
	public void flushBoard(){
		LOGGER.log(Level.INFO, "flushing board");
		region.forEach(region -> region.flushRegion());
	}
	
	public void setupBonus(){
		GameConfig.getInstance().getBonusSpace().forEach( js -> {
			int regionID = js.asObject().get("RegionID").asInt();
			int spaceID = js.asObject().get("SpaceID").asInt();
			ResourceSet bonus = new ResourceSet(js.asObject().get("Bonus").asObject());
			if(regionID >3){ // necessario castare per TowerRegion
				((TowerRegion)this.getRegion(regionID)).getTowerLayers()[spaceID].getActionSpace().setBonus(bonus);
			}
			else
				this.getRegion(regionID).getActionSpace(spaceID).setBonus(bonus);
		});
	}
	
	/**
	 * Returns a String which can easily printed on the screen. It is helpful for debugging, testing
	 * and visualize all the attributes of the Board.
	 * <p>
	 * This methods calls the toString() methods of all Region class present on the Board. 
	 * 
	 * @author 		VaporUser.
	 * @return 		a String which has all the information of the Board and its Regions.
	 * @see 		Board, Region, TowerRegion, ProductionRegion, HarverstRegion, CouncilRegion, MarketRegion, ActionSpace.
	 */
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
