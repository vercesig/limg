package it.polimi.ingsw.GC_32.Server.Game;

import java.util.HashMap;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

public class ResourceSet implements Comparable {
	
	HashMap<String, Integer> resourceSet;
	
    public ResourceSet(){
    	this.resourceSet = new HashMap<String, Integer>();
    }
    
    public ResourceSet(JsonObject jsonResourceSet) {
    	this();
    	for(Member singleResource: jsonResourceSet){
    		this.addResource(singleResource.getName(), singleResource.getValue().asInt());
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
    	}
    }
    
    public void addResource(ResourceSet resource){
    	for(String type : resource.getResourceSet().keySet()){
    		this.resourceSet.put(type, resource.getResouce(type)+resourceSet.get(type));
    	}
    }

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		for(String name : resourceSet.keySet()){
			tmp.append(name+" :"+resourceSet.get(name).toString()+"\n");
		}
		return new String(tmp);
	}
	
}
