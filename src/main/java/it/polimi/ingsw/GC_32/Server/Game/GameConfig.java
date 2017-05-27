package it.polimi.ingsw.GC_32.Server.Game;

public class GameConfig {

	private static GameConfig instance;
	
	public static GameConfig getInstance(){
		if(instance==null){
			return new GameConfig();
		}
		return instance;
	}
	
	private GameConfig(){
		
	}
	
	
	
	
}
