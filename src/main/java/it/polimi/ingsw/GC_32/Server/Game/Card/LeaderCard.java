package it.polimi.ingsw.GC_32.Server.Game.Card;


public class LeaderCard extends Card{

	private boolean discarded;
	private boolean onePerRoundAbility;
	
	public LeaderCard(String name, boolean onePerRoundAbility){
		super(name);
		this.onePerRoundAbility = onePerRoundAbility;
		this.discarded = false;
	}
	
	public void discard(){
		
	}
	
	public boolean isDiscarded(){
		return this.discarded;
	}
	
	public void turnCard(){
		onePerRoundAbility = !onePerRoundAbility;
	}
	
}
