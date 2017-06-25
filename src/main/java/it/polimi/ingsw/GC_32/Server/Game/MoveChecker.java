package it.polimi.ingsw.GC_32.Server.Game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import com.rits.cloning.Cloner;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.ServerMessageFactory;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.ActionHandler.MakeAction;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.*;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;

public class MoveChecker{
	final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	//private Set<String> list;
	//private Map<String, JsonValue> contextManager;
	
	private HashMap<ContextType, Object[]> contextQueue; // use this map if you want to open context
	private HashMap<String, JsonValue> contextInfoContainer; // use this map if you want have access to context response
	private HashSet<String> waitingContextResponseSet;
	
	// cloning handling
	private Cloner cloner = new Cloner();
	private Board cloneBoard;
	private Player clonePlayer;
	private Action cloneAction;
	private Action action;
	public boolean waitBeforeChangeFlag = true;
	
    public MoveChecker(){
    	//this.list = new HashSet <String>();
    	//this.contextManager = new HashMap<String, JsonValue>();
    	//Effects cannot be deepcloned
    	cloner.dontCloneInstanceOf(Effect.class);
    }
    
    public void registerContextQueue(HashMap<ContextType, Object[]> contextQueue){
    	this.contextQueue = contextQueue;
    }
    
    public void registerContextInfoContainer(HashMap<String, JsonValue> contextInfoContainer){
    	this.contextInfoContainer = contextInfoContainer;
    }
    
    public void registerContextResponseSet(HashSet<String> contextResponseSet){
    	this.waitingContextResponseSet = contextResponseSet;
    }
    
    public boolean firstStepCheck(Game game, Player player, Action action){
    	LOGGER.info("firstStepCheck");
    	
    	System.out.println("firststepcheck");
    	
    	this.cloneBoard = cloner.deepClone(game.getBoard());
    	this.clonePlayer = cloner.deepClone(player);
    	this.cloneAction = cloner.deepClone(action);
    	
    	this.action = action;
    	
		// checking if is a valid action
		if(simulateWithCopy(/*game, cloneBoard, clonePlayer, player, cloneAction*/ player,game)){		
			System.out.println("mancano dei pezzi");
			return false;
		}

		if(this.waitingContextResponseSet.isEmpty()){
			waitBeforeChangeFlag = false;
			System.out.println("sto simulando");
			simulate(game, game.getBoard(), player/*, action*/);
		}
		return true;
    }
    
    public void setWaitFlag(boolean waitFlag){
    	this.waitBeforeChangeFlag = waitFlag;
    }

   /* public boolean contextPull(JsonValue payload, Game game, Player player){
    	this.list.remove(payload.asObject().get("CONTEXT_TYPE").asString());
    	this.contextManager.put(payload.asObject().get("CONTEXT_TYPE").asString(), payload.asObject().get("PAYLOAD").asObject());
    	
    	System.out.println("context pull, rimuovo da list "+payload.asObject().get("CONTEXT_TYPE").asString());
    	System.out.println(list.toString());
    	System.out.println(payload.toString());
    	System.out.println();
    	
		if(this.list.isEmpty()){  // non ci sono ulteriori context attivi
			waitBeforeChangeFlag = false; // ok possiamo toccare il model
			if(simulateWithCopy(game, cloneBoard, clonePlayer, player, cloneAction)){ // simulazione completa
				System.out.println("sto simulando");
				simulate(game, game.getBoard(), player, action); // apply degli originali
				return true;
			}
		}
		return false;   	
    }*/

    public boolean checkValidID(Board board, Action action){
    	if(checkValidRegionID(board, action) && checkValidActionSpaceID(board, action)){
			return true;
		}
    	return false;
    }
    
    public boolean moveFamilyMember(Game game, Board board, Player player, Action action){ 
   	    if(!Check.checkFamilyColor(board, player, action)){
			return false;
		}

		if(!Check.isFreeSingleSpace(board, player, action)){
			return false;
		}

		if(Check.checkBlockedZone(game.getPlayerList().size(), action)){
			return false;
		}
		
		if(!Check.useServants(board, player, action)){ // change the state of the game
			return false;
		}

		if(!Check.checkCoinForTribute(board, player, action)){ // change the state of the game
			return false;
		}

   	    if(!waitBeforeChangeFlag){
   	    	int pawnID = action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt();
   	    	game.moveFamiliar(player, board,  pawnID, action); // change the state of the game
   	    }
   	    return true;
    }
        
    public boolean takeCard(Game game, Board board, Player player, Action action){
    	if(!Check.checkTerrytoryRequirement(board, player, action)){
    		return false;
    	}

		if(!Check.checkMaxCard(board, player, action)){
			return false;
		}

		if(!Check.checkCost(board, player, action)){ //change the state of the game
			return false;
		}
		if(!waitBeforeChangeFlag)
			game.takeCard(player, board, action); // change the state of the game
		return true;
    }

