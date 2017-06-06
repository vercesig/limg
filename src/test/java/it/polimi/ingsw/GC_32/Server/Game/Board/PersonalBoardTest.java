package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class PersonalBoardTest{
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	public PersonalBoard personalBoard;
	
	@Test
	public void checkBoard(){
		this.personalBoard = new PersonalBoard();
		assertNotNull(this.personalBoard.getCards());
	}
	
	@Test
	public void checkCardAdding(){
		this.personalBoard = new PersonalBoard();
		DevelopmentCard mockCard = mock(DevelopmentCard.class);
		DevelopmentCard mockCard2 = mock(DevelopmentCard.class);
		DevelopmentCard mockCard3 = mock(DevelopmentCard.class);
		when(mockCard.getType()).thenReturn("TERRITORYCARD");
		when(mockCard2.getType()).thenReturn("TERRITORYCARD");
		when(mockCard3.getType()).thenReturn("BUILDINGCARD");
		this.personalBoard.addCard(mockCard);
		this.personalBoard.addCard(mockCard2);
		this.personalBoard.addCard(mockCard3);
		LinkedList<DevelopmentCard> list = new LinkedList<DevelopmentCard>();
		list.add(mockCard);
		list.add(mockCard2);
		assertTrue(this.personalBoard.getCardsOfType("TERRITORYCARD").containsAll(list));
		assertTrue(this.personalBoard.getCardsOfType("BUILDINGCARD").contains(mockCard3));
		assertEquals(2,this.personalBoard.getCards().get("TERRITORYCARD").size());
		assertEquals(1,this.personalBoard.getCards().get("BUILDINGCARD").size());
		assertEquals(0, this.personalBoard.getCardsOfType("Test").size());
	}
	
	@Test
	public void checkToString(){
		this.personalBoard = new PersonalBoard();
		assertNotNull(this.personalBoard.toString());
		DevelopmentCard mockCard = mock(DevelopmentCard.class);
		when(mockCard.toString()).thenReturn("Test Card");
		this.personalBoard.addCard(mockCard);
		assertNotNull(this.personalBoard.toString());
	}
}