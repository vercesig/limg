package it.polimi.ingsw.GC_32.Server.Setup;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.*;
import it.polimi.ingsw.GC_32.Server.Game.Card.*;

/**
 * allows to configure all the elements of the game in their initial state. For example the preparation of the decks and the consequently associations of them
 * to the Board.
 * 
 * <p>
 * 
 * @see Game
 * @author alessandro 
 */
public class Setup {

	private Game game;
	
	/**
	 * the constructor encapsulates all the mechanisms which interest the game's status before the game is effectively started. In particular the constructor 
	 * apply on an istance of Game object all the operations which interest the card's management (both development card and excommunication cars) and the decks
	 * generation (like the import of the cards from an external file and the preparation of the decks accordint to the game's rule (each deck contains only card
	 * belonging to one specific type and is sorted by period))
	 *  
	 * @param game			the game which must be inizialized
	 * @throws IOException
	 */	
	public Setup(Game game) throws IOException{
		this.game = game;
		
		setUpCard();
		setUpTurnOrder();
		setUpPlayers();				
	}	
	
	/**
	 * this method encapsulate all the mechanisms which interest the management of the cards (both development card and excommunication card) before the 
	 * start of the game (like the import of the cards from an external file and the preparation of the decks. Each deck contains only card belonging to one 
	 * specific type, and, according to the rule of the game, each one is sorted by period). .
	 *  
	 * @see JsonImporter
	 * @throws IOException
	 */	
	private void setUpCard() throws IOException{
		// preparazione carte sviluppo
		FileReader developmentCardFile = new FileReader("src/resources/test.json");
		
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
		for(Map.Entry<String,List<DevelopmentCard>> element : tmpDecks.entrySet()){
			// strutture dati temporanee per la generazione dei mazzi
			HashMap<Integer, List<DevelopmentCard>> tmpSubDecks = new HashMap<Integer, List<DevelopmentCard>>();
			List<DevelopmentCard> tmp = new ArrayList<DevelopmentCard>();
			
			element.getValue().forEach(card -> {
					if(tmpSubDecks.keySet().contains(card.getPeriod())){
						tmpSubDecks.get(card.getPeriod()).add(card);
					}else{
						tmpSubDecks.put(card.getPeriod(), new ArrayList<DevelopmentCard>());
						tmpSubDecks.get(card.getPeriod()).add(card);
					}
				});
				// crea i vari sotto-mazzi, li mischia e li carica nel mazzo conclusivo (ordinato per periodi crescenti)
				for(int i=1; i<=tmpSubDecks.size(); i++){
					Deck<DevelopmentCard> subDeck = new Deck<DevelopmentCard>(tmpSubDecks.get(i));
					subDeck.shuffleDeck();
					tmp.addAll(subDeck.getDeck());
				}
				Deck<DevelopmentCard> finalDeck = new Deck<DevelopmentCard>(tmp);
				game.setDeck(element.getKey(), finalDeck);// setta il mazzo risultante in game
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
		FileReader excommunicationCardFile = new FileReader("src/resources/testscomunica.json");
		Deck<ExcommunicationCard> excommunicationCardDeck = new Deck<ExcommunicationCard>(JsonImporter.importExcommunicationCard(excommunicationCardFile));
		
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
			Deck<ExcommunicationCard> tmpDeck = new Deck<ExcommunicationCard>(tmpSubDecks.get(i));
			game.setExcommunicationCard(tmpDeck.drawRandomElement(), i);
		}
	}
	
	/**
	 * according to the game rule, during the first round turn order is choosen randomly. this method perform such operation. After the method has been applied
	 * the order of the player, memorized in the object Game, is randomly setted.
	 */
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
	
	/**
	 * this method set the resources and the scores of each player at their initial value (considering the random turn order as well).
	 */
	private void setUpPlayers(){
		//TODO: associare PersonalBonusTile al giocatore
		ArrayList<Player> players = game.getPlayerList();
		for(int i=0; i<players.size(); i++){
			players.get(i).getResources().setResource("WOOD", 2);
			players.get(i).getResources().setResource("STONE", 2);
			players.get(i).getResources().setResource("SERVANTS", 3);
			// in base all'ordine di turno assegno le monete iniziali
			players.get(i).getResources().setResource("COINS", 5 + i);
			// setta punteggi a 0
			players.get(i).getResources().setResource("FAITH", 0);
			players.get(i).getResources().setResource("VICTORY", 0);
			players.get(i).getResources().setResource("MILITARY", 0);
			
		}
	}
}
