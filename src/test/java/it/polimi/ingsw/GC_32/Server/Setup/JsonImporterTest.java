package it.polimi.ingsw.GC_32.Server.Setup;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;

public class JsonImporterTest{
	
	@Test
	public void checkDevelopmentCardImport() throws IOException{
		Reader reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("testDevelopmentCards.json"));
		List<DevelopmentCard> cardList = JsonImporter.importDevelopmentCards(reader);
		DevelopmentCard card = cardList.get(0);
		assertNotNull(card);
		assertEquals("TestCard", card.getName());
		assertEquals(3, card.getPeriod());
		assertEquals("TESTCARD", card.getType());
		card = cardList.get(1);
		ResourceSet resource = new ResourceSet();
		resource.addResource("WOOD", 10);
		resource.addResource("STONE", 10);
		assertTrue(card.getCost().get(0).equals(resource));
		card = cardList.get(2);
		assertTrue(card.getCost().get(0).equals(resource));
		resource = new ResourceSet();
		resource.addResource("WOOD", 10);
		resource.addResource("MILITARY_POINTS", 2);
		assertTrue(card.getCost().get(1).equals(resource));
	}
	
	@Test
	public void checkExcommunicationCardImport() throws IOException{
		Reader reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("testExcommunicationCards.json"));
		List<ExcommunicationCard> exCardList = JsonImporter.importExcommunicationCards(reader);
		ExcommunicationCard card = exCardList.get(0);
		assertEquals("TestExCard", card.getName());
		assertEquals(1, card.getPeriod());
	}
}
