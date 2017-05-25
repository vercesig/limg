package it.polimi.ingsw.GC_32.Server.Game.Card;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;


public class DevelopmentCard extends Card{

	private ResourceSet cost;
	private int period;
	private String type;
	
	public DevelopmentCard(String name, ResourceSet cost, int period, String type){
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
	
	public String getType(){
		return this.type;
	}

	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("name: "+this.getName()+"\ncost:\n"+cost.toString()+"\nperiod: "+period+"\ntype: "+type+"\n");
		return new String(tmp);
	}
	
}
