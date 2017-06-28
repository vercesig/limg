package it.polimi.ingsw.GC_32.Client;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class Track {
	
	private int score;
	private String type;
	
	public Track(String type){
		
		this.type = type;
		this.score = 0;
	}	
	
	public void addScore(ResourceSet resource){
		for(String key: resource.getResourceSet().keySet()){
			if(key.equals(this.type)){
				this.score = score + resource.getResource(key);
			}
		}
	}
	//TO-DO
	public String toString(){
		return null;
	}

}