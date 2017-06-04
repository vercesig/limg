package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersonalBoardTest{
	
	public PersonalBoard personalBoard;
	
	@Test
	public void checkBoard(){
		this.personalBoard = new PersonalBoard();
		assertNotNull(this.personalBoard.getCards());
	}
	
	@Test
	public void checkToStringNotNull(){
		this.personalBoard = new PersonalBoard();
		assertNotNull(this.personalBoard.toString());
	}
}