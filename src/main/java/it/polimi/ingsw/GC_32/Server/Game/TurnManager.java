package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class which hadle the turn mechanics
 * 
 * <ul>
 * <li>{@link #game}: the game which this instance of turn manager must handle</li>
 * <li>{@link #period}: the current period</li>
 * <li>{@link #roundID}: the current round</li>
 * <li>{@link #turnID}: the current turn</li>
 * <li>{@link #update}: if setted true the turn order must be updated</li>
 * <li>{@link #turnOrderQueue}: the queue containing the UUID of the players which represent the turn order</li>
 * <li>{@link #memoryTurnOrder}: get trace of the past turn order, is needed by updateTurnOrder() to update the turn order</li>
 * </ul>
 *
 */

public class TurnManager {
	
	private final static Logger LOGGER = Logger.getLogger(TurnManager.class.getName());
	
	private int turnID;
	private int roundID;
	private int visitPope;
	private int period;
	
	private boolean update;
	private LinkedList<UUID> turnOrderQueue;
	private ArrayList<Player> memoryTurnOrder;
	
	private Game game;
	
	/**
	 * initialize the turn manager for the given game. also set the first turn order randomly
	 * @param game the game this turn manager must handle
	 */
	public TurnManager(Game game){
		LOGGER.log(Level.INFO, "tunrmanager inizialized");
		
		this.turnID = 0;
		this.roundID = 0;
		this.period = 1;
		this.visitPope = 0;
		this.game = game;
		this.turnOrderQueue = new LinkedList<UUID>();
		this.memoryTurnOrder = new ArrayList<Player>();
		this.update = false;
		
		LOGGER.log(Level.INFO, "setting first turn order");
		
		// scelta ordine casuale del turno
		int playerListSize = game.getPlayerList().size();
		ArrayList<UUID> tmpPlayerList = new ArrayList<>();		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0; i < playerListSize; i++){
			list.add(i);
		}
		Collections.shuffle(list);
		for(int i=0; i<playerListSize; i++){
			tmpPlayerList.add(game.getPlayerList().get(list.get(i)).getUUID());
			memoryTurnOrder.add(game.getPlayerList().get(list.get(i)));
		}
		for(int i=0; i<game.getPlayerList().get(0).getFamilyMember().length; i++){
			tmpPlayerList.forEach(UUID -> turnOrderQueue.add(UUID));
		}		
	}
	
	/**
	 * get the current turn ID
	 * @return the current turn ID
	 */
	public int getTurnID(){
		return this.turnID;
	}
	
	/**
	 * get the current round ID
	 * @return the current round ID
	 */
	public int getRoundID(){
		return this.roundID;
	}
	
	/**
	 * get the current period ID
	 * @return the current period ID
	 */
	public int getPeriod(){
		return this.period;
	}
	
	/**
	 * if true the turn order queue must be updated
	 * @return true if the turn order must be updated, false otherwise
	 */
	public boolean isToUpdate(){
		return this.update;
	}
	
	/**
	 * if true, excommunication phase must be handled
	 * @return true if the excommunication phase must be handled, false otherwise
	 */
	public boolean doesPopeWantToSeeYou(){
		return visitPope > 0;
	}
	
	/**
	 * give the UUID of the player to whom the lock must be passed
	 * @return the UUID of the next player
	 */
	public UUID nextPlayer(){
		turnID++;		
		return turnOrderQueue.poll();
	}
	
	/**
	 * if true, this means that the round is ended
	 * @return true if the round is finished, false otherwise
	 */
	public boolean isRoundEnd(){
	//if((turnID-(game.getPlayerList().get(0).getFamilyMember().length*
	//		   	game.getPlayerList().size())) == 0){
		if(turnID == 2){
			LOGGER.log(Level.INFO, "updating turn order");
			updateTurnOrder();
			turnID=0;
			roundID++;
			return true;
		}
		return false;
	}
	
	public void goodbyePope(){
		System.out.println("INVITI CHE MI MANCANO: " + (visitPope -1));
		this.visitPope --;
	}
	
	/**
	 * starts the excommunication phase
	 */
	public void distributeVaticanReport(){
		this.visitPope = game.getPlayerList().size();
	}
	
	/**
	 * if true, the period is end (round ID is an even number)
	 * @return true if the period is finished, false otherwise
	 */
	public boolean isPeriodEnd(){
		if (roundID!=0&&roundID%2==0){
			this.period ++;
			return true;
		}
		return false;
	}
	
	/**
	 * tells if the game is end
	 * @return true if the game is end, false otherwise
	 */
	public boolean isGameEnd(){
		return roundID!=0&&roundID%6==0;
		
	}
	
	/**
	 * allows to set the update flag, in order to update the turn order queue
	 * @param update the value of the flag update
	 */
	public void setToUpdate(boolean update){
		this.update = update;
	}
	
	/**
	 * update the turn order queue, looking to the content of council region and the state of the previous turn order
	 */
	public void updateTurnOrder(){
		ArrayList<Player> oldTurnOrder = new ArrayList<Player>(memoryTurnOrder); //vecchio ordine di turno
		ArrayList<FamilyMember> councilRegionState = game.getBoard().getCouncilRegion().getOccupants();
		ArrayList<Player> newTurnOrder = new ArrayList<Player>();
		
		//aggiorno stato dell'ordine di turno guardando i familiari in councilRegion
		for(FamilyMember f : councilRegionState){
			if(!newTurnOrder.contains(f.getOwner())){
				newTurnOrder.add(f.getOwner());
			}
		}
		//player che non hanno piazzato familiari nel councilregion
		for(Player p : oldTurnOrder){
			if(!newTurnOrder.contains(p)){
				newTurnOrder.add(p);
			}
		}
		
		LinkedList<UUID> newTurnOrderQueue = new LinkedList<UUID>();
		
		for(int i=0; i<game.getPlayerList().get(0).getFamilyMember().length; i++){
			newTurnOrder.forEach(player -> newTurnOrderQueue.add(player.getUUID()));
		}
		
		this.turnOrderQueue = newTurnOrderQueue;
	}	
}
