package it.polimi.ingsw.GC_32.Server.Game;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MessageHandlerTest{
    private Game game;
    private Player player;
    private MoveChecker moveChecker;
    private UUID gameUUID;
    private MessageHandler messageHandler;
    private TurnManager turnManager;
    private EndPhaseHandler endPhaseHandler;
    private Board board;
    private ContextManager contextManager;
    
    
    @Before
    public void initTest(){
        this.game = mock(Game.class);
        this.player = new Player();
        player.setPersonalBonusTile(new PersonalBonusTile(new JsonObject(), new JsonObject(), false));
        this.board = new Board();
        ArrayList<Player> playerList = new ArrayList<Player>();
        playerList.add(player);
        this.moveChecker = mock(MoveChecker.class);
        when(game.getMoveChecker()).thenReturn(this.moveChecker);
        when(game.getPlayerList()).thenReturn(playerList);
        this.gameUUID = UUID.randomUUID();
        when(game.getUUID()).thenReturn(gameUUID);
        when(game.getLock()).thenReturn(player.getUUID());
        when(game.getMoveChecker()).thenReturn(this.moveChecker);
        this.contextManager = mock(ContextManager.class);
        when(game.getContextManager()).thenReturn(contextManager);
        when(game.getBoard()).thenReturn(board);
        GameRegistry.getInstance().registerPlayer(player, ConnectionType.SOCKET);
        this.turnManager = mock(TurnManager.class);
        when(game.getTurnManager()).thenReturn(turnManager);
        this.endPhaseHandler = mock(EndPhaseHandler.class);
        when(game.getEndPhaseHandler()).thenReturn(endPhaseHandler);
        this.messageHandler = new MessageHandler(game);
    }
    
    @Test
    public void testASKACT(){
        player.getFamilyMember()[0].setActionValue(3);
        JsonObject ASKACTpayload = new JsonObject();
        ASKACTpayload.add("ACTIONTYPE", "TOWER");
        ASKACTpayload.add("FAMILYMEMBER_ID", 0);
        ASKACTpayload.add("REGIONID", 4);
        ASKACTpayload.add("SPACEID", 0);
        ASKACTpayload.add("COSTINDEX", 0);
        ASKACTpayload.add("CARDNAME", "");
        GameMessage message = new GameMessage(gameUUID, player.getUUID(), "ASKACT", ASKACTpayload);
        Action action = messageHandler.parseASKACT(player.getUUID(), ASKACTpayload);
        assertEquals("TOWER", action.getActionType());
        assertEquals(4, action.getRegionId());
        assertEquals(0, action.getActionSpaceId());
        assertEquals(3, action.getActionValue());
        assertEquals(0, action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt());
        assertEquals(0, action.getAdditionalInfo().get("COSTINDEX").asInt());
        when(moveChecker.checkMove(any(Game.class), any(Player.class), any(Action.class), any(ContextManager.class))).thenReturn(true);
        messageHandler.handleASKACT(message, ASKACTpayload);
        assertEquals(true, MessageManager.getInstance().getSocketSendQueue().poll().getMessage().asObject().get("RESULT").asBoolean());
        when(moveChecker.checkMove(any(Game.class), any(Player.class), any(Action.class), any(ContextManager.class))).thenReturn(false);
        messageHandler.handleASKACT(message, ASKACTpayload);
        assertEquals(false, MessageManager.getInstance().getSocketSendQueue().poll().getMessage().asObject().get("RESULT").asBoolean());
    }
    
    @Test
    public void testTRNEND(){
        when(turnManager.isGameEnd()).thenReturn(true);
        GameMessage endMessage = new GameMessage(gameUUID, player.getUUID(), "TRNEND", new JsonObject());
        messageHandler.handleTRNEND(endMessage);
        verify(endPhaseHandler).endGame();

        when(turnManager.isRoundEnd()).thenReturn(true);
        when(turnManager.isPeriodEnd()).thenReturn(true);
        when(turnManager.isGameEnd()).thenReturn(false);
        messageHandler.handleTRNEND(endMessage);
        verify(turnManager).distributeVaticanReport();
        verify(turnManager).setToUpdate(true);

        when(turnManager.isRoundEnd()).thenReturn(false);
        when(turnManager.isPeriodEnd()).thenReturn(false);
        when(turnManager.doesPopeWantToSeeYou()).thenReturn(true);
        when(turnManager.getPeriod()).thenReturn(1);
        when(this.contextManager.waitForContextReply()).thenReturn(new JsonObject().add("ANSWER", true));
        ExcommunicationCard excommCard = mock(ExcommunicationCard.class);
        when(excommCard.getName()).thenReturn("");
        when(game.getExcommunicationCard(0)).thenReturn(excommCard);
        messageHandler.handleTRNEND(endMessage);
        verify(contextManager).openContext(eq(ContextType.EXCOMMUNICATION), any(Player.class), eq(null), any(JsonValue.class));
        verify(contextManager).waitForContextReply();
        verify(game).getExcommunicationCard(0);
        verify(excommCard).getInstantEffect();
        verify(turnManager).goodbyePope();
    }
    
    public void testTRNENDnopope(){
        GameMessage endMessage = new GameMessage(gameUUID, player.getUUID(), "TRNEND", new JsonObject());
        when(turnManager.isRoundEnd()).thenReturn(false);
        when(turnManager.isPeriodEnd()).thenReturn(false);
        when(turnManager.doesPopeWantToSeeYou()).thenReturn(true);
        when(turnManager.getPeriod()).thenReturn(1);
        when(this.contextManager.waitForContextReply()).thenReturn(new JsonObject().add("ANSWER", false));
        GameConfig.getInstance().registerExcommunicationTrack(new HashMap<>());
        player.getResources().setResource("FAITH_POINTS", 7);
        messageHandler.handleTRNEND(endMessage);
        verify(game, times(0)).getExcommunicationCard(0);
        assertEquals(0, player.getResources().getResource("FAITH_POINTS"));
        assertEquals(14, player.getResources().getResource("VICTORY_POINTS"));
        verify(turnManager).goodbyePope();
    }
    
    public void testHandleMessage(){
        MessageHandler semifakeMessageHandler = spy(new MessageHandler(game));
        doNothing().when(semifakeMessageHandler).handleASKACT(any(GameMessage.class), any(JsonObject.class));
        doNothing().when(semifakeMessageHandler).handleTRNEND(any(GameMessage.class));
        doNothing().when(semifakeMessageHandler).handleASKLDRACT(any(GameMessage.class), any(JsonObject.class));
        GameMessage handleMessage = mock(GameMessage.class);
        when(handleMessage.getOpcode()).thenReturn("ASKACT");
        semifakeMessageHandler.handleMessage(handleMessage);
        verify(semifakeMessageHandler).handleASKACT(handleMessage, any(JsonObject.class));
        handleMessage = mock(GameMessage.class);
        when(handleMessage.getOpcode()).thenReturn("TRNEND");
        semifakeMessageHandler.handleMessage(handleMessage);
        verify(semifakeMessageHandler).handleTRNEND(handleMessage);
        handleMessage = mock(GameMessage.class);
        when(handleMessage.getOpcode()).thenReturn("ASKLDRACT");
        semifakeMessageHandler.handleMessage(handleMessage);
        verify(semifakeMessageHandler).handleASKLDRACT(handleMessage, any(JsonObject.class));
        handleMessage = mock(GameMessage.class);
        when(handleMessage.getOpcode()).thenReturn("TEST_MESSAGE");
        semifakeMessageHandler.handleMessage(handleMessage);
        verifyZeroInteractions(semifakeMessageHandler);
    }
}