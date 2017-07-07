package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

public class ActionHandler{
    private Logger LOGGER = Logger.getLogger(this.getClass().toString());
    
    private Game game;
    private ContextManager contextManager;
    private Board board;
    
    public ActionHandler(Game game){
        this.game = game;
        this.contextManager = game.getContextManager();
        this.board = game.getBoard();
    }
    
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
    
    public void handleHarvest(Player player, Action action){
        player.getResources().addResource(player.getPersonalBonusTile().getPersonalHarvestBonus()); 
        contextManager.openContext(ContextType.SERVANT, player, action, null);
        
        JsonValue SERVANTHarvestresponse = contextManager.waitForContextReply();
        contextManager.setContextAck(true, player);
        action.setActionValue(action.getActionValue() + SERVANTHarvestresponse.asObject().get("CHOOSEN_SERVANTS").asInt());
        
        player.getPersonalBoard().getCardsOfType("TERRITORYCARD").forEach(card -> {
            if(card.getMinimumActionvalue() <= action.getActionValue()){ 
                card.getPermanentEffect().forEach(effect -> effect.apply(board, player, action, contextManager));
            }
        });
    }
    
    public void handleCouncil(Player player, Action action){
        contextManager.openContext(ContextType.PRIVILEGE, player, action, Json.value(1));
        JsonValue COUNCILPRIVILEGEresponse = contextManager.waitForContextReply();
        contextManager.setContextAck(true, player);
        
        LOGGER.log(Level.INFO, "PRIMA DEL PRIVILEGE:\n%s", player);
        player.getResources().addResource("COINS", 1);
        player.getResources().addResource( new ResourceSet(Json.parse(COUNCILPRIVILEGEresponse.asArray().get(0).asString()).asObject()));
        LOGGER.log(Level.INFO, "DOPO DEL PRIVILEGE:\n%s", player);
    }
    
    public void handleMarket(Player player, Action action){
    	if(action.getActionSpaceId() == 3){
			game.getContextManager().openContext(ContextType.PRIVILEGE, player, action, Json.value(2));
			JsonValue MARKETPRIVILEGEresponse = game.getContextManager().waitForContextReply();
			game.getContextManager().setContextAck(true, player);
			
			player.getResources().addResource( new ResourceSet(Json.parse(MARKETPRIVILEGEresponse.asArray().get(0).asString()).asObject()));
			player.getResources().addResource( new ResourceSet(Json.parse(MARKETPRIVILEGEresponse.asArray().get(1).asString()).asObject()));
		}
    }
    
    public void handleActionEffect(Player player, Action action, Effect effect, JsonValue effectAction){
        contextManager.setContextAck(true, player);
        Action bonusAction = game.getMessageHandler().parseASKACT(player.getUUID(), effectAction.asObject());                       
        Player bonusPlayer = player;
        
        while(!game.getMoveChecker().checkMove(game, bonusPlayer, bonusAction, contextManager)){ // se l'azione non è valida 
            System.out.println("ritento");
            MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACTCHKmessage(game, bonusPlayer, bonusAction, false));                                
            effect.apply(board, player, action, contextManager);
            effectAction = contextManager.waitForContextReply();
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