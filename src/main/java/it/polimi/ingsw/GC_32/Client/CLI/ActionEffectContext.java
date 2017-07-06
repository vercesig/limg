package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Client.Game.CardCli;
import it.polimi.ingsw.GC_32.Client.Game.ClientCardRegistry;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ActionEffectContext extends Context{

	public ActionEffectContext(ClientCLI client){
		super(client);
	}
	
	public String open(Object object){
		
		runFlag = true;
		JsonObject payload = (JsonObject) object;
		JsonObject Jsonpayload = payload.get("PAYLOAD").asObject();
		
		String actionType = Jsonpayload.get("TYPE").asString();
		int regionID = 0;
		if(!Jsonpayload.get("REGIONID").isNull())
			 regionID = Jsonpayload.get("REGIONID").asInt();
		int bonusActionValue = Jsonpayload.get("BONUSACTIONVALUE").asInt();
		ResourceSet bonusResource = null;
		if(!Jsonpayload.get("BONUSRESOURCE").isNull())
			bonusResource = new ResourceSet(Jsonpayload.get("BONUSRESOURCE").asObject());
		
		boolean flagRegion = false;
		if(!Jsonpayload.get("FLAGREGION").isNull())
			flagRegion = Jsonpayload.get("FLAGREGION").asBoolean();
		
		payload.remove("TYPE");
		payload.remove("REGIONID");
		payload.remove("FLAGREGION");
		
		int choosedRegionID = 0;
		int choosedSpaceID = 0;
		int indexCost = 0;
		
		boolean actionFlag;
		boolean sendFlag;
		
		out.println("you have a bonus "+actionType+" action to perform.\n"+bonusActionValue+" is the action value of"
				+ " your bonus action");
		if(bonusResource!=null)
			out.println("the action will also apply this discount on the cost of the card you will take\n"+bonusResource.toString());
		
		while(runFlag){
				sendFlag = true;
				if(flagRegion){
					out.println("you can select any one tower\nenter the regionID where you want to perform your bonus action. [4-7]");
					actionFlag = true;
					while(actionFlag){
						command = in.nextLine();
						try{
							if(!(Integer.parseInt(command)>=4&&Integer.parseInt(command)<=7)){
								out.println("please, type a valid regionID");
							}else{
								choosedRegionID = Integer.parseInt(command);
								actionFlag = false;
							}
						}catch(NumberFormatException e){
							System.out.println("type a valid number");
						}
					}
				}else{
					out.println("you can perform the action only on the region number "+regionID);
					choosedRegionID = regionID;
				}
				actionFlag = true;
				out.println("ok, now type the spaceID where you would place your pawn");
				while(actionFlag){
					command = in.nextLine();
					try{
						if(Integer.parseInt(command)>3 || Integer.parseInt(command)<0){
							out.println("action space with that id does not exist\ntype a number between 0-3");
						}else{
							choosedSpaceID = Integer.parseInt(command);
							actionFlag = false;
						}
					}catch(NumberFormatException e){
						System.out.println("type a valid number");
					}
				}				
				actionFlag = true;
				
				out.println("Development card on this tower layer: ");				
				String cardName = this.client.getBoard().getRegionList().get(choosedRegionID)
						.getActionSpaceList().get(choosedSpaceID).getCardName();
											
				JsonObject card = ClientCardRegistry.getInstance().getDetails(cardName);
				
				if(card==null){
					out.println("no card on this tower layer");
					sendFlag = false;
					break;
				}
				out.println(CardCli.print(cardName, card));

				if(card.get("cost")!=null){
					JsonArray costList = card.get("cost").asArray();
					if(costList.size() == 1){
						break;
					}else{
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
								System.out.println("type a valid number");
							}
						}
					}
				}
				if(sendFlag){
					out.println("action is ready to be sent to the server. Type 'y' if you want ask the server to apply your action, otherwise type 'n'");				
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
						default:
							out.println("please, type a valid letter");
						}
					}
				}
			}
		out.println("action sent to the server... waiting for response");
		
		JsonObject CONTEXTREPLY = new JsonObject();
		CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
		JsonObject CONTEXTREPLYpayload = new JsonObject();
		CONTEXTREPLYpayload.add("CONTEXT_TYPE", "ACTION");
		JsonObject CONTEXTREPLYpayloadinfo = new JsonObject();
		CONTEXTREPLYpayload.add("PAYLOAD", CONTEXTREPLYpayloadinfo);
		
		CONTEXTREPLYpayloadinfo.add("PLAYERID",client.getPlayerUUID());
		CONTEXTREPLYpayloadinfo.add("ACTIONTYPE", actionType);
		CONTEXTREPLYpayloadinfo.add("REGIONID", choosedSpaceID);
		CONTEXTREPLYpayloadinfo.add("COSTINDEX", indexCost);
		CONTEXTREPLYpayloadinfo.add("SPACEID", choosedRegionID);
		CONTEXTREPLYpayloadinfo.add("JSONPAYLOAD", payload);
		CONTEXTREPLYpayloadinfo.add("BONUSACTIONVALUE", bonusActionValue);
		if(bonusResource!=null)
			CONTEXTREPLYpayloadinfo.add("BONUSRESOURCE", bonusResource.toJson());
		
		CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);
		
		return CONTEXTREPLY.toString();
	}
}