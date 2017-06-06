package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;
import org.junit.*;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class PlayerTest{
	public Player player;
	
	@Test
	public void checkUUID(){
		this.player = new Player();
		assertNotNull(this.player.getUUID());
		assertFalse(this.player.getUUID().equals(""));
	}
	
	@Test
	public void checkPersonalBoard(){
		this.player = new Player();
		assertNotNull(this.player.getPersonalBoard());
	}
	
	@Test
	public void checkPlayerName(){
		this.player = new Player();
		this.player.setPlayerName("");
		assertEquals(this.player.getName(),"");
		this.player = new Player();
		this.player.setPlayerName(null);
		assertNull(this.player.getName());
	}
	
	@Test
	public void checkFamilyMember(){
		this.player = new Player();
		for(FamilyMember f : this.player.getFamilyMember()){
			assertNotNull(f);
		}
	}
	
	@Test
	public void checkResourceSet(){
		this.player = new Player();
		assertNotNull(this.player.getResources());
		this.player.getResources().addResource("WOOD", 10);
		assertEquals(this.player.getResources().getResouce("WOOD"), 10);
	}
	
	@Test
	public void checkEffectListNotNull(){
		this.player = new Player();
		assertNotNull(this.player.getEffectList());
	}
	
	@Test
	public void checkEffectRegistration(){
		this.player = new Player();
		Effect testEffect = (Board b, Player p, Action a) -> {};
		this.player.addEffect(testEffect);
		assertEquals(this.player.getEffectList().get(0), testEffect);
	}
	
	@Test
	public void checkToStringNotNull(){
		this.player = new Player();
		assertNotNull(this.player.toString());
	}
}
