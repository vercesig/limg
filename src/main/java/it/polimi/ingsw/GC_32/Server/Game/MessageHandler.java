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
import it.polimi.ingsw.GC_32.Common.Utils.Utils;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Card.CardRegistry;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.LeaderCard;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

/**
 * class responsable of handling all the messages received, when Game recive a new message it is passed to one of the MessageHandler methods which will manage the 
 * processing of the message
 * 
 * <ul>
 * <li>{@link #game}: the game handled by this instance of MessageHandler</li>
 * <li>{@link #board}: the board of the game</li>
 * <li>{@link #turnManager}: the turn manager of the game, required by the processing of TRNEND messages</li>
 * <li>{@link #memoryAction}: HashMap which can be used to memorize the status of an action</li>
 * </ul>
 *
 * @See TurnManager, Game, Board, Action
 */

public class MessageHandler{
    private static Logger LOGGER = Logger.getLogger(MessageHandler.class.toString());
    private Game game;
    private TurnManager turnManager;
    private Board board;
    private HashMap<UUID, Action> memoryAction;
    
    /**
     * setup the MessageHandler, initializing the memory structures and assigning the game to handle
     * @param game the game to handle
     */
    public MessageHandler(Game game){
        this.game = game;
        this.turnManager = game.getTurnManager();
        this.board = game.getBoard();
        this.memoryAction = new HashMap<>();
    }

    /**
     * given a message, based on its opcode, call the correct routine to process that message
     * @param message the message to process
     */
    public void handleMessage(GameMessage message){
        JsonObject jsonMessage = message.getMessage().asObject();
        switch(message.getOpcode()){
            case "ASKACT":
                LOGGER.log(Level.INFO, "processing ASKACT message from ", message.getPlayerID());
                if(!jsonMessage.asObject().getBoolean("NULLACTION",false)) // se l'azione bonus viene annulata non deve essere lanciato handleASKACT
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
    
    /**
     * process ASKACT messages, building the action from the message and then passing it to the moveChecker, if the action is valid it is finally applied to the real 
     * state of the game
     * @param message the message containing the ASKACT payload, is taken directly from the MessageManager
     * @param jsonMessage the JsonObject containing the ASKACT message
     */
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
    
    /**
     * process ASKLDRACT messages, appling the correct logic based on the choose of the client
     * @param message the message containing the ASKLDRACT payload, is taken directly from the MessageManager
     * @param jsonMessage the JsonObject containing the ASKLDRACT message
     */
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
                LeaderCard card = null;
                for (LeaderCard leader: p.getPersonalBoard().getLeaderCards()){
                	if(leader.getName().equals(cardName)){
                		card = leader;
                	}
                }
                GameRegistry.getInstance().getPlayerFromID(game.getLock()).getPersonalBoard().getLeaderCards().remove(card);
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
    
    
    /**
     * process TRNEND messages, calling the correct routines of the TurnManager instance. Turn manager will pass the lock to the next player in the turn order queue
     * and if the condtions are satisfied the escommunication phase is handled. When the game is end, the reception of a TRNEND messag trigger the activation of
     * the EndPhaseHandler routine to compute the final score
     * @param message the message containing the TRNEND payload, is taken directly from the MessageManager
     * @param jsonMessage the JsonObject containing the TRNEND message
     */
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
                
        System.out.println("ROUND ID :"+ turnManager.getRoundID());
        System.out.println("period ID :"+ turnManager.getPeriod());
        System.out.println("turn ID :"+ turnManager.getTurnID());
        
        if(turnManager.isRoundEnd()){ // cambio round
            if(turnManager.isPeriodEnd()){ //cambio periodo
                LOGGER.info("PERIODO FINITO!");
                turnManager.distributeVaticanReport();
            }       
            turnManager.setToUpdate(true);
        }
         // wait for TRNBGN message
         Utils.safeSleep(500);
                
        if(turnManager.doesPopeWantToSeeYou()){ 
        	
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
					ExcommunicationCard card = game.getExcommunicationCard(this.turnManager.getPeriod() - 1); // periodi sono shiftati di 1
					
					if(!card.getInstantEffect().isEmpty()){
						card.getInstantEffect().get(0).apply(game.getBoard(), excommPlayer, null, null);
					}
					
					if(!card.getPermanentEffect().isEmpty()){
						excommPlayer.addEffect(card.getPermanentEffect().get(0));
						}
				}
				else{
					int faithScore = game.getPlayerList().get(playerIndex).getResources().getResource("FAITH_POINTS");						
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
					game.getPlayerList().get(playerIndex).getResources().addResource("VICTORY_POINTS", victoryPointsConverted);
				}
				turnManager.goodbyePope();
			});	
        }
        
        System.out.println("g ROUND ID :"+ turnManager.getRoundID());
        System.out.println("g period ID :"+ turnManager.getPeriod());
        System.out.println("g turn ID :"+ turnManager.getTurnID());
        
        if(turnManager.isGameEnd()){
            LOGGER.log(Level.INFO, "Game end");
            game.getEndPhaseHandler().endGame();
            return;
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
    
        /**
         * given an ASKACT message this routine perform the parsing to build the action from the JSON message recived
         * @param playerID the player who has sent the ASKACT message
         * @param jsonMessage JsonObject containing the ASKACT message information
         * @return the action build from the ASKACT message passed as argument
         */
    public Action parseASKACT(UUID playerID, JsonObject jsonMessage){
        JsonValue pawnID = jsonMessage.get("FAMILYMEMBER_ID");
        int actionValue;
        boolean isStandardMove = (pawnID != null);
        if(isStandardMove){
            actionValue = GameRegistry.getInstance().getPlayerFromID(playerID)
                                                    .getFamilyMember()[pawnID.asInt()]
                                                    .getActionValue();
        } else {
            actionValue = jsonMessage.asObject().get("BONUSACTIONVALUE").asInt();
        }

        int regionID = jsonMessage.get("REGIONID").asInt();
        int spaceID = jsonMessage.get("SPACEID").asInt();
        String actionType = jsonMessage.get("ACTIONTYPE").asString();

        Action action = new Action(actionType,actionValue, spaceID, regionID);
        action.setAdditionalInfo(new JsonObject());
        action.getAdditionalInfo().add("COSTINDEX", jsonMessage.get("COSTINDEX").asInt()); // Cost Index
        action.getAdditionalInfo().add("BONUSFLAG", Json.value(false));
        if(isStandardMove){
            action.getAdditionalInfo().add("FAMILYMEMBER_ID", jsonMessage.get("FAMILYMEMBER_ID").asInt());
            action.getAdditionalInfo().add("CARDNAME", jsonMessage.get("CARDNAME").asString());
        } else {
            action.getAdditionalInfo().add("BONUSFLAG", Json.value(true));
        }
        return action;
    }
}