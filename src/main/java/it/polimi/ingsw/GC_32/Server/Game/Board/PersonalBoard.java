package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.HashMap;
import java.util.LinkedList;

import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Card.CardType;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.Card;

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
		return null;
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
}
