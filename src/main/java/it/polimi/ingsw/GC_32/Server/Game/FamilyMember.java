package it.polimi.ingsw.GC_32.Server.Game;

import it.polimi.ingsw.GC_32.Server.Game.Board.ActionSpace;

public class FamilyMember{
	
	private DiceColor diceColor;
	// l'actionValue dei familiy member è settato nella fase di diceRoll gestita dalla classe TurnManager
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
	
	public void setPosition(ActionSpace targetSpace){
		this.position = targetSpace;
	}
	
	public void removeFromBoard(){
		this.position = null;
	}
	
	public boolean isBusy(){
		return this.position != null;
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
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("actionValue :"+this.actionValue+"\nposition :");
		try{
			tmp.append(this.position.toString());
		}catch(NullPointerException e){
			tmp.append("la pedina non è stata ancora piazzata");
		}
		return new String(tmp);
	}

}
