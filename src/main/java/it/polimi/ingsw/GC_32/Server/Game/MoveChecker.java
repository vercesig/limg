package it.polimi.ingsw.GC_32.Server.Game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.rits.cloning.Cloner;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ContextType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
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
	private Set <String> list;
	private Map <String, JsonValue> contextManager;
	
	// cloning handling
	private Cloner cloner = new Cloner();
	private Board cloneBoard;
	private Player clonePlayer;
	private Action cloneAction;
	
	private Action action;
	
    public MoveChecker(){
    	this.log = Logger.getLogger("MoveCheckerLogger");
    	this.list = new HashSet <String> ();
    	this.contextManager = new HashMap <String, JsonValue>();   	
    	cloner.dontCloneInstanceOf(Effect.class); // Effetti non possono essere deepCopiati dalla libreria cloning 	
    } 
    
    public boolean waitForChangeFlag = true;
    
    public void firstStepCheck(Game game, Player player, Action action){
    	
    	System.out.println("firstStepCheck");
    	
    	this.cloneBoard = cloner.deepClone(game.getBoard());
    	this.clonePlayer = cloner.deepClone(player);
    	this.cloneAction = cloner.deepClone(action);
    	
    	this.action = action;
    	
		// checking if is a valid action
		if(!simulateWithCopy(game, cloneBoard, clonePlayer, player, cloneAction)){		
			
			System.out.println("azione non valida");
			// inviare ACKCHK negativo
			
			return; // non valida
		}
		System.out.println("azionevalida");
		// azione valida...
		
		// non ci sono context attivi (simulateWithCopy non ha generato alcun context)
		if(this.list.isEmpty()){
			waitForChangeFlag = false;
			//if(simulateWithCopy(game, cloneBoard, clonePlayer, player, cloneAction)){ // simulazione completa
				simulate(game, game.getBoard(), player, action); // apply degli originali
			//}
		}
		// l'azione e' sospesa: si aspettano dei ContextReply per tappare i buchi della context list
    }
    
    public Set<String> getList(){
    	return this.list;
    }
    
    public boolean contextPull(JsonValue payload, Game game, Player player){
    	this.list.remove(payload.asObject().get("CONTEXT_TYPE").asString());
    	this.contextManager.put(payload.asObject().get("CONTEXT_TYPE").asString(), payload.asObject().get("PAYLOAD"));
    	
    	System.out.println("context pull, rimuovo da list "+payload.asObject().get("CONTEXT_TYPE").asString());
    	System.out.println(list.toString());
    	System.out.println(payload.toString());
    	System.out.println();
    	
		if(this.list.isEmpty()){  // non ci sono ulteriori context attivi
			waitForChangeFlag = false; // ok possiamo toccare il model
			if(simulateWithCopy(game, cloneBoard, clonePlayer, player, cloneAction)){ // simulazione completa
				System.out.println("sto simulando");
				simulate(game, game.getBoard(), player, action); // apply degli originali
				return true;
			}
		}
		return false;   	
    }
        
  //*******************************************************************************//  
    public boolean firstCheck(Board board, Player player, Action action){
    	
    	
    	System.out.println(!Check.checkValidRegionID(board, player, action));
    	System.out.println(!Check.checkValidActionSpaceID(board, player, action));
    	
    	log.info("/******** CHECK: is a valid region id?\n" + Check.checkValidRegionID(board, player, action) + "\n");
    	if(!Check.checkValidRegionID(board, player, action)){
			return false;
		}
    	
		log.info("/******** CHECK: is a valid actionSpace id?\n" + Check.checkValidActionSpaceID(board, player, action) + "\n");
		if(!Check.checkValidActionSpaceID(board, player, action)){
			return false;
		}
		
    	return true;
    }
    
   public boolean moveFamilyMember(Game game, Board board, Player player, Action action){
	    
	   log.info("/******** CHECK: is the rule of Only one Family color respected?" + Check.familyColor(board, player, action) + "\n");
   	    if(!Check.familyColor(board, player, action)){
			return false;
		}
   	    
   	    System.out.println("familycolor");
   	
   	    log.info("/******** CHECK: is a free actionSpace?"+ Check.isFreeSingleSpace(board, player, action) + "\n");
		if(!Check.isFreeSingleSpace(board, player, action)){
			return false;
		}
		
   	    System.out.println("freesingle");
   	
   	    
   	    
		log.info("/******** CHECK: is a space blocked?"+ Check.checkBlockedZone(game.getPlayerList().size(), action) + "\n");
		if(Check.checkBlockedZone(game.getPlayerList().size(), action)){
			return false;
		}
		
   	    System.out.println("blockedzone");
		
		log.info("/******** CHECK: can the player use servants to positionate the family member?"+ Check.useServants(board, player, action) + "\n");
		if(!Check.useServants(board, player, action)){ // change the state of the game
			return false;
		}
		
   	    System.out.println("servant");
		
		log.info("/******** CHECK: has the player three coins because the tower is busy?"+ Check.checkCoinForTribute(board, player, action) + "\n");
		if(!Check.checkCoinForTribute(board, player, action)){ // change the state of the game
			return false;
		}
	
   	    System.out.println("coinfortribute");
	
   	    if(!waitForChangeFlag){
   	    	int pawnID = action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt();
   	    	game.moveFamiliar(player, board,  pawnID, action); // change the state of the game
   	    }
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
		if(!waitForChangeFlag)
			game.takeCard(player, board, action); // change the state of the game
		return true;
   }
   
