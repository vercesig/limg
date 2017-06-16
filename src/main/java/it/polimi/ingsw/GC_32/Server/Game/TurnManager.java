package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;

public class TurnManager {
	
	private int turnID;
	private int roundID;
	private Game game;
	
	public TurnManager(Game game){
		this.turnID = 1;
		this.roundID = 0;
		this.game = game;
		System.out.println("[GAME->TURNMANAGER] tunrmanager inizialized");
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
		
		//TODO: this is a mess
		//ArrayList<Player> oldTurnOrder = new ArrayList<>();
		//player che non hanno piazzato familiari nel councilregion
		/*for(Player p : oldTurnOrder){
			if(!newTurnOrder.contains(p)){
				newTurnOrder.add(p);
			}
		}	
		game.setPlayerOrder(newTurnOrder);*/		
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
