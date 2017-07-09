package it.polimi.ingsw.GC_32.Client;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

/**
 * class representing a score track
 * 
 * <ul>
 * <li>{@link #resource}: the score</li>
 * <li>{@link #type}: the type of resource this track is reffered to</li>
 * </ul>
 *
 */

public class Track {
	
	ResourceSet resource;
	String type;
	
	/**
	 * initialize the track whit the given type
	 * @param type
	 */
	public Track(String type){		
		this.type = type;
	}	

	/**
	 * register the resourceset to which retrive the score
	 * @param resource the player ResourceSet
	 */
	public void registerResourceSet(ResourceSet resource){
		this.resource = resource;
	}
	
	/**
	 * return a string representation of this track
	 */
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append(type + ": " + this.resource.getResource(type));	
		return new String(tmp);
	}
}