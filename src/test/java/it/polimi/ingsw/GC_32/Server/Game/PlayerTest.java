package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;
import org.junit.*;

public class PlayerTest{
	public Player player;
	
	@Test
	public void checkUUID(){
		this.player = new Player("Test");
		assertNotNull(this.player.getUUID());
		assertFalse(this.player.getUUID().equals(""));
	}
	
	@Test
	public void checkPersonalBoard(){
		this.player = new Player("Test");
		assertNotNull(this.player.getPersonalBoard());
	}
	
	@Test
	public void checkPlayerName(){
		this.player = new Player("");
		assertEquals(this.player.getName(),"");
		this.player = new Player(null);
		assertNull(this.player.getName());
	}
	
	@Test
	public void checkFamilyMember(){
		this.player = new Player("test");
		for(FamilyMember f : this.player.getFamilyMember()){
			assertNotNull(f);
		}
	}
	
	@Ignore
	@Test
	public void checkResourceSet(){
		this.player = new Player("Test");
		assertNotNull(this.player.getResources());
		this.player.getResources().addResource("WOOD", 10);
		assertEquals(this.player.getResources().getResouce("WOOD"), 10);
	}
}