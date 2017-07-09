package it.polimi.ingsw.GC_32.Common.Game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

/**
 * ResourceSet is the way the game handles all the resources of the client (ResourceSet contains coins and points as well). Internally a ResourceSet is represented
 * with an HashMap, which map a type of resource (COINS, WOOD, STONE, ...) with the respectively quantity owned by the player
 *
 */

public class ResourceSet implements Comparable<ResourceSet> {
	
	private HashMap<String, Integer> resourceSet;
	
	/**
	 * initialize an empty resource set
	 */
    public ResourceSet(){
    	this.resourceSet = new HashMap<String, Integer>();
    }
    
    /**
     * given a JsonObject representing a ResourceSet, build a new ResourceSet with the resources contained into the JsonObject
     * @param jsonResourceSet the JsonObject from which the information are retrived
     */
    public ResourceSet(JsonObject jsonResourceSet) {
    	this();
    	for(Member singleResource : jsonResourceSet){
    		this.setResource(singleResource.getName(), singleResource.getValue().asInt());
    	}
    }
    
    /**
     * given a string representing a ResourceSet, build a new ResourceSet with the resources contained into the string (the string is the equivalent form of a JsonObject)
     * @param jsonString the string from which the information are retrived
     */
    public ResourceSet(String jsonString){
    	this(Json.parse(jsonString).asObject());
    }

    /**
     * replace this ResourceSet with the ResourceSet passed as argument
     * @param resourceSet the ResourceSet which will substitute the actual ResourceSet
     */
    public void replaceResourceSet(ResourceSet resourceSet){
    	this.resourceSet.clear();
    	this.addResource(resourceSet);
    }
    
    /**
     * allows to retrive all the resources owned by the player
     * @return the interal HashMap which represent this resourceset
     */
	public HashMap<String, Integer> getResourceSet(){
    	return this.resourceSet;
    }
    
	/**
	 * allows to retrive the quantity of one single resource owned by the player
	 * @param resourceName the type of resource which must be retrived
	 * @return the int value of the resource owned by the player
	 */
    public int getResource(String resourceName){
    	return this.resourceSet.getOrDefault(resourceName, 0);
    }
    
    /**
     * insert a new resource to the ResourceSet
     * @param resourceName the name of the new resource
     * @param quantity the quantity of the resource just added
     */
    public void setResource(String resourceName, int quantity){
    	this.resourceSet.put(resourceName, quantity);
    }
    
    /**
     * if true, this ResourceSet has a negative quantity value for one of its resources
     * @return true if the ResourceSet has an item with a negative value
     */
    public boolean hasNegativeValue(){
        return this.isValid();
    }
    
    /**
     * add the specified quantity to the specified resource. If the resource is not owned by the player, a new voice is created with the specified quantity
     * @param resourceName the resource name to which add the specified quantity
     * @param quantity the quantity to add
     */
    public void addResource(String resourceName, int quantity){
    	if(this.resourceSet.containsKey(resourceName)){
    		int prevValue = this.resourceSet.get(resourceName);
    		this.resourceSet.put(resourceName, prevValue + quantity);
    	} else {
    		this.setResource(resourceName, quantity);
    	}
    }
    
    /**
     * sum the ResourceSet passed as parameter to this ResourceSet
     * @param resource the ResourceSet to add
     */
    public void addResource(ResourceSet resource){
    	for(Entry<String,Integer> entry : resource.getResourceSet().entrySet()){
    		this.addResource(entry.getKey(), entry.getValue());
    	}
    }
    
    /**
     * subtract the ResourceSet passed as parameter to this ResourceSet
     * @param resource the ResourceSet to subtract
     */
    public void subResource(ResourceSet resource){
    	for(Entry<String,Integer> entry : resource.getResourceSet().entrySet()){
    			this.subResource(entry.getKey(), entry.getValue());
    	}
    }
    
    
    /**
     * subtract the specified quantity to the specified resource, which must be present when the method is called.
     * @param resourceName the resource name to which subtract the specified quantity
     * @param quantity the quantity to subtract
     */
    public void subResource(String resourceName, int quantity){
    	this.addResource(resourceName, -quantity);
    }
    
    /**
     * tells if the client has the specified resource or not
     * @param resourceName the name of resource to test the presence
     * @return true if the player owned that type of resource, otherwise false
     */
    public boolean hasResource(String resourceName){
        return this.resourceSet.containsKey(resourceName);
    }
    
