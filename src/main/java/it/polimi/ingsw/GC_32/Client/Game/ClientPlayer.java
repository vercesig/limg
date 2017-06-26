package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ClientPlayer {

	private ResourceSet playerResources;
	private HashMap<String, ArrayList<String>> cards;
	private String name;
	private ClientFamilyMember[] familyMembers = new ClientFamilyMember[4];
	private String bonusTile;
	
	public ClientPlayer(){
		this.playerResources = new ResourceSet();
		this.cards = new HashMap<String, ArrayList<String>>();
		
		this.playerResources.setResource("WOOD", 0);
		this.playerResources.setResource("COINS", 0);
		this.playerResources.setResource("SERVANTS", 0);
		this.playerResources.setResource("STONE", 0);
		this.playerResources.setResource("MILITARY_POINTS", 0);
		this.playerResources.setResource("VICTORY_POINTS", 0);
		this.playerResources.setResource("FAITH_POINTS", 0);
		
		for(int i=0; i<familyMembers.length; i++){
			familyMembers[i] = new ClientFamilyMember(this.name);
		}
		
	}
		
	public void setName(String name){
		this.name = name;
	}
	
	public void setPersonalBonusTile(String bonusTile){
			this.bonusTile = bonusTile;
		}
	
	public String getName(){
		return this.name;
	}
	
	public ResourceSet getPlayerResources(){
		return this.playerResources;
	}
	
	public ClientFamilyMember[] getFamilyMembers(){
		return this.familyMembers;
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
		tmp.append(name+"\n");
		tmp.append(playerResources.toString()+"\n");
		for(ClientFamilyMember familiar : familyMembers){
			tmp.append(familiar.toString());
		}
		for(Entry<String, ArrayList<String>> item : cards.entrySet()){
			tmp.append(item.getKey()+" : ");
			for(String card : item.getValue()){
				tmp.append(card+" ");
			}
			 tmp.append(bonusTile+"\n---------------------------------------------"); 		}
		return new String(tmp);
	}
	
}
