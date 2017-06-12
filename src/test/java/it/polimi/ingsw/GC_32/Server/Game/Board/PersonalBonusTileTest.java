package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class PersonalBonusTileTest{
	
	public PersonalBonusTile personalBonusTile;
	
	@Test
	public void checkPersonalBonusTile(){
		JsonObject resource = new JsonObject().add("COINS", 10);
		this.personalBonusTile = new PersonalBonusTile(resource, false);
		assertNotNull(this.personalBonusTile.getPersonalBonus());
		assertEquals(false, this.personalBonusTile.isFlagGameRule());
	}
}