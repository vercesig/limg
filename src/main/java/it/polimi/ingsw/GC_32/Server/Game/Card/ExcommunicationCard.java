package it.polimi.ingsw.GC_32.Server.Game.Card;

import it.polimi.ingsw.GC_32.Server.Game.Card.Card;

/**
 * clas representing an excommunication card
 * 
 * <ul>
 * <li>{@link #period}: the period of the excommunication card</li>
 * </ul>
 *
 *@see Card, Deck
 */
public class ExcommunicationCard extends Card{

	private int period;
	
	/**
	 * inizialize this card with the given name and period
	 * @param name the name of this card
	 * @param period the period of this card
	 */
	public ExcommunicationCard(String name, int period){
		super(name);
		this.period = period;
	}
	
	/**
	 * allows to retrive the card period
	 * @return the period of the card
	 */
	public int getPeriod(){
		return this.period;
	}
	
	/**
	 * return a string representation of this card
	 */
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("name: "+this.getName()+"\nperiod: "+period);
		return new String(tmp);
	}
	
}
