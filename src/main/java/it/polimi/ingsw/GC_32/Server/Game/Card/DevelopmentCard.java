package it.polimi.ingsw.GC_32.Server.Game.Card;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;


public class DevelopmentCard extends Card{

	private ResourceSet cost;
	private int period;
	private CardType type;
	
	public DevelopmentCard(String name, ResourceSet cost, int period, CardType type){
		super(name);
		this.cost = cost;
		this.period = period;
		this.type = type;
	}
	
	public ResourceSet getCost(){
		return this.cost;
	}
	
	public int getPeriod(){
		return this.period;
	}
	
	public CardType getType(){
		return this.type;
	}

}
