package it.polimi.ingsw.GC_32;

import java.io.IOException;
import java.util.ArrayList;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.MoveChecker;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.TurnManager;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.EffectRegistry;
import it.polimi.ingsw.GC_32.Server.Game.Effect.ImpossibleMoveException;

public class Main {
	
	public static void newGame(ArrayList<Player> players) throws IOException{
		Game game = new Game(players);
		//Setup setupGame = new Setup(game);
		
		System.out.println(game.getBoard().toString());
		System.out.println("blackDice :" +game.getBlackDiceValue()+"\n");
		System.out.println("orangeDice :" +game.getOrangeDiceValue()+"\n");
		System.out.println("whiteDice :" +game.getWhiteDiceValue()+"\n");
		
		System.out.println("carta scomunica primo periodo :\n"+game.getExcommunicationCard(1)+"\n");
		System.out.println("carta scomunica secondo periodo :\n"+game.getExcommunicationCard(2)+"\n");
		System.out.println("carta scomunica terzo periodo :\n"+game.getExcommunicationCard(3)+"\n");
		
		game.getPlayerList().forEach(player -> System.out.println(player.toString()+"\n"));
		
		System.out.println("inviando il messaggio:");
		JsonObject obj = new JsonObject();
		obj.add("type", "ACTION");
		System.out.println(" "+obj.toString());
		game.getPlayerList().get(0).makeAction(obj);
		
	}
	
    public static void main( String[] args ) throws IOException, ImpossibleMoveException{
        
		Player p = new Player();
		Action a = new Action("TOWER_GREEN", 10, 2, 4);
	//	b.setTowerRegion(2);
	//	System.out.println(a.toString());
		DevelopmentCard card = new DevelopmentCard ("Card", 1, "TERRITORYCARD", 0);
		JsonObject ja = new JsonObject().add("MILITARY_POINTS", 10).add("WOOD", 1).add("STONE", 3);
		JsonObject jb = new JsonObject().add("MILITARY_POINTS", 2).add("WOOD", 4);
		JsonObject jc = new JsonObject().add("SERVANTS", 5).add("WOOD", 1);
		card.setRequirments(ja);
		card.registerCost(jb);
		card.registerCost(jc);
	//	((TowerRegion) b.getRegion(5)).getTowerLayers()[0].setCard(card); 
	    
		for(int i=0; i<1; i++){
		p.getPersonalBoard().addCard(card);
		}
		// risorse player
		p.getResources().addResource("WOOD", 1);
		p.getResources().addResource("MILITARY_POINTS", 3);
		p.getResources().addResource("SERVANTS", 5);
		
		//familymember player
		//b.getRegion(5).getActionSpace(2).addFamilyMember(p.getFamilyMember()[2]);
		
		MoveChecker move = new MoveChecker();
		ArrayList<Player> player = new ArrayList<>();
		player.add(p);
		Game game = new Game(player);
		Board b = game.getBoard();
		TurnManager turnManager = new TurnManager(game);
		turnManager.placeCards();
		
		System.out.println(b.getRegion(a.getActionRegionId()).getActionSpace(a.getActionSpaceId()));
		EffectRegistry.getInstance().getEffect("NO_TOWER_BONUS").apply(b, p, a);
		System.out.println(b.getRegion(a.getActionRegionId()).getActionSpace(a.getActionSpaceId()));
		//move.Simulate(game, p, a);
		//	System.out.println(b.toString());
	}
}