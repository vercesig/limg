package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.List;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;


public class DevelopmentCard extends Card{

	private List<ResourceSet> cost;
	private int period;
	private CardType type;
	
	public DevelopmentCard(String name, List<ResourceSet> cost, int period, CardType type){
		super(name);
		this.cost = cost;
		this.period = period;
		this.type = type;
	}
	
	public List<ResourceSet> getCost(){
		return this.cost;
	}
	
	public int getPeriod(){
		return this.period;
	}
	
	public CardType getType(){
		return this.type;
	}

}
