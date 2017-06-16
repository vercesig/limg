package it.polimi.ingsw.GC_32.Client.Game;

public class ClientFamilyMember {

	private int actionValue = 0;
	private String ownerUUID;
	
	public ClientFamilyMember(String ownerUUID){
		this.ownerUUID = ownerUUID;
	}
	
	public String toString(){
		return new String("ownerUUID :"+this.ownerUUID+"\nactionValue :"+this.actionValue);
	}
	
}
