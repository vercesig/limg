package it.polimi.ingsw.GC_32.Server.Game;

import java.util.LinkedList;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

public class ContextManager{

	private String waitingContextResponse;
	private LinkedList<JsonValue> payloadReplayQueue;
	private GameMessage pendingMessage;
	private Game game;
	
	public ContextManager(Game game){
		this.waitingContextResponse = null;
		this.payloadReplayQueue = new LinkedList<>();
		this.game = game;
	}

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
			default:
				break;
		}
	}
	
	public boolean isThereAnyOpenContext(){
		return waitingContextResponse == null;
	}
	
	public JsonValue waitForContextReply(){
		GameMessage message = null;
		while(true){
			try{
				message = MessageManager.getInstance().getQueueForGame(game.getUUID()).take();
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			if(message != null && message.getOpcode().equals("CONTEXTREPLY")){
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
	
	public void setContextAck(boolean accepted, Player player){
		if(accepted){
			this.waitingContextResponse = null;
			this.payloadReplayQueue.add(this.pendingMessage.getMessage());
			this.pendingMessage = null;
		}
		MessageManager.getInstance()
					  .sendMessge(ServerMessageFactory.buildCONTEXTACKMessage(this.game,
								  											  player,
								  											  accepted));
	}
}
