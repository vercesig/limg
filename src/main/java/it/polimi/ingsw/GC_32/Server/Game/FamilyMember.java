package it.polimi.ingsw.GC_32.Server.Game;

import java.util.logging.Level;

import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Board.ActionSpace;

/**
 * class representing a FamilyMember
 * 
 * <ul>
 * <li>{@link #diceColor}: the color of this family memeber</li>
 * <li>{@link #actionValue}: the action value of this family member. Family member's action value is setted by diceRoll() Game method</li>
 * <li>{@link #position}: the action space where the pawn is positionatedk</li>
 * <li>{@link #owner}: the player who own this family member</li>
 * </ul>
 *
 */
public class FamilyMember{
	
	private DiceColor diceColor;
	private int actionValue = 0; 
	private ActionSpace position;
	private final Player owner;
	
	/**
	 * set up the family member, assigning a player to it 
	 * @param owner the player who own this family member
	 */
	public FamilyMember(Player owner){
		this.position = null;
		this.owner = owner;
	}
	
	/**
	 * allow to retrive the action space where the pawn is positionated
	 * @return the position of this family member
	 */
	public ActionSpace getPosition(){
		return this.position;
	}
	
	/**
	 * set the pawn's position to the action space passes as argument
	 * @param targetSpace the position where the familiar has been moved
	 */
	public void setPosition(ActionSpace targetSpace){
		this.position = targetSpace;
	}
	
	/**
	 * remove the family member from the board, setting its position to null
	 */
	public void removeFromBoard(){
		this.position = null;
	}
	
	/**
	 * tells if the pawn is busy, i.e. if its position field is setted null or not
	 * @return true if position is not null, false otherwise
	 */
	public boolean isBusy(){
		return this.position != null;
	}
	
	/**
	 * allows to retrive the action value of this familiar
	 * @return the int value representing the action value of the pawn
	 */
	public int getActionValue(){
		return this.actionValue;
	}
	
	/**
	 * used by diceRoll() method, this method allows to set the action value of this family member
	 * @param diceValue the action value of this family member, which is equals to the value of the corresponding dice
	 */
	public void setActionValue(int diceValue){
		this.actionValue = diceValue;
	}
	
	/**
	 * allow to retrives the Player which own this family member
	 * @return the owner of this pawn
	 */
	public Player getOwner(){
		return this.owner;
	}
	
	/**
	 * get the color of this family member
	 * @return the color of this family member
	 */
	public DiceColor getColor(){
		return this.diceColor;
	}
	
	/**
	 * set the color field of the pawn
	 * @param diceColor the color which must be assigned to the pawn
	 */
	public void setColor(DiceColor diceColor){
		this.diceColor = diceColor;
	}
	
	/**
	 * return a string representation of the family member
	 */
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("actionValue :"+this.actionValue+"\nposition :");
		try{
			tmp.append(this.position.toString());
		}catch(NullPointerException e){
			Logger.getLogger("").log(Level.SEVERE, "context", e);
			tmp.append("la pedina non è stata ancora piazzata");
		}
		return new String(tmp);
	}

}
