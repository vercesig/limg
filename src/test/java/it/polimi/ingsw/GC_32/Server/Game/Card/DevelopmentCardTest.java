package it.polimi.ingsw.GC_32.Server.Game.Card;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.eclipsesource.json.JsonObject;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
public class DevelopmentCardTest {
	public DevelopmentCard card;
	
	@Before
	public void prepareTests(){
		this.card = new DevelopmentCard("NAME", 0, "TYPE", 0);
	}
	
	@Test
	public void checkGetCost(){
		assertNotNull(this.card.getCost());
	}
	
	@Test
	public void checkGetPeriod(){
		assertEquals(0, this.card.getPeriod());
	}
	
	@Test
	public void checkGetType(){
		assertEquals("TYPE", this.card.getType());
	}
	
	@Test
	public void checkGetRequirements(){
		assertNotNull(this.card.getRequirments());
	}
	
	@Test
	public void checkToString(){
		assertNotNull(this.card.toString());
	}
	
	@Test
	public void checkRegisterCost(){
		JsonObject jcost = new JsonObject().add("COIN", 10).add("WOOD", 100).add("STONE", 9);
		this.card.registerCost(jcost);
		assertEquals(new ResourceSet(jcost).toString(), this.card.getCost().get(0).toString());
	}
	
	@Test
	public void checkSetRequirements(){
		JsonObject jcost = new JsonObject().add("COIN", 10).add("WOOD", 100).add("STONE", 9);
		this.card.setRequirments(jcost);
		assertEquals(new ResourceSet(jcost).toString(), this.card.getRequirments().toString());
	}
}
	