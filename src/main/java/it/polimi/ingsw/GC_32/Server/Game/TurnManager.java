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
	
	private LinkedList<UUID> turnOrderQueue;
	private ArrayList<Player> memoryTurnOrder; // tiene memeotia del precedente ordine di turno
	
	private Game game;
	
	public TurnManager(Game game){
		LOGGER.log(Level.INFO, "tunrmanager inizialized");
		
		this.turnID = 0;
		this.roundID = 0;
		this.game = game;
		this.turnOrderQueue = new LinkedList<UUID>();
		this.memoryTurnOrder = new ArrayList<Player>();
		
		LOGGER.log(Level.INFO, "setting first turn order");
		
		// scelta ordine casuale del turno
		int playerListSize = game.getPlayerList().size();
		ArrayList<UUID> tmpPlayerList = new ArrayList<>();		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<playerListSize; i++){
			list.add(new Integer(i));
		}
		Collections.shuffle(list);
		for(int i=0; i<playerListSize; i++){
			tmpPlayerList.add(game.getPlayerList().get(list.get(i)).getUUID());
			memoryTurnOrder.add(game.getPlayerList().get(list.get(i)));
		}
		for(int i=0; i<game.getPlayerList().get(0).getFamilyMember().length; i++){
			tmpPlayerList.forEach(UUID -> turnOrderQueue.add(UUID));
		}
		
		System.out.println(turnOrderQueue.toString());
		
	}
	
	public int getTurnID(){
		return this.turnID;
	}
	
	public int getRoundID(){
		return this.roundID;
	}
	
	public int getPeriod(){
		return getRoundID()/2;
	}
	
	// restituisce il player a cui passare il lock
	public UUID nextPlayer(){
		turnID++;		
		return turnOrderQueue.poll();
	}
	
	public boolean isRoundEnd(){
	if((turnID-(game.getPlayerList().get(0).getFamilyMember().length*
			   	game.getPlayerList().size())) == 0){
			LOGGER.log(Level.INFO, "updating turn order");
			updateTurnOrder();
			turnID=0;
			roundID++;
			return true;
		}
		return false;
	}
	
	public boolean isPeriodEnd(){
		return roundID!=0&&roundID%2==0;
		
	}
	
	public boolean isGameEnd(){
		return isPeriodEnd()&&roundID%6==0;
		
	}
	
	public void updateTurnOrder(){
		ArrayList<Player> oldTurnOrder = new ArrayList<Player>(memoryTurnOrder); //vecchio ordine di turno
		ArrayList<FamilyMember> councilRegionState = game.getBoard().getCouncilRegion().getOccupants();
		ArrayList<Player> newTurnOrder = new ArrayList<Player>();
		
		//aggiorno stato dell'ordine di turno quardando i familiari in councilRegion
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
		
		for(int i=0; i<game.getPlayerList().get(0).getFamilyMember().length; i++){
			newTurnOrder.forEach(player -> turnOrderQueue.add(player.getUUID()));
		}
	}	
}
