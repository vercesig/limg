package it.polimi.ingsw.GC_32.Server.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import it.polimi.ingsw.GC_32.Server.Game.Board.*;
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class TurnManager {
	
	private int turnID;
	private int roundID;
	private Game game;
	
	public TurnManager(Game game){
		this.turnID = 1;
		this.roundID = 1;
		this.game = game;
		System.out.println("[TURNMANAGER] inizialized");
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
		roundID++;
		return turnID%(game.getPlayerList().get(0).getFamilyMember().length*game.getPlayerList().size())==0;
	}
	
	public boolean isPeriodEnd(){
		return roundID%2==0;
	}
	
	public boolean isGameEnd(){
		return roundID%6==0;
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
