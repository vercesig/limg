package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.PlayerRegistry;

public class GameLobby {
	
	private int MIN_PLAYERS = 2;
	private int MAX_PLAYERS = 4;
	private int startGameTimeout = 5000;
	private Game game;
	
	public GameLobby() throws InterruptedException{
		System.out.println("[GAMELOBBY] GameLobby start, waiting for players");
		while(true){
			if(PlayerRegistry.getInstance().getConnectedPlayers().size()>=MIN_PLAYERS){
				System.out.println("[GAMELOBBY] minimum number of players ("+MIN_PLAYERS+") achieved.");
				System.out.println("[GAMELOBBY] timeout started, waiting for other players");
				long startTimeoutTime = System.currentTimeMillis();
				while(true){
					if(startTimeoutTime + startGameTimeout < System.currentTimeMillis()){
						System.out.println("[GAMELOBBY] timeout finished. starting new game...");
						break;
					}
					if(PlayerRegistry.getInstance().getConnectedPlayers().size()==MAX_PLAYERS){
						System.out.println("[GAMELOBBY] maximum number of players ("+MAX_PLAYERS+") achieved. timeout stopped");
						break;
					}
					Thread.sleep(200);
				}
				break;
			}
		}
		game = new Game(PlayerRegistry.getInstance().getConnectedPlayers());
		System.out.println("[GAMELOBBY] new game created with "+game.getPlayerList().size()+" players");
		MessageManager.getInstance().registerGame(game);
		System.out.println("[GAMELOBBY] launching game thread");
		Thread gameThread = new Thread(game);
		gameThread.start();
	}
}
