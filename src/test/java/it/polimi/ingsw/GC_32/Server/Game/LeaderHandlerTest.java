package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.CardRegistry;
import it.polimi.ingsw.GC_32.Server.Game.Card.LeaderCard;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;
import it.polimi.ingsw.GC_32.Server.Setup.JsonImporter;

public class LeaderHandlerTest{
    
    private Game game;
    private Player player;
    private Player player2;
    private LeaderHandler leaderHandler;

    @Before
    public void initTest() throws IOException{
        Reader leaderCardFile = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("leader_cards.json"));
        CardRegistry.getInstance().registerDeck(new Deck<LeaderCard>(JsonImporter.importLeaderCards(leaderCardFile)));
        this.game = mock(Game.class);
        this.player = new Player();
        this.player2 = new Player();
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player2);
        GameRegistry.getInstance().registerPlayer(player, ConnectionType.SOCKET);
        GameRegistry.getInstance().registerPlayer(player2, ConnectionType.SOCKET);
        when(game.getPlayerList()).thenReturn(playerList);
        this.leaderHandler = new LeaderHandler(game);
    }
    
    @Test
    public void checkConstructor(){
        assertEquals(0, this.leaderHandler.getTurn());
        assertTrue(this.leaderHandler.getRunning());
    }
}