//*******************************************************************************//  
    
   // SimulateWithCopy: Try the Action with Copies of Board, Player, action. It returns a boolean and it does not change the state of the game
     // Everything with Effect will produce an exception with copies.
   
    public boolean simulateWithCopy(Game game, Board cloneBoard, Player clonePlayer, Player player, Action cloneAction){
    		
    	System.out.println("simulateWithCopy");
    	
    	// are valid arguments?
    	if(!firstCheck(cloneBoard, clonePlayer, cloneAction)){
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
    				
    			//CONTEXT MESSAGE HANDLER: SERVANT
    			if(!this.contextManager.containsKey("SERVANT")){
    				System.out.println("aggiungo SERVANT");
    				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.SERVANT, clonePlayer.getResources().getResource("SERVANTS"), cloneAction.getActionType()));
    				this.list.add("SERVANT");
    			}
    			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
    				//System.out.println("prendo SERVANT");
    				cloneAction.setActionValue(cloneAction.getActionValue() + contextManager.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
    			}
    			
    			//CONTEXT MESSAGE HANDLER: CHANGE
    			if(!this.contextManager.containsKey("CHANGE")) {
    				System.out.println("aggiungo CHANGE");
    				LinkedList<DevelopmentCard> cardlist = new LinkedList<DevelopmentCard>();
    				cardlist = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
    				for(DevelopmentCard card : cardlist){
    					if(card.getMinimumActionvalue() > cloneAction.getActionValue()){ // potrebbe creare conflitti con il context Servant
    						cardlist.remove(card);
    					}
    				}
    				if(!cardlist.isEmpty()){
    					MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTmessage(clonePlayer.getUUID(), ContextType.CHANGE, cardlist));
    					this.list.add("CHANGE");
    				}
    			}
    			
    			else { // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
    				System.out.println("prendo CHANGE");
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
    			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
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
    			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
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
        			else{ // se contiene il contextPayload recupero le informazioni da questo e le applico nella simulazione
        				clonePlayer.getResources().addResource( new ResourceSet(contextManager.get("PRIVILEGE").asObject()));
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
    public void simulate(Game game, Board board, Player player,Action action){
    	
		//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
		MakeAction.usePermamentEffect(board, player, action);
		System.out.println("simulate - permanentEffect");
		
		// uses the servants if the player can't pass the check on ActionValue
		moveFamilyMember(game, board, player, action);
		System.out.println("simulate - moveFamilyMember");
		
		//prendo il bonus dell'actionSpace
		if(board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId()).getBonus()!=null){
			player.getResources().addResource(board.getRegion(action.getActionRegionId())
					.getActionSpace(action.getActionSpaceId()).getBonus());
		}
		
		System.out.println("simulate - bonus azione");
		ArrayList<DevelopmentCard> newCards = new ArrayList<DevelopmentCard>();
		
		System.out.println("simulate - entro nello switch");
		//effettuo l'azione
		switch(action.getActionType()){
			
			case "PRODUCTION" : {
				
				// TODO: assegnare personalBonus
    			//player.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
				action.setActionValue(action.getActionValue() + contextManager.get("SERVANT").asObject().get("CHOOSEN_SERVANTS").asInt());
				
				if(contextManager.containsKey("CHANGE")){
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
				break;
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
				break;
			}
			case "COUNCIL" : {
				
    			player.getResources().addResource("COINS", 1);
				player.getResources().addResource( new ResourceSet(contextManager.get("PRIVILEGE").asObject()));
				
				break;

			}
			case "MARKET" : {
				
				if(action.getActionSpaceId() == 3){
					player.getResources().addResource( new ResourceSet(contextManager.get("PRIVILEGE").asObject()));
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
		    	try {
		    		card.getInstantEffect().apply(board, player, action);
		    	} catch (ImpossibleMoveException e) {
					log.severe("ERRORE!");
		    	}
		    	
		    	break;
			}
		}
		
		waitForChangeFlag = true;
		
		// notifico i cambiamenti
		System.out.println(player.getResources().toString());
		
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(player.getUUID(), player.getResources()));
		MessageManager.getInstance().sendMessge(ServerMessageFactory.buildSTATCHNGmessage(player.getUUID(), newCards));
		System.out.println("fine moveChecker");
		
    }
}
