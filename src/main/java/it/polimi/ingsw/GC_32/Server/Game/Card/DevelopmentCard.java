package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.ArrayList;
import java.util.Iterator;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;


public class DevelopmentCard extends Card{

	private ArrayList<ResourceSet> cost;
	private int period;
	private String type;
	
	public DevelopmentCard(String name, int period, String type){
		super(name);
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

	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("name: "+this.getName()+"\ncost:\n"+cost.toString()+"\nperiod: "+period+"\ntype: "+type+"\n");
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
	
}
