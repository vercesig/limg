package it.polimi.ingsw.GC_32.Server.Game;

public class Action{
    
	private ActionType type;
    private int actionValue;
    private int actionSpaceId;
    private int actionRegionId;
    
    public Action(ActionType type, int actionValue, int actionSpaceId, int actionRegionId ){
    	
    	this.type = type;
    	this.actionValue = actionValue;
    	this.actionSpaceId = actionSpaceId;
    	this.actionRegionId = actionRegionId;
    }
    
    public ActionType getActionType(){
    	return this.type;
    }
    
    public int getActionValue(){
    	return this.actionValue;
    }
    public int getActionSpaceId(){
    	return this.actionSpaceId;
    }
    public int getActionRegionId(){
    	return this.actionRegionId;
    }
    
    // Stampa usata per testare il codice
    public void print(){
    	System.out.println(" Action:");
    	System.out.println("type: " + type.toString() + "actionValue: "
    					+ actionValue + "actionSpaceId: " + actionSpaceId + 
    					"actionRegionId: " + actionRegionId);
    }
}    