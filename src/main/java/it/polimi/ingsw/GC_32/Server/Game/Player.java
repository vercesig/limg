package it.polimi.ingsw.GC_32.Server.Game;

import java.util.ArrayList;
import java.util.List;
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
	//private PersonalBonusTile personalBonusTile;
	private FamilyMember[] familyMemberList;
	private final String uuid;
	
	public Player(){
		this.personalBoard = new PersonalBoard();
		this.resources = new ResourceSet();
		
		// CONVENZIONE: familyMemberList[0] è sempre il familiare neutro
		this.familyMemberList = new FamilyMember[4];
		for(int i=0; i<familyMemberList.length; i++){
			familyMemberList[i] = new FamilyMember(this);	
		}
		this.uuid = UUID.randomUUID().toString();
		this.effectList = new ArrayList<Effect>();
	}
	
	public void setPlayerName(String name){
		this.name = name;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public PersonalBoard getPersonalBoard(){
		return this.personalBoard;
	}
	
	public String getName(){
		return this.name;
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

    public List<Effect> getEffectList(){
        return this.effectList;
    }
    
    public FamilyMember[] getFamilyMember(){
    	return this.familyMemberList;
    }
    
    public String toString(){
    	StringBuilder tmp = new StringBuilder();
    	tmp.append("name :"+this.name+"\nUUID :"+this.uuid+"\nresources :"+this.resources.toString()+"\nPERSONALBOARD :"+this.personalBoard.toString());
    	tmp.append("stato dei familiari: \n");
    	for(FamilyMember f : familyMemberList){
    		tmp.append(f.toString()+"\n");
    	}
    	return new String(tmp);
    }
    
    // TODO: inserire gli opportuni check (o ci pensa il move checker??)
    public void takeCard(Game game, Action action){
    	TowerRegion selectedTower = (TowerRegion)game.getBoard().getRegion(action.getActionRegionId());
    	this.personalBoard.addCard(selectedTower.getTowerLayers()[action.getActionSpaceId()].takeCard());
    }
    
    // richiede di effettuare un azione a seguito dell'attivazione di un effeto
    public void makeAction(JsonValue payload){
    	GameMessage message = new GameMessage(this.uuid,"TURNBGN", payload.toString());
    	MessageManager.getInstance().sendMessge(message);
    }
    
    // richiede di effettuare un azione
    public void makeAction(){
    	JsonObject TURNBGN = new JsonObject();
    	TURNBGN.add("TYPE", "TOWER");
    	GameMessage message = new GameMessage(this.uuid,"TURNBGN", TURNBGN.toString());
    	MessageManager.getInstance().sendMessge(message);
    }
    
}
