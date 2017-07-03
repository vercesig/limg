package it.polimi.ingsw.GC_32.Client.CLI;

import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;

public class LeaderDialog extends Context{
	
	public LeaderDialog(ClientCLI client){ // command Friendly per inviare azione leader da client
		super(client);
	}

	@Override
	public String open(Object object) {
		
		int index = 0;
		String decision = null;
		runFlag=true;
		ClientPlayer player = client.getPlayerList().get(client.getPlayerUUID());

		out.println("In this section you can activate a Leader Action. You can perform one of these action:\n"
				+ "-discard: to discard a card and get a council privilege\n"
				+ "-activate an effect: actvate a special effect of a leader card\n"
				+ "-play: to put on the game the card. you can activate the effect only to cards on the game.");
		
		while(runFlag){
			
			boolean optionSelected = false;
			while(!optionSelected){
				command = in.nextLine();	
				switch(command){
				case("discard"):
					decision = "DISCARD";
					optionSelected = true;
					break;
				case("activate"):
					decision = "ACTIVATE";
					optionSelected = true;
					break;
				case("play"):
					decision = "PLAY";
					optionSelected = true;
					break;
				default:
					out.println("type a valid option");
					break;
				}
			}
			optionSelected = false;
			out.println("Choose one of the following card: ");
			for(int i=0; i<player.getCards().get("LEADER").size(); i++){
				System.out.println(i + "]" + player.getCards().get("LEADER").get(i));
			}	
			out.println("type the index of the card you want ");
			while(!optionSelected){	
				try{
					command = in.nextLine();	
					
					if(Integer.parseInt(command)>=0&&Integer.parseInt(command)<=3){
						if(player.getCards().get("LEADER").size()<Integer.parseInt(command)){
							out.println("type a valid index");
							break;
						} 
						index = Integer.parseInt(command);
						out.println("You choose the card: " + player.getCards().get("LEADER").get(index));
						optionSelected = true;
					}else{
						out.println("type a valid index");
					}
			}catch(NumberFormatException e) {
				out.println("type a number, please");
				}
		}
			
		out.println("NAME:"+ player.getCards().get("LEADER").get(index) + " ACTION:" + decision);
		close();
		}
	return ClientMessageFactory.buildASKLDRACTmessage(client.getGameUUID(),player.getCards().get("LEADER").get(index), decision);
	}
}
	
	