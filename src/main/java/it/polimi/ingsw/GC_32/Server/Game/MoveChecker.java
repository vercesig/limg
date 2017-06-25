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
	
	// context management
	private HashMap<ContextType, Object[]> contextQueue; // use this map if you want to open context
	private HashMap<String, JsonValue> contextInfoContainer; // use this map if you want have access to context response
	private HashSet<String> waitingContextResponseSet;
	
	// cloning handling
	private Cloner cloner = new Cloner();
	private Board cloneBoard;
	private Player clonePlayer;
	private Action cloneAction;
	private Action action;
	
	public boolean waitBeforeChangeFlag = true; //if setted true, moveChecker can't change the game's status
	
    public MoveChecker(){
    	cloner.dontCloneInstanceOf(Effect.class); //Effects cannot be deepcloned
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
    
    public void firstStepCheck(Game game, Player player, Action action){
    	LOGGER.info("firstStepCheck");
    	
    	this.cloneBoard = cloner.deepClone(game.getBoard());
    	this.clonePlayer = cloner.deepClone(player);
    	this.cloneAction = cloner.deepClone(action);
    	
    	this.action = action;
    	
		// checking if is a valid action
		if(simulateWithCopy(player,game)){
			waitBeforeChangeFlag = false;
			simulate(game, game.getBoard(), player);
		}
    }
    
    public void setWaitFlag(boolean waitFlag){
    	this.waitBeforeChangeFlag = waitFlag;
    }
    
    public boolean moveFamilyMember(Game game, Board board, Player player, Action action){ 
   	    if(!Check.checkFamilyColor(board, player, action))
			return false;

		if(!Check.isFreeSingleSpace(board, player, action))
			return false;

		if(Check.checkBlockedZone(game.getPlayerList().size(), action))
			return false;
		
		if(!Check.useServants(board, player, action)) // change the state of the game
			return false;

		if(!Check.checkCoinForTribute(board, player, action)) // change the state of the game
			return false;

   	    if(!waitBeforeChangeFlag){
   	    	int pawnID = action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt();
   	    	game.moveFamiliar(player, board,  pawnID, action); // change the state of the game
   	    }
   	    return true;
    }
        
    public boolean takeCard(Game game, Board board, Player player, Action action){
    	if(!Check.checkTerrytoryRequirement(board, player, action))
    		return false;

		if(!Check.checkMaxCard(board, player, action))
			return false;

		if(!Check.checkCost(board, player, action)) //change the state of the game
			return false;
		if(!waitBeforeChangeFlag)
			game.takeCard(player, board, action); // change the state of the game
		return true;
    }

   /** SimulateWithCopy: Try the Action with Copies of Board, Player, action. It returns a boolean and it does not change the state of the game
    *  Everything with Effect will produce an exception with copies.
    */
	public boolean simulateWithCopy(Player player, Game game){
				
    	if(!Check.checkValidID(cloneBoard, cloneAction))
    		return false;

    	//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
    	if(!MakeAction.usePermamentEffect(cloneBoard, clonePlayer, player, cloneAction))
    		return false;
    	
    	//System.out.println(!moveFamilyMember(game, cloneBoard, clonePlayer, cloneAction));
    	if(!moveFamilyMember(game, cloneBoard, clonePlayer, cloneAction))
    		return false;

    	//prendo il bonus dell'actionSpace
    	if(cloneBoard.getRegion(cloneAction.getActionRegionId()).getActionSpace(cloneAction.getActionSpaceId()).getBonus()!=null){
    		clonePlayer.getResources().addResource(cloneBoard.getRegion(cloneAction.getActionRegionId())
    				   .getActionSpace(cloneAction.getActionSpaceId()).getBonus());
    	}

    	switch(cloneAction.getActionType()){	
    			
    		case "PRODUCTION" : {
    			
    			// TODO: assegnare personalBonusTile
    			//clonePlayer.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
    				   			
    			//CONTEXT MESSAGE HANDLER: SERVANT
    			if(!this.contextInfoContainer.containsKey(ContextType.SERVANT.toString())){
    				// open context
    				contextQueue.put(ContextType.SERVANT, new Object[]{clonePlayer.getResources().getResource("SERVANTS"), cloneAction.getActionType()});
    			}
    			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
    				cloneAction.setActionValue(cloneAction.getActionValue() + contextInfoContainer.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
    			}
    			
    			//CONTEXT MESSAGE HANDLER: CHANGE
    			if(!this.contextInfoContainer.containsKey(ContextType.CHANGE.toString())) {
    				LinkedList<DevelopmentCard> cardlist = new LinkedList<DevelopmentCard>();
    				cardlist = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
    				for(DevelopmentCard card : cardlist){
    					if(card.getMinimumActionvalue() > cloneAction.getActionValue()){ // potrebbe creare conflitti con il context Servant
    						cardlist.remove(card);
    					}
    				}
    				if(!cardlist.isEmpty()){
    					contextQueue.put(ContextType.CHANGE, new Object[]{cardlist});
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
    			break;
    		}   		
    		case "HARVEST" : {
    			
    			//TODO
    			//clonePlayer.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
    			
    			//CONTEXT MESSAGE HANDLER: SERVANT
    			if(!this.contextInfoContainer.containsKey(ContextType.SERVANT.toString())){
    				contextQueue.put(ContextType.SERVANT, new Object[]{clonePlayer.getResources().getResource("SERVANTS"), cloneAction.getActionType()});
    			}
    			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
    				cloneAction.setActionValue(cloneAction.getActionValue() + contextInfoContainer.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
    			}
    			break;
    		}  		
    		case "COUNCIL" : {
    			
    			clonePlayer.getResources().addResource("COINS", 1);
    			
    			//COMTEXT MESSAGE HANDLER: PRIVILEGE
    			if(!this.contextInfoContainer.containsKey(ContextType.PRIVILEGE.toString())){
    				contextQueue.put(ContextType.PRIVILEGE, new Object[]{1});
    			}
    			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
    				clonePlayer.getResources().addResource( new ResourceSet(contextInfoContainer.get("PRIVILEGE").asObject()));
    			}
    			break;
    		}    		
    		case "MARKET" : {
    			if(cloneAction.getActionSpaceId() == 3){
    				
    				//CONTEXT MESSAGE HANDLER: PRIVILEGE
        			if(!this.contextInfoContainer.containsKey(ContextType.PRIVILEGE.toString())){
        				contextQueue.put(ContextType.PRIVILEGE, new Object[]{2});
        			}
        			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
        				clonePlayer.getResources().addResource( new ResourceSet(contextInfoContainer.get("PRIVILEGE").asObject()));
        			}
    			}
    			break;
    		}    			
    		//case  of "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
    		case "TOWER" : {
    			System.out.println("takeCard");
    			return takeCard(game, cloneBoard, clonePlayer, cloneAction);
    		}    		
    	}
    	
    	if(waitingContextResponseSet.isEmpty())
			return false;
		else
			return true;
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
    }
}
