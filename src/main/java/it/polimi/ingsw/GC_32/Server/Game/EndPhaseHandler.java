package it.polimi.ingsw.GC_32.Server.Game;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

public class EndPhaseHandler{
    private Game game;
    
	public EndPhaseHandler(Game game){
	    this.game = game;
	}
	
	public static int countBuildingCost(ResourceSet cost){
		int wood = 0;
		int stone = 0;
		if(cost.hasResource("WOOD")){
		    wood = cost.getResource("WOOD");
		}
		if(cost.hasResource("STONE")){
			stone = cost.getResource("STONE");
		}
		return wood + stone;
	}
	
	public void endGame(){
			HashMap <String, JsonValue> json = GameConfig.getInstance().getPointsConversion();
			JsonObject finalScore = new JsonObject();
			int firstMilitary = json.get("FIRSTMILITARY").asInt();
			int secondMilitary = json.get("SECONDMILITARY").asInt();
			int conversion = json.get("RESOURCE").asInt();
			
			LinkedList<Player> militaryScore = new LinkedList<>();
			game.getPlayerList().forEach(player -> {militaryScore.add(player);});
			militaryScore.sort(new Comparator<Player>(){ // ordino questa lista per player con piu' military_poits
				@Override  // DA TESTARE
				public int compare(Player p1, Player p2) {
					return ((Integer)p1.getResources()
							           .getResource("MILITARY_POINTS"))
							           .compareTo((Integer)p2
							        		   		.getResources()
							        		   		.getResource("MILITARY_POINTS"));
				}
			});
			for(int i=0; i < militaryScore.size()-2; i++){
				militaryScore.removeLast(); // ipotizzando che il sort abbia ordinato in modo crescente.
			}
			
			game.getPlayerList().forEach(player -> { // aggiungo i faith points dei player (solo quelli che non hanno mostrato 
													 // la propria fede al papa hanno faithPoints diversi da 0)
				int faithPoints = player.getResources().getResource("FAITH_POINTS");
				player.getResources().addResource("VICTORY_POINTS", faithPoints);
				player.getResources().setResource("FAITH_POINTS", 0);
			});
			
			game.getPlayerList().forEach( player -> {
				
				// carte impresa
				if(!player.isFlagged("NOENDPURPLE")){
					LinkedList <DevelopmentCard> ventureCard = player.getPersonalBoard().getCardsOfType("VENTURECARD");
					for (DevelopmentCard card: ventureCard){
						card.getPermanentEffect().forEach(effect -> effect.apply(game.getBoard(), player, null, null));
					}
				}
				int score = player.getResources().getResource("VICTORY_POINTS");
				
				// carte territorio
				if(!player.isFlagged("NOENDGREEN")){
                    JsonValue jTerrCard = json.get("TERRITORYCARD");
                    if(jTerrCard != null){
                        score =+ jTerrCard.asObject().get(Integer.toString(player.getPersonalBoard()
                                                                                 .getCardsOfType("TERRITORYCARD")
                                                                                 .size())).asInt(); 
                    }
                }
                    
                // carte personaggio
                if(!player.isFlagged("NOENDBLUE")){
                    JsonValue jCharCard = json.get("CHARACTERCARD");
                    if(jCharCard != null){
                        score =+ jCharCard.asObject().get(Integer.toString(player.getPersonalBoard()
                                                                                 .getCardsOfType("CHARACTERCARD")
                                                                                 .size())).asInt();
                    }
                }
				
				// military
				if(militaryScore.getFirst().getUUID().equals(player.getUUID())){
					score = score + firstMilitary;
					} 
				else
					if(militaryScore.getLast().getUUID().equals(player.getUUID())){
				
						score = score + secondMilitary;
					}
				
				//resource
				int resource = player.getResources().getResource("COINS");
				resource += player.getResources().getResource("WOOD");
				resource += player.getResources().getResource("STONE");
				resource += player.getResources().getResource("SERVANTS");
				
				score += resource/conversion;
				
				//perdi un punto vittori per ogni risorsa
				if(player.isFlagged("LESSFORRESOURCE")){
					score -= resource;
				}
				
				//perdi un punto vittoria per ogni 5 punti vittoria
				if(player.isFlagged("LESSFORVICTORY")){
					score -= score/5;
				}
				
				//perdi un punto vittoria per ogni punto militare
				if(player.isFlagged("LESSFORMILITARY")){
					score -= player.getResources().getResource("MILITARY_POINTS");
				}

				// perdi un punto vittoria per ogni wood e stone nei costi carte building
				if(player.isFlagged("LESSFORBUILDING")){
					for(DevelopmentCard card : player.getPersonalBoard().getCardsOfType("BUILDINGCARD")){
						score -= countBuildingCost(card.getCost().get(0)); // Carte Building hanno un solo costo
					}
				}
				finalScore.add(player.getID(), score); // jsonObject
			});
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildENDGAMEmessage(game, finalScore));	
	}
}
