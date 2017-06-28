package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;


import it.polimi.ingsw.GC_32.Client.Game.CardCli;
import it.polimi.ingsw.GC_32.Client.Game.ClientFamilyMember;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Card.Card;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Setup.JsonImporter;

public class ZeroLevelContext extends Context implements Runnable{

	private ClientCLI client;
	
	private ShowCardDialog showCard;
	private AskActDialog askAct;
	
	public ZeroLevelContext(ClientCLI client){
		super();
		this.client = client;
		this.showCard = new ShowCardDialog(this.client);
		this.askAct = new AskActDialog(this.client);
	}
		
	public void run(){
		open(null);
	}
	
	public AskActDialog getAskAct(){
		return this.askAct;
	}
	
	public void open(Object object){
		
		runFlag = true;		
		while(runFlag){
			
			if(client.isWaiting()){
				System.out.println("type a command:\n- board: display the board status\n- players: display players' status\n"
						+ "- show card: to show details of cards on the game");
			}
			else
				System.out.println("type a command:\n- board: display the board status\n- players: display players' status\n"
						+ "- show card: to show details of cards on the game\n"
						+ "- action: make an action (if isn't your turn your requests won't be applied)");
				
			command = in.nextLine();
			switch(command){
			
			case "board":
				System.out.println(this.client.getBoard().toString());
				break;
			
			case "players":
				this.client.getPlayerList().forEach((UUID, client) -> System.out.println(client.toString()));
				break;
				
			case "show card":
					showCard.open(object);
					break;
		
			case "action":		
				if(!client.isWaiting()){
					askAct.open(object);
				}	
				break;	
			}
		}
	}	
}
