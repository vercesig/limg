package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.rits.cloning.Cloner;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Effect.*;

public class MoveChecker{
	
    public MoveChecker(){}
    
    //FamilyColorCheck
    private boolean checkFamilyColor(Board board, Player player, Action action){
    	try{
    		if(action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt() == 0){
    			return true;
    		}
    	} catch(NullPointerException e){};
      	for (FamilyMember f : player.getFamilyMember()){
    		try{
    			if(f.getPosition().getRegionID() == action.getActionRegionId() && !f.getColor().equals(DiceColor.GREY)){
    				return false;
    			}
    		}catch(NullPointerException e){};	
    	}return true;
    }
    //Free Action Space
    boolean checkIsFreeSingleSpace(Board board, Player player, Action action){
    	
    	if(board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId())
    			.isSingleActionSpace() && board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId())
    			.isBusy()){
    		return false;
    	}else 
    		return true;
    }
    // 3CoinsCheck
    private boolean checkCoinForTribute(Board board, Player player, Action action){
    	ResourceSet coins = new ResourceSet();
    	coins.addResource("COINS", 3);
    	System.out.println("TowerBusy:" + ((TowerRegion) board.getRegion(action.getActionRegionId())).isTowerBusy());
    	System.out.println("Compare to 3 Coins:" + player.getResources().compareTo(coins));
    	if (((TowerRegion) board.getRegion(action.getActionRegionId())).isTowerBusy() &&
    			player.getResources().compareTo(coins) < 0){
    		return false;
    	} return true;
    }
    //ValueActionCheck
    private boolean checkActionValue(Board board, Player player, Action action){
    	if(board.getRegion(action.getActionRegionId()).getActionSpace(action.getActionSpaceId())
    			.getActionValue() <= action.getActionValue())
    		return true;
    	else 
    		return false;
    }
    
    //CostCheck
    private boolean checkCost(Board board, Player player, Action action){
    	DevelopmentCard card =((TowerRegion) board.getRegion(action.getActionRegionId()))
    			.getTowerLayers()[action.getActionSpaceId()]
    					.getCard();
    	
    	ArrayList<ResourceSet> costCard = card.getCost();
    	ResourceSet requirements = card.getRequirments();
    	if(!requirements.getResourceSet().isEmpty()){
    		if(player.getResources().compareTo(requirements)>=0){
    			System.out.println("Player ha requirements!");
    			return true;
    		}
    		System.out.println("Player NON soddisfa requirements");
    		System.out.println(requirements.getResourceSet().keySet());
    		for (ResourceSet cost: costCard){
				System.out.println(cost.getResourceSet().keySet());
    			if(cost.getResourceSet().keySet().containsAll(requirements.getResourceSet().keySet())){
    				System.out.println(costCard.toString());
    				System.out.println("rimuovo il Requirements");
    				costCard.remove(cost);
    				System.out.println(costCard.toString());
    			}
    		}	
    	}
    	else {
    		System.out.println("CARTA SENZA REQUIREMENTS");
    	}
    	System.out.println(costCard.toString());
    	for(ResourceSet cost: costCard){
    	   if(player.getResources().compareTo(cost)>=0){
    		   System.out.println(cost.toString());
    		   System.out.println("Player OK!");
    		   return true;
    	   }
    	}
        System.out.println("Player NON RIESCE A PAGARE I COSTI");
    	return false;
    }   
    private boolean checkTerrytoryRequirement(Board board, Player player, Action action){
    	DevelopmentCard card =((TowerRegion) board.getRegion(action.getActionRegionId()))
    			.getTowerLayers()[action.getActionSpaceId()]
    					.getCard();
    	if(card.getType().equals("TERRITORYCARD")){
    		int point = player.getResources().getResource("MILITARY_POINTS");
    		switch(player.getPersonalBoard().getCardsOfType("TERRITORYCARD").size()){
    		case 2 : if(point >= 3){return true;}  else {return false;} 
    		case 3 : if(point >= 7){return true;}  else {return false;} 
    		case 4 : if(point >= 12){return true;} else {return false;}  
    		case 5 : if(point >= 18){return true;} else {return false;}  	
    		default : return true;
    		}
    	} return true;	
    }
    //CheckMaxCard
    private boolean checkMaxCard(Board board, Player player, Action action){
        String cardType = ((TowerRegion) board.getRegion(action.getActionRegionId()))
        		.getTowerLayers()[action.getActionSpaceId()].getCard().getType();
        if(player.getPersonalBoard().getCardsOfType(cardType).size() < 6){
        	return true;
        } return false;
    }
    
    //StandardCheck
    private boolean checkValidRegionID(Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId()) == null){
			return false;
		}
		return true;
	}
    private boolean checkValidActionSpaceID(Board board, Player player, Action action){	
		if(board.getRegion(action.getActionRegionId())
				.getActionSpace(action.getActionSpaceId()) == null){
			return false;
		}
		return true;
	}
    
    private boolean checkValidActionType(Board board, Player player, Action action){	
		switch (action.getActionType()){
		case "PRODUCTION" : {if(action.getActionRegionId()!=0){return false;} else {return true;}}
		case "HARVEST" : {if(action.getActionRegionId()!=1){return false;} else {return true;}}
		case "COUNCIL" : {if(action.getActionRegionId()!=2){return false;} else {return true;}}
		case "MARKET" : {if(action.getActionRegionId()!=3){return false;} else {return true;}}
		case "TOWER_GREEN" : {if(action.getActionRegionId()!=4){return false;} else {return true;}}
		case "TOWER_BLUE" : {if(action.getActionRegionId()!=5){return false;} else {return true;}}
		case "TOWER_YELLOW" : {if(action.getActionRegionId()!=6){return false;} else {return true;}}
		case "TOWER_PURPLE" : {if(action.getActionRegionId()!=7){return false;} else {return true;}}
		default : return false;
		}
	}
    
    private boolean firstCheck(Board board, Player player, Action action){
    	boolean result = true;
    	while(result){
			result = checkValidRegionID(board,player, action); // region ID valida
			System.out.println("/******** CHECK validRegionID:\n" + result);
			if(result==false){break;}
			result = checkValidActionSpaceID(board,player, action); // actionId valido
			System.out.println("/******** CHECK aCtionSpaceID:\n" + result);
			result = checkValidActionType(board,player, action);
			System.out.println("/******** CHECK validActionTYpe:\n" + result);
			break;
    	} return result;
    }
	// check da controllare sempre
    private boolean checkStandardMove(Board board, Player player, Action action){ 
    		boolean result = true;
    		while(result){
    			result = firstCheck(board,player, action);
    			result = checkIsFreeSingleSpace(board,player, action); // family member su free SingleSpace
    			System.out.println("/******** FreeSingleSpace: "+ result);
    			if(result==false){break;}
    			result = checkActionValue(board,player, action); // actionValue azione >= actionValue space
    			System.out.println("/******** ActionValue: " + result);
    			break;
    	} return result;
    }

    private boolean checkTowerMove(Board board, Player player, Action action){ 
		boolean result = true;
		while(result){
			result = checkFamilyColor(board,player, action); // region ID valida
			System.out.println("/******** CHECK FamilColor: PASSATO "+ result);
			if(!result)
				break;
			result = checkCoinForTribute(board,player, action);
			System.out.println("/******** CHECK COINS: PASSATO "+ result);
			if(!result)
				break;
			result = checkIsFreeSingleSpace(board,player, action); // actionId valido
			System.out.println("/******** CHECK FreeSingleSpace:" + result);
			if(!result)
				break;
			result = checkMaxCard(board,player, action);
			System.out.println("/******** CHECK MAxCard: "+ result );
			if(!result)
				break;
			result = checkTerrytoryRequirement(board,player, action); // family member su free SingleSpace
			System.out.println("/******** CHECK Terrytory" + result);
			if(!result)
				break;
			result = checkCost(board,player, action); // actionValue azione >= actionValue space
			System.out.println("/******** CHECK checkCost: " + result);
			break;
		}
		return result;
    }
    private boolean checkProductionHarvestMove(Board board, Player player, Action action){
    	boolean result = true;
    	while(result){
			result = checkFamilyColor(board,player, action); // region ID valida
			System.out.println("/******** CHECK FamilColor :"+ result);
			break;
    	} System.out.println("/******** CHECK FINE Step:"); 
    	return result;
    }
    
    // Check della Mossa
    public boolean checkMove(Board board, Player player, Action action){
    	System.out.println("/******** INIZIO CHECK");
    	boolean result = checkStandardMove(board,player, action);
    	System.out.println("/******** STANDARD CHECK result :\n" + result);
		if(action.getActionRegionId()<=1){
			System.out.println("/********Check production/Harvest");
			result = result && checkProductionHarvestMove(board,player, action);
		} if(action.getActionRegionId()>=4){
			System.out.println("/********Check TOWER");
			result = result && checkTowerMove(board,player, action);
		}System.out.println("/********Check FINITO result: " +result); 
		return result; 
    }
    
    // Simulatore della Mossa
    public void Simulate (Game game, Player player, Action action){
    	Board board = game.getBoard();
    	
    	//check Zone Bloccate per insufficiente numero di Giocatori
    	switch (game.getPlayerList().size()){
    	case 2 : if((action.getActionRegionId() <=1 && action.getActionSpaceId()==1) ||
    			(action.getActionRegionId() == 3 && action.getActionSpaceId() >= 2))
    			{return;} // zone bloccate: Harvest-1,Production-1 e Market 2-3
    	case 3 : if(action.getActionRegionId() == 3 && action.getActionSpaceId() >= 2)
				{return;} // zone bloccate Market 2-3
    	default : break;
    	}
    	if(firstCheck(board, player, action)){
    		Cloner cloner = new Cloner();
    		cloner.dontCloneInstanceOf(Effect.class); // Effetti non possono essere deepCopiati dalla libreria cloning
    		Board cloneBoard = cloner.deepClone(board);
    		Player clonePlayer = cloner.deepClone(player);
    		Action cloneAction = cloner.deepClone(action);
    		
    		//applico gli effetti sul player. Se ho effetti che negano l'azione ottengo una ImpossibleMoveException.
    		try{
    			for(Effect buff : player.getEffectList()){
    				buff.apply(cloneBoard, clonePlayer, cloneAction);
    			}
    		}catch(ImpossibleMoveException e){return;};
    		
    		//prendo il bonus dell'actionSpace
    		clonePlayer.getResources().addResource(cloneBoard.getRegion(cloneAction.getActionRegionId())
    				.getActionSpace(cloneAction.getActionSpaceId()).getBonus());
    		
    		// check l'azione
    		if(checkMove(cloneBoard, clonePlayer, cloneAction)){
    			
    			//effettuo l'azione
    			switch(cloneAction.getActionType()){
    			
    			case "PRODUCTION" : {
    				
    				// sommo i bonus della tile 
    				clonePlayer.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
    				
    				// move FamilyMember
    				int familyIndex = action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt(); // indice fm da spostare
    				cloneBoard.moveFamiliar(clonePlayer, familyIndex, cloneAction.getActionRegionId(), cloneAction.getActionSpaceId());
    				
    				//active cardEffect
    				LinkedList<DevelopmentCard> cardlist = new LinkedList<DevelopmentCard>();
    				cardlist = player.getPersonalBoard().getCardsOfType("BUILDINGCARD");
    				for (DevelopmentCard c : cardlist){
    					if(c.getMinimumActionvalue() > cloneAction.getActionValue()){
    						cardlist.remove(c);
    					}
    				}
    				//send Json with cardList to activate their effects
    				// receive Json with cardList to activate
    				// payload doppio array, primo cosa mettere dentro, secondo quello che esce fuori.
    				// pacchetto cambio di conbte
    				try{
    					for(DevelopmentCard c : cardlist){
    						c.getInstantEffect().apply(cloneBoard, clonePlayer, cloneAction);
    						//gestione dell'effetto change
    					}	
    				}catch(ImpossibleMoveException e){return;} // failed
    				// sostituisco copie con originali.
    			}
    			case "HARVEST" : {
    				
    				// sommo i bonus della tile 
    				clonePlayer.getResources().addResource(player.getPersonalBonusTile().getPersonalBonus()); 
    				
    				// move FamilyMember
    				int familyIndex = action.getAdditionalInfo().get("FAMILYMEMBER_ID").asInt(); // indice fm da spostare
    				cloneBoard.moveFamiliar(clonePlayer, familyIndex, cloneAction.getActionRegionId(), cloneAction.getActionSpaceId());
    				
    				//active cardEffect
    				LinkedList<DevelopmentCard> cardlist = new LinkedList<DevelopmentCard>();
    				cardlist = player.getPersonalBoard().getCardsOfType("TERRITORYCARD");
    				for(DevelopmentCard c : cardlist){
						try {
							c.getInstantEffect()
							.apply(cloneBoard, clonePlayer, cloneAction);
						} catch (ImpossibleMoveException e) {return;}
					}
    				// sostituisco copie con originali.
    			}
    			case "COUNCIL" : {}
    			case "MARKET" : {}
    			//case  of a "TOWER_GREEN""TOWER_BLUE""TOWER_YELLOW""TOWER_PURPLE" 
    			default : {}
    			
    			}
    			//sostituisco le copie con le originali
    			game.setPlayer(clonePlayer);
    			game.setBoard(cloneBoard);
    		}
    	}
    }
}