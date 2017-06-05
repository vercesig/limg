package it.polimi.ingsw.GC_32.Server.Game;

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
    
    public int getResouce(String resourceName){
    	return this.resourceSet.getOrDefault(resourceName, 0);
    }
    
    public void setResource(String resourceName, int quantity){
    	this.resourceSet.put(resourceName, quantity);
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
    	for(String type : resource.getResourceSet().keySet()){
    		this.resourceSet.put(type, resource.getResouce(type)+resourceSet.get(type));
    	}
    }

    @Override
	public int compareTo(ResourceSet resource) {
		if(this.equals(resource)){
			return 0;
		}
		Set<String> thisResources = this.resourceSet.keySet();
		Set<String> otherResources = resource.resourceSet.keySet();
		Set<String> thisResourcesDiff = new HashSet<String>(thisResources);
		Set<String> otherResourcesDiff = new HashSet<String>(otherResources);
		thisResourcesDiff.removeAll(otherResources);
		otherResourcesDiff.removeAll(thisResources);
		if(!thisResourcesDiff.isEmpty() && otherResourcesDiff.isEmpty()){
			for(Map.Entry<String, Integer> element: this.resourceSet.entrySet()){
				if( element.getValue() < resource.getResouce(element.getKey())){
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
    	return 0;
    }
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		for(Map.Entry<String, Integer> element : resourceSet.entrySet()){
			tmp.append("\n"+element.getKey()+" :"+element.getValue().toString());
		}
		return new String(tmp);
	}
	
}
