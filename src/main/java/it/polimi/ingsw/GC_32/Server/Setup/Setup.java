package it.polimi.ingsw.GC_32.Server.Setup;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.GC_32.Server.Game.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.*;
import it.polimi.ingsw.GC_32.Server.Game.Card.*;

public class Setup {

	private Game game;
	
	private void setUpCard() throws IOException{
		// preparazione carte sviluppo
		FileReader developmentCardFile = new FileReader("/home/alessandro/Scrivania/test.json");
		
		Deck<DevelopmentCard> developmentCardDeck = new Deck(JsonImporter.importDevelopmentCard(developmentCardFile));
		HashMap<String, List<DevelopmentCard>> tmpDecks = new HashMap<String, List<DevelopmentCard>>();
		
		//suddivide i mazzi per tipologia di carta
		developmentCardDeck.getDeck().forEach(card -> {
			String type = card.getType();
			if(tmpDecks.keySet().contains(type)){
				tmpDecks.get(type).add(card);
			}else{
				tmpDecks.put(type, new ArrayList<DevelopmentCard>());
				tmpDecks.get(type).add(card);
			}
		});	
		// crea i mazzi e li carica in game
		for(String type : tmpDecks.keySet()){
			// strutture dati temporanee per la generazione dei mazzi
			HashMap<Integer, List<DevelopmentCard>> tmpSubDecks = new HashMap<Integer, List<DevelopmentCard>>();
			List<DevelopmentCard> tmp = new ArrayList<DevelopmentCard>();
			
			tmpDecks.get(type).forEach(card -> {
					if(tmpSubDecks.keySet().contains(card.getPeriod())){
						tmpSubDecks.get(card.getPeriod()).add(card);
					}else{
						tmpSubDecks.put(card.getPeriod(), new ArrayList<DevelopmentCard>());
						tmpSubDecks.get(card.getPeriod()).add(card);
					}
				});
				// crea i vari sotto-mazzi, li mischia e li carica nel mazzo conclusivo (ordinato per periodi crescenti)
				for(int i=1; i<=tmpSubDecks.size(); i++){
					Deck<DevelopmentCard> subDeck = new Deck(tmpSubDecks.get(i));
					subDeck.shuffleDeck();
					tmp.addAll(subDeck.getDeck());
				}
				Deck<DevelopmentCard> finalDeck = new Deck(tmp);
				game.setDeck(type, finalDeck);// setta il mazzo risultante in game
		}
		
		// configura le torri
		Object[] types = tmpDecks.keySet().toArray();
		String[] cardTypes = new String[types.length];
		game.getBoard().setTowerRegion(types.length);
		for(int j=0; j<cardTypes.length; j++){
			cardTypes[j] = (String)types[j];
		}
		for(int i=0; i<game.getBoard().getTowerRegion().length; i++){
			game.getBoard().getTowerRegion()[i].setTypeCard(cardTypes[i]);
		}
		
		// preparazione carte scomunica
		FileReader excommunicationCardFile = new FileReader("/home/alessandro/Scrivania/testscomunica.json");
		Deck<ExcommunicationCard> excommunicationCardDeck = new Deck(JsonImporter.importExcommunicationCard(excommunicationCardFile));
		
		HashMap<Integer, List<ExcommunicationCard>> tmpSubDecks = new HashMap<Integer, List<ExcommunicationCard>>();
		// divido carte scomunica per periodo
		excommunicationCardDeck.getDeck().forEach(card -> {
			if(tmpSubDecks.keySet().contains(card.getPeriod())){
				tmpSubDecks.get(card.getPeriod()).add(card);
			}else{
				tmpSubDecks.put(card.getPeriod(), new ArrayList<ExcommunicationCard>());
				tmpSubDecks.get(card.getPeriod()).add(card);
			}
		});
		// carica per ogni periodo una carta scomunica scelta a caso
		for(int i=1; i<=3; i++){ // --------------------------------- caricare il numero di periodi da file di configurazione
			Deck<ExcommunicationCard> tmpDeck = new Deck(tmpSubDecks.get(i));
			game.setExcommunicationCard(tmpDeck.drawRandomElement(), i);
		}
	}
	
	// imposta casualmente l'ordine di turno iniziale
	private void setUpTurnOrder(){
		ArrayList<Player> tmpPlayerList = game.getPlayerList();
		Random randomGenerator = new Random();
		ArrayList<Player> startPlayerOrder = new ArrayList<Player>();
		int numberOfPlayers = tmpPlayerList.size();
		
		for(int i=0; i<numberOfPlayers; i++){
			int randomNumber = randomGenerator.nextInt(tmpPlayerList.size());
			startPlayerOrder.add(tmpPlayerList.get(randomNumber));
			tmpPlayerList.remove(randomNumber);
		}
		game.setPlayerOrder(startPlayerOrder);
	}
	
	//TODO: associare PersonalBonusTile al giocatore
	private void setUpPlayers(){
		ArrayList<Player> players = game.getPlayerList();
		for(int i=0; i<players.size(); i++){
			players.get(i).getResources().setResource("WOOD", 2);
			players.get(i).getResources().setResource("STONE", 2);
			players.get(i).getResources().setResource("SERVANTS", 3);
			// in base all'ordine di turno assegno le monete iniziali
			players.get(i).getResources().setResource("COINS", 5 + i);
			// setta punteggi a 0
			players.get(i).getResources().setResource("FAITH", 0);
			players.get(i).getResources().setResource("VP", 0);
			players.get(i).getResources().setResource("MILITARY", 0);
			
		}
	}
	
	public Setup(Game game) throws IOException{
		this.game = game;
		
		setUpCard();
		setUpTurnOrder();
		setUpPlayers();				
	}
	
	public static void main(String[] args) throws IOException{
		
		Player a1 = new Player("cazzuto");
		Player a2 = new Player("stronzo");
		Player a3 = new Player("spennato");
		
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		playerList.add(a1);
		playerList.add(a2);
		playerList.add(a3);
		
		
		Game game = new Game(playerList);
		Setup setupGame = new Setup(game);
		
		System.out.println("deck trovato: ");
		System.out.println(game.getDeck("bbb").toString());
		
		for(Player p : game.getPlayerList()){
			System.out.println(p.getName());
		}
		
		System.out.println("risorse: "+ a1.getResources().toString());
		
		System.out.println(game.getExcommunicationCard(1).toString());
		
	}

	
}
