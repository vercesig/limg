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
	}
	
	public int getTurnID(){
		return this.turnID;
	}
	
	private void diceRoll(){
		Random randomGenerator = new Random();
		game.setBlackDiceValue(1+randomGenerator.nextInt(6));
		game.setOrangeDiceValue(1+randomGenerator.nextInt(6));
		game.setWhiteDiceValue(1+randomGenerator.nextInt(6));
		for(Player player : game.getPlayerList()){
			player.getFamilyMember()[1].setActionValue(game.getBlackDiceValue());
			player.getFamilyMember()[2].setActionValue(game.getWhiteDiceValue());
			player.getFamilyMember()[3].setActionValue(game.getOrangeDiceValue());
		}
	}
	
	private void updateTurnOrder(){
		ArrayList<Player> oldTurnOrder = game.getPlayerList(); //vecchio ordine di turno	
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
		game.setPlayerOrder(newTurnOrder);		
	}
	
	public void placeCards(){
		for(TowerRegion towerRegion : this.game.getBoard().getTowerRegion()){
			for(TowerLayer towerLayer : towerRegion.getTowerLayers()){
				towerLayer.setCard(game.getDeck(towerRegion.getTypeCard()).drawElement());
			}
		}	
	}
	
	// controlla punti fede posseduti e se del caso attiva carta scomunica sul giocatore da scomunicare
	private void checkExcommunication(){
		int excommunicationLevel = 3 + this.turnID/2 -1 ; //calcolo punti fede richiesti 
		for(Player p : game.getPlayerList()){
			if(p.getResources().getResouce("VICTORY")<=excommunicationLevel){
				System.out.println("TIE! beccati la scomunica!");
			}
		}
	}
		
	// ---------- METODI RELATIVI ALLA ROTAZIONE DEI TURNI

	// fa partire la partita, da lanciare esplicitamente dopo la creazione di game SOLO UNA VOLTA
	public void gameStart(){
		placeCards();
		diceRoll();
		game.setLock(game.getPlayerList().get(0).getUUID());
		PlayerRegistry.getInstance().getPlayerFromID(game.getLock()).makeAction();
	}
	
	// passa il turno al player successivo
	public void nextPlayer(){
		turnID++;
		Player tmp;
		int currentIndexPlayer = game.getPlayerList().indexOf(PlayerRegistry.getInstance().getPlayerFromID(game.getLock()));
		
		// non sono l'ultimo giocatore della lista
		if(currentIndexPlayer+1<=game.getPlayerList().size()){
			game.setLock(game.getPlayerList().get(currentIndexPlayer+1).getUUID());
		}
		else{ // il giro ricomincia
			game.setLock(game.getPlayerList().get(0).getUUID());
		}
		PlayerRegistry.getInstance().getPlayerFromID(game.getLock()).makeAction();
		
		// fine round
		if(turnID%(game.getPlayerList().get(0).getFamilyMember().length*game.getPlayerList().size())==0){
			updateTurnOrder();
			this.game.getBoard().flushBoard();
			
			// prepara il prossimo round
			this.roundID++;
			placeCards();
			diceRoll();
			
			// fine periodo
			if(roundID%2==0){
				checkExcommunication();	
			}
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
