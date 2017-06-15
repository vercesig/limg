package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class ActionSpaceTest{
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	public ActionSpace actionSpace;
	
	@Test
	public void checkActionSpaceSingle(){
		this.actionSpace = new ActionSpace(new ResourceSet(), 0, true, 0, 0);
		assertTrue(this.actionSpace.isSingleActionSpace());
		this.actionSpace = new ActionSpace(new ResourceSet(), 0, false, 0, 0);
		assertFalse(this.actionSpace.isSingleActionSpace());
	}
	
	@Test
	public void checkBonus(){
		ResourceSet resource = new ResourceSet();
		this.actionSpace = new ActionSpace(resource, 0, true, 0, 0);
		assertEquals(resource, this.actionSpace.getBonus());
	}
	
	@Test
	public void checkActionValue(){
		for(int i=0; i<7; i++){
			this.actionSpace = new ActionSpace(new ResourceSet(), i, true, 0, 0);
			assertEquals(i, this.actionSpace.getActionValue());
		}
	}
	
	@Test
	public void checkActionIDs(){
		this.actionSpace = new ActionSpace(new ResourceSet(), 0, true, 10, 17);
		assertEquals(10, this.actionSpace.getRegionID());
		assertEquals(17, this.actionSpace.getActionSpaceID());
	}
	
	@Test
	public void checkAddFamilyMember(){
		this.actionSpace = new ActionSpace(new ResourceSet(), 0, true, 0, 0);
		FamilyMember familyMember = mock(FamilyMember.class);
		FamilyMember familyMember2 = mock(FamilyMember.class);
		assertFalse(this.actionSpace.isBusy());
		assertTrue(this.actionSpace.addFamilyMember(familyMember));
		assertTrue(this.actionSpace.isBusy());
		assertFalse(this.actionSpace.addFamilyMember(familyMember2));
		this.actionSpace.removeFamilyMember(familyMember);
		assertFalse(this.actionSpace.isBusy());
		this.actionSpace = new ActionSpace(new ResourceSet(), 0, false, 0, 0);
		assertTrue(this.actionSpace.addFamilyMember(familyMember));
		assertTrue(this.actionSpace.addFamilyMember(familyMember2));
	}
	
	@Test
	public void checkGetPlayers(){
		this.actionSpace = new ActionSpace(new ResourceSet(), 0, true, 0, 0);
		FamilyMember familyMember = mock(FamilyMember.class);
		Player player = new Player();
		when(familyMember.getOwner()).thenReturn(player);
		this.actionSpace.addFamilyMember(familyMember);
		assertTrue(this.actionSpace.getPlayers().contains(player));
	}
}