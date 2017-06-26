package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;

public class PersonalBonusTileTest{
	
	public PersonalBonusTile personalBonusTile;
	
	@Test
	public void checkPersonalBonusTile(){
		JsonObject resource = new JsonObject().add("COINS", 10);
		this.personalBonusTile = new PersonalBonusTile(resource, null, false);
		assertNotNull(this.personalBonusTile);
		assertEquals(false, this.personalBonusTile.isFlagGameRule());
	}
}