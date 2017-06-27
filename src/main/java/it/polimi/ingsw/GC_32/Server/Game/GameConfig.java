package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;

import com.eclipsesource.json.JsonArray;

import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;

public class GameConfig {
	private ArrayList<PersonalBonusTile> bonusTileList;
	private JsonArray bonusSpace;
	private static GameConfig instance;
	
	private GameConfig(){}
	
	public static GameConfig getInstance(){
		if(instance == null){
			instance = new GameConfig();
		}
		return instance;
	}

	public void registerBonusTile(ArrayList<PersonalBonusTile> bonusTileList){
		this.bonusTileList = bonusTileList;
	}
	
	public void registerBonusSpace(JsonArray bonusSpace){
		this.bonusSpace = bonusSpace;
	}
	
	public JsonArray getBonusSpace(){
		return this.bonusSpace;
	}
	
	public ArrayList<PersonalBonusTile> getBonusTileList(){
		return this.bonusTileList;
	}
}
