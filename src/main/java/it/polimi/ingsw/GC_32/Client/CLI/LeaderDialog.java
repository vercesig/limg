package it.polimi.ingsw.GC_32.Client.CLI;

import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;

public class LeaderDialog extends Context{
	
	private ClientCLI client;
	
	public LeaderDialog(ClientCLI client){ // command Friendly per inviare azione leader da client
		
		this.client = client;
	}

	@Override
	public void open(Object object) {
		
		int index = 0;
		String decision = null;
		runFlag=true;
		ClientPlayer player = client.getPlayerList().get(client.getUUID());

		System.out.println("In this section you can activate a Leader Action. You can perform one of these action:\n"
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
				case ("play"):
					decision = "PLAY";
					optionSelected = true;
					break;
				default:
					System.out.println("type a valid option");
					break;
				}
			}
			optionSelected = false;
			System.out.println("Choose one od your card: ");
			System.out.println("Choose one of the following card");
			for(int i=0; i<player.getCards().get("LEADER").size(); i++){
				System.out.println(i + "]" + player.getCards().get("LEADER").get(i));
			}	
			System.out.println("type the index of the card you want ");
			while(!optionSelected){	
				try{
					command = in.nextLine();	
					switch(Integer.parseInt(command)){
							case 0:
								index = 0;
								System.out.println("You choose the card: " + player.getCards().get("LEADER").get(index));
								optionSelected = true;
								break;
								
							case 1:
								if(player.getCards().get("LEADER").size()<1){
									System.out.println("type a valid index");
									break;
								} index = 1;
								System.out.println("You choose the card: " + player.getCards().get("LEADER").get(index));
								optionSelected = true;
								break;
			
							case 2:
								if(player.getCards().get("LEADER").size()<2){
									System.out.println("type a valid index");
									break;
								} index = 2;
								System.out.println("You choose the card: " + player.getCards().get("LEADER").get(index));
								optionSelected = true;
								break;
								
							case 3:
								if(player.getCards().get("LEADER").size()<3){
									System.out.println("type a valid index");
									break;
								} index = 3;
								System.out.println("You choose the card: " + player.getCards().get("LEADER").get(index));
								optionSelected = true;
								break;
								
							default:
								System.out.println("type a valid index");
								break;
						}
			}catch(NumberFormatException e) {System.out.println("type a number, please");}
		}
			
		System.out.println("NAME:"+ player.getCards().get("LEADER").get(index) + " ACTION:" + decision);
		client.getSendQueue().add(ClientMessageFactory.buildASKLDRACTmessage(client.getGameUUID(), // stato registrato da ZeroContext
							player.getCards().get("LEADER").get(index), decision));
		runFlag = false;
		}
	}
}
	
	