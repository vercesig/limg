package it.polimi.ingsw.GC_32.Server.Game;

import java.util.LinkedList;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

/**
 * ContextManager is responsable of handling the contexts. Contexts are the way to ask the client more information on the action he wants to perform. For example if the 
 * player wants perform a PRODUCTION or an HARVEST action a context is open to ask how many servants the player wants to spend, or if a PRIVILEGE must be spent a context
 * is opened to allow the player to choose what resources he wants gain from his councile privilege. 
 * Context are also prompt when CHANGE effects or ACTION effects are triggered by the activation of permanent effects. Context are used during the excommunication
 * phase as well, when the server must ask if the player wants show his faith to the pope or not.
 * 
 *<ul>
 *<li>{@link #game}: the game this context manager must handle</li>
 *<li>{@link #payloadReplayQueue}: a queue of JsonValue which memorize all the responses taken by contexts</li>
 *<li>{@link #pendingMessage}: the recived message</li>
 *<li>{@link #waitingContextResponse}: the type of context that ContextManager his waiting</li>
 *</ul>
 *
 *@see Game, GameMessage, ContextType, Player, Action, ServerMessageFactory
 *
 */
public class ContextManager{

	private String waitingContextResponse;
	private LinkedList<JsonValue> payloadReplayQueue;
	private GameMessage pendingMessage;
	private Game game;
	
	/**
	 * initialize the manager to handle contexts for the spcified game
	 * @param game the game the context manager must handle
	 */
	public ContextManager(Game game){
		this.waitingContextResponse = null;
		this.payloadReplayQueue = new LinkedList<>();
		this.game = game;
	}

	/**
	 * open a context. When openContext() is called a CONTEXT message is sent to the client with all the information needed to customize the context. finally the
	 * waitingContextResponse is setted on the type of context the ContextManager is waiting for.
	 * 
	 * @param context the type of context to open
	 * @param player the recipient of the message
	 * @param action the action which has triggered the opening of the context
	 * @param extraInfo the info needed to customize the context
	 */
	public void openContext(ContextType context, Player player, Action action, JsonValue extraInfo){
		switch(context){
			case SERVANT:
				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
						this.game,
						player, 
						ContextType.SERVANT, 
						player.getResources().getResource("SERVANTS"),action.getActionType()));
				waitingContextResponse = "SERVANT";
				break;
			case CHANGE:
				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
						this.game,
						player,
						ContextType.CHANGE,
						extraInfo.asArray().get(0).asArray(), // see ServerMessageFactory
						extraInfo.asArray().get(1).asArray()
						));
				waitingContextResponse = "CHANGE";
				break;
			case PRIVILEGE:
				if(extraInfo.isArray()){ // privilege with cost
				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
						this.game,
						player,
						ContextType.PRIVILEGE,
						extraInfo.asArray().get(0).asInt(), // number of privilege to consume
						extraInfo.asArray().get(1).asObject())); // cost of the privilege
				}else{
				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
						this.game,
						player,
						ContextType.PRIVILEGE,
						extraInfo.asInt())); // number of privilege to consume
				}
				waitingContextResponse = "PRIVILEGE";
				break;
			case ACTION:
				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
						this.game,
						player,
						ContextType.ACTION,
						extraInfo));
				waitingContextResponse = "ACTION";
				break;
			case EXCOMMUNICATION:
				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(
						this.game, 
						player, 
						ContextType.EXCOMMUNICATION,
						extraInfo.asArray().get(0).asInt(), //excommunicationlevel
						extraInfo.asArray().get(1).asInt()));
				waitingContextResponse = "EXCOMMUNICATION";
				break;
			default:
				break;
		}
	}
	
	/**
	 * tells if the ContextManager is waiting for a response
	 * @return true if there is an open context, false otherwise
	 */
	public boolean isThereAnyOpenContext(){
		return waitingContextResponse != null;
	}
	
	/**
	 * allows to retrive the content of the waitingContextResponse string
	 * @return the type of context the ContextManager is waiting for
	 */
	public String getWaitContextResponse(){
		return this.waitingContextResponse;
	}
	
	/**
	 * blocking method which await for a CONTEXTREPLY message, if the CONTEXT_TYPE field is equals to the value of waitingContextResponse string the response is accepted
	 * and the response is returned to the caller. null is returned if ContextManager is no context are opened.
	 * @return the context response, as JsonValue
	 */
	public JsonValue waitForContextReply(){
		GameMessage message = null;
		if(isThereAnyOpenContext()){ // non attendere se non ci sono context aperti
			System.out.println("sto attendendo... "+this.waitingContextResponse);
			while(true){
				try{
					message = MessageManager.getInstance().getQueueForGame(game.getUUID()).take();
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				if(message != null && "CONTEXTREPLY".equals(message.getOpcode())){
					JsonValue contextReply = message.getMessage();
					String contextType = contextReply.asObject().get("CONTEXT_TYPE").asString();
					if(waitingContextResponse.equals(contextType)){
						this.pendingMessage = message;
						JsonValue returnValue = contextReply.asObject().get("PAYLOAD");
						return returnValue;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * send the CONTEXTACK response to the client
	 * @param accepted flag that tells the client if the context response has been assessed as valid or not
	 * @param player the recipient of the message
	 */
	public void setContextAck(boolean accepted, Player player){
		if(accepted){
			this.waitingContextResponse = null;
			if(this.pendingMessage != null)
				this.payloadReplayQueue.add(this.pendingMessage.getMessage());
			this.pendingMessage = null;
		}
		MessageManager.getInstance()
					  .sendMessge(ServerMessageFactory.buildCONTEXTACKMessage(this.game,
								  											  player,
								  											  accepted));
	}
}
