package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnManager {
	
	private final static Logger LOGGER = Logger.getLogger(TurnManager.class.getName());
	
	private int turnID;
	private int roundID;
	private int visitPope;
	private int period;
	
	private boolean update;
	private LinkedList<UUID> turnOrderQueue;
	private ArrayList<Player> memoryTurnOrder; // tiene memeotia del precedente ordine di turno
	
	private Game game;
	
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
	
	public int getTurnID(){
		return this.turnID;
	}
	
	public int getRoundID(){
		return this.roundID;
	}
	
	public int getPeriod(){
		return this.period;
	}
	
	public boolean isToUpdate(){
		return this.update;
	}
	
	public boolean DoesPopeWantToSeeYou(){
		return visitPope>0;
	}
	
	// restituisce il player a cui passare il lock
	public UUID nextPlayer(){
		turnID++;		
		return turnOrderQueue.poll();
	}
	
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
	
	public void distributeVaticanReport(){
		this.visitPope = game.getPlayerList().size();
	}
	
	public boolean isPeriodEnd(){
		if (roundID!=0&&roundID%2==0){
			this.period ++;
			return true;
		}
		return false;
	}
	
	public boolean isGameEnd(){
		return roundID!=0&&roundID%6==0;
		
	}
	
	public void setToUpdate(boolean update){
		this.update = update;
	}
	
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
