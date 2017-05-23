package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Game.Board.ActionSpace;

public class FamilyMember{
	
	private boolean busy;
	private DiceColor diceColor;
	private int actionValue = 0;
	private ActionSpace position;
	private Player owner;
	
	public FamilyMember(Player owner){
		this.position = null;
		this.owner = owner;
	}
	
	public ActionSpace getPosition(){
		return this.position;
	}
		
	public void move(ActionSpace targetBox){
		
	}
	
	public boolean isBusy(){
		return this.busy;
	}
	
	public int getActionValue(){
		return this.actionValue;
	}
	
	public void setActionValue(int diceValue){
		this.actionValue = diceValue;
	}
	
	public Player getOwner(){
		return this.owner;
	}

}
