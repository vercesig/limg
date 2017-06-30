package it.polimi.ingsw.GC_32.Server.Game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.LinkedList;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;
import it.polimi.ingsw.GC_32.Server.Network.ServerMessageFactory;

public class EndPhase {

	public EndPhase(){}
	
	public static int countBuildingCost(ResourceSet cost){
		int wood = 0;
		int stone = 0;
		try{
			wood = cost.getResource("WOOD");
		}
		catch(NullPointerException e){}
		try{
			stone = cost.getResource("STONE");
		}
		catch(NullPointerException e){}
		return wood + stone;
	}
	
	public static void endGame(Game game){
		
		Reader path = new InputStreamReader(game.getClass().getClassLoader().getResourceAsStream("score_conversion.json"));
		try {
			JsonObject json = Json.parse(path).asObject();
			JsonObject finalScore = new JsonObject();
			int firstMilitary = json.get("FIRSTMILITARY").asInt();
			int secondMilitary = json.get("SECONDMILITARY").asInt();
			int conversion = json.get("RESOURCE").asInt();
			
			LinkedList<Player> militaryScore = new LinkedList<>();
			game.getPlayerList().forEach(player -> {militaryScore.add(player);});
			militaryScore.sort(new Comparator<Player>(){ // ordino questa lista per player con piu' military_poits
				@Override
				public int compare(Player p1, Player p2) {
					return ((Integer)p1.getResources()
							           .getResource("MILITARY_POINTS"))
							           .compareTo((Integer)p2
							        		   		.getResources()
							        		   		.getResource("MILITARY_POINTS"));
				};    
			});
			for(int i=0; i < militaryScore.size()-2; i++){
				militaryScore.removeLast(); // ipotizzando che il sort abbia ordinato in modo crescente.
			}
			game.getPlayerList().forEach( player -> {
				
				// carte impresa
				if(!player.getExcomunicateFlag().contains("NOENDPURPLE")){
					LinkedList <DevelopmentCard> ventureCard = player.getPersonalBoard().getCardsOfType("VENTURECARD");
					for (DevelopmentCard card: ventureCard){
						card.getPermanentEffect().forEach(effect -> effect.apply(game.getBoard(), player, null, null));
					}
				}	
				int score = player.getResources().getResource("VICTORY_POINTS");
				
				try{
					// carte territorio
					if(!player.getExcomunicateFlag().contains("NOENDGREEN")){
						score =+ json.get("TERRITORYCARD").asObject()
												    .get( ((Integer)player.getPersonalBoard()
							    .getCardsOfType("TERRITORYCARD").size()).toString()).asInt(); 
					}
				} catch (NullPointerException e){}
					
				// carte personaggio
				
				try{
					if(!player.getExcomunicateFlag().contains("NOENDBLUE")){
						score =+ json.get("CHARACTERCARD").asObject()
							    					.get( ((Integer)player.getPersonalBoard()
							    					.getCardsOfType("CHARACTERCARD").size()).toString()).asInt(); 
					}
				} catch (NullPointerException e){}
				
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
				if(player.getExcomunicateFlag().contains("LESSFORRESOURCE")){
					score -= resource;
				}
				
				//perdi un punto vittoria per ogni 5 punti vittoria
				if(player.getExcomunicateFlag().contains("LESSFORVICTORY")){
					score -= score/5;
				}
				
				//perdi un punto vittoria per ogni punto militare
				if(player.getExcomunicateFlag().contains("LESSFORMILITARY")){
					score -= player.getResources().getResource("MILITARY_POINTS");
				}

				// perdi un punto vittoria per ogni wood e stone nei costi carte building
				if(player.getExcomunicateFlag().contains("LESSFORBUILDING")){
					for(DevelopmentCard card : player.getPersonalBoard().getCardsOfType("BUILDINGCARD")){
						score -= countBuildingCost(card.getCost().get(0)); // Carte Building hanno un solo costo
					}
				}
				finalScore.add(player.getID(), score); // jsonObject
			});
			MessageManager.getInstance().sendMessge(ServerMessageFactory.buildENDGAMEmessage(game, finalScore));
			
		} catch (IOException e) {}	
	}
}
