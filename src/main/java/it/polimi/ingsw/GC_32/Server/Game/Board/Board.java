package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.ExcommunicationCard;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;



public class Board {

	private static Board instance;
	
	private TowerRegion[] towerRegion;
	private ProductionRegion productionRegion;
	private HarvestRegion harvestRegion;
	private CouncilRegion councilRegion;
	
	private Deck<DevelopmentCard> ventureCardDeck;
	private Deck<DevelopmentCard> characterCardDeck;
	private Deck<DevelopmentCard> buildingCardDeck;
	private Deck<DevelopmentCard> territoryCardDeck;
	
	private ExcommunicationCard[] excommunicationCards;
	
	private ArrayList<Player> players;
	
	
	private Board(){
		this.towerRegion = new TowerRegion[3]; //rendere scalabile
		this.excommunicationCards = new ExcommunicationCard[2];
		
		this.productionRegion = new ProductionRegion();
		this.harvestRegion = new HarvestRegion();
		this.councilRegion = new CouncilRegion();
	}
	
	public static Board newBoard(){
		if(instance==null){
			return new Board();
		}
		return instance;
	}
	
	public TowerRegion[] getTowerRegion(){
		return this.towerRegion;
	}
	
	public ProductionRegion getProductionRegion(){
		return this.productionRegion;
	}
	
	public HarvestRegion getHarvastRegion(){
		return this.harvestRegion;
	}
	
	public CouncilRegion getCouncilRegion(){
		return this.councilRegion;
	}
	
	public void setVentureCardDeck(Deck<DevelopmentCard> deck){
		this.ventureCardDeck = deck;
	}
	
	public void setBuildingCardDeck(Deck<DevelopmentCard> deck){
		this.ventureCardDeck = deck;
	}
	
	public void setTerritoryCardDeck(Deck<DevelopmentCard> deck){
		this.ventureCardDeck = deck;
	}
	
	public void setCharacterCardDeck(Deck<DevelopmentCard> deck){
		this.ventureCardDeck = deck;
	}
		
	public ExcommunicationCard getExcommunicationCard(int period){
		return this.excommunicationCards[period];
	}
	
	public void setExcommunicationCard(ExcommunicationCard card, int period){
		this.excommunicationCards[period] = card;
	}
	
	public void flushBoard(){
		
	}
	
}
