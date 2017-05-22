package it.polimi.ingsw.GC_32.Server.Game;

import java.util.HashMap;

public class ResourceSet implements Comparable {
	
	HashMap<String, Integer> resourceSet;
	
    public ResourceSet(){
    	this.resourceSet = new HashMap<String, Integer>();
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
    	}else 
    		setResource(resourceName, quantity);
    }
    
    // print usata per Test del codice
    public void print(){
    	System.out.print(resourceSet.toString());
    }
    
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}

