package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.UUID;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Board.*;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;
import it.polimi.ingsw.GC_32.Server.Game.Effect.EffectRegistry;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;

public class Player {
	private PersonalBoard personalBoard;
	private String name;
    private ArrayList<Effect> effectList;
	private ResourceSet resources;
	private PersonalBonusTile personalBonusTile;
	private FamilyMember[] familyMemberList;
	private final UUID uuid;
	private UUID gameID;
	
	private JsonObject dictionaryFlag; // dizionario dei flag
	
	public Player(){
		this.personalBoard = new PersonalBoard();
		this.resources = new ResourceSet();
		this.dictionaryFlag = new JsonObject();
		
		// CONVENZIONE: familyMemberList[0] è sempre il familiare neutro
		this.familyMemberList = new FamilyMember[4];
		for(int i=0; i<familyMemberList.length; i++){
			familyMemberList[i] = new FamilyMember(this);	
		}
		familyMemberList[0].setColor(DiceColor.GREY);
		familyMemberList[1].setColor(DiceColor.BLACK);
		familyMemberList[2].setColor(DiceColor.WHITE);
		familyMemberList[3].setColor(DiceColor.ORANGE);
		this.uuid = UUID.randomUUID();
		this.effectList = new ArrayList<Effect>();
	}
	
	public void setPlayerName(String name){
		this.name = name;
	}
	
	public void setPersonalBonusTile(PersonalBonusTile bonusTile){
		this.personalBonusTile = bonusTile;
		}
	
	public UUID getUUID(){
		return uuid;
	}
	
	public String getID(){
		return uuid.toString();
	}
	
	public PersonalBoard getPersonalBoard(){
		return this.personalBoard;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getMilitaryPoints(){
		return this.resources.getResource("MILITARY");
	}
	
	public int getVictoryPoints(){
		return this.resources.getResource("VP");
	}
	
	public int getFaithPoints(){
		return this.resources.getResource("FAITH");
	}
	
	public int getWoodQuantity() {
		return this.resources.getResource("WOOD");
	}

	public int getStoneQuantity() {
		return this.resources.getResource("STONE");
	}

	public int getCoins() {
		return this.resources.getResource("COINS");
	}

	public int getServants() {
		return this.resources.getResource("SERVANTS");
	}
	
	public JsonObject getDictionaryFlag(){
		return this.dictionaryFlag;
	}
	
	public ResourceSet getResources(){
		return this.resources;
	}

    public void addEffect(Effect e){
        this.effectList.add(e);
    }
    
    public void addEffect(String s){
    	this.effectList.add(EffectRegistry.getInstance().getEffect(s));
    }

    public ArrayList<Effect> getEffectList(){
        return this.effectList;
    }
    
    public FamilyMember[] getFamilyMember(){
    	return this.familyMemberList;
    }
    
    public String toString(){
    	StringBuilder tmp = new StringBuilder();
    	tmp.append("name :"+this.name+"\n"
    			 + "UUID :"+this.uuid.toString()+"\n"
    			 + "resources :"+this.resources.toString()+"\n"
    			 + "PERSONALBOARD :"+this.personalBoard.toString());
    	tmp.append("stato dei familiari: \n");
    	for(FamilyMember f : familyMemberList){
    		tmp.append(f.toString()+"\n");
    	}
    	return new String(tmp);
    }
    
    public void takeCard(Board board, Action action){
    	TowerRegion selectedTower = (TowerRegion)(board.getRegion(action.getActionRegionId()));
    	this.personalBoard.addCard(selectedTower.getTowerLayers()[action.getActionSpaceId()].takeCard());
    }
    
    // richiede di effettuare un azione a seguito dell'attivazione di un effeto
    public void makeAction(JsonValue payload){
    	GameMessage message = new GameMessage(this.gameID, this.uuid,"TURNBGN", payload);
    	MessageManager.getInstance().sendMessge(message);
    }
    
    public void moveFamilyMember(int i, Action a, Board b){
    	FamilyMember f = this.getFamilyMember()[i];
    	ActionSpace space = b.getRegion(a.getActionRegionId())
    			.getActionSpace(a.getActionSpaceId());
    	space.addFamilyMember(f);
    }
    
    public PersonalBonusTile getPersonalBonusTile(){
    	return this.personalBonusTile;
    }
    
    public void registerGame(UUID gameID){
    	this.gameID = gameID;
    }
    
    public UUID getGameID(){
    	return this.gameID;
    }
    
    public boolean isFlagged(String flag){
    	System.out.println("DIZIONARIO: " + this.getDictionaryFlag().toString());
    	try{
    		for(JsonValue js: this.getDictionaryFlag().get("NOENDPOINTS").asArray()){
    			if(js.asString().equals(flag)){
    				System.out.println("DENTRO IL PRIMO TRY: RITORNO TRUE");
    				return true;
    			}
    		}
    	}catch(NullPointerException e){}
    	
    	try{
    		for(JsonValue js: this.getDictionaryFlag().get("LESSRESOURCE").asArray()){
    			if(js.asString().equals(flag)){
    				return true;
		   			}
    		}
    	}catch(NullPointerException e){}	
	    try{
	    	if(!this.getDictionaryFlag().get(flag).isNull())
	    		return true;
	    }catch(NullPointerException e){}	
	    return false;
    }
}
