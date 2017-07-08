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

	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append(type + " :" + score + '\n');	
		return new String(tmp);
	}

}