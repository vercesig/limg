package it.polimi.ingsw.GC_32.Common.Game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

public class ResourceSet implements Comparable<ResourceSet> {
	
	private HashMap<String, Integer> resourceSet;
	
    public ResourceSet(){
    	this.resourceSet = new HashMap<String, Integer>();
    }
    
    public ResourceSet(JsonObject jsonResourceSet) {
    	this();
    	for(Member singleResource: jsonResourceSet){
    		this.setResource(singleResource.getName(), singleResource.getValue().asInt());
    	}
    }

	public HashMap<String, Integer> getResourceSet(){
    	return this.resourceSet;
    }
    
    public int getResource(String resourceName){
    	return this.resourceSet.getOrDefault(resourceName, 0);
    }
    
    public void setResource(String resourceName, int quantity){
    	this.resourceSet.put(resourceName, quantity);
    }
    
    public boolean hasNegativeValue(){
    	for(String key : this.getResourceSet().keySet()){
    		if(this.getResourceSet().get(key) < 0){
    			return true;
    		}
    	}return false;
    }
    
    public void addResource(String resourceName, int quantity){
    	if(this.resourceSet.containsKey(resourceName)){
    		int prevValue = this.resourceSet.get(resourceName);
    		this.resourceSet.put(resourceName, prevValue + quantity);
    	} else {
    		this.setResource(resourceName, quantity);
    	}
    }
    
    public void addResource(ResourceSet resource){
    	for(Map.Entry<String,Integer> entry : resource.getResourceSet().entrySet()){
    		this.addResource(entry.getKey(), entry.getValue());
    	}
    }

    public JsonObject toJson(){
    	JsonObject resource = new JsonObject();
    	for(String key : this.getResourceSet().keySet()){
    		resource.add(key, this.getResource(key));
    	}
    	return resource;
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
			for(Map.Entry<String, Integer> element: this.resourceSet.entrySet()){
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
		for(Map.Entry<String, Integer> element : resourceSet.entrySet()){
			tmp.append(" "+element.getKey()+" :"+element.getValue().toString());
		}
		return new String(tmp);
	}
	
}
