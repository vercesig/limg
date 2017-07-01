package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;
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
		int regionID = Jsonpayload.get("REGIONID").asInt();
		int bonusActionValue = Jsonpayload.get("BONUSACTIONVALUE").asInt();
		ResourceSet bonusResource = null;
		if(!Jsonpayload.get("BONUSRESOURCE").isNull())
			bonusResource = new ResourceSet(Jsonpayload.get("BONUSRESOURCE").asObject());
		
		String flagRegion = Jsonpayload.get("FLAGREGION").asString();
		
		payload.remove("TYPE");
		payload.remove("REGIONID");
		payload.remove("FLAGREGION");
		
		int choosedRegionID = 0;
		int choosedSpaceID = 0;
		
		boolean actionFlag;
		boolean flagAction = true;
		
		System.out.println("you have a bonus "+actionType+" action to perform.\nThe effect will increase of "+bonusActionValue+" the action value of"
				+ " your bonus action");
		if(bonusResource!=null)
			System.out.println("the action will also apply this discount on the cost of the card you will take\n"+bonusResource.toString());
		
		while(runFlag){
				
				while(flagAction){
					if(flagRegion.equals("ALL")){
						System.out.println("you can selcet any one tower\nenter the regionID where you want to perform your bonus action. [4-7]");
						actionFlag = true;
						while(actionFlag){
							command = in.nextLine();
							try{
								if(!(Integer.parseInt(command)>=4&&Integer.parseInt(command)<=7)){
									System.out.println("please, type a valid regionID");
								}else{
									choosedRegionID = Integer.parseInt(command);
									actionFlag = false;
								}
							}catch(NumberFormatException e){
								System.out.println("type a valid number");
							}
						}
					}else{
						System.out.println("you can perform the action only on the region number "+regionID);
						choosedRegionID = regionID;
					}
					actionFlag = true;
					System.out.println("ok, now type the spaceID where you would place your pawn");
					while(actionFlag){
						command = in.nextLine();
						try{
							if(Integer.parseInt(command)>7 || Integer.parseInt(command)<0){
								System.out.println(">action space with that id does not exist");
								System.out.println("type a number between 0-3");
							}else{
								choosedSpaceID = Integer.parseInt(command);
								actionFlag = false;
							}
						}catch(NumberFormatException e){
							System.out.println("type a valid number");
						}
					}
					System.out.println("action is ready to be sent to the server. Type 'y' if you want ask the server to apply your action, otherwise type 'n'");
					
					//TODO printare riassunto della mossa
					
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
							close();
							return null;
						default:
							System.out.println("please, type a valid letter");
						}
					}
				}
			}
		System.out.println("action sent to the server... waiting for response");
		
		return ClientMessageFactory.buildASKACTmessage(actionType, choosedSpaceID, choosedRegionID, payload);
	}
}