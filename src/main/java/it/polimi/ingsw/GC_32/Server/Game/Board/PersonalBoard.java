package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class PersonalBoard {

	private HashMap<String, LinkedList<DevelopmentCard>> cards;
	
	public PersonalBoard(){
		this.cards = new HashMap<String, LinkedList<DevelopmentCard>>();
	}

	public HashMap<String, LinkedList<DevelopmentCard>> getCards(){
		return this.cards;
	}
	
	public LinkedList<DevelopmentCard> getCardsOfType(String type){
		if(cards.containsKey(type))
			return this.cards.get(type);
		return new LinkedList<DevelopmentCard>();
	}
	
	public void addCard(DevelopmentCard card){
		if(cards.containsKey(card.getType())){
			this.cards.get(card.getType()).add(card);
		}else{
			LinkedList<DevelopmentCard> tmpList = new LinkedList<DevelopmentCard>();
			this.cards.put(card.getType(), tmpList);
			this.cards.get(card.getType()).add(card);
		}
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		for(Map.Entry<String,LinkedList<DevelopmentCard>> type : cards.entrySet()){
			tmp.append("**********************  "+type.getKey()+": \n");
			type.getValue().forEach(card -> tmp.append(card.toString()));
		}
		return new String(tmp);
	}
}
