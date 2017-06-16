package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ClientPlayer {

	private ResourceSet playerResources;
	private HashMap<String, ArrayList<String>> cards;
	
	public ClientPlayer(){
		this.playerResources = new ResourceSet();
		this.cards = new HashMap<String, ArrayList<String>>();
		
		this.playerResources.setResource("WOOD", 0);
		this.playerResources.setResource("COINS", 0);
		this.playerResources.setResource("SERVANTS", 0);
		this.playerResources.setResource("STONE", 0);
		this.playerResources.setResource("MILITARY", 0);
		this.playerResources.setResource("VICTORY", 0);
		this.playerResources.setResource("FAITH", 0);
	}
		
	public ResourceSet getPlayerResources(){
		return this.playerResources;
	}
	
	public void addCard(String cardType, String card){
		if(cards.containsKey(cardType)){
			this.cards.get(cardType).add(card);
		}else{
			this.cards.put(cardType, new ArrayList<String>());
			this.cards.get(cardType).add(card);
		}
	}
	
	public HashMap<String,ArrayList<String>> getCards(){
		return this.cards;
	}
	
	public void addResources(ResourceSet resources){
		this.playerResources.addResource(resources);
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append(playerResources.toString()+"\n");
		for(Entry<String, ArrayList<String>> item : cards.entrySet()){
			tmp.append(item.getKey()+" : ");
			for(String card : item.getValue()){
				tmp.append(card+" ");
			}
			tmp.append("\n");
		}
		return new String(tmp);
	}
	
}
