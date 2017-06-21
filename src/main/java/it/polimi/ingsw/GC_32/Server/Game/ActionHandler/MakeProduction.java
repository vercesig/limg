package it.polimi.ingsw.GC_32.Server.Game.ActionHandler;

import java.util.LinkedList;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.rits.cloning.Cloner;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Network.ServerContextMessageFactory;
import it.polimi.ingsw.GC_32.Common.Network.ServerMessageFactory;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Server.Game.Effect.ImpossibleMoveException;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;

public class MakeProduction {
	
	public static boolean tryMake(Board board, Player player, Action action){
		player.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus());
		
		//ultimo check
		if(player.getResources().hasNegativeValue()){
			return false;
		}
		return true;
	
	}
	
	public static void make(Game game, Board board, Player player, Action action){
		player.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
		
		
		//active cardEffect
	    LinkedList<DevelopmentCard> cardlist = new LinkedList<DevelopmentCard>();
		cardlist = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
		
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTMessage(player.getUUID(), ServerContextMessageFactory.buildServantMessage(player)));
		
		//HANDLE SERVANTS CONTEXT
		while(true){
			if(MessageManager.getInstance().hasMessage()){
				for(GameMessage message : MessageManager.getInstance().getRecivedQueue()){
					String uuid = message.getPlayerID();
					if(message.getOpcode().equals("CONTEXTREPLY") && player.getUUID().equals(uuid)){
						JsonObject payload = Json.parse(message.getMessage()).asObject();
						
						if(payload.get("OPCODE").equals("SERVANTS")){ // e' un contextReply di Servants!
							int number = payload.get("NUMBER").asInt();
							player.getResources().addResource("SERVANTS", -number);
							
							// ERRORE: scelta non valida del client
							if(player.getResources().hasNegativeValue()){
								player.getResources().addResource("SERVANTS", number); //ripristino								
								MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTMessage(player.getUUID(), ServerContextMessageFactory.buildServantMessage(player)));
							 continue;
							}
							action.setActionValue(number + action.getActionValue()); 
							// send ACK
							MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACKCONTEXTMessage(player.getUUID()));
						}
					}
				}
			break;	
			}
		}
		//Select only the Cards that can been activated
		for (DevelopmentCard c : cardlist){
			if(c.getMinimumActionvalue() > action.getActionValue()){
				cardlist.remove(c);
			}
		}
		
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTMessage(player.getUUID(), ServerContextMessageFactory.buildChangeMessage(cardlist)));
		
		//HANDLE CHANGE CONTEXT
		while(true){
			if(MessageManager.getInstance().hasMessage()){
				for(GameMessage message : MessageManager.getInstance().getRecivedQueue()){
					
					String uuid = message.getPlayerID();
					if(message.getOpcode().equals("CONTEXTREPLY") && player.getUUID().equals(uuid)){
						JsonObject payload = Json.parse(message.getMessage()).asObject();
						
						if(payload.get("OPCODE").equals("CHANGE")){ // e' un contextReply di CHANGE
							
							//ContextReply di Change ha nel message un array di id degli effetti da attivare;
							JsonArray idList = payload.get("PAYLOAD").asArray();
							
							// II CHECK DEGLI EFFETTI SU DELLE COPIE
							Cloner cloner = new Cloner();
					    	cloner.dontCloneInstanceOf(Effect.class); // Effetti non possono essere deepCopiati dalla libreria cloning
					    	Board cpBoard = cloner.deepClone(board);
					    	Player cpPlayer = cloner.deepClone(player);
					    	Action cpAction = cloner.deepClone(action);
					    	
					    	 LinkedList<DevelopmentCard> list = cardlist;
					    	// TO-DO: GROSSO PROBLEMA
							idList.forEach(js -> {
								try {
									list.get(js.asInt()).getInstantEffect().apply(cpBoard, cpPlayer, cpAction);
								}
								// It throws Exception if the sum of the resourceSet of the effect is 
								catch (ImpossibleMoveException e) {
									
									// ERRORE: scelta non valida del client
						    		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTMessage(player.getUUID(), ServerContextMessageFactory.buildChangeMessage(list)));
						    		return;
								}
							});
							// Applico gli effetti agli originali
							idList.forEach(js -> {
								try {
									list.get(js.asInt()).getInstantEffect().apply(board, player, action);
								} catch (ImpossibleMoveException e) {
									// TODO Auto-generated catch block
									return;
								}
							});
							// send ACK
							MessageManager.getInstance().sendMessge(ServerMessageFactory.buildACKCONTEXTMessage(player.getUUID()));
						}
					}
				}
				break;
			}
		}
	}	
}
