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

/**
* class used to handle the first phase of the game in which the leader cards are distributed.
* 
* <ul>
* <li>{@link #game}: the game instance</li>
* <li>{@link #turnId}: the integer used to calculate turn of the distribution </li>
* <li>{@link #running}: boolean flag to set true to start the phase</li>
* <li>{@link #cardCollection}: collection of the leader card used in this game</li>
* </ul>
*
* @See Game, LeaderCard, LeaderUtils
*/

public class LeaderHandler {
	
	Game game;
	int turnId;
	boolean running;
	
	ArrayList <List<LeaderCard>> cardCollection;
	List<LeaderCard> listOne;
	List<LeaderCard> listTwo;
	List<LeaderCard> listThree;
	List<LeaderCard> listFour;
	
	/**
     * setup the LeaderHandler, initializing the memory structures and assigning the game to handle
     * @param game the game to handle
     */
	
	public LeaderHandler(Game game){
		
		this.game = game;
		this.cardCollection = new ArrayList<List<LeaderCard>>();

		Deck<LeaderCard> deck = CardRegistry.getInstance().getLeaderDeck();
		deck.shuffleDeck();
		
		int drawMultiplier;
		switch(game.getPlayerList().size()){
    		case 3:
    		    drawMultiplier = 3;
    			break;
    		case 4:
    		    drawMultiplier = 4;
    			break;
    		default:
    		    drawMultiplier = 2;
		}
		List<LeaderCard> tmpDeck = deck.drawManyElements(4*drawMultiplier);
		for(int i = 0; i < drawMultiplier; i++){
		    cardCollection.add(tmpDeck.subList(0, 4));
		}
		running = true;
		turnId = 0;
	}
	
	
	/**
     * get a list of leader cards for a player. He/She will receive a list of card based on the turn id
     * @param player 
 	 * @return the list card of the turn
     */
	
	public List<LeaderCard> getList(Player player){	
		List<LeaderCard> list;
		if(getIndex(player) - turnId < 0){
			if ((game.getPlayerList().size() +(getIndex(player) - turnId))<0){ //modulo! // a partire da turno 3
				list = cardCollection.get(-(game.getPlayerList().size() +(getIndex(player) - turnId)));
			} else {
				list = cardCollection.get(game.getPlayerList().size() +(getIndex(player) - turnId)); // prendo lista 4 per player 1;
			}
		} else { 
			list = cardCollection.get(getIndex(player) - turnId ); // esemp
		}
		return list;
	}
	
	/**
     * sets the card collection to a specified leader card. 
     * @param player of the list card needed to get the correct sublist in the card collection
 	 * @param list of leader cards used to modify the card collection;
	*/
	
	public void setList(Player player, List <LeaderCard> list){
		if(getIndex(player) - turnId < 0){
			if ((game.getPlayerList().size() +(getIndex(player) - turnId))<0){ //modulo! // a partire da turno 3
				cardCollection.set(-(game.getPlayerList().size() +(getIndex(player) - turnId)),list);
			}
			else
				cardCollection.set(game.getPlayerList().size() +(getIndex(player) - turnId), list); // prendo lista 4 per player 1;
		}
		else 
			cardCollection.set(getIndex(player) - turnId, list); // esemp
	}
	
	/**
     * creates a new list of cards to substitute the old list of cards in memory. 
     * @param player of the list card needed to get the correct sublist in the card collection
 	 * @param json of the card names used to fill the new list and to add the card reference int he player instance
	*/	

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
	
	/**
     * returns the index of a player in the player list instance in this game
     * @param player of a player list
 	 * @return the integer index of the current player
	*/	

	public int getIndex(Player player){
	    return game.getPlayerList().indexOf(player);
	}
	
	/**
	 * increments the turn id in this LeaderHandler object
 	*/	
	
	public void addTurn(){
		this.turnId++;
	}

	/**
	 * returns the turn id of this LeaderHandler object
	 * @return integer of the turn id
	*/
	
	public int getTurn(){
		return this.turnId;
	}
	
	/**
	 * returns the attribute running on this LeaderHandler
	 * @return boolean. true if this phase is running, false otherwise.
	*/
	public boolean getRunning(){
		return this.running;
	}
	
	/**
	 * ends this phase
	*/
	
	public void setInactive(){
		this.running = false;
	}
	
	/**
	 * send a message to the client and opens a context message in which the client can choose the leader card he/she wants 
	 * @param player of a player list
	*/
	
	public  void leaderPhase(Player player){
		List <LeaderCard> list = getList(player);
		JsonArray payload = new JsonArray();
		list.forEach(card -> payload.add(card.getName()));
		
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(game, player, ContextType.LEADERSET,  payload));
	}
}
