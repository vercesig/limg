package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.ArrayList;
import java.util.Iterator;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

import com.eclipsesource.json.JsonValue;

/**
 * class representing a DevelopmentCard.
 * 
 * <ul>
 * <li>{@link #cost}: a ResourceSet representing the cost of this card</li>
 * <li>{@link #requirments}: a ResourceSet representing the requirments that must be owned by the player to take the card.(like VentureCards)</li>
 * <li>{@link #period}: the period of this card</li>
 * <li>{@link #type}: a string representing the type of this card</li>
 * <li>{@link #minimumActionValue}: the minimum action value required by an action to activate the permanent effects of this card</li>
 * </ul>
 * 
 * @see ResourceSet, Card, Deck
 *
 */

public class DevelopmentCard extends Card{

	private ArrayList<ResourceSet> cost;
	private ResourceSet requirments;
	private int period;
	private String type;
	private int minimumActionValue;
	
	/**
	 * initialize a DevelopmentCard
	 * @param name the name of this card
	 * @param period the period of this card
	 * @param type the type of this card
	 * @param minimumActionValue the minimum actino value required by an action to activate the permanent effects of this card
	 */
	public DevelopmentCard(String name, int period, String type, int minimumActionValue){
		super(name);
		this.requirments = new ResourceSet();
		this.cost = new ArrayList<ResourceSet>();
		this.period = period;
		this.type = type;
		this.minimumActionValue = minimumActionValue;
	}
	/**
	 * allows to retrive the cost of this card
	 * @return the cost of this card (as a ResourceSet)
	 */
	public ArrayList<ResourceSet> getCost(){
		return this.cost;
	}
	
	/**
	 * allows to retrive the period of this card
	 * @return the period of the card
	 */
	public int getPeriod(){
		return this.period;
	}
	
	/**
	 * allows to retrive the type of this card
	 * @return the card type
	 */
	public String getType(){
		return this.type;
	}

	/**
	 * allows to retrive the requirements of this card
	 * @return the requirements of this card (as a ResourceSet)
	 */
	public ResourceSet getRequirments(){
		return this.requirments;
	}
	
	/**
	 * return a string representation of the card
	 */
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("name: "+this.getName()+"\nperiod: "+period+"\ntype: "+type+"\n");
		cost.forEach(cost -> tmp.append("cost :" +cost.toString()+"\n"));
		tmp.append("requirements" +this.getRequirments()+'\n');
		return new String(tmp);
	}	
	
	/**
	 * register the requirements of the card
	 * @param jsonResourceSet the JsonObject representing the requirements ResourceSet
	 */
	public void registerCost(JsonObject jsonResourceSet){
		this.cost.add(new ResourceSet(jsonResourceSet));
	}
	
	/**
	 * register the cost of the card
	 * @param jsonArray a JsonArray containing all the costs of the card, each one formatted as a JSON ResourceSet
	 */
	public void registerCost(Iterator<JsonValue> jsonArray){
		while(jsonArray.hasNext()){
			this.registerCost(jsonArray.next().asObject());
		}
	}
	/**
	 * give a discount on the cost of this card
	 * @param discount the ResourceSet representing a discount on the cost of this card
	 */
	public void discountCard(ResourceSet discount){
		for(ResourceSet singleCost : this.cost){
			singleCost.addResource(discount);
		}
	}
	
	/**
	 * allows to retrive the minimum action value
	 * @return the minimum action value required by the card to activate the permanent effects of this card
	 */
	public int getMinimumActionvalue(){
		return this.minimumActionValue;
	}
	
	/**
	 * register the requirements of the card
	 * @param requirments a JsonObject representing the requirements ResourceSet 
	 */
	public void setRequirments(JsonObject requirments){
		Iterator<Member> singleItem = requirments.iterator();
		while(singleItem.hasNext()){
			String resourceType = singleItem.next().getName();
			int value = requirments.asObject().get(resourceType).asInt();
			this.requirments.setResource(resourceType, value);			
		}
	}
	
}
