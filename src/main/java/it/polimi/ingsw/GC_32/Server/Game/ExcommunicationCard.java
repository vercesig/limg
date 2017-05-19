package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Game.Card.Card;

public class ExcommunicationCard extends Card{

	private int period;
	private int faithPointNeeded;
	
	public ExcommunicationCard(String name, int period, int faithPointNeeded){
		super(name);
		this.period = period;
		this.faithPointNeeded = faithPointNeeded;
	}
	
	public int getPeriod(){
		return this.period;
	}
	
	public int getFaithPointNeeded(){
		return this.faithPointNeeded;
	}
	
	public void activateExcommunication(Player target){
		
	}
	
}
