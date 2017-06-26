package it.polimi.ingsw.GC_32.Server.Setup;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Server.Game.CardRegistry;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.GameConfig;
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
	
	private final static Logger LOGGER = Logger.getLogger(Setup.class.getName());
	
	public void loadCard(String path) throws IOException{
		this.loadCard(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(path)));
	}
	
	/**
	 * this method encapsulate all the mechanisms which interest the management of the cards (both development card and excommunication card) before the 
	 * start of the game (like the import of the cards from an external file and the preparation of the decks. Each deck contains only card belonging to one 
	 * specific type, and, according to the rule of the game, each one is sorted by period). .
	 *  
	 * @see JsonImporter
	 * @throws IOException
	 */
	public void loadCard(Reader inputReader) throws IOException{
		// preparazione carte sviluppo
		LOGGER.log(Level.INFO, "loading development card...");
		Reader developmentCardFile = inputReader;
		
		Deck<DevelopmentCard> developmentCardDeck = new Deck<DevelopmentCard>(JsonImporter.importDevelopmentCard(developmentCardFile));
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
		// crea i mazzi e li carica nel CardRegistry
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
				CardRegistry.getInstance().registerDeck(element.getKey(), finalDeck);
		}
		LOGGER.log(Level.INFO, "development card correctly loaded into CardRegistry");
		LOGGER.log(Level.INFO, "loading excommunication card...");
		
		// preparazione carte scomunica
		Reader excommunicationCardFile = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("testscomunica.json"));
		//FileReader excommunicationCardFile = new FileReader("src/resources/testscomunica.json");
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
		// carica nel CardRegistry il mazzo di carte scomunica assouciato al rispettivo periodo
		for(int i=1; i<=3; i++){ // --------------------------------- caricare il numero di periodi da file di configurazione
			Deck<ExcommunicationCard> tmpDeck = new Deck<ExcommunicationCard>(tmpSubDecks.get(i));
			CardRegistry.getInstance().registerDeck(i,tmpDeck);
		}
		LOGGER.log(Level.INFO, "excommunication card correctly loaded into CardRegistry");		
	}
	
	public void loadBonusTile(Reader bonusTileReader) throws IOException{
		GameConfig.getInstance().registerBonusTile(JsonImporter.importPersonalBonusTile(bonusTileReader));
	}
	
	public void loadBonusTile(String path) throws IOException{
		this.loadBonusTile(new InputStreamReader(this.getClass()
													 .getClassLoader()
													 .getResourceAsStream(path)));
	}
}
