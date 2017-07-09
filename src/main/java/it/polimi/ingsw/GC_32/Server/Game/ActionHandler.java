package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

/**
 * class which is responsable of the handling of all the action performed by the clients. Is used by the Game object to perform the actions, after an ASKACT message
 * has been received. 
 * 
 * the action which must be handled are of 5 different types:
 * 
 * <ul>
 * <li>{@link #handleTower(Player, Action)}: handle all the action performed on tower regions</li>
 * <li>{@link #handleCouncil(Player, Action)}: handle all the action performed on the council region</li>
 * <li>{@link #handleHarvest(Player, Action)}: handle all the action performed on the harvest region</li>
 * <li>{@link #handleProduction(Player, Action)}: handle all the action performed on the production regions</li>
 * <li>{@link #handleMarket(Player, Action)}: handle all the action performed on the market region</li>
 * <li>{@link #handleActionEffect(Player, Action, Effect, JsonValue)}: handle all the action which has been triggered by an ACTION effect of one card</li>
 * </ul>
 * 
 * attributes of ActionHandler class
 * 
 * <ul>
 * <li>{@link #board}: the board of the game</li>
 * <li>{@link #game}: the Game object needed from ActionHandler to perform actions</li>
 * <li>{@link #contextManager}: context manager is used to open context and handle their responses</il>
 * </ul>
 * 
 * @see Game, ContextManager, Board, TowerRegion, Player, Action, DevelopmentCard, Effect, ResourceSet, MessageManager, ServerMessageFactory
 *
 */
public class ActionHandler{
    private Logger LOGGER = Logger.getLogger(this.getClass().toString());
    
    private Game game;
    private ContextManager contextManager;
    private Board board;
    
    /**
     * initialize the ActionHandler whit the game this instance of ActionHandler has to handle
     * @param game the game this ActionHandler must manage
     */
    public ActionHandler(Game game){
        this.game = game;
        this.contextManager = game.getContextManager();
        this.board = game.getBoard();
    }
    
    /**
     * method used to handle TOWER action. this method take the card from the board, add the permanent effect of CHARACHTER cards to the player list of effects and then apply
     * the instant effect of the card taken, if the card has an ACTION effect handleActionEffect() is called
     * 
     * @param player the player who has maden the action
     * @param action the action maden by the player
     */
    public void handleTower(Player player, Action action){
        TowerRegion selectedTower = (TowerRegion)(board.getRegion(action.getRegionId()));
        DevelopmentCard card = selectedTower.getTowerLayers()[action.getActionSpaceId()].getCard();
        game.takeCard(board, player, action);
        
        if("CHARACTERCARD".equals(card.getType()) && card.getPermanentEffect()!= null){
            card.getPermanentEffect().forEach(effect -> player.addEffect(effect));
            LOGGER.info("AGGIUNTO EFFETTO PERMANENTE");
        }
        if(!card.getInstantEffect().isEmpty()){
            card.getInstantEffect().forEach(effect -> {
                effect.apply(board, player, action, contextManager); 
                // only ACTION effect doesn't close the context               
                JsonValue effectAction = contextManager.waitForContextReply();

                if(effectAction!=null){
                    handleActionEffect(player, action, effect, effectAction);
                }
            });
        }
    }
    
    /**
     * method used to handle PRODUCTION action. this method add the bonus of the bonus tile of the player and then apply all the permanent effects of BUILDING cards
     * owned by the player. If there are cards which have a CHANGE effect as permanent effect a context is opened on the client screen and after the response has been
     * received the changes are performed
     * 
     * @param player the player who has maden the action
     * @param action the action maden by the player
     */
    
    public void handleProduction(Player player, Action action){
        player.getResources().addResource(player.getPersonalBonusTile().getPersonalProductionBonus()); 
        contextManager.openContext(ContextType.SERVANT, player, action, null);
        
        JsonValue SERVANTProductionresponse = contextManager.waitForContextReply();
        contextManager.setContextAck(true, player);
        action.setActionValue(action.getActionValue() +
                              SERVANTProductionresponse.asObject().get("CHOSEN_SERVANTS").asInt());
        
        JsonArray CHANGEcontextPayload = new JsonArray();
        JsonArray CHANGEnameCardArray = new JsonArray();
        
        ArrayList<DevelopmentCard> CHANGEeffectCardList = new ArrayList<DevelopmentCard>();
        ArrayList<DevelopmentCard> notCHANGEeffectCardList = new ArrayList<DevelopmentCard>();
        
        player.getPersonalBoard().getCardsOfType("BUILDINGCARD").forEach(card -> {
            if(card.getMinimumActionvalue() <= action.getActionValue()){ 
                // cards with CHANGE effect
                if(card.getPermanentEffectType().contains("CHANGE")){
                    CHANGEeffectCardList.add(card);
                    card.getPayloadInfo().forEach(payload -> {
                        CHANGEcontextPayload.add(payload);
                        CHANGEnameCardArray.add(card.getName());
                    });
                }else{
                    notCHANGEeffectCardList.add(card);
                }
            }
        });
        JsonArray CHANGEpacket = new JsonArray();
        CHANGEpacket.add(CHANGEnameCardArray);
        CHANGEpacket.add(CHANGEcontextPayload);
        
        // c'è almeno una carta con effetto CHANGE
        if(!CHANGEeffectCardList.isEmpty()){
            contextManager.openContext(ContextType.CHANGE, player, action, CHANGEpacket);
            
            JsonArray indexResponse = contextManager.waitForContextReply().asObject().get("CHANGEIDARRAY").asArray();
            for(int i = 0; i < CHANGEeffectCardList.size(); i++){
                action.getAdditionalInfo().set("CHANGEID", indexResponse.get(i));
                CHANGEeffectCardList.get(i).getPermanentEffect().forEach(effect -> effect.apply(board, player, action, contextManager));
            }
        }
        notCHANGEeffectCardList.forEach(card -> { 
            card.getPermanentEffect().forEach(effect ->
                effect.apply(board, player, action, contextManager)
            );
        });
    }
    
