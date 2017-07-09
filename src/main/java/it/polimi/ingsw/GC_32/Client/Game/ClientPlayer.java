package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.polimi.ingsw.GC_32.Client.Track;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

/**
 * this class is the client-side representation of the server-side concept of Player. Because client only show information on the screen, the information contained into 
 * this class (like all the classes of the client-side game model) is really less then the server-side equivalent class.
 * 
 * <ul>
 * <li>{@link ClientPlayer#playerResources}: resources of this player</li>
 * <li>{@link #cards}: cards owned by the player (only the card name is memorized)</li>
 * <li>{@link #name}: the name of this player</li>
 * <li>{@link #familyMembers}: the family memebers owned by the player</li>
 * <li>{@link #bonusTile}: string representation of bonus tile</li>
 * <li>{@link #track}: the position of this player on the score tracks, each track contains the points quantity owned by the player</li> 
 * </ul>
 *
 * @see ResourceSet, ClientFamilyMember, Track
 */
public class ClientPlayer {

	private ResourceSet playerResources;
	private HashMap<String, ArrayList<String>> cards;
	private String name;
	private ClientFamilyMember[] familyMembers = new ClientFamilyMember[4];
	private String bonusTile;
	private Track[] track = new Track[3];
	
	/**
	 * set up the player
	 */
	public ClientPlayer(){
		this.playerResources = new ResourceSet();
		this.cards = new HashMap<String, ArrayList<String>>();
		
		this.playerResources.setResource("WOOD", 0);
		this.playerResources.setResource("COINS", 0);
		this.playerResources.setResource("SERVANTS", 0);
		this.playerResources.setResource("STONE", 0);
		this.playerResources.setResource("MILITARY_POINTS", 0);
		this.playerResources.setResource("VICTORY_POINTS", 0);
		this.playerResources.setResource("FAITH_POINTS", 0);
		
		track[0] = new Track("MILITARY_POINTS");
		track[1] = new Track("FAITH_POINTS");
		track[2] = new Track("VICTORY_POINTS");
		
		for(int i=0; i<this.track.length; i++){
			track[i].registerResourceSet(this.playerResources);
		}
		
		for(int i=0; i<familyMembers.length; i++){
			familyMembers[i] = new ClientFamilyMember();
		}
		
	}
	
	/**
	 * register the player name
	 * @param name the name of the player to be registered
	 */
	public void setName(String name){
		this.name = name;
		for(int i=0; i<familyMembers.length; i++){
			familyMembers[i].setName(name);
		}
	}
	
	/**
	 * register the string representation of the bonus tile
	 * @param bonusTile the bonus tile owned by this player
	 */
	public void setPersonalBonusTile(String bonusTile){
			this.bonusTile = "\n"+bonusTile+"\n";
		}
	
	/**
	 * get the player name
	 * @return the player name
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * allows to retrive the player ResourceSet 
	 * @return the ResourceSet owned by this player
	 */
	public ResourceSet getPlayerResources(){
		return this.playerResources;
	}
	
	/**
	 * allows to retrive the player family member list 
	 * @return the list of family members owned by this player
	 */
	public ClientFamilyMember[] getFamilyMembers(){
		return this.familyMembers;
	}
	
	/**
	 * allows to retrive the track array
	 * @return an array of track
	 */
	public Track[] getTrack(){
		return this.track;
	}
	
	/**
	 * add card to the card of this player
	 * @param cardType the type of card to add
	 * @param card the name of the card to add
	 */
	public void addCard(String cardType, String card){
		if(cards.containsKey(cardType)){
			this.cards.get(cardType).add(card);
		}else{
			this.cards.put(cardType, new ArrayList<String>());
			this.cards.get(cardType).add(card);
		}
	}
	
	/**
	 * allows to retrive all the cards owned by this player
	 * @return the HashMap representing all the cards owned by the player PersonalBoard
	 */
	public HashMap<String,ArrayList<String>> getCards(){
		return this.cards;
	}
		
	private void fillWith(StringBuilder stringBuilder, int howManyTimes, String string){
		for(int i=0; i<howManyTimes; i++){
			stringBuilder.append(string);
		}
	}
	
	/**
	 * return a string representation of the player objectS
	 */
	public String toString(){
		int x = 100;
		StringBuilder tmp = new StringBuilder();
		fillWith(tmp, x, "=");
		tmp.append(" "+name+" ");
		tmp.append("\n\n");
		int numberOfDashes = (x - "RESOURCES".length() -2)/2;
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(" RESOURCES -");
		fillWith(tmp, numberOfDashes, "-");
		tmp.append("\n");
		tmp.append(playerResources.toStringPlayer()+"\n\n");
		numberOfDashes = (x - "FAMILY MEMBERS".length() -2)/2;
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(" FAMILY MEMBERS ");
		fillWith(tmp, numberOfDashes, "-");
		tmp.append("\n");
		for(int i=0; i<familyMembers.length; i++){
			tmp.append(i +"] " + familyMembers[i].toString());
		}
		numberOfDashes = (x - "CARDS".length() - 2)/2;
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(" CARDS -");
		fillWith(tmp, numberOfDashes, "-");
		tmp.append("\n");
		for(Entry<String, ArrayList<String>> item : cards.entrySet()){
			tmp.append(item.getKey()+" : ");
			for(String card : item.getValue()){
				tmp.append(card+", ");
			}
			tmp.append("\n");
		}
		numberOfDashes = (x - "BONUS TILE".length() - 2)/2;
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(" BONUS TILE ");
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(bonusTile);
		tmp.append("\n");
		numberOfDashes = (x - "TRACK".length() - 2)/2;
		fillWith(tmp, numberOfDashes, "-");
		tmp.append(" TRACK ");
		fillWith(tmp, numberOfDashes, "-");
		tmp.append("\n");
		for(Track t : this.getTrack()){
			tmp.append(t);
			tmp.append("\n");
		}
		return new String(tmp);
	}
}
