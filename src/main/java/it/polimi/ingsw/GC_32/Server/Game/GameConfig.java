package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.HashMap;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;

/**
 * singleton class which contains all the game configuration setting loaded from external JSON file during the Setup start phase. GameConfig is called in many parts of the 
 * code every time a game configuration must be retrived.
 * 
 * <ul>
 * <li>{@link #bonusSpace}: the bonus of all the action space of the board</li>
 * <li>{@link #bonusTileList}: the list of all the personal bonus tile of the game</li>
 * <li>{@link #excommunicationTrack}: HashMap which map each position of the faith track to the corresponding victory points bonus</li>
 * <li>{@link #pointsConversion}: the rules applied by the EndPhaseHandler to compute the final score</li>
 * <li>{@link GameConfig#instance}: the instance of this singleton classk</li>
 * </ul>
 *
 *@see PersonalBonusTile
 */

public class GameConfig {
	private ArrayList<PersonalBonusTile> bonusTileList;
	private JsonArray bonusSpace;
	private HashMap <Integer, Integer> excommunicationTrack;
	private HashMap <String, JsonValue> pointsConversion;
	private static GameConfig instance;
	
	private GameConfig(){}
	
	/**
	 * allows to get the instance of this singleton class
	 * @return the instance of the GameConfig class
	 */
	public static GameConfig getInstance(){
		if(instance == null){
			instance = new GameConfig();
		}
		return instance;
	}

	/**
	 * register all the personal bonus tiles parsed from the JSON configuration file
	 * @param bonusTileList the list of bonus tiles
	 */
	public void registerBonusTile(ArrayList<PersonalBonusTile> bonusTileList){
		this.bonusTileList = bonusTileList;
	}
	
	/**
	 * register the configuration of action space bonuses of the board parsed from the JSON configuration file
	 * @param bonusSpace JsonArray representing the configuration of bonus action spaces
	 */
	public void registerBonusSpace(JsonArray bonusSpace){
		this.bonusSpace = bonusSpace;
	}
	
	/**
	 * register the configuration of the bonuses given by the faith point track parsed from the JSON configuration file
	 * @param excommunicationTrack HashMap mapping every position of the faith track to the corresponding bonus expressed in victory points
	 */
	public void registerExcommunicationTrack(HashMap <Integer, Integer> excommunicationTrack){
		this.excommunicationTrack = excommunicationTrack;
	}
	
	/**
	 * register the conversion rules applied in the final score evaluation parsed from the JSON configuration file
	 * @param pointsConversion HashMap representing the point conversion rules
	 */
	public void registerPointsConversion(HashMap <String, JsonValue> pointsConversion){
		this.pointsConversion = pointsConversion;
	}
	
	/**
	 * allow to get the bonus space configuration
	 * @return a JsonArry of bonus
	 */
	public JsonArray getBonusSpace(){
		return this.bonusSpace;
	}
	
	/**
	 * allow to get the bonus tile list
	 * @return a list of personal bonus tile
	 */
	public ArrayList<PersonalBonusTile> getBonusTileList(){
		return this.bonusTileList;
	}
	
	/**
	 * allow to get the faith point track configuration
	 * @return the configuration of the faith track
	 */
	public HashMap <Integer, Integer> getExcommunicationTrack(){
		return this.excommunicationTrack;
	}
	
	/**
	 * allow to get the final points conversion rules 
	 * @return the final points conversion rules
	 */
	public HashMap <String, JsonValue> getPointsConversion(){
		return this.pointsConversion;
	}
}
