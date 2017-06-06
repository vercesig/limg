package it.polimi.ingsw.GC_32;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.GC_32.Server.Game.Player;

public class MainTest{
    
	@Test
	public void checkNewGame() throws IOException{
		Player player = new Player();
		ArrayList<Player> playerList = new ArrayList<>();
		playerList.add(player);
		Main.newGame(playerList);
	}
}
