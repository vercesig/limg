package it.polimi.ingsw.GC_32.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Game.DevelopmentCard;
import it.polimi.ingsw.GC_32.Game.ExcommunicationCard;

public class Board {

	private static Board instance;
	
	private TowerRegion[] towerRegion;
	private ProductionRegion productionRegion;
	private HarvestRegion harvestRegion;
	private CouncilRegion councilRegion;
	
	private Deck ventureCardDeck;
	private Deck characterCardDeck;
	private Deck buildingCardDeck;
	private Deck territoryCardDeck;
	
	private ExcommunicationCard[] excommunicationCards;
	
	private ArrayList<Player> players;
	
	private int blackDiceValue = 0;
	private int orangeDiceValue = 0;
	private int whiteDiceValue = 0;
	
	
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
