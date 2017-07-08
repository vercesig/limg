package it.polimi.ingsw.GC_32.Client;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class Track {
	
	ResourceSet resource;
	String type;
	
	public Track(String type){
		
		this.type = type;
	}	

	public void registerResourceSet(ResourceSet resource){
		this.resource = resource;
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append(type + ": " + this.resource.getResource(type));	
		return new String(tmp);
	}
}