package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;
import org.junit.Test;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class PersonalBonusTileTest{
	
	public PersonalBonusTile personalBonusTile;
	
	@Test
	public void checkPersonalBonusTile(){
		Effect effect = (Board b, Player p, Action a) -> {};
		this.personalBonusTile = new PersonalBonusTile(effect,false);
		assertEquals(effect, this.personalBonusTile.getPersonalBonus());
		assertEquals(false, this.personalBonusTile.isFlagGameRule());
	}
}