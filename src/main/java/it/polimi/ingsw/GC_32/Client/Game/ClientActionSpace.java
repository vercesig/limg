package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.ActionSpace;

/**
 * this class is the client-side representation of the server-side concept of Region. Because client only show information on the screen, the information contained into 
 * this class (like all the classes of the client-side game model) is really less then the server-side equivalent class.
 * 
 * <ul>
 *  <li>{@link #regionID}: Region id in which is present this ActionSpace.
 *  <li>{@link #actionSpaceID}: ActionSpace specific id in its Region.
 *  <li>{@link ActionSpace#single}: boolean attribute if it is a single ActionSpace in which it can be placed only one FamilyMember
 *  <li>{@link ActionSpace#actionValue}: a number of the actionValue needed to place a FamilyMember on it.
 *  <li>{@link #bonus}: a ResourceSet of bonus resources that a Player obtains when he places a FamilyMember on this ActionSpace. 
 *  <li>{@link #blocked}: a flag representing if tha action space is blocked or not</li>
 *  <li>{@link #occupants}: the list of ClientFamilyMember on this action space</li>
 *  <li>{@link #cardName}: only for tower regions, this field contains the name of the card related to this action space</li>
 * </ul>
 * <p>
 * @see FamilyMember, Player, Region, ResourceSet.
 */

public class ClientActionSpace {

	private final Integer regionID;
	private final Integer actionSpaceID;
	private Integer actionValue;
	private ArrayList<ClientFamilyMember> occupants;
	private ResourceSet bonus;
	private Boolean single;
	// solo per tower region
	private String cardName;
	protected Boolean blocked;
	
	public ResourceSet getBonus(){
		return this.bonus;
	}
	
	/**
	 * builds an ActionSpace object with the specific parameters passed.  
	 * <p>
	 * An actionSpace that has to be used in the Game, must have actionSpaceID and regionID 
	 * not null.
	 * 
	 * @param bonus			ResourceSet of the bonus of this ActionSpace 			
	 * @param actionValue   integer that a FamilyMember needs as actionValue to be placed on this ActionSpace 
	 * @param single 		boolean attribute that specifies if in this ActionSpace it can be placed only one FamilyMember
	 * @param regionID		integer that represents the RegionID in which is present this ActionSpace
	 * @param actionSpaceID integer that represents the ActionSpaceID of this ActionSpace in its Region
	 * @param blockFlag indicates if the action space is blocked or not
	 *  
	 */
	public ClientActionSpace(ResourceSet bonus, int actionValue, boolean single, int regionID, int actionSpaceID, boolean blockFlag){
		this.bonus = bonus;
		this.actionValue = actionValue;
		this.occupants = new ArrayList<ClientFamilyMember>();
		this.single = single;
		this.actionSpaceID = actionSpaceID;
		this.regionID = regionID;
		this.blocked = blockFlag;
	}
	
	/**
	 * set the card name field, only valid for tower regions
	 * @param cardName the name of the card related to this action space
	 */
	public void setCard(String cardName){
		this.cardName = cardName;
	}
	
	/**
	 * allows to retrive the name of the card related to this action space
	 * @return the cardName field
	 */
	public String getCardName(){
		return this.cardName;	
	}
	
	/**
	 * set true the blocked flag
	 */
	public void Lock(){
		this.blocked = true;
	}
	
	/**
	 * set false the blocked flag
	 */
	public void Unlock(){
		this.blocked = false;
	}
	
	/**
	 * allow to retrive the list of all the family members on this action space
	 * @return an ArrayList of ClientFamilyMember
	 */
	public ArrayList<ClientFamilyMember> getOccupants(){
		return this.occupants;
	}
	
	/**
	 * add the given family member to the list of occupants of this action space
	 * @param familyMember the pawn to add
	 */
	public void addFamilyMember(ClientFamilyMember familyMember){
		this.occupants.add(familyMember);
	}
	
	/**
	 * used by ClientBoard toString() method for build the string representation of the board, insert all the information about this action space into an array of String
	 */
	public String[] getInfoContainer(){
		String[] infoContainer = new String[8];
		infoContainer[0] = regionID.toString();
		infoContainer[1] = actionSpaceID.toString();
		infoContainer[2] = actionValue.toString();
		infoContainer[3] = single.toString();
		infoContainer[4] = blocked.toString();
		if(bonus!=null){		
			
			HashMap<String,String> tmp = bonus.getDecomposedResourceSetString();
			StringBuilder tmpStringBuilder = new StringBuilder();
			for(Entry<String,String> entry : tmp.entrySet()){
				if(entry.getKey().contains("MILITARY_POINTS")){
					tmpStringBuilder.append("MILITARY:"+entry.getValue());
				}else{
					tmpStringBuilder.append(entry.getKey()+entry.getValue()+" ");
				}
			}			
			
			infoContainer[5] = tmpStringBuilder.toString();
		}
		else
			infoContainer[5] = "empty";
		StringBuilder occupantsString = new StringBuilder();
		occupants.forEach(familiar -> occupantsString.append(familiar.getOwner()+","));
		infoContainer[6] = new String(occupantsString);
		if(cardName!=null)
			infoContainer[7] = cardName;
		else
			infoContainer[7] = "empty";
		
		return infoContainer;
	}
	
	public boolean isBusy(){
		return !this.getOccupants().isEmpty();
	}
	
	/**
	 * return a string representation of this action space
	 */
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("regionID :"+this.regionID+"\nactionSpaceID :"+this.actionSpaceID+"\nactionValue :"
				+this.actionValue+"\nsingleFlag :"+this.single+"\nblocked :" + this.blocked);
		if(bonus != null){
			tmp.append("bonus :"+bonus.toString()+"\n");
		} else {
			tmp.append("no bonus\n");
		}
		tmp.append("occupants :");
		occupants.forEach(familiar -> tmp.append(familiar.toString()+","));
		tmp.append("\n");
	
		if(cardName!=null)
			tmp.append("Card :"+this.cardName+"\n");
		
		tmp.append("-------------------------------------\n");
		return new String(tmp);
	}
	
	/**
	 * remove all the family members on this action space
	 */
	public void flushFamilyMember(){
		this.occupants.clear();
	}
}
