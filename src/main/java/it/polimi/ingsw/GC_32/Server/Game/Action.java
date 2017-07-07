package it.polimi.ingsw.GC_32.Server.Game;

import com.eclipsesource.json.JsonObject;

public class Action{
    
	private String type;
    private int actionValue;
    private int actionSpaceId;
    private int actionRegionId;
    private JsonObject additionalInfo;
    private boolean isValid;
    
    public Action(String type, int actionValue, int actionSpaceId, int actionRegionId){	
    	this.type = type;
    	this.actionValue = actionValue;
    	this.actionSpaceId = actionSpaceId;
    	this.actionRegionId = actionRegionId;
    	this.additionalInfo = new JsonObject();
    	this.isValid = true;
    }
    
    public void setAdditionalInfo(JsonObject info){
    	this.additionalInfo.merge(info);
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
    public int getRegionId(){
    	return this.actionRegionId;
    }
    
    public boolean isValid(){
    	return this.isValid;
    }
    
    public void invalidate(){
    	this.isValid = false;
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
    
    public boolean equals(Action otherAction){
        return (this.actionValue == otherAction.getActionValue() &&
                this.actionSpaceId == otherAction.getActionSpaceId() &&
                this.actionRegionId == otherAction.getRegionId() &&
                this.type == otherAction.getActionType() &&
                this.additionalInfo.equals(otherAction.getAdditionalInfo()));
    }
}  
