package it.polimi.ingsw.GC_32;

import java.io.IOException;
import java.util.ArrayList;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.MoveChecker;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.TurnManager;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

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
        
		Player p = new Player();
		Action a = new Action("Test", 10, 0, 5);
	//	b.setTowerRegion(2);
	//	System.out.println(a.toString());
		DevelopmentCard card = new DevelopmentCard ("Card", 1, "TERRITORYCARD");
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
	//	b.getRegion(5).getActionSpace(2).addFamilyMember(p.getFamilyMember()[2]);
		
		MoveChecker move = new MoveChecker();
		ArrayList<Player> player = new ArrayList<>();
		player.add(p);
		Game game = new Game(player);
		TurnManager turnManager = new TurnManager(game);
		turnManager.placeCards();

		move.Simulate(game, p, a);
		
		//	System.out.println(b.toString());
	/*	System.out.println(a.toString());
		System.out.println(p.toString());
		System.out.println(card.toString());
	*/	
	//	System.out.println(MoveChecker.checkCost(b, p, a));
	//	System.out.println(MoveChecker.checkTerrytoryRequirement(b, p, a));
	//	System.out.println(MoveChecker.checkMaxCard(b, p, a));
		
		/*SocketListener socketListener = new SocketListener(9500);
		Thread socketListenerThread = new Thread(socketListener);
		socketListenerThread.start();		
		*/
		
    	
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
	//				playerList.add(a1);
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