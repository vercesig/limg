package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ClientActionSpace {

	private final Integer regionID;
	private final Integer actionSpaceID;
	private Integer actionValue;
	private ArrayList<ClientFamilyMember> occupants;
	private ResourceSet bonus;
	private Boolean single;
	// solo per tower region
	private String cardName;
	private Boolean blocked;
	
	public ResourceSet getBonus(){
		return this.bonus;
	}
	
	public ClientActionSpace(ResourceSet bonus, int actionValue, boolean single, int regionID, int actionSpaceID, boolean blockFlag){
		this.bonus = bonus;
		this.actionValue = actionValue;
		this.occupants = new ArrayList<ClientFamilyMember>();
		this.single = single;
		this.actionSpaceID = actionSpaceID;
		this.regionID = regionID;
		this.blocked = blockFlag;
	}
	
	public void setCard(String cardName){
		this.cardName = cardName;
	}
	
	public String getCardName(){
		return this.cardName;	
	}
	
	public void Lock(){
		this.blocked = true;
	}
	
	public void Unlock(){
		this.blocked = false;
	}
	
	public void addFamilyMember(ClientFamilyMember familyMember){
		this.occupants.add(familyMember);
	}
	
	public String[] getInfoContainer(){
		String[] infoContainer = new String[8];
		infoContainer[0] = regionID.toString();
		infoContainer[1] = actionSpaceID.toString();
		infoContainer[2] = actionValue.toString();
		infoContainer[3] = single.toString();
		infoContainer[4] = blocked.toString();
		if(bonus!=null){		
			// soluzione orrenda per mettere una rapida pezza al toString ClientBoard
			HashMap<String,String> tmp = bonus.getDecomposedResourceSetString();
			StringBuilder tmpStringBuilder = new StringBuilder();
			for(Entry<String,String> entry : tmp.entrySet()){
				if(entry.getKey().contains("MILITARY_POINTS")){
					tmpStringBuilder.append("MILITARY:"+entry.getValue());
				}else{
					tmpStringBuilder.append(entry.getKey()+entry.getValue()+" ");
				}
			}			
			
			infoContainer[5] = tmpStringBuilder.toString();
		}
		else
			infoContainer[5] = "empty";
		StringBuilder occupantsString = new StringBuilder();
		occupants.forEach(familiar -> occupantsString.append(familiar.getOwner()+","));
		infoContainer[6] = new String(occupantsString);
		if(cardName!=null)
			infoContainer[7] = cardName;
		else
			infoContainer[7] = "empty";
		
		return infoContainer;
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("regionID :"+this.regionID+"\nactionSpaceID :"+this.actionSpaceID+"\nactionValue :"
				+this.actionValue+"\nsingleFlag :"+this.single+"\nblocked :" + this.blocked);
		if(bonus!=null)
			tmp.append("bonus :"+bonus.toString()+"\n");
		else
			tmp.append("no bonus\n");
		tmp.append("occupants :");
		occupants.forEach(familiar -> tmp.append(familiar.toString()+","));
		tmp.append("\n");
	
		if(cardName!=null)
			tmp.append("Card :"+this.cardName+"\n");
		
		tmp.append("-------------------------------------\n");
		return new String(tmp);
	}
	
	public void flushFamilyMember(){
		this.occupants.clear();
	}
}
