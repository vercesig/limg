package it.polimi.ingsw.GC_32.Server.Game.Effect;

import static org.junit.Assert.*;

import org.junit.Test;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class ChangeEffectTest {
	
	@Test
	public void checkChangeEffect(){
		
		JsonArray payloadArray = new JsonArray();
		JsonObject choose1 = new JsonObject();
		JsonObject choose2 = new JsonObject();
		
		choose1.add("WOOD", -2);
		choose1.add("COINS", -2);
		choose1.add("MILITARY", 1);
		choose1.add("SERVANTS", 1);
		
		choose2.add("VICTORY", -1);
		choose2.add("FAITH", 2);
		
		payloadArray.add(choose1);
		payloadArray.add(choose2);
		
		Player testPlayer = new Player();
		testPlayer.getResources().setResource("WOOD", 5);
		testPlayer.getResources().setResource("COINS", 8);
		testPlayer.getResources().setResource("MILITARY", 17);
		testPlayer.getResources().setResource("SERVANTS", 2);
		testPlayer.getResources().setResource("VICTORY", 23);
		testPlayer.getResources().setResource("FAITH", 1);
		
		
		Effect chooseEffectTest = EffectRegistry.getInstance().getEffect("CHANGE", payloadArray);
		
		Action testAction1 = new Action("HARVAST",5,2,2);
		testAction1.setAdditionalInfo(0);
		
		chooseEffectTest.apply(null, testPlayer, testAction1);
		
		assertEquals(3, testPlayer.getResources().getResource("WOOD"));
		assertEquals(6, testPlayer.getResources().getResource("COINS"));
		assertEquals(18, testPlayer.getResources().getResource("MILITARY"));
		assertEquals(3, testPlayer.getResources().getResource("SERVANTS"));
		

		Action testAction2 = new Action("HARVAST",5,2,2);
		testAction2.setAdditionalInfo(1);
		
		chooseEffectTest.apply(null, testPlayer, testAction2);
		
		assertEquals(22, testPlayer.getResources().getResource("VICTORY"));
		assertEquals(3, testPlayer.getResources().getResource("FAITH"));
	}
	
}
