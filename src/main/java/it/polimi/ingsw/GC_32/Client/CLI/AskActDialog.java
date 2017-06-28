package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Client.Game.ClientFamilyMember;
import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

import it.polimi.ingsw.GC_32.Server.Setup.JsonImporter;

public class AskActDialog extends Context{
	
	private ClientCLI client;
	private boolean flagAction;
	private String gameUUID;
	
	public AskActDialog(ClientCLI client, String gameUUID){
		super();
		this.gameUUID = gameUUID;
		this.client = client;
		flagAction = true;
	}
	
	@Override
	public void open(Object object) {
		
		int familyMemberIndex = 0;
		int regionID = 0;
		int spaceID = 0;
		int indexCost = 0;
		String actionType = null;
		String cardName = null;
	
		while(flagAction){
			boolean actionFlag = true;
			while(actionFlag){						
				System.out.println("type the ID of the family member you want to place\nThe symbol '>' means that the family member is busy\n");
				int i = 0;
				for(ClientFamilyMember fm : client.getPlayerList().get(client.getUUID()).getFamilyMembers()){
					System.out.println("["+i+"] "+fm.toString());
					i++;
				}						
				command = in.nextLine();
				try{
					if(Integer.parseInt(command)>client.getPlayerList().get(client.getUUID()).getFamilyMembers().length -1){
						System.out.println("Invalid Family member Index");
						actionFlag = false;
						break;
					}
					if(client.getPlayerList().get(client.getUUID()).getFamilyMembers()[Integer.parseInt(command)].isBusy()){
						System.out.println("the choosen family member is already busy, please enter a valid index");
						actionFlag = false;
						break;
					}else{
						familyMemberIndex = Integer.parseInt(command);
						break;
					}
				}catch(NumberFormatException e){
					System.out.println("type a valid number");
				}
			}
			if(!actionFlag){
				break;
			}
			System.out.println("type the regionID where you would place your pawn");
			while(actionFlag){
				command = in.nextLine();
				try{
					if(Integer.parseInt(command)>7 || Integer.parseInt(command)<0){
						System.out.println(">region with that id does not exist");
						System.out.println("type a number between 0-7");
						continue;
					}
					regionID = Integer.parseInt(command);
					actionFlag = false;
				}catch(NumberFormatException e){
					System.out.println("type a valid number");
				}
			}
			actionFlag = true;
			System.out.println("ok, now type the spaceID where you would place your pawn");
			while(actionFlag){
				command = in.nextLine();
				try{
					if(Integer.parseInt(command)>7 || Integer.parseInt(command)<0){
						System.out.println(">action space with that id does not exist");
						System.out.println("type a number between 0-3");
						continue;
					}
					spaceID = Integer.parseInt(command);
					actionFlag = false;
				}catch(NumberFormatException e){
					System.out.println("type a valid number");
				}
			}
			
			switch(regionID){
			case 0:
				actionType = "PRODUCTION";
				break;
			case 1:
				actionType = "HARVEST";
				break;
			case 2:
				actionType = "COUNCIL";
				break;
			case 3:
				actionType = "MARKET";
				break;
			default:
				actionType = "TOWER";
				
				actionFlag = true;
				
				System.out.println("Development card on this tower layer: ");
				try {
					Reader json = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("cards.json"));
					cardName = this.client.getBoard().getRegionList().get(regionID)
							.getActionSpaceList().get(spaceID).getCardName();
					JsonValue card = JsonImporter.importSingleCard(json, cardName);
							
					System.out.println(card);
					try{
						JsonArray costList = card.asObject().get("cost").asArray();
						if(costList.size() == 1){
							actionFlag = false;
							break;
						}
						System.out.println("Choose one cost of the card: ");
						for(JsonValue js : costList){
							System.out.print("> ");	
							System.out.println(new ResourceSet(js.asObject()).toString() + " ");
							}
					}
					catch(IndexOutOfBoundsException | NullPointerException e){
						break;
					}
					System.out.println("type 0 or 1");
					
					while(actionFlag){
						
						command = in.nextLine();
						
						switch(Integer.parseInt(command)){
						case 0:
							indexCost = 0;
							actionFlag = false;
							break;
						case 1:
							indexCost = 1;
							actionFlag = false;
							break;
						default:
							System.out.println("please, type a valid number");
						break;
						}
					}	
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}	
			
			System.out.println("action is ready to be sent to the server. Type 'y' if you want ask the server to apply your action, otherwise type 'n'");
			actionFlag = true;
			while(actionFlag){
				command = in.nextLine();
				switch(command){
				case "y":
					actionFlag = false;
					//endFlag = true;
					break;
				case "n":
					actionFlag = false;
					//endFlag = false;
					return;
				default:
					System.out.println("please, type a valid letter");
					return;
				}
			}
			break;
		}
		
		// sending ASKACT message
		client.getSendQueue().add(ClientMessageFactory.buildASKACTmessage(gameUUID, actionType, familyMemberIndex, spaceID, regionID, indexCost, cardName));
		
		System.out.println("action sent to the server... waiting for response");
		
		try{ // do tempo ad eventuali context di aprirsi
			Thread.sleep(1000);
		}catch(Exception e){}
		return;
	}
}



	
	