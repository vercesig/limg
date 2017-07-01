package it.polimi.ingsw.GC_32.Server.Game.Effect;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class ChangeEffectTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void checkChangeEffect(){
		JsonObject payload1 = new JsonObject();
		JsonObject payload2 = new JsonObject();
		JsonArray payload = new JsonArray();
		
		JsonObject in1 = new JsonObject();
		in1.add("WOOD", 2).add("COINS", 2);
		JsonObject out1 = new JsonObject().add("MILITARY", 1).add("SERVANTS", 1);
		JsonObject in2 = new JsonObject().add("VICTORY", 1);
		JsonObject out2 = new JsonObject().add("FAITH", 2);
		
		
		payload1.add("RESOURCEIN", in1);
		payload1.add("RESOURCEOUT", out1);
		payload2.add("RESOURCEIN", in2);
		payload2.add("RESOURCEOUT", out2);
		payload.add(payload1).add(payload2);
		
		ResourceSet playerResources = new ResourceSet();
		Player testPlayer = mock(Player.class);
		when(testPlayer.getResources()).thenReturn(playerResources);
		playerResources.setResource("WOOD", 5);
		playerResources.setResource("COINS", 8);
		playerResources.setResource("MILITARY", 17);
		playerResources.setResource("SERVANTS", 2);
		playerResources.setResource("VICTORY", 23);
		playerResources.setResource("FAITH", 1);
		
		
		Effect choiceEffectTest = ChangeEffect.changeEffectBuilder.apply(payload);
		
		Action testAction1 = new Action("HARVEST",5,2,2);
		testAction1.setAdditionalInfo(new JsonObject().add("CHANGEID", 0));
		
		choiceEffectTest.apply(null, testPlayer, testAction1, null);
		
		assertEquals(3, playerResources.getResource("WOOD"));
		assertEquals(6, playerResources.getResource("COINS"));
		assertEquals(18, playerResources.getResource("MILITARY"));
		assertEquals(3, playerResources.getResource("SERVANTS"));
		

		Action testAction2 = new Action("HARVEST",5,2,2);
		testAction2.setAdditionalInfo(new JsonObject().add("CHANGEID", 1));
		
		choiceEffectTest.apply(null, testPlayer, testAction2, null);
		
		assertEquals(22, playerResources.getResource("VICTORY"));
		assertEquals(3, playerResources.getResource("FAITH"));
	}
	
}
