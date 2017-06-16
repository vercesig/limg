package it.polimi.ingsw.GC_32.Server.Game;

import com.eclipsesource.json.JsonObject;

public class Action{
    
	private String type;
    private int actionValue;
    private int actionSpaceId;
    private int actionRegionId;
    private JsonObject additionalInfo;
    
    public Action(String type, int actionValue, int actionSpaceId, int actionRegionId ){
    	
    	this.type = type;
    	this.actionValue = actionValue;
    	this.actionSpaceId = actionSpaceId;
    	this.actionRegionId = actionRegionId;
    	this.additionalInfo = new JsonObject();
    }
    
    public void setAdditionalInfo(JsonObject info){
    	this.additionalInfo = info;
    }
    
    public JsonObject getAdditionalInfo(){
    	return this.additionalInfo;
    }
    
    public String getActionType(){
    	return this.type;
    }
    
    public int getActionValue(){
    	return this.actionValue;
    }
    public void setActionValue(int actionValue){
    	this.actionValue = actionValue;
    }
    public int getActionSpaceId(){
    	return this.actionSpaceId;
    }
    public int getActionRegionId(){
    	return this.actionRegionId;
    }
    
    	// to String
    public String toString(){
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append(" Action:");
    	stringBuilder.append("type:" + type + " actionValue:"
    					+ actionValue + " actionSpaceId:" + actionSpaceId + 
    					" actionRegionId:" + actionRegionId +
    					" additionalInfo:"+ ((additionalInfo != null) ? additionalInfo.toString() : ""));
    return stringBuilder.toString();
    }
}  
