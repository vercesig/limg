package it.polimi.ingsw.GC_32.Server.Game;

import java.util.List;

import it.polimi.ingsw.GC_32.Server.Game.Board.*;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class Player {
	private PersonalBoard personalBoard;
	private final String name;
    private List<Effect> effectList;
	private ResourceSet resources;
	private PersonalBonusTile personalBonusTile;
	private FamilyMember[] familyMemberList;
	
	public Player(String name){
		this.personalBoard = new PersonalBoard(this);
		this.name = name;
		this.resources = new ResourceSet();
		this.familyMemberList = new FamilyMember[3];
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

    public void addEffect(String e){
        this.effectList.add(e);
    }

    public List<String> getEffectList(){
        return this.effectList;
    }
    
    public FamilyMember[] getFamilyMember(){
    	return this.familyMemberList;
    }
}
