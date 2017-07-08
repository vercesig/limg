package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.CardRegistry;
import it.polimi.ingsw.GC_32.Server.Game.Card.LeaderCard;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

public class LeaderHandler {
	
	Game game;
	int turnId;
	boolean running;
	
	ArrayList <List<LeaderCard>> collection;
	List<LeaderCard> listOne;
	List<LeaderCard> listTwo;
	List<LeaderCard> listThree;
	List<LeaderCard> listFour;
	
	public LeaderHandler(Game game){
		
		this.game = game;
		this.collection = new ArrayList<List<LeaderCard>>();

		Deck<LeaderCard> deck = CardRegistry.getInstance().getLeaderDeck();
		deck.shuffleDeck();
		
		switch(game.getPlayerList().size()){
		case 3:
			listOne = deck.drawManyElements(4);
			listTwo = deck.drawManyElements(4);
			listThree = deck.drawManyElements(4);
			collection.add(listOne);
			collection.add(listTwo);
			collection.add(listThree);
			break;
		case 4:
			listOne = deck.drawManyElements(4);
			listTwo = deck.drawManyElements(4);
			listThree = deck.drawManyElements(4);
			listFour = deck.drawManyElements(4);
			collection.add(listOne);
			collection.add(listTwo);
			collection.add(listThree);
			collection.add(listFour);
			break;
		default:
			listOne = deck.drawManyElements(4);
			listTwo = deck.drawManyElements(4);
			collection.add(listOne);
			collection.add(listTwo);
		}
		running = true;
		turnId = 0;
	}
	
	public List <LeaderCard> getList(Player player){	
		List<LeaderCard> list;
		if(getIndex(player) - turnId < 0){
			if ((game.getPlayerList().size() +(getIndex(player) - turnId))<0){ //modulo! // a partire da turno 3
				list = collection.get(-(game.getPlayerList().size() +(getIndex(player) - turnId)));
			} else {
				list = collection.get(game.getPlayerList().size() +(getIndex(player) - turnId)); // prendo lista 4 per player 1;
			}
		} else { 
			list = collection.get(getIndex(player) - turnId ); // esemp
		}
		return list;
	}
	
	public void setList(Player player, List <LeaderCard> list){
		if(getIndex(player) - turnId < 0){
			if ((game.getPlayerList().size() +(getIndex(player) - turnId))<0){ //modulo! // a partire da turno 3
				collection.set(-(game.getPlayerList().size() +(getIndex(player) - turnId)),list);
			}
			else
				collection.set(game.getPlayerList().size() +(getIndex(player) - turnId), list); // prendo lista 4 per player 1;
		}
		else 
			collection.set(getIndex(player) - turnId, list); // esemp
	}
	
	public void setList(Player player, JsonArray json){
		
		List <LeaderCard> list = getList(player);	
		List <LeaderCard> newlist = new ArrayList<LeaderCard>();
		
		for(LeaderCard ld : list){
			for(JsonValue js : json){
				if(ld.getName().equals(js.asString())){
					newlist.add(ld);
				}
			}
		}	
		//aggiungo la carta al player
		list.forEach(leaderCard ->{
			if(!newlist.contains(leaderCard)){
				player.getPersonalBoard().addCardLeader(leaderCard);
			}
		});
		
		// set to collection
		setList(player, newlist);
	}
	
	public int getIndex(Player player){
		for (int i=0; i < game.getPlayerList().size(); i++){
			if(player.getUUID().equals(game.getPlayerList().get(i).getUUID())){
				return i;
			}
		}return 0;
	}
	
	public void addTurn(){
		this.turnId++;
	}
	
	public int getTurn(){
		return this.turnId;
	}
	
	public boolean getRunning(){
		return this.running;
	}
	
	public void setInactive(){
		this.running = false;
	}
	
	public  void leaderPhase(Player player){
		List <LeaderCard> list = getList(player);
		JsonArray payload = new JsonArray();
		list.forEach(card -> payload.add(card.getName()));
		
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(game, player, ContextType.LEADERSET,  payload));
	}
}
