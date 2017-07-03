package it.polimi.ingsw.GC_32.Server.Game;

import java.util.UUID;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.LeaderCard;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;

public class LeaderUtils {
	
	public LeaderUtils(){}
	
	public static boolean checkLeaderMove(UUID playerUUID, String nameCard, String decision){
		
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
    			if(!leaderCard.isOnTheGame() && !leaderCard.hasATokenAbility()){
    				return false;
    			}
    			if(leaderCard.getInstantEffect()!=null){ //ha senso attivare l'effetto
    				return true;
    			}
    			return false;

    		case "PLAY":
    			if(hasRequirements(playerUUID, leaderCard) && !leaderCard.isOnTheGame()){
    				leaderCard.playCard();
    				// Attivo l'effetto Flag della carta
    				if(leaderCard.getFlagEffect()!=null){
    					leaderCard.getFlagEffect().apply(null, GameRegistry.getInstance().getPlayerFromID(playerUUID)
    							, null, null);
    				}
    				// Attivo l'effetto permanente della carta
    				if(leaderCard.getPermanentEffect()!=null){
    					GameRegistry.getInstance().getPlayerFromID(playerUUID).addEffect(leaderCard.getPermanentEffect().get(0));
    				}
    				return true;
    			}
    			return false;
    		default:
    			return false;
		}
	}

	private static boolean hasRequirements(UUID playerUUID, LeaderCard leader){
		Player player = GameRegistry.getInstance().getPlayerFromID(playerUUID);
		JsonObject requirements = leader.getRequirements();
		JsonValue jCard = requirements.get("CARDTYPE");
        if(jCard != null && !jCard.isNull()){
            JsonObject cardType = requirements.get("CARDTYPE").asObject();
            for(Member item : cardType){
                if( player.getPersonalBoard().getCards().get(item.toString()).size() 
                        < item.getValue().asInt()){
                            System.out.println("CARTE INSUFFICIENTI");
                            return false;
                }
            }
        }
        JsonValue jResource = requirements.get("RESOURCE");
        if(jResource != null && !jResource.isNull()){
            if(player.getResources().compareTo(new ResourceSet(requirements.get("RESOURCE").asObject()))<0){
                System.out.println("RISORSE INSUFFICIENTI");
                return false;
            }
        }
		return true;
	}
}
