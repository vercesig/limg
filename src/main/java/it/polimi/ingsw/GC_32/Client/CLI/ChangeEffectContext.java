package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

/**
 * ChangeEffectContext handles the interaction with the client when CHANGE effects are triggered (after a PRODUCTION action has triggered permanent effect of building
 * cards with CHANGE effect). Context ends with the sending of a CONTEXTREPLY message.
 * 
 * @see Context
 *
 */

public class ChangeEffectContext extends Context{

	public ChangeEffectContext(ClientCLI client){
		super(client);
	}
	
	public String open(Object object) {
		
		runFlag = true;
		JsonObject JsonPayload = (JsonObject) object;
		
		JsonArray CHANGEcardName = new JsonArray();
		JsonArray CHANGEresourcePayload = new JsonArray();
		
		if(JsonPayload.get("NAME").isArray())
			CHANGEcardName = JsonPayload.get("NAME").asArray();
		else
			CHANGEcardName.add(JsonPayload.get("NAME"));
		
		if(JsonPayload.get("RESOURCE").isArray())
			CHANGEresourcePayload = JsonPayload.get("RESOURCE").asArray();
		else
			CHANGEresourcePayload.add(JsonPayload.get("RESOURCE"));
		
		System.out.println("your PRODUCTION action has triggered some CHANGE effect:");
		
		JsonObject CONTEXTREPLY = new JsonObject();
		CONTEXTREPLY.add("MESSAGETYPE", "CONTEXTREPLY");
		JsonObject CONTEXTREPLYpayload = new JsonObject();
		CONTEXTREPLYpayload.add("CONTEXT_TYPE", "CHANGE");
		JsonObject CONTEXTREPLYpayloadinfo = new JsonObject();
		CONTEXTREPLYpayload.add("PAYLOAD", CONTEXTREPLYpayloadinfo);
		
		JsonArray indexArray = new JsonArray();
		CONTEXTREPLYpayloadinfo.add("CHANGEIDARRAY", indexArray);
		
		int i=0;
		
		while(runFlag){
			
			boolean actionFlag = true;			
			out.println("--------------------  "+CHANGEcardName.get(i).asString()+"  --------------------\n");			
			JsonValue item = CHANGEresourcePayload.get(i);
			if(item.isObject()){ // CHANGE singolo				
				ResourceSet resourceIn = new ResourceSet(item.asArray().asObject().get("RESOURCEIN").asObject());
				
				out.println("this card offer only this exchange:");
				out.println(resourceIn+" -> "+new ResourceSet(item.asObject().get("RESOURCEOUT").asObject()).toString()+"\n");

				if(client.getPlayerList().get(client.getPlayerUUID()).getPlayerResources()
				                                                     .compareTo(resourceIn) < 0){
					out.println(" *** :( you can't activate this effect because you haven't enought resources ***");
					i++;
					indexArray.add("n");
				}
				else{
					out.println("type 'y' if you want apply this effect, otherwise type 'n'");
					while(actionFlag){
						command = in.nextLine();
						switch(command){
						case "y":
							indexArray.add(0);
							actionFlag=false;
							i++;
							break;
						case "n":
							indexArray.add("n");
							actionFlag=false;
							i++;
							break;
						default:
							out.println("type a valid argument");
						}
					}	
				}
			}else{ // CHANGE esclusivo
				out.println("select what exchange you want apply. please type the corresponding ID of the effect you want activate\n"
						+ "type 'n' if you don't want to apply this effect. An 'X' next to the choise means you can't perform that change because you "
						+ "haven't enought resources:");
				
				// si ipotizza che scambi ad ID più alto costino di più
				boolean[] lessResourceFlag = new boolean[item.asArray().size()];
				
				for(int j=0; j<item.asArray().size(); j++){
					
					ResourceSet resourceIn = new ResourceSet(item.asArray().get(j).asObject().get("RESOURCEIN").asObject());					
					boolean lessResource = (client.getPlayerList().get(client.getPlayerUUID()).getPlayerResources()
					                                                                          .compareTo(resourceIn) < 0);
					lessResourceFlag[j] = lessResource;
					
					StringBuilder output = new StringBuilder();
					output.append(lessResource ? "X" : " ");
					output.append("["+j+"]  "+resourceIn.toString()+" -> "
							  				 +new ResourceSet(item.asArray().get(j).asObject().get("RESOURCEOUT").asObject()).toString()+"\n");
					out.println(output);
				}
				boolean finalResourceFlag = true;
				for(int k=0; k<lessResourceFlag.length; k++)
					finalResourceFlag = finalResourceFlag&&lessResourceFlag[k];
				
				if(finalResourceFlag){
					out.println(" *** :( you can't activate this effect because you haven't enought resources ***");
					i++;
					indexArray.add("n");
				}
				else{	
					while(actionFlag){
						command = in.nextLine();
						try{
							if(Integer.parseInt(command)<item.asArray().size()&&Integer.parseInt(command)>=0&&!lessResourceFlag[Integer.parseInt(command)]){
								indexArray.add(Integer.parseInt(command));
								actionFlag = false;
								i++;
							}else{
								out.println("type a valid number or choose an effect you can activate");
							}					
						}catch(NumberFormatException e){
							if("n".equals(command)){
								indexArray.add(command);
								actionFlag = false;
								i++;
							}else{
								out.println("type a valid number");
							}
						}
					}
				}
			}
			
			if(i==CHANGEcardName.size()){
				CONTEXTREPLY.add("PAYLOAD", CONTEXTREPLYpayload);	
				close();
			}
		}
		return CONTEXTREPLY.toString();
	}
}
