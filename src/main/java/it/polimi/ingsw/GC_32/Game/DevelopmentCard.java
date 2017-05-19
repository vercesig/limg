package it.polimi.ingsw.GC_32.Game;

import java.util.HashMap;

public class DevelopmentCard extends Card{

	private HashMap<Integer, Resource> cost;
	private int period;
	private CardType type;
	
	public DevelopmentCard(String name, HashMap<Integer, Resource> cost, int period, CardType type){
		super(name);
		this.cost = cost;
		this.period = period;
		this.type = type;
	}
	
	public HashMap<Integer, Resource> getCost(){
		return this.cost;
	}
	
	public int getPeriod(){
		return this.period;
	}
	
	public CardType getType(){
		return this.type;
	}

}