    /**
     * given a ResourceSet, this method build its JSON representation. very usefull for all the packets which must contain a ResourceSet
     * @return the JsonObject representing this ResourceSet
     */
    public JsonObject toJson(){
    	JsonObject resource = new JsonObject();
    	for(String key : this.getResourceSet().keySet()){
    		resource.add(key, this.getResource(key));
    	}
    	return resource;
    }
    
    /**
     * Checks whether or not the resourceSet has negative values. If a ResourceSet has negative values it is consider not valid (this is one of the way to check if
     * the action can be assessed as valid or not) 
     * @return true if the ResourceSet is a valid ResourceSet, false otherwise
     */
    
    public boolean isValid(){
    	for(Entry<String,Integer> resource: this.resourceSet.entrySet()){
    		if(resource.getValue() < 0){
    			return false;
    		}
    	}
    	return true;
    }
     /**
     * It returns a comparison between two ResourceSet.
	 * <ul>
	 *  <li> -2 if the resource compared is not a subset of this
	 *  <li> -1 if this.resource is less than input resource
	 *  <li>  0 if this.resource is equal to input resource
	 *  <li>  1 if this.resource is more than input resource
	 * </ul>
	 *
	 * @param resource input to compare with this.
	 * @return one of the possible values
	 */
    @Override
	public int compareTo(ResourceSet resource) {
		if(this.equals(resource)){
			return 0;
		}
		Set<String> thisResources = this.resourceSet.keySet();
		Set<String> otherResources = resource.resourceSet.keySet();
		Set<String> thisResourcesDiff = new HashSet<>(thisResources);
		Set<String> otherResourcesDiff = new HashSet<>(otherResources);
		thisResourcesDiff.removeAll(otherResources);
		otherResourcesDiff.removeAll(thisResources);
		if(!thisResourcesDiff.isEmpty() && otherResourcesDiff.isEmpty()){
			for(Entry<String, Integer> element: this.resourceSet.entrySet()){
				if( element.getValue() < resource.getResource(element.getKey())){
					return -1;
				}
			}
			return 1;
		} else {
			return -2;
		}
    }
    
    @Override
    public boolean equals(Object resource){
    	if(resource instanceof ResourceSet){
    		return this.resourceSet.equals(((ResourceSet) resource).getResourceSet());
    	} else {
    		return false;
    	}
    }
    
    @Override
    public int hashCode() {
    	return this.toString().hashCode();
    }
	
    @Override
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		for(Entry<String, Integer> element : resourceSet.entrySet()){
			tmp.append(" "+element.getKey()+" :"+element.getValue().toString());
		}
		return new String(tmp);
	}
    
    /**
     * give a string representation of the resources owned by the player, filtering score fields
     * @return a string representation of only the resources (COINS, WOOD, STONE, SERVANT) owned by the player
     */
	public String toStringPlayer(){
		StringBuilder tmp = new StringBuilder();
		for(Entry<String, Integer> element : resourceSet.entrySet()){
			if(!element.getKey().equals("MILITARY_POINTS") && 
			   !element.getKey().equals("VICTORY_POINTS") &&
		       !element.getKey().equals("FAITH_POINTS"))
			tmp.append(" "+element.getKey()+" :"+element.getValue().toString());
		}
		return new String(tmp);
	}
    
	/**
	 * return the string representation of the resources contained into this resourceset, organized in an HashMap structure easy to handle 
	 * @return an HashMap which map each type of resource with its value, in a printable format
	 */
    public HashMap<String,String> getDecomposedResourceSetString(){
    	HashMap<String,String> tmp = new HashMap<String,String>();
    	
    	for(Entry<String, Integer> element : resourceSet.entrySet()){
			tmp.put(element.getKey()+":",element.getValue().toString());
		}
		return tmp;
    }
    
    /**
     * tells if the ResourceSet passed as parameter is contained by this ResourceSet
     * @param resource the ResourceSet to test
     * @return true if the ResourceSet passed as argument is contained into this ResourceSet, otherwise false
     */
    public boolean contains(ResourceSet resource){
        return this.resourceSet.keySet()
                               .containsAll(resource.getResourceSet().keySet());
    }
	
}
