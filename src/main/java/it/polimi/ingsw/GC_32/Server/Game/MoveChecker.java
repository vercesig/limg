package it.polimi.ingsw.GC_32.Server.Game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

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
	Logger log;
	Set <String> list;
	Map <String, JsonValue> contextManager;
	
    public MoveChecker(){
    	this.log = Logger.getLogger("MoveCheckerLogger");
    	this.list = new HashSet <String> ();
    	this.contextManager = new HashMap <String, JsonValue>();
    } 
    
    public Set<String> getList(){
    	return this.list;
    }
    
    public void contextPull(JsonValue payload){
    	this.list.remove(payload.asObject().get("CONTEXT_TYPE"));
    	this.contextManager.put(payload.asObject().get("CONTEXT_TYPE").asString(), payload.asObject().get("PAYLOAD"));
    	
    }
  //*******************************************************************************//  
    public boolean firstCheck(Board board, Player player, Action action){
    	
    	log.info("/******** CHECK: is a valid region id?\n" + Check.checkValidRegionID(board, player, action) + "\n");
    	if(!Check.checkValidRegionID(board, player, action)){
			return false;
		}
    	
		log.info("/******** CHECK: is a valid actionSpace id?\n" + Check.checkValidActionSpaceID(board, player, action) + "\n");
		if(!Check.checkValidActionSpaceID(board, player, action)){
			return false;
		}
		
		log.info("/******** CHECK: is a valid action type?\n" + Check.checkValidActionType(board,player, action) + "\n");
		if(!Check.checkValidActionType(board,player, action)){
			return false;
    	} 
    	return true;
    }
    
   public boolean moveFamilyMember(Game game, Board board, Player player, Action action){
	    
	   log.info("/******** CHECK: is the rule of Only one Family color respected?" + Check.familyColor(board, player, action) + "\n");
   	    if(!Check.familyColor(board, player, action)){
			return false;
		}
   	
   	    log.info("/******** CHECK: is a free actionSpace?"+ Check.isFreeSingleSpace(board, player, action) + "\n");
		if(!Check.isFreeSingleSpace(board, player, action)){
			return false;
		}
   	
		log.info("/******** CHECK: is a space blocked?"+ Check.checkBlockedZone(game.getPlayerList().size(), action) + "\n");
		if(!Check.checkBlockedZone(game.getPlayerList().size(), action)){
			return false;
		}
		
		log.info("/******** CHECK: can the player use servants to positionate the family member?"+ Check.useServants(board, player, action) + "\n");
		if(!Check.useServants(board, player, action)){ // change the state of the game
			return false;
		}
		
		log.info("/******** CHECK: has the player three coins because the tower is busy?"+ Check.checkCoinForTribute(board, player, action) + "\n");
		if(!Check.checkCoinForTribute(board, player, action)){ // change the state of the game
			return false;
		}
	
	int pawnID = action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt();
	game.moveFamiliar(player, board,  pawnID, action); // change the state of the game
   	return true;
   }
        
   public boolean takeCard(Game game, Board board, Player player, Action action){
	   
	   log.info("/******** CHECK: has the player enough military points?"+ Check.checkTerrytoryRequirement(board, player, action) + "\n");
	   if(!Check.checkTerrytoryRequirement(board, player, action)){
		   return false;
	   }
	   
	   log.info("/******** CHECK: has the player six card of the same type of the card he wants to buy?"+ Check.checkMaxCard(board, player, action) + "\n");
		if(!Check.checkMaxCard(board, player, action)){
			return false;
		}
		
		log.info("/******** CHECK: can the player pay the cost of the card?"+ Check.checkCost(board, player, action) + "\n");
		if(!Check.checkCost(board, player, action)){ //change the state of the game
			return false;
		}
		
		game.takeCard(player, board, action); // change the state of the game
		return true;
   }
   
