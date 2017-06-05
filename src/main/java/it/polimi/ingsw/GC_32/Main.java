package it.polimi.ingsw.GC_32;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.TurnManager;
import it.polimi.ingsw.GC_32.Server.Game.Effect.EffectRegistry;
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;
import it.polimi.ingsw.GC_32.Server.Network.SocketListener;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

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
	
    public static void main( String[] args ) throws IOException{
        
		
		
		SocketListener socketListener = new SocketListener(9500);
		Thread socketListenerThread = new Thread(socketListener);
		socketListenerThread.start();		
		
		
    	
    	/*ServerSocket serverSocket = new ServerSocket(9500);
		System.out.println("waiting...");
		
		Socket socket = serverSocket.accept();
		System.out.println("Connection established");
    			
		Scanner in = new Scanner(socket.getInputStream());
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		
		while(true){
			if(in.hasNextLine()){
				String msg = in.nextLine();
				JsonObject o = Json.parse(msg).asObject();
				if(o.get("type").asString().equals("CONN")){
					out.println("sei connesso");
					out.flush();
				}
				if(o.get("type").asString().equals("INTGM")){
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
					//out.flush();
				}
			}
		}*/
		/*
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
		
		
		Action action = new Action(ActionType.TOWER,3,1,6);
		a1.takeCard(game, action);
		System.out.println(game.getBoard().toString());

		System.out.println(a1.toString());
		
		a1.getPersonalBoard().getCardsOfType("TERRITORYCARD").forEach(card -> card.getInstantEffect().apply(game.getBoard(), a1, null));
		
		System.out.println("dopo effetto ^^^^^^^^^^^^^^^^^^^\n");
		System.out.println(a1.toString());*/
    }
}