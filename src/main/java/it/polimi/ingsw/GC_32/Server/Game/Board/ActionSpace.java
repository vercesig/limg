package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
/**
 * ActionSpace is a specific Box of the Game in which can be placed a FamilyMember.
 * In the ActionSpace there are informations about:
 * <ul>
 *  <li> {@link #regionID}: Region id in which is present this ActionSpace.
 *  <li> {@link #actionSpaceID}: ActionSpace specific id in its Region.
 *  <li> {@link ActionSpace#single}: boolean attribute if it is a single ActionSpace in which it can be placed only one FamilyMember
 *  <li> {@link ActionSpace#actionValue}: a number of the actionValue needed to place a FamilyMember on it.
 *  <li> {@link #bonus}: a ResourceSet of bonus resources that a Player obtains when he places a FamilyMember on this ActionSpace. 
 * </ul>
 * <p>
 * ActionSpace's regionID and actionSpaceID are final and all the ActionSpace of the game are
 * identified with its unique combination of regionID and actionSpaceID.
 * @author VaporUser
 * @see FamilyMember, Player, Region, ResourceSet.
 */
public class ActionSpace{

	private ResourceSet bonus;
	private int actionValue;
	private ArrayList<FamilyMember> occupants;
	private boolean single;
	private final int regionID;
	private final int actionSpaceID;
	
	/**
	 * This method builds an ActionSpace object with the specific parameters passed.  
	 * <p>
	 * An actionSpace that has to be used in the Game, must have actionSpaceID and regionID 
	 * not null.
	 * 
	 * @param bonus			ResourceSet of the bonus of this ActionSpace 			
	 * @param actionValue   integer that a FamilyMember needs as actionValue to be placed on this ActionSpace 
	 * @param single 		boolean attribute that specifies if in this ActionSpace it can be placed only one FamilyMember
	 * @param regionID		integer that represents the RegionID in which is present this ActionSpace
	 * @param actionSpaceID integer that represents the ActionSpaceID of this ActionSpace in its Region
	 *  
	 * @author VaporUser
	 * @see Region, ActionSpace, FamilyMember, ResourceSet
	 */
	public ActionSpace(ResourceSet bonus, int actionValue, boolean single, int regionID, int actionSpaceID){
		this.bonus = bonus;
		this.actionValue = actionValue;
		this.occupants = new ArrayList<FamilyMember>();
		this.single = single;
		this.actionSpaceID = actionSpaceID;
		this.regionID = regionID;
	}
	/**
	 * returns this ActionSpace.{@link #single} attribute.
	 * <p>
	 * This method is helpful to check if on this ActionSpace it can be place only one FamilyMember
	 * If returns true, this is a single ActionSpace and only one FamilyMember is permitted.
	 * If returns false, this is a multiple ActionSpace and there is no limit of FamilyMember that can occupy this ActionSpace 
	 *
	 * @return this ActionSpace {@link #single}.
	 * @author VaporUser
	 * @see Region, ActionSpace, FamilyMember.
	 * */
	public boolean isSingleActionSpace(){
		return this.single;
	}
	
	/**
	 * returns this ActionSpace.{@link #bonus} attribute.
	 * <p>
	 * This method returns a ResourceSet of an instant bonus of the ActionSpace. This bonus
	 * is used to build an Effect to give this resources to the Player which owns the FamilyMember present on this ActionSpace. 
	 *
	 * @return this ActionSpace {@link #bonus}.
	 * @author VaporUser
	 * @see ActionSpace, FamilyMember, ResourceSet, Player, Effect.
	 * */
	public ResourceSet getBonus(){
		return this.bonus;
	}
	
	/**
	 * returns this ActionSpace.{@link #regionID} attribute.
	 * <p>
	 * This method returns an integer which represents the number of the RegionId of this ActionSpace.
	 * All the ActionSpace of the game are identified with its unique combination of regionID and actionSpaceID.
	 *
	 * @return this ActionSpace {@link #regionID}.
	 * @author VaporUser
	 * @see ActionSpace, Region.
	 * */
	public int getRegionID(){
		return this.regionID;
	}
	
