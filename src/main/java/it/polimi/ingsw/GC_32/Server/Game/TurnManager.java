package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;

public class TurnManager {
	
	private final static Logger LOGGER = Logger.getLogger(TurnManager.class.getName());
	
	private int turnID;
	private int roundID;
	private Game game;
	
	public TurnManager(Game game){
		this.turnID = 1;
		this.roundID = 0;
		this.game = game;
		LOGGER.log(Level.INFO, "tunrmanager inizialized");
	}
	
	public int getTurnID(){
		return this.turnID;
	}
	
	public int getRoundID(){
		return this.roundID;
	}
	
	// restituisce il player a cui passare il lock
	public Player nextPlayer(){
		turnID++;
		int currentIndexPlayer = game.getPlayerList().indexOf(PlayerRegistry.getInstance().getPlayerFromID(game.getLock()));		
		
		try{// non sono l'ultimo giocatore della lista
			return game.getPlayerList().get(currentIndexPlayer+1);
		}catch(IndexOutOfBoundsException e){// il giro ricomincia
			return game.getPlayerList().get(0); 
		}	
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
	
	public ArrayList<Player> updateTurnOrder(){
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
		return newTurnOrder;	
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
