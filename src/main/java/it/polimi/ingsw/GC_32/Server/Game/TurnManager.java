package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Game.Board.*;

public class TurnManager {
	
	private static TurnManager instance;
	
	private int turnID;
	private Board board;
	
	private TurnManager(Board board){
		this.turnID = 0;
		this.board = board;
	}
	
	public static TurnManager newTurnManager(Board board){
		if(instance==null){
			return new TurnManager(board);
		}
		return instance;
	}
	
	public int getTurnID(){
		return this.turnID;
	}
	
	
	private void diceRoll(){
		
	}
	
	private void updateTurnOrder(){
		
	}
	
	private void placeCards(){
		
	}
	
	// chiede al client, secondo il protocollo di comunicazione, di effettuare una mossa
	private void performAction(Player currentPlayer){
		
	}
	
	// controlla tracciato punti fede e se è il caso attiva gli effetti della scomunica per ogni giocatore da scomunicare
	private void checkExcommunication(){
		
	}
	
	public void roundSetup(){
		placeCards();
		diceRoll();
	}
	
	public void actionPhase(){
		for(Player p : board.getTurnOrderTrack().getPlayerOrder()){
			performAction(p);
		}
	}
	public void vaticanReportPhase(){
		checkExcommunication();
	}
	
	public void roundEnd(){
		flushBoard();
		updateTurnOrder();
	}
	
	/**
	 * start up
	 * - mischiare ciasun mazzo da 8 carte (divise per tipo e periodo)
	 * - genereare un mazzo per ogni periodo
	 * - pescare casualmente una carta scomunica per ogni periodo
	 * - disabilitare spazi azione grandi per partite a 2 giocatori
	 * - inizializzazione giocatori (scelta colore, instanziazione personlBoard
	 *   e bonus personale, familiari, cubi scomunica, piazzamento marker disk,
	 *   consegna prime risorse)
	 * - scelta casuale dell'ordine di turno
	 * - consegna monete in base alla posizione nell'ordine turno
	 */
	
	/**
	 * round setup 
	 * - pesca 4 carte dalla cima del mazzo per ogni mazzo e piazzale nelle 
	 *   torri
	 * - il primo giocatore lancia i dadi
	 * 
	 * actions
	 * - chiedi di eseguire un azione al client
	 * 
	 * vatican report
	 * - solo per turni pari, controllo tracciato punti fede e se è il caso di
	 *   scomunicare gestisce l'effetto della carta scomunica del periodo
	 *   corrente o gestisce il caso di non scomunica
	 *   
	 * fine turno
	 * - flush delle carte sviluppo rimaste
	 * - aggiornamento dell'ordine di turno
	 * - recupero familiari
	 */
	
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
