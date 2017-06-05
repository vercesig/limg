package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.ArrayList;
import java.util.Iterator;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;


public class DevelopmentCard extends Card{

	private ArrayList<ResourceSet> cost;
	private ResourceSet requirments;
	private int period;
	private String type;
	
	public DevelopmentCard(String name, int period, String type){
		super(name);
		this.requirments = new ResourceSet();
		this.cost = new ArrayList<ResourceSet>();
		this.period = period;
		this.type = type;
	}
	
	public ArrayList<ResourceSet> getCost(){
		return this.cost;
	}
	
	public int getPeriod(){
		return this.period;
	}
	
	public String getType(){
		return this.type;
	}

	public ResourceSet getRequirments(){
		return this.requirments;
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("name: "+this.getName()+"\nperiod: "+period+"\ntype: "+type+"\n");
		cost.forEach(cost -> tmp.append("cost :" +cost.toString()+"\n"));
		return new String(tmp);
	}	
	
	public void registerCost(JsonObject jsonResourceSet){
		this.cost.add(new ResourceSet(jsonResourceSet));
	}
	
	public void registerCost(Iterator<JsonValue> jsonArray){
		while(jsonArray.hasNext()){
			this.registerCost(jsonArray.next().asObject());
		}
	}
	
	public void setRequirments(JsonObject requirments){
		Iterator<Member> singleItem = requirments.iterator();
		while(singleItem.hasNext()){
			String resourceType = singleItem.next().getName();
			int value = requirments.asObject().get(resourceType).asInt();
			this.requirments.setResource(resourceType, value);			
		}
	}
	
}
