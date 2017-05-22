	/*
 * ID REGIONI:
 *  - 0) TOWER_GREEN
 *  - 1) TOWER_BLUE
 *  - 2) TOWER_YELLOW
 *  - 3) TOWER_PURPLE
 *  
 *  - 4) HARVEST_REGION
 *  - 5) PRODUCTION_REGION
 *  - 6) COUNCIL_REGION
 *  - 7) MARKET_REGION
 *  
 	*/
/*	
 * 	Piccola Demo di check sull'azione; Per adesso si fanno 
 * solo due check e si controlla che:
 * 	- ID dell'actionSpace della mossa dichiarata dal giocatore e' valida,
 *  cioe' esiste effettivamente una casella sul tabellone con quell'ID;
 *  - ID della Regione della casella su cui si vuole muovere il giocatore 
 *   e' valida, cioe' esiste effettivamente una regione con quell'ID;
 * 
 * 		Per una miglior risultato si sono piazzate un po' di stampe in giro
 *  per il codice. 
 */


import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ActionType;
import it.polimi.ingsw.GC_32.Server.Game.MoveChecker;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Effect.DictionaryEffect;
import it.polimi.ingsw.GC_32.Server.Game.Effect.EffectRegistry;

public class Main {
	
	public static void main(String[] args){
		
		//create Board
		Board board = new Board();
	    board.print();
		
		//create Player
		Player playerOne = new Player("PLAYER ONE");
		playerOne.getResources().addResource("COINS", 10); 
		
		//print PLayer info
		System.out.println("Giocatore: " + playerOne.getName());
		playerOne.getResources().print();
		
		//create Action
		Action actionNotValid = new Action(ActionType.TOWER, 1, 35, 2); // ActionType type, int SpaceActionId, int IdRegion
		Action actionValid = new Action(ActionType.TOWER, 5, 2, 2);
		
        //create Effect
		EffectRegistry.getInstance();
		DictionaryEffect dictionaryEffect = new DictionaryEffect();
		
		//create MoveChecker
		new MoveChecker(); 
		boolean checkOne = MoveChecker.checkStandardMove(board, playerOne, actionNotValid);
		boolean checkTwo = MoveChecker.checkStandardMove(board, playerOne, actionValid);

		//stampe
		actionNotValid.print();
		System.out.println("Risultato del Check: " + checkOne);
		actionValid.print();
		System.out.println("Risultato del Check: " + checkTwo);
		
	}
}
