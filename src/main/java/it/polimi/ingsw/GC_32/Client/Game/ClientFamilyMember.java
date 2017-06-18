package it.polimi.ingsw.GC_32.Client.Game;

public class ClientFamilyMember {

	private int actionValue = 0;
	private String owner;
	
	public ClientFamilyMember(String owner){
		this.owner = owner;
	}
	
	public void setActionValue(int actionValue){
		this.actionValue = actionValue;
	}
	
	public String toString(){
		return new String("actionValue :"+this.actionValue+"\n");
	}
	
}
