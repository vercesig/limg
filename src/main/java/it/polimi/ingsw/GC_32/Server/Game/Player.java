package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Board.*;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Server.Game.Effect.EffectRegistry;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;

/**
 * class which represent a Player.
 * 
 * <ul>
 * <li>{@link #effectList}: the list of effects related to this player. Effects in this list are taken from CAHRACHTER card permanent effect ane excommunication
 *  effect</li>
 * <li>{@link #familyMemberList}: the list of family members owned by the player</li>
 * <li>{@link #flags}: HashMap used to map effects of excommunication card. Adding a flag to this hashmap means that the player will suffer the effect of the excommunication.
 *  the JsonValue is used to customize the effect</li>
 * <li>{@link #gameID}: the ID of the game to whitch the player has been registered</li>
 * <li>{@link #name}: the player's name</li>
 * <li>{@link #personalBoard}: the personal board owned by this player</li>
 * <li>{@link #personalBonusTile}: the personal bonus tile assigned to this player</li>
 * <li>{@link #resources}: the resources owned by the player</li>
 * <li>{@link #uuid}: the UUID of this player</li> 
 * </ul>
 * 
 * @see PersonalBoard, PersonalBonusTile, Effect, ResourceSet, FamilyMember
 *
 */

public class Player {
	private PersonalBoard personalBoard;
	private String name;
    private ArrayList<Effect> effectList;
	private ResourceSet resources;
	private PersonalBonusTile personalBonusTile;
	private FamilyMember[] familyMemberList;
	private final UUID uuid;
	private UUID gameID;
	
	private HashMap<String, JsonValue> flags;
	
	/**
	 * initialize the player to his initial state
	 */
	public Player(){
		this.personalBoard = new PersonalBoard();
		this.resources = new ResourceSet();
		this.flags = new HashMap<>();
		
		// CONVENZIONE: familyMemberList[0] è sempre il familiare neutro
		this.familyMemberList = new FamilyMember[4];
		for(int i=0; i<familyMemberList.length; i++){
			familyMemberList[i] = new FamilyMember(this);	
		}
		familyMemberList[0].setColor(DiceColor.GREY);
		familyMemberList[1].setColor(DiceColor.BLACK);
		familyMemberList[2].setColor(DiceColor.WHITE);
		familyMemberList[3].setColor(DiceColor.ORANGE);
		this.uuid = UUID.randomUUID();
		this.effectList = new ArrayList<Effect>();
	}
	
	/**
	 * set the player name
	 * @param name the name of the player
	 */
	public void setPlayerName(String name){
		this.name = name;
	}
	
	/**
	 * set the player personal bonus tile
	 * @param bonusTile the bonus tile to assign to the player
	 */
	public void setPersonalBonusTile(PersonalBonusTile bonusTile){
		this.personalBonusTile = bonusTile;
	}
	
	/**
	 * get the UUID of this player
	 * @return the UUID of the player
	 */
	public UUID getUUID(){
		return uuid;
	}
	
	/**
	 * get the string representation of the player UUID
	 * @return the string representation of player UUID
	 */
	public String getID(){
		return uuid.toString();
	}
	
	/**
	 * allow to retrive the personal bonus tile of the player
	 * @return the personal bonus tile of this player
	 */
	public PersonalBoard getPersonalBoard(){
		return this.personalBoard;
	}
	
	/**
	 * allows to retrive the player name
	 * @return the player name
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * allows to retrive all the information about player's flag
	 * @return the HashMap containing player's flags
	 */
	public HashMap<String, JsonValue> getFlags(){
		return this.flags;
	}
	
	/**
	 * allows to get the player resources as ResourceSet
	 * @return the ResourceSet of the player
	 */
	public ResourceSet getResources(){
		return this.resources;
	}

	/**
	 * register the given effect to the player effect list
	 * @param e the effect to add
	 */
    public void addEffect(Effect e){
        this.effectList.add(e);
    }
    
    /**
     * allows to get all the effects of this player
     * @return an ArrayList of the player effects
     */
    public ArrayList<Effect> getEffectList(){
        return this.effectList;
    }
    
    /**
     * get sll the family member owned by the player
     * @return an array of family memeber
     */
    public FamilyMember[] getFamilyMember(){
    	return this.familyMemberList;
    }
    
    /**
     * return a string representation of the player
     */
    public String toString(){
    	StringBuilder tmp = new StringBuilder();
    	tmp.append("name :"+this.name+"\n"
    			 + "UUID :"+this.uuid.toString()+"\n"
    			 + "resources :"+this.resources.toString()+"\n"
    			 + "PERSONALBOARD :"+this.personalBoard.toString());
    	tmp.append("stato dei familiari: \n");
    	for(FamilyMember f : familyMemberList){
    		tmp.append(f.toString()+"\n");
    	}
    	return new String(tmp);
    }
    
    /**
     * allows the player to take a card from the board
     * @param board the board of the game
     * @param action the action performed by the player
     */
    public void takeCard(Board board, Action action){
    	TowerRegion selectedTower = (TowerRegion)(board.getRegion(action.getRegionId()));
    	this.personalBoard.addCard(selectedTower.getTowerLayers()[action.getActionSpaceId()].takeCard());
    }
    
    /**
     * allows the player to move a family member on a specific action space
     * @param i the index of the family member to move
     * @param action the action performed by the player
     * @param board the board of the game
     */
    public void moveFamilyMember(int i, Action action, Board board){
    	FamilyMember f = this.getFamilyMember()[i];
    	ActionSpace space = board.getRegion(action.getRegionId())
    			.getActionSpace(action.getActionSpaceId());
    	space.addFamilyMember(f);
    }
    
    /**
     * get the personal bonus tile assigned to this player
     * @return the bonus tile of this player
     */
    public PersonalBonusTile getPersonalBonusTile(){
    	return this.personalBonusTile;
    }
    
    /**
     * register this player to the Game into which the player has been registered, setting the correct UUID of the game
     * @param gameID the UUID of the game to assign
     */
    public void registerGame(UUID gameID){
    	this.gameID = gameID;
    }
    
    /**
     * allows to retrive the game UUID of the game into which the player has been registered
     * @return the game UUID of the player
     */
    public UUID getGameID(){
    	return this.gameID;
    }
    
    /**
     * tells if the player has been flagged by the flag passed as argument
     * @param flag the flag to check
     * @return if true the player has been flagged, otherwise not
     */
    public boolean isFlagged(String flag){
    	return this.flags.containsKey(flag);
    }
}
