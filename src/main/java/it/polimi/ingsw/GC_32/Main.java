package it.polimi.ingsw.GC_32;

import java.io.IOException;
import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.TurnManager;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class Main 
{
    public static void main( String[] args ) throws IOException
    {
        
    	Player a1 = new Player("PlayerOne");
		Player a2 = new Player("PlayerTwo");
		Player a3 = new Player("PlayerThree");
		
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		playerList.add(a1);
		playerList.add(a2);
		playerList.add(a3);
		
		
		Game game = new Game(playerList);
		Setup setupGame = new Setup(game);
		
		TurnManager turnManager = new TurnManager(game);
		turnManager.roundSetup();
		
		System.out.println(game.getBoard().toString());
		System.out.println("blackDice :" +game.getBlackDiceValue()+"\n");
		System.out.println("orangeDice :" +game.getOrangeDiceValue()+"\n");
		System.out.println("whiteDice :" +game.getWhiteDiceValue()+"\n");
		
		System.out.println("carta scomunica primo periodo :\n"+game.getExcommunicationCard(1)+"\n");
		System.out.println("carta scomunica secondo periodo :\n"+game.getExcommunicationCard(2)+"\n");
		System.out.println("carta scomunica terzo periodo :\n"+game.getExcommunicationCard(3)+"\n");
		
		game.getPlayerList().forEach(player -> System.out.println(player.toString()+"\n"));
		
    	
    }
}