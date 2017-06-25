package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnManager {
	
	private final static Logger LOGGER = Logger.getLogger(TurnManager.class.getName());
	
	private int turnID;
	private int roundID;
	
	private LinkedList<String> turnOrderQueue;
	
	private Game game;
	
	public TurnManager(Game game){
		LOGGER.log(Level.INFO, "tunrmanager inizialized");
		
		this.turnID = 1;
		this.roundID = 0;
		this.game = game;
		this.turnOrderQueue = new LinkedList<String>();
		
		LOGGER.log(Level.INFO, "setting first turn order");
		
		// scelta ordine casuale del turno
		Random randomGenerator = new Random();
		ArrayList<Player> tmpPlayerList = new ArrayList<Player>(game.getPlayerList());
		int playerListSize = game.getPlayerList().size();
		ArrayList<String> tmp = new ArrayList<String>();
		
		for(int i=0; i<playerListSize; i++){
			int randomNumber = randomGenerator.nextInt(tmpPlayerList.size());
			tmp.add(game.getPlayerList().get(randomNumber).getUUID());
			tmpPlayerList.remove(randomNumber);
		}
		
		for(int i=0; i<game.getPlayerList().get(0).getFamilyMember().length; i++){
			tmp.forEach(UUID -> turnOrderQueue.add(UUID));
		}
	}
	
	public int getTurnID(){
		return this.turnID;
	}
	
	public int getRoundID(){
		return this.roundID;
	}
	
	// restituisce il player a cui passare il lock
	public String nextPlayer(){
		turnID++;
		//int currentIndexPlayer = game.getPlayerList().indexOf(PlayerRegistry.getInstance().getPlayerFromID(game.getLock()));		
		
		return turnOrderQueue.poll();
		
		/*try{// non sono l'ultimo giocatore della lista
			return game.getPlayerList().get(currentIndexPlayer+1);
		}catch(IndexOutOfBoundsException e){// il giro ricomincia
			return game.getPlayerList().get(0); 
		}	*/
	}
	
	public boolean isRoundEnd(){
		if(turnID%(game.getPlayerList().get(0).getFamilyMember().length*game.getPlayerList().size())==0){
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
		ArrayList<Player> oldTurnOrder = new ArrayList<Player>(game.getPlayerList()); //vecchio ordine di turno	
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
	
	/**
	 * solo per il turno finale
	 * - i giocatori che non hanno i punti fede richiesti, dopo aver attivato
	 *   gli effetti della scomunica, guadagnano tanti punti vittoria quanti
	 *   i punti fede posseduti
	 * - calcolo punteggio tenendo conto di eventuali scomuniche del terzo 
	 *   periodo
	 * 
	 */
	
	
}
