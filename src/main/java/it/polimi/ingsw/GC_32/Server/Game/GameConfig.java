package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.HashMap;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;

public class GameConfig {
	private ArrayList<PersonalBonusTile> bonusTileList;
	private JsonArray bonusSpace;
	private HashMap <Integer, Integer> excommunicationTrack;
	private HashMap <String, JsonValue> pointsConversion;
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
	
	public void registerExcommunicationTrack(HashMap <Integer, Integer> excommunicationTrack){
		this.excommunicationTrack = excommunicationTrack;
	}
	
	public void registerPointsConversion(HashMap <String, JsonValue> pointsConversion){
		this.pointsConversion = pointsConversion;
	}
	
	public JsonArray getBonusSpace(){
		return this.bonusSpace;
	}
	
	public ArrayList<PersonalBonusTile> getBonusTileList(){
		return this.bonusTileList;
	}
	
	public HashMap <Integer, Integer> getExcommunicationTrack(){
		return this.excommunicationTrack;
	}
	
	public HashMap <String, JsonValue> getPointsConversion(){
		return this.pointsConversion;
	}
}
