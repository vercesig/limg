package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Client.Game.CardCli;
import it.polimi.ingsw.GC_32.Client.Game.ClientCardRegistry;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;


public class ShowCardDialog extends Context{
	
	public ShowCardDialog(ClientCLI client){
		super(client);
	}
	
	@Override
	public String open(Object object) {
		
			System.out.println("type a command:\n-region: show detail of a card on the board\n-player: show detail of a player's card"
					+ "\n-excommunication: to show the excommunication cards on the board"
					+ "\n-quit: to exit");
			command = in.nextLine();
			switch (command){

			case("excommunication"): {						
				System.out.println("Excommunication cards on the board are these:\n");
				
				for(int i=0; i<	client.getBoard().getExcommunicationCards().size(); i++){
					System.out.println("Period: "+i+ " Card: " + client.getBoard()
																		.getExcommunicationCards()
																		.get(i) + '\n');
				}
				System.out.println("type the period of the card to show or q to quit:");
				boolean optionSelected = false;
				
				while(!optionSelected){
					command = in.nextLine();
					if(command.equals("q")){
						optionSelected = true;
					}
					try{
						if(Integer.parseInt(command) < client.getBoard().getExcommunicationCards().size() 
								&& Integer.parseInt(command)>= 0){
						
							if(!ClientCardRegistry.getInstance()
									.getDetails(client.getBoard()
											.getExcommunicationCards()
											.get(Integer.parseInt(command))).isNull()){
								
								String nameCard = client.getBoard().getExcommunicationCards().get(Integer.parseInt(command));	
								System.out.println(CardCli.print(nameCard, ClientCardRegistry.getInstance()
										.getDetails(nameCard)));
							}
						}
					}catch(NumberFormatException e){
						System.out.println("type a valid number or type q to exit");
						break;
					}
				}
				break;
			}
			case("region"):	{
				int regionID = 4;
				int spaceID = 0;
				System.out.println("type the regionID of the card yo want to see\n[4-7]");
				boolean optionSelected = false;
				
				while(!optionSelected){
					command = in.nextLine();
					try{
						if(Integer.parseInt(command)<8 && Integer.parseInt(command) >3){
							regionID = Integer.parseInt(command);
							break;
						}
						else
							System.out.println("type a valid number or type q to exit");
					}catch(NumberFormatException e){
						System.out.println("type a valid number or type q to exit");
					}
				}
				
				while(!optionSelected){	
					System.out.println("ok, now type the spaceID of the card yo want to see\n[0-3]");
					command = in.nextLine();	
					if(command.equals("q")){
						optionSelected = true;
						break;
					}	
					try{
						if(Integer.parseInt(command)<4 && Integer.parseInt(command) >=0){
							spaceID = Integer.parseInt(command);
						
							if(!ClientCardRegistry.getInstance()
									.getDetails(client.getBoard().getRegionList().get(regionID)
											.getActionSpaceList().get(spaceID).getCardName()).isNull()){
								
								String nameCard = client.getBoard().getRegionList().get(regionID)
										.getActionSpaceList().get(spaceID).getCardName();
								
								System.out.println("Showing details of " + nameCard);
								
								System.out.println(CardCli.print(nameCard, ClientCardRegistry.getInstance()
										.getDetails(nameCard)));	
							}
						else
							System.out.println("There are not any cards on this space");
						}	
					}
					catch(NumberFormatException e){
						System.out.println("type a valid number or type q to exit");
					}	
				}	
				break;
			}	
			
			case("player"):
				boolean optionSelected = false;
				ClientPlayer player = client.getPlayerList().get(client.getPlayerUUID());
				ArrayList<String> stringList = new ArrayList<String>();
				System.out.println("Chose the index of your player's card to show more details\n");
				
				player.getCards().forEach((cardtype, cardlist) ->{
					cardlist.forEach(card ->{
						stringList.add(card);
						System.out.println(stringList.indexOf(card) + "]" + card);
					});
				});	
				System.out.println(stringList.toString());
				while(!optionSelected){
					command = in.nextLine();
					if(command.equals("q")){
						break;
					}
					try{
						if(Integer.parseInt(command) < stringList.size() && Integer.parseInt(command) >= 0){
								String name = stringList.get(Integer.parseInt(command));
								System.out.println(CardCli.print(name, ClientCardRegistry.getInstance()
										.getDetails(name)));
						}
						else
							System.out.println("type a valid index or type q to exit");
					}catch(NumberFormatException e){
						System.out.println("type a valid index or type q to exit");
					}
				}	
				break;
				
			case("quit"):
				return null;
			
			default:
				System.out.println("please, type a valid string");
				break;
			}
		return null;
	}	
		
}

