package it.polimi.ingsw.GC_32.Server.Game;

import com.eclipsesource.json.JsonObject;

/**
 * Action class represent the way for the client to ask the server to perform an action, so to move a family member on the board. follow this definitions an instance of
 * the class Action contains all rhe information required by the server to perform all the necessary check on the validity of the move the client has asked for.
 * 
 * <ul>
 * <li>{@link #actionRegionId}: the region ID where the action must be performed</li>
 * <li>{@link #actionSpaceId}: the action space ID of the region ID where the action must be performed</li>
 * <li>{@link #actionValue}: the value of the action to perform</li>
 * <li>{@link Action#isValid}: a flag representing the validity of the action</li>
 * <li>{@link #additionalInfo}: a JsonObject containing additional information required by the check. The information required depends on the type of action performed
 * and can't be generalized</li>
 * <li>{@link #type}: the type of action to perform</li>
 * </ul> 
 */
public class Action{
    
	private String type;
    private int actionValue;
    private int actionSpaceId;
    private int actionRegionId;
    private JsonObject additionalInfo;
    private boolean isValid;
    
    /**
     * create an action
     * @param type the type of action performed
     * @param actionValue the value of this action
     * @param actionSpaceId the region ID choosed by the client which indicates where the action must be checked
     * @param actionRegionId the action space ID, choosed by the client, inside the region with that region ID, where action has been performed
     */
    public Action(String type, int actionValue, int actionSpaceId, int actionRegionId){	
    	this.type = type;
    	this.actionValue = actionValue;
    	this.actionSpaceId = actionSpaceId;
    	this.actionRegionId = actionRegionId;
    	this.additionalInfo = new JsonObject();
    	this.isValid = true;
    }
    
    /**
     * set the additionalInfo field with the JsonObject representing all the information needed by the check and depending on the particular action performed
     * @param info the JsonObject which must be loaded as additional information
     */
    public void setAdditionalInfo(JsonObject info){
    	this.additionalInfo.merge(info);
    }
    
    /**
     * get the additionalInfo JsonObject
     * @return the additional informations inserted into this action
     */
    public JsonObject getAdditionalInfo(){
    	return this.additionalInfo;
    }
    
    /**
     * get the type of this action
     * @return the type of the action
     */
    public String getActionType(){
    	return this.type;
    }
    /**
     * get the value of this action
     * @return the actionValue of the action performed
     */
    public int getActionValue(){
    	return this.actionValue;
    }
    /**
     * set the action value to the given value
     * @param actionValue the value of the action
     */
    public void setActionValue(int actionValue){
    	this.actionValue = actionValue;
    }
    /**
     * allows to retrive the space ID where the action has been performed
     * @return the ID of the action space of this action
     */
    public int getActionSpaceId(){
    	return this.actionSpaceId;
    }
    
    /**
     * allows to retrive the region ID where the action has been performed
     * @return the ID of the region of this action
     */
    public int getRegionId(){
    	return this.actionRegionId;
    }
    
    /**
     * show if the action is valid or has been invalidated during the simulation of the move, for example by an effect activated by one specific card
     * @return true if the action is valid, false if not
     */
    public boolean isValid(){
    	return this.isValid;
    }
    
    /**
     * invalidate this action, setting to false the flag isValid
     */
    public void invalidate(){
    	this.isValid = false;
    }
    
    /**
     * give a string representation of the action
     */
    @Override
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
