package it.polimi.ingsw.GC_32.Server.Game.Effect;

import org.junit.Test;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;

import static org.junit.Assert.*;

public class EffectRegistryTest {

	private EffectRegistry effectRegistry;
	
	@Test
	public void checkEffectRegistryNotNull(){
		this.effectRegistry = EffectRegistry.getInstance();
		assertNotNull(this.effectRegistry);
	}
	
	@Test
	public void checkGetEffect(){
		Effect testEffect = (Board b, Player p, Action a, ContextManager cm) -> {
			System.out.println("this is a testEffect");
		};
		String testEffectName = "effectName";
		
		EffectRegistry.getInstance().registerEffect(testEffectName, testEffect);		
		assertEquals(testEffect, EffectRegistry.getInstance().getEffect(testEffectName));
		
	}
	
	@Test
	public void checkRegisterEffect(){
		Effect testEffect = (Board b, Player p, Action a, ContextManager cm) -> {
			System.out.println("this is a testEffect");
		};
		String testEffectName = "effectName";
		
		EffectRegistry.getInstance().registerEffect(testEffectName, testEffect);	
		assertNotNull(EffectRegistry.getInstance().getEffect(testEffectName));
	}
	
	@Test
	public void checkRegisterBuilder(){
		EffectBuilder testBuilder = AddEffect.buildAddResource;
		String testBuilderName = "ADD";
		JsonObject testPayload = new JsonObject();
		testPayload.add("WOOD", 2);
		testPayload.add("COINS", 2);
		
		EffectRegistry.getInstance().registerBuilder(testBuilderName, testBuilder);
		assertNotNull(EffectRegistry.getInstance().getEffect(testBuilderName, testPayload));
	}
		
}