   /** SimulateWithCopy: Try the Action with Copies of Board, Player, action. It returns a boolean and it does not change the state of the game
    *  Everything with Effect will produce an exception with copies.
    */
	public boolean simulateWithCopy(/*Game game, Board cloneBoard, Player clonePlayer, Player player, Action cloneAction*/ Player player, Game game){
				
    	if(!checkValidID(cloneBoard, cloneAction)){
    		return false;
    	}
    	System.out.println("firstcheck passato");
    	//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
    	if(!MakeAction.usePermamentEffect(cloneBoard, clonePlayer, player, cloneAction)){
    		return false;
    	}
    	System.out.println("usePermanenteffect passato");
    	
    	//System.out.println(!moveFamilyMember(game, cloneBoard, clonePlayer, cloneAction));
    	if(!moveFamilyMember(game, cloneBoard, clonePlayer, cloneAction)){
    		return false;
    	}
    	//prendo il bonus dell'actionSpace
    	if(cloneBoard.getRegion(cloneAction.getActionRegionId()).getActionSpace(cloneAction.getActionSpaceId()).getBonus()!=null){
    		clonePlayer.getResources().addResource(cloneBoard.getRegion(cloneAction.getActionRegionId())
    				   .getActionSpace(cloneAction.getActionSpaceId()).getBonus());
    	}
    	System.out.println("entro nello switch");
    	switch(cloneAction.getActionType()){	
    			
    		case "PRODUCTION" : {
    			
    			// TODO: assegnare personalBonusTile
    			//clonePlayer.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
    				
    			System.out.println("sono nella production");
    			
    			//CONTEXT MESSAGE HANDLER: SERVANT
    			if(!this.contextInfoContainer.containsKey(ContextType.SERVANT.toString())){
    				// apro il context
    				System.out.println("apro il context");
    				contextQueue.put(ContextType.SERVANT, new Object[]{clonePlayer.getResources().getResource("SERVANTS"), cloneAction.getActionType()});
    				//MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.SERVANT, clonePlayer.getResources().getResource("SERVANTS"), cloneAction.getActionType()));
    				//this.list.add("SERVANT");
    			}
    			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
    				cloneAction.setActionValue(cloneAction.getActionValue() + contextInfoContainer.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
    			}
    			
    			//CONTEXT MESSAGE HANDLER: CHANGE
    			if(!this.contextInfoContainer.containsKey(ContextType.CHANGE.toString())) {
    				
    				System.out.println("vorrei aprire il context CHANGE");
    				LinkedList<DevelopmentCard> cardlist = new LinkedList<DevelopmentCard>();
    				cardlist = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
    				for(DevelopmentCard card : cardlist){
    					if(card.getMinimumActionvalue() > cloneAction.getActionValue()){ // potrebbe creare conflitti con il context Servant
    						cardlist.remove(card);
    					}
    				}
    				if(!cardlist.isEmpty()){
    					contextQueue.put(ContextType.CHANGE, new Object[]{cardlist});
    					//MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.CHANGE, cardlist));
    					//this.list.add("CHANGE");
    				}
    			}  			
    			else { // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
    				JsonArray payloadChange = this.contextInfoContainer.get("CHANGE").asObject().get("RESOURCE_IN").asArray();
    				ArrayList <ResourceSet> resourceIn = new ArrayList <ResourceSet>();
    				payloadChange.forEach(resourceJs -> {
    					resourceIn.add(new ResourceSet(resourceJs.asObject()));
    				});
    				ResourceSet tot = new ResourceSet();
    				resourceIn.forEach(rs -> tot.addResource(rs));
    				if(clonePlayer.getResources().compareTo(tot) <0){
    					return false;	
    					// change effect failed //cancello il payload memorizzato sulla HashMap???
    				}
    				
    			}
    			return true;
    		}
    		
    		case "HARVEST" : {
    			
    			//clonePlayer.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
    			
    			//CONTEXT MESSAGE HANDLER: SERVANT
    			if(!this.contextInfoContainer.containsKey("SERVANT")){
    				contextQueue.put(ContextType.SERVANT, new Object[]{clonePlayer.getResources().getResource("SERVANTS"), cloneAction.getActionType()});
    				//MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.SERVANT, clonePlayer.getResources().getResource("SERVANTS"), cloneAction.getActionType()));
    				//this.list.add("SERVANT");
    			}
    			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
    				cloneAction.setActionValue(cloneAction.getActionValue() + contextInfoContainer.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
    			}
    			return true;
    		}
    		
    		case "COUNCIL" : {
    			
    			clonePlayer.getResources().addResource("COINS", 1);
    			
    			//COMTEXT MESSAGE HANDLER: PRIVILEGE
    			if(!this.contextInfoContainer.containsKey(ContextType.PRIVILEGE.toString())){
    				contextQueue.put(ContextType.PRIVILEGE, new Object[]{1});
    				//MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.PRIVILEGE, 1));
    				//this.list.add("PRIVILEGE");
    			}
    			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
    				clonePlayer.getResources().addResource( new ResourceSet(contextInfoContainer.get("PRIVILEGE").asObject()));
    			}
    			return true;
    		}
    		
    		case "MARKET" : {
    			if(cloneAction.getActionSpaceId() == 3){
    				
    				//CONTEXT MESSAGE HANDLER: PRIVILEGE
        			if(!this.contextInfoContainer.containsKey(ContextType.PRIVILEGE.toString())){
        				contextQueue.put(ContextType.PRIVILEGE, new Object[]{2});
        				//MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.PRIVILEGE, 2));
        				//this.list.add("PRIVILEGE");
        			}
        			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
        				clonePlayer.getResources().addResource( new ResourceSet(contextInfoContainer.get("PRIVILEGE").asObject()));
        			}
    			}
    			return true;
    		}
    			
