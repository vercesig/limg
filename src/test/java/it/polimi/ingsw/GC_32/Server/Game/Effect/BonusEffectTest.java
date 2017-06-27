package it.polimi.ingsw.GC_32.Server.Game.Effect;

import org.junit.Rule;
import org.junit.Test;

import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.eclipsesource.json.JsonObject;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class BonusEffectTest {

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 
	
	@Test
	public void checkBonusEffectCard(){
		
		Player testPlayer = new Player();
		testPlayer.getResources().setResource("COINS", 0);
		
		DevelopmentCard card1 = mock(DevelopmentCard.class);
		DevelopmentCard card2 = mock(DevelopmentCard.class);
		when(card1.getType()).thenReturn("TERRITORYCARD");
		when(card2.getType()).thenReturn("TERRITORYCARD");
		
		testPlayer.getPersonalBoard().addCard(card1);
		testPlayer.getPersonalBoard().addCard(card2);
		
		JsonObject payloadTest = new JsonObject();	
		payloadTest.add("TYPE", "CARD");
		payloadTest.add("FOREACH", "TERRITORYCARD");
		payloadTest.add("QUANTITY", 1);
		payloadTest.add("INCREASE", "COINS");
		payloadTest.add("INCREASINGQUANTITY", 4);
		
		Effect bonusCardTest = EffectRegistry.getInstance().getEffect("BONUS", payloadTest);

		bonusCardTest.apply(null, testPlayer, null, null);
		
		assertEquals(8, testPlayer.getResources().getResource("COINS"));	
	}
	
	@Test
	public void checkBonusEffectResource(){
		Player testPlayer = new Player();
		testPlayer.getResources().setResource("WOOD", 12);
		testPlayer.getResources().setResource("MILITARY", 0);
		
		JsonObject payloadTest = new JsonObject();	
		payloadTest.add("TYPE", "RESOURCE");
		payloadTest.add("FOREACH", "WOOD");
		payloadTest.add("QUANTITY", 2);
		payloadTest.add("INCREASE", "MILITARY");
		payloadTest.add("INCREASINGQUANTITY", 1);
		
		Effect bonusResourceTest = EffectRegistry.getInstance().getEffect("BONUS", payloadTest);
		
		bonusResourceTest.apply(null, testPlayer, null, null);
		
		assertEquals(6, testPlayer.getResources().getResource("MILITARY"));
	}
	
}
