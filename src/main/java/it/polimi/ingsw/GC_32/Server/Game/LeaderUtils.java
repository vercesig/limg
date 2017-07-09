package it.polimi.ingsw.GC_32.Server.Game;

import java.util.UUID;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Card.LeaderCard;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;

/**
 *	Class used to manage the logic of the leader cards. it has static methods for utilities 
*/

public class LeaderUtils {
	
	public LeaderUtils(){}
	
	/**
	 * Checks if a client request to make a leader action it is possible. 
	 *
	 * @param playerUIID of the player who sent the message
	 * @param name of the leader card used for an action
	 * @param type of the action
	 * @param board instance used to apply effects
	 * @param context manager instance used to specific context
	 * @return boolean value of the check
	 */
	public static boolean checkLeaderMove(UUID playerUUID, String nameCard, String decision, Board board, ContextManager cm){
		
		Player p = GameRegistry.getInstance().getPlayerFromID(playerUUID);
		int index = 0;
		
		for(int i=0; i<p.getPersonalBoard().getLeaderCards().size(); i++){
			if(p.getPersonalBoard().getLeaderCards().get(i).getName().equals((nameCard))){
				System.out.println("TROVATA: " + p.getPersonalBoard().getLeaderCards().get(i).getName());
				index = i;
			}
		}
		LeaderCard leaderCard = p.getPersonalBoard().getLeaderCards().get(index);
		switch(decision){
    		case "DISCARD":
    			if(leaderCard.isOnTheGame()){
    				System.out.println("AZIONE NON CONSENTITA, CARTA GIA' GIOCATA"); 
    				return false;
    			} 
    			return true;

    		case "ACTIVATE":
    			if(!leaderCard.isOnTheGame() || !leaderCard.hasATokenAbility()){
    				return false;
    			}
    			if(!leaderCard.getInstantEffect().isEmpty()){ //ha senso attivare l'effetto
    				System.out.println("Player prima dell'activate: " + p); 
    				Action a = new Action("HARVEST", 0, 0, 0);
    				leaderCard.getInstantEffect().get(0).apply(board, p, a, cm);
    				System.out.println("Player prima dell'activate: " + p); 
    				leaderCard.turnCard(false);
    				return true;
    			}
    			return false;

    		case "PLAY":
    			if(hasRequirements(playerUUID, leaderCard) && !leaderCard.isOnTheGame()){
    				System.out.println("CARTA GIOCATA CORRETTAMENTE"); 
    				System.out.println(leaderCard);
    				leaderCard.playCard();
    				// Attivo l'effetto Flag della carta
    				if(leaderCard.getFlagEffect()!=null){
    					leaderCard.getFlagEffect().apply(null, GameRegistry.getInstance().getPlayerFromID(playerUUID)
    							, null, null); 

    				}
    				// Attivo l'effetto permanente della carta
    				if(!leaderCard.getPermanentEffect().isEmpty()){
    					System.out.println("PRIMA DELLA GIOCATA: " + GameRegistry.getInstance().getPlayerFromID(playerUUID).getEffectList());
    					GameRegistry.getInstance().getPlayerFromID(playerUUID).addEffect(leaderCard.getPermanentEffect().get(0));
    					System.out.println("DOPO DELLA GIOCATA: " + GameRegistry.getInstance().getPlayerFromID(playerUUID).getEffectList());	
    				}
    				return true;
    			}
    			return false;
    		default:
    			return false;
		}
	}

	/**
	 * It checks if the requirements of a leader cards are meet for a player
	 * @param playerUUID of the player
	 * @param leader card object of the card used for the leader action
	 * @return boolean value of this test
	 */
	
	private static boolean hasRequirements(UUID playerUUID, LeaderCard leader){
		Player player = GameRegistry.getInstance().getPlayerFromID(playerUUID);
		JsonObject requirements = leader.getRequirements();
		JsonValue jCard = requirements.get("CARDTYPE");
        if(jCard != null){
        	if(requirements.get("CARDTYPE").isObject()){
	            JsonObject cardType = requirements.get("CARDTYPE").asObject();
	            for(Member item : cardType){
	            	if(player.getPersonalBoard().getCards().get(item.getName())==null){
		            	System.out.println("no presente:" + item.getName());
	            		return false;
	               }
	               System.out.println("CARTE Player: " + (player.getPersonalBoard().getCards().get(item.getName()).size()  + " Carte requirements: " + item.getValue().asInt()));
	
	               if(player.getPersonalBoard().getCards().get(item.getName()).size() 
		                        < item.getValue().asInt()){
                     return false;
	           }
              }	
        	}
        	// there are 6 card of a type
        	if(requirements.get("CARDTYPE").isArray()){
        	JsonArray array = requirements.get("CARDTYPE").asArray();
        	for(JsonValue item : array){
        		JsonObject value = item.asObject();
        		for(Member r: value){
        			if(player.getPersonalBoard().getCards().get(r.getName())!=null && 
        			   player.getPersonalBoard().getCards().get(r.getName()).size()==6){
                		System.out.println("SI 6 carte.");
        				return true;
        			}
        		}
        	}	
        	System.out.println("no 6 carte.");
        	return false;
        	}
        }
        if(requirements.get("RESOURCE")!=null){
            System.out.println("RISORSE: "+new ResourceSet(requirements.get("RESOURCE").asObject()).toString());
        	if(player.getResources().compareTo(new ResourceSet(requirements.get("RESOURCE").asObject()))<0){
                System.out.println("RISORSE INSUFFICIENTI");
                return false;
            }
        }
		return true;
	}
}
