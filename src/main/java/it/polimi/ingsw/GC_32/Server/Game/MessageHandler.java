package it.polimi.ingsw.GC_32.Server.Game;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

public class MessageHandler{
    private static Logger LOGGER = Logger.getLogger(MessageHandler.class.toString());
    private Game game;
    private TurnManager turnManager;
    private Board board;
    private HashMap<UUID, Action> memoryAction;
    
    public MessageHandler(Game game){
        this.game = game;
        this.turnManager = game.getTurnManager();
        this.board = game.getBoard();
        this.memoryAction = new HashMap<>();
    }

    public void handleMessage(GameMessage message){
        JsonObject jsonMessage = message.getMessage().asObject();
        switch(message.getOpcode()){
            case "ASKACT":
                LOGGER.log(Level.INFO, "processing ASKACT message from ", message.getPlayerID());
                handleASKACT(message, jsonMessage);
                break;
            case "ASKLDRACT":
                handleASKLDRACT(message, jsonMessage);
                break;
            case "TRNEND":
                handleTRNEND(message);
                break;
            default:
                break;
        }
    }
    
    protected void handleASKACT(GameMessage message, JsonObject jsonMessage){
        int index = game.getPlayerList().indexOf(GameRegistry.getInstance().getPlayerFromID(message.getPlayerUUID())); 
        Player player = game.getPlayerList().get(index);
        Action action = parseASKACT(player.getUUID(), jsonMessage);
        memoryAction.put(player.getUUID(), action);
        
        LOGGER.info("INIZIO CHECK: ");
        LOGGER.info("STATO PRIMA DELL'ESECUZIONE:");
        
        System.out.println("inizion check, stato prima esecuzione\n"+action.toString()+"\n"+player.toString());
        
        LOGGER.info(action::toString);
        LOGGER.info(player::toString);
        if(game.getMoveChecker().checkMove(game, player, action, game.getContextManager())){
            LOGGER.info("check with copy: PASSATO");
            game.makeMove(player, action);
            LOGGER.info("AZIONE ESEGUITA!\n");
            LOGGER.info("STATO DOPO AZIONE: ");
            LOGGER.info(player::toString);
            
            // notifiche server
            MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACTCHKmessage(game, player, action, true));
        } else {
            MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACTCHKmessage(game, player, action, false));
        }
    }
        
    protected void handleASKLDRACT(GameMessage message, JsonObject jsonMessage){
        String cardName = jsonMessage.get("LEADERCARD").asString();
        String decision = jsonMessage.get("DECISION").asString();
        LOGGER.log(Level.INFO, "LEADERCARD: %s", cardName);
        LOGGER.log(Level.INFO, "DECISION: %s", decision);
        Player p = GameRegistry.getInstance().getPlayerFromID(message.getPlayerUUID());
        boolean result;
        if(LeaderUtils.checkLeaderMove(p.getUUID(), cardName, decision)){
            LOGGER.info("ATTIVATO!");
            result = true;
            if("DISCARD".equals(decision)){ //GUADAGNA UN PRIVILEGIO
                game.getContextManager().openContext(ContextType.PRIVILEGE, p, null, Json.value(1));
                JsonValue COUNCILPRIVILEGEresponse = game.getContextManager().waitForContextReply();
                
                LOGGER.info("PRIMA DEL PRIVILEGE:\n" + GameRegistry.getInstance().getPlayerFromID(game.getLock()));
                GameRegistry.getInstance().getPlayerFromID(game.getLock()).getResources().addResource("COINS", 1);
                GameRegistry.getInstance().getPlayerFromID(game.getLock()).getResources().addResource( new ResourceSet(Json.parse(COUNCILPRIVILEGEresponse.asArray().get(0).asString()).asObject()));
                LOGGER.info("DOPO DEL PRIVILEGE:\n" + GameRegistry.getInstance().getPlayerFromID(game.getLock()));
            }
        }
        else{
            LOGGER.info("QUALCOSA NON VA!\n NON PUOI ATTIVARE QUESTA AZIONE LEADER!\n");
            result = false;
        }
        MessageManager.getInstance()
                      .sendMessge(ServerMessageFactory
                                  .buildASKLDRACKmessage(game, p, cardName, decision, result));      
    }
    
    protected void handleTRNEND(GameMessage message){
        MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCHGBOARDSTATmessage(game, board));
        MessageManager.getInstance().sendMessge(ServerMessageFactory
                                                .buildSTATCHNGmessage(game, 
                                                                      GameRegistry.getInstance()
                                                                                  .getPlayerFromID(game.getLock())));
        if(memoryAction.get(game.getLock())!=null){
            MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCHGBOARDSTATmessage(game,
                                                                                                  game.getLock().toString(), 
                                                                                                  memoryAction.get(game.getLock())));
        }
        try{ // wait for TRNBGN message
            Thread.sleep(500);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        
        memoryAction.remove(game.getLock());
        
        LOGGER.info("ricevo turn end [GAME]");
        LOGGER.log(Level.INFO, "ROUND ID: %s", turnManager.getRoundID());
        LOGGER.log(Level.INFO, "PERIOD ID: %s", turnManager.getPeriod());
        LOGGER.log(Level.INFO, "TURN ID: %s", turnManager.getTurnID());
        
        if(turnManager.isGameEnd()){
            LOGGER.log(Level.INFO, "Game end");
            game.getEndPhaseHandler().endGame();
            return;
        }
   
        LOGGER.log(Level.INFO, message.getPlayerID()+" has terminated his turn");
        if(turnManager.isRoundEnd()){ // cambio round
            LOGGER.info("ROUND FINITO!");
            LOGGER.log(Level.INFO, "round end");
            if(turnManager.isPeriodEnd()){ //cambio periodo
                LOGGER.info("PERIODO FINITO!");
                turnManager.distributeVaticanReport();
            }       
            turnManager.setToUpdate(true);
        }   
        try { // wait for TRNBGN message
            Thread.sleep(500);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
                
        if(turnManager.doesPopeWantToSeeYou()){ 
        	
        	LOGGER.log(Level.INFO, "period "+(turnManager.getPeriod()-1) + " finished");
			int excommunicationLevel = 3 + turnManager.getPeriod()-2; //calcolo punti fede richiesti 	
        	
        	game.getPlayerList().forEach(excommPlayer -> {
				JsonArray excommPayload = new JsonArray();
				excommPayload.add(excommunicationLevel);
				excommPayload.add(excommPlayer.getResources().getResource("FAITH_POINTS"));
				
				game.getContextManager().openContext(ContextType.EXCOMMUNICATION, excommPlayer, null, excommPayload);
				JsonObject excommMessage = game.getContextManager().waitForContextReply().asObject();
				game.getContextManager().setContextAck(true, excommPlayer);
								
				boolean answer = excommMessage.get("ANSWER").asBoolean();
				int playerIndex= game.getPlayerList().indexOf(excommPlayer); 
				if(answer){ 
					//ATTIVAZIONE CARTA SCOMUNICA
					LOGGER.info("FIGLIOLO...IL PAPA TI HA SCOMUNICATO, MI SPIACE");
					ExcommunicationCard card = game.getExcommunicationCard(this.turnManager.getPeriod() - 1); // periodi sono shiftati di 1
					LOGGER.log(Level.INFO, "Attivo effetto carta: %s", card.getName());
					
					if(!card.getInstantEffect().isEmpty()){
						card.getInstantEffect().get(0).apply(game.getBoard(), excommPlayer, null, null);
					}
					else 
						LOGGER.info("Non ha effetti instantanei!");
					if(!card.getPermanentEffect().isEmpty()){
						excommPlayer.addEffect(card.getPermanentEffect().get(0));
						System.out.println("------------------------- aggiunto effetto scomunica al player");
					}
					else
						LOGGER.info("Non ha effetti permanenti!");
				}	
				else{
					LOGGER.info("Sostegno alla Chiesa!");
					int faithScore = game.getPlayerList().get(playerIndex).getResources().getResource("FAITH_POINTS");	
					LOGGER.log(Level.INFO, "Punti Fede Giocatore: %d", faithScore);
					
					game.getPlayerList().get(playerIndex).getResources().setResource("FAITH_POINTS", 0); //azzera punteggio player
					int victoryPointsConverted = 0;
					
					if(GameConfig.getInstance().getExcommunicationTrack().get(faithScore) != null){
						victoryPointsConverted += GameConfig.getInstance().getExcommunicationTrack().get(faithScore);
					}
					else
						victoryPointsConverted = faithScore*2; // caso faithPoints > 15
					
					if(game.getPlayerList().get(playerIndex).isFlagged("MOREFAITH")){  // Sisto IV
						victoryPointsConverted += 5;
					}
					LOGGER.log(Level.INFO, "Punti Vittoria convertiti Giocatore: %d", victoryPointsConverted);
					game.getPlayerList().get(playerIndex).getResources().addResource("VICTORY_POINTS", victoryPointsConverted);
				}
				turnManager.goodbyePope();
			});	
        }
        
        if(turnManager.isToUpdate() && !turnManager.doesPopeWantToSeeYou()){
            
            // reset board
            board.flushBoard();
            board.placeCards(game);
            game.diceRoll();
            turnManager.setToUpdate(false);
            
            MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCHGBOARDSTATmessage(game, game.getBoard()));
            MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCHGBOARDSTATmessage(game, true));
            game.getPlayerList().forEach(gamePlayer ->
                MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(game, gamePlayer))
            );
            game.sendDICEROLL();
            
            try{ // wait for TRNBGN message
                Thread.sleep(500);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt(); 
            }
        }   
        
        LOGGER.log(Level.INFO, "giving lock to the next player");
        UUID nextPlayer = turnManager.nextPlayer();
        
        game.setLock(nextPlayer);
        LOGGER.log(Level.INFO, "player %s has the lock", game.getLock());
        MessageManager.getInstance().sendMessge(ServerMessageFactory.buildTRNBGNmessage(game, game.getLock()));
    }
    
    public Action parseASKACT(UUID playerID, JsonObject jsonMessage){
        int pawnID = jsonMessage.get("FAMILYMEMBER_ID").asInt();
        int actionValue = GameRegistry.getInstance().getPlayerFromID(playerID)
                                                    .getFamilyMember()[pawnID].getActionValue();

        int regionID = jsonMessage.get("REGIONID").asInt();
        int spaceID = jsonMessage.get("SPACEID").asInt();
        String actionType = jsonMessage.get("ACTIONTYPE").asString();

        Action action = new Action(actionType,actionValue, spaceID, regionID);
        action.setAdditionalInfo(new JsonObject().add("FAMILYMEMBER_ID", jsonMessage.get("FAMILYMEMBER_ID").asInt()));
        action.getAdditionalInfo().add("COSTINDEX", jsonMessage.get("COSTINDEX").asInt()); // Cost Index
        action.getAdditionalInfo().add("CARDNAME", jsonMessage.get("CARDNAME").asString());
        action.getAdditionalInfo().add("BONUSFLAG", Json.value(false));
        return action;
    }
}