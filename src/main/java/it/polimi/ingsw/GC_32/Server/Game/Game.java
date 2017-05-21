package Model;

import java.util.ArrayList;

public class Game {

	private ArrayList<Player> playerList;
	private Board board;
	
	private Deck<DevelopmentCard> ventureCardDeck;
	private Deck<DevelopmentCard> characterCardDeck;
	private Deck<DevelopmentCard> buildingCardDeck;
	private Deck<DevelopmentCard> territoryCardDeck;
	
	private ExcommunicationCard[] excommunicationCards;
	
	private int blackDice;
	private int whiteDice;
	private int orangeDice;
	
	public Game(){
		this.playerList = new ArrayList<Player>();
		this.board = new Board();
		this.excommunicationCards = new ExcommunicationCard[2];
	}
	
	public ArrayList<Player> getPlayerList(){
		return this.playerList;
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public Deck<DevelopmentCard> getVentureCardDeck(){
		return this.ventureCardDeck;
	}
	
	public Deck<DevelopmentCard> getCharacterCardDeck(){
		return this.characterCardDeck;
	}
	
	public Deck<DevelopmentCard> getBuildingCardDeck(){
		return this.buildingCardDeck;
	}
	
	public Deck<DevelopmentCard> getTerritoryCardDeck(){
		return this.territoryCardDeck;
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
	
	public void setExcommunicationCard(ExcommunicationCard card, int period){
		this.excommunicationCards[period] = card;
	}
	
	public ExcommunicationCard getExcommunicationCard(int period){
		return this.excommunicationCards[period];
	}
	
	public int getBlackDiceValue(){
		return this.blackDice;
	}
	
	public int getWhiteDiceValue(){
		return this.whiteDice;
	}
	
	public int getOrangeDiceValue(){
		return this.orangeDice;
	}
	
	public void setBlackDiceValue(int value){
		this.blackDice = value;
	}
	
	public void setWhiteDiceValue(int value){
		this.whiteDice = value;
	}
	
	public void setOrangeDiceValue(int value){
		this.orangeDice = value;
	}
	
}
