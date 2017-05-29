package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

/**
 * TowerLayer is a specific level of a  {@link TowerRegion} in which can be placed a FamilyMember and performed a TowerAction.
 * In the TowerLayer there are informations about:
 * <ul>
 *  <li> {@link #card}: card of this layer.
 *  <li> {@link #actionSpace}: actionspace of this layer.
 * </ul>
 * <p>
 * A {@link TowerLayer} can be considered as a Set of a {@link DevelopmentCard} and a {@link ActionSpace}.
 *
 * @author VaporUser.
 * @see TowerRegion, DevelopmentCard, ActionSpace.
 */
public class TowerLayer {

	private DevelopmentCard card;
	private ActionSpace actionSpace;
	
	/**
	 * TowerLayer constructor.
	 * <p>
	 * The constructor needs the {@link TowerRegion#RegionID} and the id of the ActionSpace.
	 * It sets the {@link ActionSpace#actionValue} in function of the {@link actionSpaceID}.
	 * 
	 * @author VaporUser.
	 * @param regionID is the {@link TowerRegion#RegionID}.
	 * @param actionSpaceID is the {@link ActionSpace#actionSpaceID}.
	 * @see TowerLayer, TowerRegion, ActionSpace.
	 */
	public TowerLayer(int regionID, int actionSpaceID){
		ResourceSet bonus = new ResourceSet();
		boolean single = true;
		int actionValue = 1;
		
		switch(actionSpaceID){		// BRUTTO_ CI VUOLE UNA NOVA SOLUZIONE
			case 0:
				actionValue = 1;
				break;
			case 1:
				actionValue = 3;
				break;
			case 2:
				actionValue = 5;
				break;
			case 3:
				actionValue = 7;
				break;
		}
		
		this.actionSpace = new ActionSpace(bonus, actionValue, single, regionID, actionSpaceID);
	}
	
	/**
	 * Get method for the {@link #card}.
	 * <p>
	 * It is helpful to get informations about the specified {@link DevelopmentCard} of this {@link TowerLayer}.
	 * 
	 * @author VaporUser.
	 * @return this {@link #card}.
	 * @see TowerLayer, DevelopmentCard.
	 */
	public DevelopmentCard getCard(){
		return this.card;
	}
	
	/**
	 * Get method for the {@link #actionSpace}.
	 * <p>
	 * It is helpful to get informations about the specified {@link ActionSpace} of this {@link TowerLayer}.
	 * 
	 * @author VaporUser.
	 * @return this {@link #actionSpace}.
	 * @see TowerLayer, ActionSpace.
	 */
	public ActionSpace getActionSpace(){
		return this.actionSpace;
	}
	
	/**
	 * Set method for this {@link TowerLayer#card}.
	 * <p>
	 * Change the specified {@link DevelopmentCard} of this {@link TowerLayer} with a new one.
	 * It is helpful when it starts a new turn and it is necessary to draw new cards 
	 * from the {@link Deck} and set them on the {@link TowerRegion}. 
	 * 
	 * @author VaporUser.
	 * @param card a {@link DevelopmentCard} which substitutes the old {@link #card}.
	 * @see TowerLayer, DevelopmentCard.
	 */
	public void setCard(DevelopmentCard card){
		this.card = card;
	}
	
	
}
