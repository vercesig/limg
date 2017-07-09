package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonObject;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ActionEffectContext extends Context{
    private ClientActionEffectHandler actionHandler;

	public ActionEffectContext(ClientCLI client){
		super(client);
		this.actionHandler = new ClientActionEffectHandler(this);
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
		
        boolean chooseServants = (Jsonpayload.get("CHOSESERVANT") != null &&
                                  Jsonpayload.get("CHOSESERVANT").asBoolean());
        
        int maxServants = client.getPlayerList().get(client.getPlayerUUID())
                                .getPlayerResources().getResourceSet().get("SERVANTS");
		
		out.println("you have a bonus "+actionType+" action to perform.\n"+bonusActionValue+" is the action value of"
				+ " your bonus action\nType 'q' if you don't want to perform any action, or if you can't");
		if(bonusResource!=null)
			out.println("the action will also apply this discount on the cost of the card you will take\n"+bonusResource.toString());
		
		JsonObject CONTEXTREPLYpayloadinfo = null;
		while(runFlag){
			switch(actionType){
				case "TOWER":
				    CONTEXTREPLYpayloadinfo = actionHandler.handleTower(flagRegion, regionID, 0);
					break;
				case "HARVEST":

				    CONTEXTREPLYpayloadinfo = actionHandler.handleHarvest(chooseServants, maxServants);
					break;
				case "PRODUCTION":
				    CONTEXTREPLYpayloadinfo = actionHandler.handleProduction(chooseServants, maxServants);
			        break;
			    default:
			        CONTEXTREPLYpayloadinfo = null;
			}
			if(CONTEXTREPLYpayloadinfo != null){
				if(CONTEXTREPLYpayloadinfo.get("NULLACTION").asBoolean()){
					close();
				} else {
					out.println("action is ready to be sent to the server. Type 'y' if you want ask the server to apply your action, otherwise type 'n'");
					boolean haveResponse = false;
					while(!haveResponse){
						command = in.nextLine();
						switch(command){
							case "y":
								close();
								haveResponse = true;
								break;
							case "n":
								haveResponse = true;
								break;
							default:
								out.println("please, type a valid letter");
						}
					}
				}
			}
	    }
		out.println("action sent to the server... waiting for response");
		
        JsonObject CONTEXTREPLY = new JsonObject();
        CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
        JsonObject CONTEXTREPLYpayload = new JsonObject();
        CONTEXTREPLYpayload.add("CONTEXT_TYPE", "ACTION");

        CONTEXTREPLYpayloadinfo.add("PLAYERID",client.getPlayerUUID());
        CONTEXTREPLYpayloadinfo.add("ACTIONTYPE", actionType);
        CONTEXTREPLYpayloadinfo.add("JSONPAYLOAD", payload);
        CONTEXTREPLYpayloadinfo.add("BONUSACTIONVALUE", bonusActionValue);
        if(bonusResource!=null){
            CONTEXTREPLYpayloadinfo.add("BONUSRESOURCE", bonusResource.toJson());
        }
        CONTEXTREPLYpayload.add("PAYLOAD", CONTEXTREPLYpayloadinfo);
        CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);
		
		return CONTEXTREPLY.toString();
	}
}
