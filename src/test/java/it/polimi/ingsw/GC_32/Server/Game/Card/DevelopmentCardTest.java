package it.polimi.ingsw.GC_32.Server.Game.Card;

import static org.junit.Assert.*;

import org.junit.Test;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class DevelopmentCardTest {
	public DevelopmentCard card;
	
	@Test
	public void checkGetCost(){
		this.card = new DevelopmentCard("NAME", 0, "TYPE");
		assertNotNull(this.card.getCost());
	}
	@Test
	public void checkGetPeriod(){
		this.card = new DevelopmentCard("NAME", 0, "TYPE");
		assertEquals(0, this.card.getPeriod());
	}
	@Test
	public void checkGetType(){
		this.card = new DevelopmentCard("NAME", 0, "TYPE");
		assertEquals("TYPE", this.card.getType());
	}
	@Test
	public void checkGetRequirements(){
		this.card = new DevelopmentCard("NAME", 0, "TYPE");
		assertNotNull(this.card.getRequirments());
	}
	@Test
	public void checkToString(){
		this.card = new DevelopmentCard("NAME", 0, "TYPE");
		assertEquals("name: NAME\nperiod: 0\ntype: TYPE\n", this.card.toString());
	}
	@Test
	public void checkRegisterCost(){
		this.card = new DevelopmentCard("NAME", 0, "TYPE");
		JsonObject jcost = new JsonObject().add("COIN", 10).add("WOOD", 100).add("STONE", 9);
		this.card.registerCost(jcost);
		assertEquals(new ResourceSet(jcost).toString(), this.card.getCost().get(0).toString());
	}
	@Test
	public void checkSetRequirements(){
		this.card = new DevelopmentCard("NAME", 0, "TYPE");
		JsonObject jcost = new JsonObject().add("COIN", 10).add("WOOD", 100).add("STONE", 9);
		this.card.setRequirments(jcost);
		assertEquals(new ResourceSet(jcost).toString(), this.card.getRequirments().toString());
	}
}
	