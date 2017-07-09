package it.polimi.ingsw.GC_32.Common.Network;

/**
 * enum containing all the possible typology of openable contexts. Each context is identificated by an univoque ID assigned in this enumeration.
 * 
 * <ul>
 * <li>{@link #PRIVILEGE}: privielge context, used to ask the client what resources he wants gain by spending his council privilege. ID 1</li>
 * <li>{@link #SERVANT}: servant context, used to ask how many servants the client wants spend to increase his PRODUCTION or HARVEST action value. ID 2</li>
 * <li>{@link #EXCOMMUNICATION}: excommunication context, used to ask the client if he wants or not show his faith to the pope. ID 3</li>
 * <li>{@link #CHANGE}: change context, triggered by BUILDING card permanent effect to ask the client what change he want perform. ID 4</li>
 * <li>{@link #LEADERSET}: leader context, used during the start phase of the game to allow the clients to choose their leader cards. ID 5</li>
 * <li>{@link #ACTION}: action context, triggered when a bonus action must be performed. ID 6</li>
 * </ul>
 *
 */

public enum ContextType {

	PRIVILEGE(1), SERVANT(2), EXCOMMUNICATION(3), CHANGE(4), LEADERSET(5), ACTION(6);
	
	private int contextID;
	
	/**
	 * initialize the context with the specified context ID
	 * @param contextID the ID to assign to the context
	 */
	private ContextType(int contextID){
		this.contextID = contextID;
	}
	
	/**
	 * allow to get the context ID
	 * @return the context ID
	 */
	public int getContextID(){
		return this.contextID;
	}
	
}
