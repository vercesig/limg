package it.polimi.ingsw.GC_32.Client.Game;


/**
 * this class is the client-side representation of the server-side concept of FamilyMember. Because client only show information on the screen, the information contained into 
 * this class (like all the classes of the client-side game model) is really less then the server-side equivalent class.
 * 
 * <ul>
 * <li>{@link #actionValue}: the action value of this family member</li>
 * <li>{@link #owner}: the player who own this family member</li>
 * <li>{@link #busy}: flag which tells if the pawn is busy or not</li>
 * </ul>
 *
 * @see ClientActionSpace
 */

public class ClientFamilyMember {

	protected int actionValue = 0;
	private String owner;
	private boolean busy;
	
	/**
	 * initialize the pawn. busy flag is setted false
	 */
	public ClientFamilyMember(){
		this.busy = false;
	}
	
	/**
	 * register the owned of this family member
	 * @param name the pawn's owner
	 */
	public void setName(String name){
		this.owner = name;
	}
	
	/**
	 * change the value of the busy flag
	 * @param busyFlag the value to assign to the busy flag
	 */
	public void setBusyFlag(boolean busyFlag){
		this.busy = busyFlag;
	}
	
	/**
	 * allows to change the action value of this family member
	 * @param actionValue the action value to assign
	 */
	public void setActionValue(int actionValue){
		this.actionValue = actionValue;
	}
	
	/**
	 * tells if the pawn is busy or not
	 * @return true if the pawn is busy, otherwise false
	 */
	public boolean isBusy(){
		return this.busy;
	}
	
	/**
	 * return the owner of this family member
	 * @return the name of the pawn's owner
	 */
	public String getOwner(){
		return this.owner;
	}
	
	/**
	 * return a string representation of this family member
	 */
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		if(busy==true)
			tmp.append(" > ");
		else
			tmp.append("   ");
		tmp.append("actionValue :"+this.actionValue+"\n");
		return new String(tmp);
	}
	
}
