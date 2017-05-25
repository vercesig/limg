package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Game.Card.Card;

public class ExcommunicationCard extends Card{

	private int period;
	
	public ExcommunicationCard(String name, int period){
		super(name);
		this.period = period;
	}
	
	public int getPeriod(){
		return this.period;
	}
	
	public void activateExcommunication(Player target){
		
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("name: "+this.getName()+"\nperiod: "+period);
		return new String(tmp);
	}
	
}