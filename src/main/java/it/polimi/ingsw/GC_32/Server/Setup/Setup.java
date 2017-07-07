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

import it.polimi.ingsw.GC_32.Server.Game.GameConfig;
import it.polimi.ingsw.GC_32.Server.Game.Board.*;
import it.polimi.ingsw.GC_32.Server.Game.Card.*;

/**
 * this class is responsable of all the intial load of configuration files, so the configuration file loading is performed only once time and all the information 
 * parsed by those configuration files are loaded into the singleton class GameConfig.
 * 
 * configuration files are formatted in JSON.
 * 
 * 
 * @see GameConfig
 */
public class Setup {
	
	private final static Logger LOGGER = Logger.getLogger(Setup.class.getName());
	
	/**
	 * given the file name, loadCard import the file to parse
	 * @param path the file name
	 * @throws IOException
	 */
	
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
		
		Deck<DevelopmentCard> developmentCardDeck = new Deck<DevelopmentCard>(JsonImporter.importDevelopmentCards(developmentCardFile));
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
		Reader excommunicationCardFile = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("excom_cards.json"));
		Deck<ExcommunicationCard> excommunicationCardDeck = new Deck<ExcommunicationCard>(JsonImporter.importExcommunicationCards(excommunicationCardFile));
		
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
		// Carte Leader
		Reader leaderCardFile = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("leader_cards.json"));
		Deck<LeaderCard> leaderDeck = new Deck<>(JsonImporter.importLeaderCards(leaderCardFile)); 
		CardRegistry.getInstance().registerDeck(leaderDeck);
	}
	
	/**
	 * this method load the personalBonusTile from an external JSON file and then load it into the GameConfig class
	 * 
	 * @see GameConfig
	 * @param path the file name
	 * @throws IOException
	 */
	public void loadBonusTile(String path) throws IOException{
		Reader bonusTileReader = new InputStreamReader(this.getClass()
													 .getClassLoader()
													 .getResourceAsStream(path));
		GameConfig.getInstance().registerBonusTile(JsonImporter.importPersonalBonusTile(bonusTileReader));
	}
	
	
	/**
	 * this method load all the configurable action spaces' bonuses from an external JSON file and then load it into the GameConfig class
	 * 
	 * @see GameConfig
	 * @param path the file name
	 * @throws IOException
	 */
	public void loadBonusSpace(String path) throws IOException {
		Reader bonusSpaceReader =  new InputStreamReader(this.getClass()
													  .getClassLoader()
													  .getResourceAsStream(path));
		GameConfig.getInstance().registerBonusSpace(JsonImporter.importBonusSpace((bonusSpaceReader)));
	}
	
	
	/**
	 * this method load the configuration of victory points on the faith track from an external JSON file and then load it into the GameConfig class
	 * 
	 * @see GameConfig
	 * @param path the file name
	 * @throws IOException
	 */
	public void loadExcommunicationTrack(String path) throws IOException {
		Reader fileReader = new InputStreamReader(this.getClass()
															.getClassLoader()
															.getResourceAsStream(path));
		GameConfig.getInstance().registerExcommunicationTrack(JsonImporter
																.importExcommunicationTrack(fileReader));
	}
	
	
	/**
	 * this method load all the rules applied for the final score calculation from an external JSON file and then load it into the GameConfig class
	 * 
	 * @see GameConfig
	 * @param path the file name
	 * @throws IOException
	 */
	public void loadConversionPoints(String path) throws IOException {
		Reader fileReader = new InputStreamReader(this.getClass()
															.getClassLoader()
															.getResourceAsStream(path));
		GameConfig.getInstance().registerPointsConversion(JsonImporter
																.importPointsConversion((fileReader)));
	}
}
