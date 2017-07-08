package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.PersonalBonusTile;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class ActionHandlerTest{
    private Game game;
    private Player player;
    private Board board;
    private ContextManager contextManager;
    private ActionHandler actionHandler;
    
    @Before
    public void initTest(){
        this.game = mock(Game.class);
        this.player = new Player();
        this.board = new Board();
        this.contextManager = mock(ContextManager.class);
        when(game.getBoard()).thenReturn(board);
        when(game.getContextManager()).thenReturn(contextManager);
        this.actionHandler = new ActionHandler(game);
    }
    
    @Test
    public void checkHandleTower(){
        Action towerAction = new Action("TOWER", 3, 0, 4);
        DevelopmentCard card = spy(new DevelopmentCard("TEST", 1, "CHARACTERCARD", 1));
        Effect instantEffect = mock(Effect.class);
        Effect permanentEffect = mock(Effect.class);
        //card.registerInstantEffect(instantEffect);
        card.registerPermanentEffect(permanentEffect);
        board.getTowerRegion()[0].getTowerLayers()[0].setCard(card);
        actionHandler.handleTower(player, towerAction);
        verify(card, times(2)).getPermanentEffect();
        verify(game).takeCard(board, player, towerAction);
    }
    
    @Test
    public void checkHandleProduction(){
        player.setPersonalBonusTile(new PersonalBonusTile(new JsonObject(),
                                                          new JsonObject(),
                                                          false));
        JsonArray choiceArray = new JsonArray();
        choiceArray.add(0).add('n').add(0);
        when(contextManager.waitForContextReply()).thenReturn(new JsonObject()
                                                              .add("CHOSEN_SERVANTS", 5),
                                                              new JsonObject()
                                                              .add("CHANGEIDARRAY", choiceArray));
        Effect effect1 = mock(Effect.class);
        Effect effect2 = mock(Effect.class);
        Effect effect3 = mock(Effect.class);
        DevelopmentCard card1 = new DevelopmentCard("Test1", 1, "BUILDINGCARD", 2);
        DevelopmentCard card2 = new DevelopmentCard("Test1", 1, "BUILDINGCARD", 2);
        DevelopmentCard card3 = new DevelopmentCard("Test1", 1, "BUILDINGCARD", 2);
        card1.registerPermanentEffect(effect1);
        card1.registerPermanentEffectType("CHANGE");
        card1.addPayload(new JsonObject());
        card2.registerPermanentEffect(effect2);
        card2.registerPermanentEffectType("CHANGE");
        card2.addPayload(new JsonObject());
        card3.registerPermanentEffect(effect3);
        card3.registerPermanentEffectType("CHANGE");
        card3.addPayload(new JsonObject());
        player.getPersonalBoard().addCard(card1);
        player.getPersonalBoard().addCard(card2);
        player.getPersonalBoard().addCard(card3);
        Action productionAction = new Action("PRODUCTION", 3, 0, 0);
        ArgumentCaptor<Action> captor = ArgumentCaptor.forClass(Action.class);
        productionAction.setAdditionalInfo(new JsonObject());
        actionHandler.handleProduction(player, productionAction);
        verify(effect1).apply(eq(board), eq(player), captor.capture(), eq(contextManager));
        verify(effect3).apply(eq(board), eq(player), captor.capture(), eq(contextManager));
        verify(effect2).apply(eq(board), eq(player), captor.capture(), eq(contextManager));
        for(Action genAction: captor.getAllValues()){
            assertEquals(Json.value(0), genAction.getAdditionalInfo().get("CHANGEID"));
        }
    }
    
    @Test
    public void checkHandleActionEffect(){
        MoveChecker moveChecker = mock(MoveChecker.class);
        when(game.getMoveChecker()).thenReturn(moveChecker);
        MessageHandler messageHandler = mock(MessageHandler.class);
        when(game.getMessageHandler()).thenReturn(messageHandler);
        Action action = new Action("TEST", 3, 0, 0);
        action.setAdditionalInfo(new JsonObject().add("BONUSFLAG", true));
        when(moveChecker.checkMove(eq(game), eq(player), any(Action.class), eq(contextManager))).thenReturn(false, true);
        when(contextManager.waitForContextReply()).thenReturn(new JsonObject());
        when(messageHandler.parseASKACT(eq(player.getUUID()), any(JsonObject.class))).thenReturn(action);
        actionHandler.handleActionEffect(player, action, mock(Effect.class), new JsonObject());
        verify(messageHandler, times(2)).parseASKACT(eq(player.getUUID()), any(JsonObject.class));
        verify(game).makeMove(player, action);
    }
}