    		//case  of "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
    		default : {
    			System.out.println("takeCard");
    			return takeCard(game, cloneBoard, clonePlayer, cloneAction);
    		}
    		
    	}
    }	
    
    //Simulate: SimulateWithCopy version using the original objects. It changes the state of the game.
    public void simulate(Game game, Board board, Player player/*,Action action*/){
    	
		//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
		MakeAction.usePermamentEffect(board, player, action);
		
		// uses the servants if the player can't pass the check on ActionValue
		moveFamilyMember(game, board, player, action);
		
		//prendo il bonus dell'actionSpace
		if(board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId()).getBonus()!=null){
			player.getResources().addResource(board.getRegion(action.getActionRegionId())
					.getActionSpace(action.getActionSpaceId()).getBonus());
		}
		
		ArrayList<DevelopmentCard> newCards = new ArrayList<DevelopmentCard>();
		
		//effettuo l'azione
		switch(action.getActionType()){
			
			case "PRODUCTION" : {
				
				// TODO: assegnare personalBonus
    			//player.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
				action.setActionValue(action.getActionValue() + contextInfoContainer.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
				
				if(contextInfoContainer.containsKey(ContextType.CHANGE.toString())){
					LinkedList<DevelopmentCard> playerCard = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
					JsonArray cardlist = contextInfoContainer.get("CHANGE").asObject().get("ID").asArray();
					for( JsonValue json: cardlist){
						playerCard.get(json.asInt()).getInstantEffect().apply(board, player, action);
					}
				}
				break;
			}

			case "HARVEST" : {
				
				//TODO
				//player.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
				action.setActionValue(action.getActionValue() + contextInfoContainer.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
				
				LinkedList<DevelopmentCard> playerCard = player.getPersonalBoard().getCardsOfType("TERRITORYCARD");
				for(DevelopmentCard card : playerCard){
					if(card.getMinimumActionvalue() <= action.getActionValue()){
						card.getInstantEffect().apply(board, player, action);
					}
				}
				break;
			}
			case "COUNCIL" : {
				
    			player.getResources().addResource("COINS", 1);
				player.getResources().addResource( new ResourceSet(contextInfoContainer.get("PRIVILEGE").asObject()));
				break;

			}
			case "MARKET" : {
				
				if(action.getActionSpaceId() == 3){
					player.getResources().addResource( new ResourceSet(contextInfoContainer.get("PRIVILEGE").asObject()));
				}
				break;
			}

			//case  of a "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
			default:{
				
				TowerRegion selectedTower = (TowerRegion)(board.getRegion(action.getActionRegionId()));
		    	DevelopmentCard card = selectedTower.getTowerLayers()[action.getActionSpaceId()].getCard();
				newCards.add(card);
				
		    	takeCard(game, board, player, action);
		    	player.addEffect(card.getPermanentEffect());
		    	card.getInstantEffect().apply(board, player, action);
		    	break;
			}
		}
		
		waitBeforeChangeFlag = true;
		contextInfoContainer.clear();
		// notifico i cambiamenti
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(player.getUUID(), player.getResources()));
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(player.getUUID(), newCards));
		System.out.println("fine moveChecker");
		
    }
    
	private boolean checkValidRegionID(Board board, Action action){	
		if(board.getRegion(action.getActionRegionId()) == null){
			return false;
		}
		return true;
	}

	private boolean checkValidActionSpaceID(Board board, Action action){	
		if(board.getRegion(action.getActionRegionId())
				.getActionSpace(action.getActionSpaceId()) == null){
			return false;
		}
		return true;
	}
}
