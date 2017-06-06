package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

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

	private ArrayList <Region> region;
	private TowerRegion[] towerRegion;
	private ProductionRegion productionRegion;
	private HarvestRegion harvestRegion;
	private CouncilRegion councilRegion;
	private MarketRegion marketRegion;
	
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
	
	/**
	 * Get method for the {@link #towerRegion}.
	 * <p>
	 * It is helpful to get informations about the TowerRegion in the Board.
	 * 
	 * @author VaporUser.
	 * @return this {@link #towerRegion}.
	 * @see Board, TowerRegion.
	 */
	public TowerRegion[] getTowerRegion(){
		return this.towerRegion;
	}
	
	/**
	 * Set method for the {@link #towerRegion}.
	 * <p>
	 * With this method it can be build all the TowerRegion in the Board.
	 * It calls the {@link TowerRegion}'s Constructor.
	 * 
	 * @author VaporUser.
	 * @param numberOfTowers specifies the number of layer for each TowerRegion.
	 * @see Board, TowerRegion, TOwerLayer.
	 */
	public void setTowerRegion(int numberOfTowers){
		this.towerRegion = new TowerRegion[numberOfTowers];
		for(int i=0; i<numberOfTowers; i++){
			towerRegion[i] = new TowerRegion(i + 4,4);
			region.add(4 + i, (Region) towerRegion[i]);
		}
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
	public Region getRegion(int idRegion){
		try{
			return this.region.get(idRegion);
		} catch (IndexOutOfBoundsException e){
			Logger.getLogger("").log(Level.SEVERE, "context", e);
			return null;
		}
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
	public ArrayList <Region> getRegionMap(){
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
		return this.productionRegion;
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
		return this.harvestRegion;
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
		return this.councilRegion;
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
		return this.marketRegion;
	}
	
	public void flushBoard(){
		
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