//*******************************************************************************//  
    
   // SimulateWithCopy: Try the Action with Copies of Board, Player, action. It returns a boolean and it does not change the state of the game
     // Everything with Effect will produce an exception with copies.
   
    public boolean simulateWithCopy(Game game, Board cloneBoard, Player clonePlayer, Player player, Action cloneAction){
    		
    	// are valid arguments?
    	if(!firstCheck(cloneBoard, clonePlayer, cloneAction)){
    		return false;
    	}
    	//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
    	if(!MakeAction.usePermamentEffect(cloneBoard, clonePlayer, player, cloneAction)){
    		return false;
    	}
    		
    	if(!moveFamilyMember(game, cloneBoard, clonePlayer, cloneAction)){
    		return false;
    	}
    		//prendo il bonus dell'actionSpace
    	clonePlayer.getResources().addResource(cloneBoard.getRegion(cloneAction.getActionRegionId())
    			.getActionSpace(cloneAction.getActionSpaceId()).getBonus());
    		
    	switch(cloneAction.getActionType()){	
    			
    		case "PRODUCTION" : {
    			
    			clonePlayer.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
    				
    			//CONTEXT MESSAGE HANDLER: SERVANT
    			if(!this.contextManager.containsKey("SERVANT")){
    				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.SERVANT, clonePlayer.getResources().getResource("SERVANTS"), cloneAction.getActionType()));
    				this.list.add("SERVANT");
    			}
    			else{
    				cloneAction.setActionValue(cloneAction.getActionValue() + contextManager.get("SERVANT").asInt());
    			}
    			
    			//CONTEXT MESSAGE HANDLER: CHANGE
    			if(!this.contextManager.containsKey("CHANGE")) {
    				
    				LinkedList<DevelopmentCard> cardlist = new LinkedList<DevelopmentCard>();
    				cardlist = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
    				for(DevelopmentCard card : cardlist){
    					if(card.getMinimumActionvalue() > cloneAction.getActionValue()){ // potrebbe creare conflitti con il context Servant
    						cardlist.remove(card);
    					}
    				}
    				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.CHANGE, cardlist));
    				this.list.add("CHANGE");
    			}
    			
    			else {
    				JsonArray payloadChange = this.contextManager.get("CHANGE").asObject().get("RESOURCE_IN").asArray();
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
    			
    			clonePlayer.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
    			
    			//CONTEXT MESSAGE HANDLER: SERVANT
    			if(!this.contextManager.containsKey("SERVANT")){
    				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.SERVANT, clonePlayer.getResources().getResource("SERVANTS"), cloneAction.getActionType()));
    				this.list.add("SERVANT");
    			}
    			else{
    				cloneAction.setActionValue(cloneAction.getActionValue() + contextManager.get("SERVANT").asInt());
    			}
    			return true;
    		}
    		
    		case "COUNCIL" : {
    			
    			clonePlayer.getResources().addResource("COINS", 1);
    			
    			//COMTEXT MESSAGE HANDLER: PRIVILEGE
    			if(!this.contextManager.containsKey("PRIVILEGE")){
    				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.PRIVILEGE, 1));
    				this.list.add("PRIVILEGE");
    			}
    			else{
    				clonePlayer.getResources().addResource( new ResourceSet(contextManager.get("PRIVILEGE").asObject()));
    			}
    			return true;
    		}
    		
    		case "MARKET" : {
    			if(cloneAction.getActionSpaceId() == 3){
    				
    				//CONTEXT MESSAGE HANDLER: PRIVILEGE
        			if(!this.contextManager.containsKey("PRIVILEGE")){
        				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.PRIVILEGE, 2));
        				this.list.add("PRIVILEGE");
        			}
        			else{
        				clonePlayer.getResources().addResource( new ResourceSet(contextManager.get("PRIVILEGE").asObject()));
        			}
    			}
    			return true;
    		}
    			
    		//case  of a "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
    		default : {
   	
    			return takeCard(game, cloneBoard, clonePlayer, cloneAction);
    		}
    		
    	}
    }	

    //Simulate: SimulateWithCopy version using the original objects. It changes the state of the game.
    public void simulate(Game game, Board board, Player player,Action action){
    	
		//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
		MakeAction.usePermamentEffect(board, player, action);
		
		// uses the servants if the player can't pass the check on ActionValue
		moveFamilyMember(game, board, player, action);
		
		//prendo il bonus dell'actionSpace
		player.getResources().addResource(board.getRegion(action.getActionRegionId())
				.getActionSpace(action.getActionSpaceId()).getBonus());
		
		//effettuo l'azione
		switch(action.getActionType()){
			
			case "PRODUCTION" : {
				
    			player.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
				action.setActionValue(action.getActionValue() + contextManager.get("SERVANT").asInt());
				
				LinkedList<DevelopmentCard> playerCard = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
				JsonArray cardlist = contextManager.get("CHANGE").asObject().get("ID").asArray();
				
				for( JsonValue json: cardlist){
					try {
						playerCard.get(json.asInt()).getInstantEffect().apply(board, player, action);
					} 
					catch (ImpossibleMoveException e) {
						log.severe("ERRORE"); // ERRORE GRAVE!
					}
				}
			}	
			case "HARVEST" : {
				
    			player.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
				action.setActionValue(action.getActionValue() + contextManager.get("SERVANT").asInt());
				
				LinkedList<DevelopmentCard> playerCard = player.getPersonalBoard().getCardsOfType("TERRITORYCARD");
				for(DevelopmentCard card : playerCard){
					if(card.getMinimumActionvalue() <= action.getActionValue()){
						try {
							card.getInstantEffect().apply(board, player, action);
						}
						catch (ImpossibleMoveException e) {
							log.severe("ERRORE"); // ERRORE GRAVE!
						}
					}
				}
			}
			case "COUNCIL" : {
				
    			player.getResources().addResource("COINS", 1);
				player.getResources().addResource( new ResourceSet(contextManager.get("PRIVILEGE").asObject()));

			}
			case "MARKET" : {
				
				if(action.getActionSpaceId() == 3){
					player.getResources().addResource( new ResourceSet(contextManager.get("PRIVILEGE").asObject()));
				}
			}
			//case  of a "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
			default:{
				
				TowerRegion selectedTower = (TowerRegion)(board.getRegion(action.getActionRegionId()));
		    	DevelopmentCard card = selectedTower.getTowerLayers()[action.getActionSpaceId()].getCard();
		    	
		    	takeCard(game, board, player, action);
		    	player.addEffect(card.getPermanentEffect());
		    	try {
		    		card.getInstantEffect().apply(board, player, action);
		    	} catch (ImpossibleMoveException e) {
					log.severe("ERRORE!");
		    	}
			}
		}
    }
}
