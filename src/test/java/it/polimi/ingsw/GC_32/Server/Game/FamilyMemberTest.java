package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import it.polimi.ingsw.GC_32.Server.Game.Board.ActionSpace;

public class FamilyMemberTest{
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	public FamilyMember familyMember;
	
	@Mock
	public Player mockPlayer;
	
	@Mock
	public ActionSpace mockActionSpace;

	@Test
	public void checkOwnerNotNull(){
		this.familyMember = new FamilyMember(this.mockPlayer);
		assertNotNull(this.familyMember.getOwner());
		assertEquals(this.familyMember.getOwner(), this.mockPlayer);
	}
	
	@Test
	public void checkPositionSetting(){
		this.familyMember = new FamilyMember(this.mockPlayer);
		this.familyMember.setPosition(mockActionSpace);
		assertEquals(this.familyMember.getPosition(), this.mockActionSpace);
	}
	
	@Test
	public void checkPositionandBusy(){
		this.familyMember = new FamilyMember(this.mockPlayer);
		assertNull(this.familyMember.getPosition());
		assertFalse(this.familyMember.isBusy());
		this.familyMember.setPosition(this.mockActionSpace);
		assertTrue(this.familyMember.isBusy());
		this.familyMember.removeFromBoard();
		assertFalse(this.familyMember.isBusy());
	}
	
	@Test
	public void checkActionValue(){
		this.familyMember = new FamilyMember(this.mockPlayer);
		this.familyMember.setActionValue(5);
		assertEquals(this.familyMember.getActionValue(),5);
	}
	
	@Test
	public void checkToStringNotNull(){
		this.familyMember = new FamilyMember(this.mockPlayer);
		assertNotNull(this.familyMember.toString());
	}
}