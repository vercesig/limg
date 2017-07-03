package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.ArrayList;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;

public class LeaderSetContext extends Context{

	public  LeaderSetContext(ClientCLI client){ // context aperto nella distribuzione delle carte leader
		super(client);
	}
	
	public String open(Object object){
		int index = 0;
		runFlag=true;
		
		JsonObject jsonPayload = (JsonObject) object;
		
		JsonArray list = jsonPayload.get("LIST").asArray();
		
		ArrayList <String> cardList = new ArrayList<String>();
		list.forEach(js -> {
			cardList.add(js.asString());
		});
		out.println("Choose one of the following card");
			for(int i=0; i<cardList.size(); i++){
				System.out.println(i + "]" + cardList.get(i));
		}
		boolean optionSelected = false;
		out.println("type the index of the card you want to get");
		while(!optionSelected){	
			try{
				command = in.nextLine();	
				
				if(Integer.parseInt(command)>=0&&Integer.parseInt(command)<cardList.size()){
					if(cardList.size()<Integer.parseInt(command)){
						out.println("type a valid index");
						break;
					} 
					index = Integer.parseInt(command);
					out.println("You choose the card: " +cardList.get(index));
					optionSelected = true;
				}else{
					out.println("type a valid index");
				}
			}catch(NumberFormatException e) {
				out.println("type a number, please");
				}
		}
		client.getPlayerList().get(client.getPlayerUUID()).addCard("LEADER", cardList.get(index));
		cardList.remove(index);
		list.remove(index);
		out.println("Prima di inviare al Server:" + list);		
		return ClientMessageFactory.buildLDRSETmessage(client.getGameUUID(), client.getPlayerUUID(), list);
	}
}
