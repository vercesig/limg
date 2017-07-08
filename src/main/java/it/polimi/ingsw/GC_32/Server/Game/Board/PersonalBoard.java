package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.LeaderCard;

/**
 * represent the personal bonus of the Player. a PersonalBoard contains all the card owned by the player
 * 
 * <ul>
 * <li>{@link cards}: the DevelopmentCard owned by the player.</li>
 * <li>{@link leader}: the LeaderCard owned by the player.</li>
 * <ul>
 *
 *@see DevelopmentCard, LeaderCard
 *
 */

public class PersonalBoard {

	private HashMap<String, LinkedList<DevelopmentCard>> cards;
	private LinkedList <LeaderCard> leader;
	
	/**
	 * initialize the data structures
	 */
	public PersonalBoard(){
		this.cards = new HashMap<String, LinkedList<DevelopmentCard>>();
		this.leader = new LinkedList<LeaderCard>();
	}

	/**
	 * allows to retrive all the development card of the player
	 * @return an HashMap representing all the DevelopmentCard owned by the player
	 */
	public HashMap<String, LinkedList<DevelopmentCard>> getCards(){
		return this.cards;
	}
	
	/**
	 * allows to retrive all the LeaderCard owned by the player
	 * @return the leader cards owned by the player
	 */
	public LinkedList<LeaderCard> getLeaderCards(){
		return this.leader;
	}
	
	/**
	 * get all the DevelopmentCard of the given type owned by the player 
	 * @param type the type of cards which must be retrived
	 * @return the list of development card of the specified type owned by the player
	 */
	public LinkedList<DevelopmentCard> getCardsOfType(String type){
		if(cards.containsKey(type))
			return this.cards.get(type);
		return new LinkedList<DevelopmentCard>();
	}
	
	/**
	 * add a LeaderCard to the leader cards owned by the player
	 * @param leaderCard the card to add
	 */
	public void addCardLeader(LeaderCard leaderCard){
		this.leader.add(leaderCard);
	}
	
	/**
	 * add a DevelopmentCard to the development cards owned by the player
	 * @param card the card to add
	 */
	public void addCard(DevelopmentCard card){
		if(cards.containsKey(card.getType())){
			this.cards.get(card.getType()).add(card);
		}else{
			LinkedList<DevelopmentCard> tmpList = new LinkedList<DevelopmentCard>();
			this.cards.put(card.getType(), tmpList);
			this.cards.get(card.getType()).add(card);
		}
	}
	
	/**
	 * give a string representation of the personal board
	 */
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		for(Map.Entry<String,LinkedList<DevelopmentCard>> type : cards.entrySet()){
			tmp.append("**********************  "+type.getKey()+": \n");
			type.getValue().forEach(card -> tmp.append(card.toString()));
		}
		return new String(tmp);
	}
}
