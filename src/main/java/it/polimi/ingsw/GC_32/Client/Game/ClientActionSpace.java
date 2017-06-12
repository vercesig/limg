package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;

public class ClientActionSpace {

	private final int regionID;
	private final int actionSpaceID;
	private int actionValue;
	private ArrayList<ClientFamilyMember> occupants;
	private ResourceSet bonus;
	private boolean single;
	
	// solo per tower region
	private String cardName;
	
	public ClientActionSpace(ResourceSet bonus, int actionValue, boolean single, int regionID, int actionSpaceID){
		this.bonus = bonus;
		this.actionValue = actionValue;
		this.occupants = new ArrayList<ClientFamilyMember>();
		this.single = single;
		this.actionSpaceID = actionSpaceID;
		this.regionID = regionID;
	}
	
	public void setCard(String cardName){
		this.cardName = cardName;
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("regionID :"+this.regionID+"\nactionSpaceID :"+this.actionSpaceID+"\nactionValue :"
				+this.actionValue+"\nsingleFlag :"+this.single+"\n");
		try{
			tmp.append("bonus :"+bonus.toString()+"\n");
		}catch(NullPointerException e){
			tmp.append("no bonus\n");
		}
		tmp.append("occupants :");
		occupants.forEach(familiar -> tmp.append(familiar.toString()+","));
		tmp.append("\n");

		if(cardName!=null)
			tmp.append("Card :"+this.cardName);
		
		tmp.append("-------------------------------------\n");
		return new String(tmp);
	}
}
