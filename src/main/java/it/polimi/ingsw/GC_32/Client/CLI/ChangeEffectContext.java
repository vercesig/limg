package it.polimi.ingsw.GC_32.Client.CLI;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ChangeEffectContext extends Context{

	public ChangeEffectContext(ClientCLI client){
		super(client);
	}
	
	public String open(Object object) {
		
		runFlag = true;
		JsonObject JsonPayload = (JsonObject) object;
		
		JsonArray CHANGEcardName = JsonPayload.get("NAME").asArray();
		JsonArray CHANGEresourcePayload = JsonPayload.get("RESOURCE").asArray();
		
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
			
			System.out.println("--------------------  "+CHANGEcardName.get(i).asString()+"  --------------------\n");
			
			JsonValue item = CHANGEresourcePayload.get(i);
			if(item.isObject()){ // CHANGE singolo
				
				ResourceSet resourceIn = new ResourceSet(item.asArray().asObject().get("RESOURCEIN").asObject());
				
				System.out.println("this card offer only this exchange:");
				System.out.println(resourceIn+" -> "
								  +new ResourceSet(item.asObject().get("RESOURCEOUT").asObject()).toString()+"\n");

				if(client.getPlayerList().get(client.getPlayerUUID()).getPlayerResources().compareTo(resourceIn)==-1){
					System.out.println(" *** :( you can't activate this effect because you haven't enought resources ***");
					i++;
					indexArray.add("n");
				}
				else{
					System.out.println("type 'y' if you want apply this effect, otherwise type 'n'");
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
							System.out.println("type a valid argument");
						}
					}	
				}
			}else{ // CHANGE esclusivo
				System.out.println("select what exchange you want apply. please type the corresponding ID of the effect you want activate\n"
						+ "type 'n' if you don't want to apply this effect. An 'X' next to the choise means you can't perform that change because you "
						+ "haven't enought resources:");
				
				// si ipotizza che scambi ad ID più alto costino di più
				boolean[] lessResourceFlag = new boolean[item.asArray().size()];
				
				for(int j=0; j<item.asArray().size(); j++){
					
					ResourceSet resourceIn = new ResourceSet(item.asArray().get(j).asObject().get("RESOURCEIN").asObject());
					
					boolean lessResource = client.getPlayerList().get(client.getPlayerUUID()).getPlayerResources().compareTo(resourceIn)==-1;
					lessResourceFlag[j] = lessResource;
					
					StringBuilder output = new StringBuilder();
					output.append(lessResource ? "X" : " ");
					output.append("["+j+"]  "+resourceIn.toString()+" -> "
							  				 +new ResourceSet(item.asArray().get(j).asObject().get("RESOURCEOUT").asObject()).toString()+"\n");
					System.out.println(output);
				}
				boolean finalResourceFlag = true;
				for(int k=0; k<lessResourceFlag.length; k++)
					finalResourceFlag = finalResourceFlag&&lessResourceFlag[k];
				
				if(finalResourceFlag){
					System.out.println(" *** :( you can't activate this effect because you haven't enought resources ***");
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
								System.out.println("type a valid number or choose an effect you can activate");
							}					
						}catch(NumberFormatException e){
							if("n".equals(command)){
								indexArray.add(command);
								actionFlag = false;
								i++;
							}else{
								System.out.println("type a valid number");
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
