package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.polimi.ingsw.GC_32.Client.Track;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ClientPlayer {

	private ResourceSet playerResources;
	private HashMap<String, ArrayList<String>> cards;
	private String name;
	private ClientFamilyMember[] familyMembers = new ClientFamilyMember[4];
	private String bonusTile;
	private Track[] track = new Track[3];
	
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
		
		track[0] = new Track("MILITARY_POINTS");
		track[1] = new Track("FAITH_POINTS");
		track[2] = new Track("VICTORY_POINTS");
		
		for(int i=0; i<familyMembers.length; i++){
			familyMembers[i] = new ClientFamilyMember();
		}
		
	}
		
	public void setName(String name){
		this.name = name;
		for(int i=0; i<familyMembers.length; i++){
			familyMembers[i].setName(name);
		}
	}
	
	public void setPersonalBonusTile(String bonusTile){
			this.bonusTile = "\n"+bonusTile+"\n";
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
	
	public Track[] getTrack(){
		return this.track;
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
	
	private void fillWith(StringBuilder stringBuilder, int howManyTimes, String string){
		for(int i=0; i<howManyTimes; i++){
			stringBuilder.append(string);
		}
	}
	
	public String toString(){
		int x = 100;
		StringBuilder tmp = new StringBuilder();
		fillWith(tmp, x, "=");
		tmp.append(" "+name+" ");
		tmp.append("\n\n");
		int numberOfDashes = (x - "RESOURCES".length() -2)/2;
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(" RESOURCES -");
		fillWith(tmp, numberOfDashes, "-");
		tmp.append("\n");
		tmp.append(playerResources.toString()+"\n\n");
		numberOfDashes = (x - "FAMILY MEMBERS".length() -2)/2;
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(" FAMILY MEMBERS ");
		fillWith(tmp, numberOfDashes, "-");
		tmp.append("\n");
		for(int i=0; i<familyMembers.length; i++){
			tmp.append(i +"] " + familyMembers[i].toString());
		}
		numberOfDashes = (x - "CARDS".length() - 2)/2;
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(" CARDS -");
		fillWith(tmp, numberOfDashes, "-");
		tmp.append("\n");
		for(Entry<String, ArrayList<String>> item : cards.entrySet()){
			tmp.append(item.getKey()+" : ");
			for(String card : item.getValue()){
				tmp.append(card+", ");
			}
			tmp.append("\n");
		}
		numberOfDashes = (x - "BONUS TILE".length() - 2)/2;
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(" BONUS TILE ");
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(bonusTile);
		fillWith(tmp, x, "-");
		return new String(tmp);
	}
	
}
