package it.polimi.ingsw.GC_32.Server.Game;

import java.io.IOException;

import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;

public class GameLobby {
	
	private int min_players = 2;
	private int startGameTimeout = 5000;
	private Game game;
	
	public GameLobby() throws IOException, InterruptedException{
		System.out.println("[GAMELOBBY] GameLobby start, waiting for players");
		while(true){
			if(PlayerRegistry.getInstance().getConnectedPlayers().size()>=min_players){
				System.out.println("[GAMELOBBY] minimum number of players ("+min_players+") achieved.");
				System.out.println("[GAMELOBBY] timeout started, waiting for other players");
				Thread.sleep(startGameTimeout);
				System.out.println("[GAMELOBBY] timeout finished. starting new game...");
				game = new Game(PlayerRegistry.getInstance().getConnectedPlayers());
				System.out.println("[GAMELOBBY] new game created with "+game.getPlayerList().size()+" players");
				MessageManager.getInstance().registerGame(game);
				break;
			}
		}
		System.out.println("[GAMELOBBY] launching game thread");
		Thread gameThread = new Thread(game);
		gameThread.start();
		
	}
	
}
