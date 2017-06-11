package it.polimi.ingsw.GC_32.Client.Network;

public class SlimFamilyMember {

	private int actionValue = 0;
	private String ownerUUID;
	
	public SlimFamilyMember(String ownerUUID){
		this.ownerUUID = ownerUUID;
	}
	
	public String toString(){
		return new String("ownerUUID :"+this.ownerUUID+"\nactionValue :"+this.actionValue);
	}
	
}
