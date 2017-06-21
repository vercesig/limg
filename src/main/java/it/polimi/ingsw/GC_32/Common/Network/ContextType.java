package it.polimi.ingsw.GC_32.Common.Network;

public enum ContextType {

	PRIVILEGE(1), SERVANT(2), EXCOMMUNICATION(3), CHANGE(4);
	
	private int contextID;
	
	private ContextType(int contextID){
		this.contextID = contextID;
	}
	
	public int getContextID(){
		return this.contextID;
	}
	
}
