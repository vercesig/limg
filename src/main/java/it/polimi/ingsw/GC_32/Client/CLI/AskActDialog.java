package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Client.Game.CardCli;
import it.polimi.ingsw.GC_32.Client.Game.ClientCardRegistry;
import it.polimi.ingsw.GC_32.Client.Game.ClientFamilyMember;
import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class AskActDialog extends Context{

	public AskActDialog(ClientCLI client){
		super(client);
	}
	
	public String open(Object object){
		
		int familyMemberIndex = 0;
		int regionID = 0;
		int spaceID = 0;
		int indexCost = 0;
		String actionType = null;
		String cardName = null;
		
		runFlag = true;
		boolean sendFlag;
	
		while(runFlag){
			sendFlag = true;
			boolean actionFlag = true;
			while(actionFlag){						
				out.println("type the ID of the family member you want to place, type 'q' if you want return to the main menù\n" +
								   "The symbol '>' means that the family member is busy\n");
				int i = 0;
				for(ClientFamilyMember fm : client.getPlayerList().get(client.getPlayerUUID()).getFamilyMembers()){
					out.println("["+i+"] "+fm.toString());
					i++;
				}						
				command = in.nextLine();
				try{
					if(Integer.parseInt(command)<client.getPlayerList().get(client.getPlayerUUID()).getFamilyMembers().length){
						if(client.getPlayerList().get(client.getPlayerUUID()).getFamilyMembers()[Integer.parseInt(command)].isBusy()){
							out.println("the choosen family member is already busy, please enter a valid index");
						}else{
							actionFlag = false;
							familyMemberIndex = Integer.parseInt(command);
						}
					}
					else{
						out.println("Invalid Family member Index");
					}
				}catch(NumberFormatException e){
					if("q".equals(command))
						return "";
					out.println("type a valid number");
				}
			}
			actionFlag=true;
			out.println("type the regionID where you would place your pawn");
			while(actionFlag){
				command = in.nextLine();
				try{
					if(Integer.parseInt(command)>7 || Integer.parseInt(command)<0){
						out.println(">region with that id does not exist");
						out.println("type a number between 0-7");
					}else{
						regionID = Integer.parseInt(command);
						actionFlag = false;
					}
				}catch(NumberFormatException e){
					if("q".equals(command))
						return "";
					out.println("type a valid number");
				}
			}
			actionFlag = true;
			out.println("ok, now type the spaceID where you would place your pawn");
			while(actionFlag){
				command = in.nextLine();
				try{
					if(Integer.parseInt(command)>7 || Integer.parseInt(command)<0){
						out.println(">action space with that id does not exist");
						out.println("type a number between 0-3");
					}else{
						spaceID = Integer.parseInt(command);
						actionFlag = false;
					}
				}catch(NumberFormatException e){
					if("q".equals(command))
						return "";
					out.println("type a valid number");
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
				out.println("Development card on this tower layer: ");				
				cardName = this.client.getBoard().getRegionList().get(regionID)
						.getActionSpaceList().get(spaceID).getCardName();
								
				JsonObject card = ClientCardRegistry.getInstance().getDetails(cardName);
				
				if(card==null){
					out.println("no card on this tower layer");
					sendFlag = false;
					break;
				}				
				out.println(CardCli.print(cardName, card));
				
				if(card.get("cost") != null){
					JsonArray costList = card.get("cost").asArray();
					if(costList.size() == 1){
						break;
					}
					out.println("Choose one cost of the card: ");
					for(JsonValue js : costList){
							out.println("> "+new ResourceSet(js.asObject()).toString() + " ");
						}					
					out.println("type 0 or 1");					
					while(actionFlag){
						command = in.nextLine();						
						try{
							if(Integer.parseInt(command) == 0){
								indexCost = 0;
								break;
							}	
							if(Integer.parseInt(command) == 1){
								indexCost = 1;
								break;
							}
							else
								out.println("please, type a valid number");
						} catch(NumberFormatException e){
							if("q".equals(command))
								return "";
							out.println("type a valid number");
						}
					}	
				}
				break;
			}	
			
			if(sendFlag){
				out.println("action is ready to be sent to the server.\n" +
						   "Type 'y' if you want ask the server to apply your action, otherwise type 'n'");
					
				actionFlag = true;
				while(actionFlag){
					command = in.nextLine();
					switch(command){
					case "y":
						actionFlag = false;
						close();
						break;
					case "n":
						actionFlag = false;
						break;
					case "q":
						return "";
					default:
						out.println("please, type a valid letter");
					}
				}
			}
			
		}
		out.println("action sent to the server... waiting for response");		
		return ClientMessageFactory.buildASKACTmessage(actionType, familyMemberIndex, regionID, spaceID, indexCost, cardName);
	}
}



	
	