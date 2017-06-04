package it.polimi.ingsw.GC_32.Server.Game;

public class Action{
    
	private String type;
    private int actionValue;
    private int actionSpaceId;
    private int actionRegionId;
    
    public Action(String type, int actionValue, int actionSpaceId, int actionRegionId ){
    	
    	this.type = type;
    	this.actionValue = actionValue;
    	this.actionSpaceId = actionSpaceId;
    	this.actionRegionId = actionRegionId;
    }
    
    public String getActionType(){
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
    
    public String toString(){
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append(" Action:");
    	stringBuilder.append("type:" + type.toString() + " actionValue:"
    					+ actionValue + " actionSpaceId:" + actionSpaceId + 
    					" actionRegionId:" + actionRegionId);
    return new String(stringBuilder);
    }
}  