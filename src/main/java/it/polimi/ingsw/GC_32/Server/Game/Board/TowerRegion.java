package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Card.Card;
import it.polimi.ingsw.GC_32.Server.Game.Card.CardType;

/**
 * TowerRegion is a specific Region of the {@link Game} in which can be placed a FamilyMember and performed a TowerAction.
 * In the TowerRegion there are informations about:
 * <ul>
 *  <li> {@link #regionID}: Region id of this Region.
 *  <li> {@link #towerLayers}: Array of {@link TowerLayer}.
 *  <li> {@link #towerBusy}: boolean which is true if exists a {@link TowerLayer} occupied by a {@link FamilyMember}.
 *  <li> {@link #typeCard}: String of a type of {@link Card}.
 * </ul>
 * <p>
 * In this Region the {@link #towerLayers} is a staticArray of set of {@link ActionSpace} and {@link Card}.
 * 
 *
 * @author VaporUser.
 * @see TowerRegion, TowerLayer, Card, ActionSpace, Region.
 */
public class TowerRegion implements Region {

	private TowerLayer[] towerLayers;
	private boolean towerBusy;
	private final int regionID;
	private String typeCard;
	
	/**
	 * Constructor of TowerRegion. 
	 * <p>
	 * Initializes the {@link #towerLayers} calling the Constructor of {@link TowerLayer}
	 * and their {@link ActionSpace} passing this  {@link #regionID}.
	 *	
	 * @param regionID			unique integer of this {@link Region}.
	 * @param numberOfLayers	number of levels of the Tower.
	 * @author VaporUser.
	 * @see ProductionRegion, Region, ActionSpace.
	 */
	public TowerRegion(int regionID, int numberOfLayers){
		this.regionID = regionID;
		this.towerBusy = false;
		this.towerLayers = new TowerLayer[numberOfLayers];
		for(int i=0; i<numberOfLayers; i++){
			this.towerLayers[i] = new TowerLayer(this.regionID, i);
		}
	}
	
	/**
	 * this method returns the {@link #typeCard} of this {@link TowerRegion}.
	 * <p>
	 * method which is helpful to get information for what {@link DevelopmentCard#type} is used this
	 * {@link TowerRegion}.
	 * 
	 * @author VaporUser.
	 * @return a string of a specific {@link DevelopmentCard#type} .
	 * @see TowerRegion, DevelopmentCard, Card. 
	*/
	public String getTypeCard(){
		return this.typeCard;
	}
	
	/**
	 * this method set the {@link #typeCard} of this {@link TowerRegion}.
	 * <p>
	 * method which is helpful to set for what {@link DevelopmentCard#type} is used this
	 * {@link TowerRegion} in the game. the {@link type} has to be a {@link CardType#name()}.
	 * 
	 * @author VaporUser.
	 * @param type		string which specifies the type of cards present on this {@link TowerRegion}.
	 * @see TowerRegion, DevelopmentCard, CardType. 
	 */
	public void setTypeCard(String type){
		this.typeCard = type;
	}
	
	/**
	 * this method checks if in this {@link TowerRegion} there is a {@link FamilyMember}.
	 * <p>
	 * returns the value of the check. If it is true, the {@link TowerRegion} is busy.
	 * 
	 * @author VaporUser.
	 * @return boolean result of the check.
	 * @see TowerRegion. 
	 */
	public boolean isTowerBusy(){
		return this.towerBusy;
	}
	
	/**
	 * this method checks if in this {@link TowerRegion} there is a {@link FamilyMember}.
	 * <p>
	 * returns the value of the check. If it is true, the {@link TowerRegion} is busy.
	 * 
	 * @author VaporUser.
	 * @return boolean result of the check.
	 * @see TowerRegion. 
	 */
	public TowerLayer[] getTowerLayers(){
		return this.towerLayers;
	}
	
	/**
	 * this method checks if a {@link Player} can place a {@link FamilyMember} 
	 * on this {@link TowerRegion}.
	 * <p>
	 * method which controls if on all {@link TowerRegion#towerLayers} it is 
	 * present an actionSpace which is occupied by {@link player}'s familyMember.
	 * 
	 * @author VaporUser.
	 * @param  Player  	{@link player}.
	 * @return a boolean which is the result of the check. 
	 * @see TowerRegion, TowerLayer, Player. 
	*/
	public boolean canIPlaceFamilyMember(Player player){
		// c'è già un familiare del mio stesso colore sulla torre
		for(TowerLayer level : towerLayers){
			if(level.getActionSpace().getPlayers().get(0).getUUID() == player.getUUID()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * this method try to place a {@link FamilyMember} on a specific {@link TowerRegion#towerLayers}.
	 * <p>
	 * If the {@link Player} owner of the familyMember can't place the familyMember, this method does not
	 * change the state of the {@link TowerLayers}.
	 * The check is performed calling the {@link TowerRegion#canIPlaceFamilyMember(Player)}.
	 * 
	 * @author VaporUser.
	 * @param familyMember  which is trying to place on a specific level.
	 * @param layer which specifies the {@link ActionSpace#actionSpaceID}.
	 * @see TowerRegion, TowerLayer, FamilyMember, ActionSpace. 
	 */
	public void placeFamilyMember(FamilyMember familyMember, int layer){
		if(canIPlaceFamilyMember(familyMember.getOwner())){
			towerLayers[layer].getActionSpace().addFamilyMember(familyMember);
			
		}
	}
	
	// rimuove carte e familiari sulla torre
	public void flushTowerRegion(){
		
	}
	
	public void placeCards(){
		
	}
		
	@Override
	public int getRegionID(){
		return this.regionID;
	}

	@Override
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

	@Override
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
