package it.polimi.ingsw.GC_32.Game;

import java.util.List;

import it.polimi.ingsw.GC_32.Game.Board.*;
import it.polimi.ingsw.GC_32.Game.Effect.Effect;

public class Player {

	private DiceColor color;
	private PersonalBoard personalBoard;
	private final String name;
	
	private Track militaryPoints;
	private Track faithPoints;
	private Track victoryPoints;
    private List<Effect> effectList;
	
	public Player(String name, Track militaryPoints, Track faithPoints, Track victoryPoints){
		this.personalBoard = new PersonalBoard(this);
		this.name = name;
		this.militaryPoints = militaryPoints;
		this.faithPoints = faithPoints;
		this.victoryPoints = victoryPoints;
	}
	
	public PersonalBoard getPersonalBoard(){
		return this.personalBoard;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getMilitaryPoints(){
		return this.militaryPoints.getPoint(this);
	}
	
	public int getVictoryPoints(){
		return this.victoryPoints.getPoint(this);
	}
	
	public int getFaithPoints(){
		return this.faithPoints.getPoint(this);
	}
	
	public DiceColor getColor(){
		return this.color;
	}

    public void addEffect(Effect e){
        this.effectList.add(e);
    }

    public List<Effect> getEffectList(){
        return this.effectList;
    }
}
