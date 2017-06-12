package it.polimi.ingsw.GC_32.Server.Game.Effect;

import org.junit.Test;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Player;

import static org.junit.Assert.*;

public class AddEffectTest {
	
	@Test
	public void checkAddEffectBuilder(){
		JsonObject payload = new JsonObject();
		payload.add("WOOD", 2);
		payload.add("COINS", 4);
		payload.add("STONE", 3);
		
		Effect addTestEffect = EffectRegistry.getInstance().getEffect("ADD", payload);
		
		Player testPlayer = new Player();
		testPlayer.getResources().setResource("WOOD", 0);
		testPlayer.getResources().setResource("COINS", 0);
		testPlayer.getResources().setResource("STONE", 0);
		
		addTestEffect.apply(null, testPlayer, null);
		
		assertEquals(2,testPlayer.getResources().getResouce("WOOD"));
		assertEquals(4,testPlayer.getResources().getResouce("COINS"));
		assertEquals(3,testPlayer.getResources().getResouce("STONE"));
	}
	
	
}
