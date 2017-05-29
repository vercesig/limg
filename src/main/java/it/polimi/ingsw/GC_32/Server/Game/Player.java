package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import it.polimi.ingsw.GC_32.Server.Game.Board.*;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Server.Game.Effect.EffectRegistry;

public class Player {
	private PersonalBoard personalBoard;
	private final String name;
    private ArrayList<Effect> effectList;
	private ResourceSet resources;
	//private PersonalBonusTile personalBonusTile;
	private FamilyMember[] familyMemberList;
	private String uuid;
	
	public Player(String name){
		this.personalBoard = new PersonalBoard();
		this.name = name;
		this.resources = new ResourceSet();
		this.familyMemberList = new FamilyMember[3];
		for(int i = 0; i < this.familyMemberList.length; i++){
			this.familyMemberList[i] = new FamilyMember(this);
		}
		this.uuid = UUID.randomUUID().toString();
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public PersonalBoard getPersonalBoard(){
		return this.personalBoard;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getMilitaryPoints(){
		return this.resources.getResouce("MILITARY");
	}
	
	public int getVictoryPoints(){
		return this.resources.getResouce("VP");
	}
	
	public int getFaithPoints(){
		return this.resources.getResouce("FAITH");
	}
	
	public int getWoodQuantity() {
		return this.resources.getResouce("WOOD");
	}

	public int getStoneQuantity() {
		return this.resources.getResouce("STONE");
	}

	public int getCoins() {
		return this.resources.getResouce("COINS");
	}

	public int getServants() {
		return this.resources.getResouce("SERVANTS");
	}
	
	public ResourceSet getResources(){
		return this.resources;
	}

    public void addEffect(Effect e){
        this.effectList.add(e);
    }
    
    public void addEffect(String s){
    	this.effectList.add(EffectRegistry.getInstance().getEffect(s));
    }

    public List<Effect> getEffectList(){
        return this.effectList;
    }
    
    public FamilyMember[] getFamilyMember(){
    	return this.familyMemberList;
    }
    
    public String toString(){
    	StringBuilder tmp = new StringBuilder();
    	tmp.append("name :"+this.name+"\nUUID :"+this.uuid+"\nresources :"+this.resources.toString()+"\n");
    	return new String(tmp);
    }
}