	/**
	 * returns this ActionSpace.{@link #actionSpaceID} attribute.
	 * <p>
	 * This method returns an integer which represents the index of this ActionSpace in the ArrayList of ActionSpace of a Region.
	 * All the ActionSpace of the game are identified with its unique combination of regionID and actionSpaceID.
	 *
	 * @return this ActionSpace {@link #actionSpaceID}.
	 * @author VaporUser
	 * @see ActionSpace, Region.
	 * */
	public int getActionSpaceID(){
		return this.actionSpaceID;
	}
	
	/**
	 * returns this ActionSpace.{@link #actionValue} attribute.
	 * <p>
	 * This method returns an integer which represents the integer that a FamilyMember needs as actionValue to be placed on this ActionSpace 
	 * To have a working Game this actionValue can't be null.
	 *
	 * @return this ActionSpace {@link #actionValue}. 
	 * @author VaporUser
	 * @see ActionSpace, FamilyMember.
	 * */
	public int getActionValue(){
		return this.actionValue;
	}
	
	/**
	 * returns an ArrayList of Players which have a FamilyMember on this ActionSpace 
	 * <p>
	 * This method is necessary for the ActioSpaces which has ActionSpace.{@link #single} == false to get a Player instance.
	 * 
	 * @return ArrayList	Players which have a FamilyMember on this ActionSpace.		
	 * @author VaporUser
	 * @see ActionSpace, FamilyMember, Player.
	 * */
	public ArrayList<Player> getPlayers(){
		ArrayList<Player> tmp = new ArrayList<Player>();
		for(FamilyMember familyMember : occupants){
			tmp.add(familyMember.getOwner());
		}
		return tmp;
	}
	
	/**
	 * Returns a boolean to check if It can be placed a FamilyMember on this ActionSpace.
	 * <p>
	 * This method modifies the ArrayList <FamilyMember> {@link #occupants}} of this ActionSpace.
	 * returns false if {@link ActionSpace#isBusy()} == true. 
	 * 
	 * @param familyMember		A FamilyMember that is trying to occupy this ActionSpace.
	 * @return boolean			a boolean which represents if the operation has succeeded.
	 * @author VaporUser
	 * @see ActionSpace, FamilyMember, Player.
	 * */
	public boolean addFamilyMember(FamilyMember familyMember){
		if(this.isBusy() && this.isSingleActionSpace()){
			return false;
		}
		occupants.add(familyMember);
		familyMember.setPosition(this);
		return true;
	}
	
	/**
	 * Removes from {@link ActionSpace#occupants} an FamilyMember which is not more an occupant.
	 * <p>
	 * This method modifies the ArrayList <FamilyMember> {@link #occupants}} of this ActionSpace.
	 * 
	 * @param familyMember		A FamilyMember that is not more an occupant of this ActionSpace.
	 * 
	 * @author VaporUser
	 * @see ActionSpace, FamilyMember.
	 * */
	public void removeFamilyMember(FamilyMember familyMember){
		this.occupants.remove(familyMember);
	}
	
	/**
	 * Returns a boolean to specify if on this ActionSpace it can't be placed a FamilyMember.
	 * <p>
	 * This method returns false if {@link #isSingleActionSpace()} == true and {@link #occupants}.isEmpty() == true.
	 * 
	 * @return boolean which represents the result of this check.
	 * @author VaporUser
	 * @see ActionSpace.
	 * */
	public boolean isBusy(){
		return !this.occupants.isEmpty();
	}
	
	/**
	 * returns a String which can easily printed on the screen. It is helpful for debugging, testing
	 * and visualize all the attributes of this ActionSpace.
	 * <p>
	 * The String is builded with this pattern: 
	 * <ul>
	 * 		[{@link #actionSpaceID}]*{@link #actionValue}
	 * </ul>
	 * 
	 * @return 	a String which has all the information of the ActionSpace.
	 * @author VaporUser.
	 * @see 	ActionSpace.
	 */
	public String toString(){
		String string = "[" + actionSpaceID + " ]" + "*" + actionValue + " ";
		return string;
	}
}