    /**
     * method used to handle HARVEST action. this method add the bonus of the bonus tile of the player and then apply all the permanent effects of TERRITORY cards
     * owned by the player.
     * 
     * @param player the player who has maden the action
     * @param action the action maden by the player
     */
    public void handleHarvest(Player player, Action action){
        player.getResources().addResource(player.getPersonalBonusTile().getPersonalHarvestBonus()); 
        contextManager.openContext(ContextType.SERVANT, player, action, null);
        
        JsonValue SERVANTHarvestresponse = contextManager.waitForContextReply();
        contextManager.setContextAck(true, player);
        action.setActionValue(action.getActionValue() + SERVANTHarvestresponse.asObject().get("CHOSEN_SERVANTS").asInt());
        
        player.getPersonalBoard().getCardsOfType("TERRITORYCARD").forEach(card -> {
            if(card.getMinimumActionvalue() <= action.getActionValue()){ 
                card.getPermanentEffect().forEach(effect -> effect.apply(board, player, action, contextManager));
            }
        });
    }
    
    /**
     * method used to handle COUNCIL action. this method open a context on the client screen to allow the player to choose what resources he want gain from his
     * council privilege. after the response has been received the method finally apply the change
     * 
     * @param player the player who has maden the action
     * @param action the action maden by the player
     */
    public void handleCouncil(Player player, Action action){
        contextManager.openContext(ContextType.PRIVILEGE, player, action, Json.value(1));
        JsonValue COUNCILPRIVILEGEresponse = contextManager.waitForContextReply();
        contextManager.setContextAck(true, player);
        
        LOGGER.log(Level.INFO, "PRIMA DEL PRIVILEGE:\n%s", player);
        player.getResources().addResource("COINS", 1);
        player.getResources().addResource( new ResourceSet(Json.parse(COUNCILPRIVILEGEresponse.asArray().get(0).asString()).asObject()));
        LOGGER.log(Level.INFO, "DOPO DEL PRIVILEGE:\n%s", player);
    }
    
    /**
     * method used to handle MARKET action. if the action has been performed on the action space with ID 3 of the market region, a context is open on the client screen
     * to allow the player choose the resources he wants gain from his two council privilges
     * 
     * @param player the player who has maden the action
     * @param action the action maden by the player
     */
    public void handleMarket(Player player, Action action){
    	if(action.getActionSpaceId() == 3){
			game.getContextManager().openContext(ContextType.PRIVILEGE, player, action, Json.value(2));
			JsonValue MARKETPRIVILEGEresponse = game.getContextManager().waitForContextReply();
			game.getContextManager().setContextAck(true, player);
			
			player.getResources().addResource( new ResourceSet(Json.parse(MARKETPRIVILEGEresponse.asArray().get(0).asString()).asObject()));
			player.getResources().addResource( new ResourceSet(Json.parse(MARKETPRIVILEGEresponse.asArray().get(1).asString()).asObject()));
		}
    }
    
    /**
     * method used to handle ACTION effect action, i.e. action triggered by the activation of an effect which allow to make another action without placing a family member.
     * handleActionEffect() open an ActionEffectContext on the client screen and, as long as the response represent a non valid action the server continue to open
     * context to allow the client to perform his bonus action. the action can be annulled if the player can't perform any action or he doesn't want to perform it. 
     * 
     * @param player the player who has maden the action
     * @param action the action maden by the player
     * @param effect the effect which has triggered the bonus action
     * @param effectAction a JsonValue used to handle the context response
     */
    public void handleActionEffect(Player player, Action action, Effect effect, JsonValue jEffect){
        contextManager.setContextAck(true, player);
        JsonObject effectAction = jEffect.asObject();
        Action bonusAction = game.getMessageHandler().parseASKACT(player.getUUID(), effectAction);                       
        Player bonusPlayer = player;
        
        while(!game.getMoveChecker().checkMove(game, bonusPlayer, bonusAction, contextManager)){ // se l'azione non è valida 
            MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACTCHKmessage(game, bonusPlayer, bonusAction, false));                                
            effect.apply(board, player, action, contextManager);
            effectAction = contextManager.waitForContextReply().asObject();
            contextManager.setContextAck(true, player);                             
            if(effectAction != null){
                bonusAction = game.getMessageHandler().parseASKACT(player.getUUID(), effectAction.asObject());
            }                               
        } 
        game.makeMove(bonusPlayer, bonusAction);
        // notifiche server
        MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACTCHKmessage(game, bonusPlayer, bonusAction, true));
    }
}
