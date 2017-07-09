package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Client.Game.ClientCardRegistry;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

/**
 * ShowCardDialaog allow the players to see details of cards on the board, or to see what excommunication tiles have been extracted for this game. It offer
 * a very simple menù to hadle all this feature. 
 * 
 * @see Context
 *
 */

public class ShowCardDialog extends Context{
	
	public ShowCardDialog(ClientCLI client){
		super(client);
	}
	
	@Override
	public String open(Object object) {
		
			out.println("type a command:\n-region: show detail of a card on the board\n-player: show detail of a player's card"
					+ "\n-excommunication: to show the excommunication cards on the board"
					+ "\n-quit: to exit");
			command = in.nextLine();
			switch (command){

			case("excommunication"): {						
				out.println("Excommunication cards on the board are these:\n");
				
				for(int i=0; i<	client.getBoard().getExcommunicationCards().size(); i++){
					out.println("Period: "+i+ " Card: " + client.getBoard()
																		.getExcommunicationCards()
																		.get(i) + '\n');
				}
				out.println("type the period of the card to show or q to quit:");
				boolean optionSelected = false;
				
				while(!optionSelected){
					command = in.nextLine();
					if("q".equals(command)){
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
				int spaceID;
				out.println("type the regionID of the card yo want to see\n[4-7]");
				boolean optionSelected = false;
				
				while(!optionSelected){
					command = in.nextLine();
					try{
						if(Integer.parseInt(command)<8 && Integer.parseInt(command) >3){
							regionID = Integer.parseInt(command);
							break;
						}
						else
							out.println("type a valid number or type q to exit");
					}catch(NumberFormatException e){
						out.println("type a valid number or type q to exit");
					}
				}
				
				while(!optionSelected){	
					out.println("ok, now type the spaceID of the card yo want to see\n[0-3]");
					command = in.nextLine();	
					if("q".equals(command)){
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
								
								out.println("Showing details of " + nameCard);
								
								out.println(CardCli.print(nameCard, ClientCardRegistry.getInstance()
										.getDetails(nameCard)));	
							}
						else
							out.println("There are not any cards on this space");
						}	
					}
					catch(NumberFormatException e){
						out.println("type a valid number or type q to exit");
					}	
				}	
				break;
			}	
			
			case("player"):
				boolean optionSelected = false;
				ClientPlayer player = client.getPlayerList().get(client.getPlayerUUID());
				ArrayList<String> stringList = new ArrayList<String>();
				out.println("Chose the index of your player's card to show more details\n");
				
				player.getCards().forEach((cardtype, cardlist) ->{
					cardlist.forEach(card ->{
						stringList.add(card);
						out.println(stringList.indexOf(card) + "]" + card);
					});
				});	
				out.println(stringList.toString());
				while(!optionSelected){
					command = in.nextLine();
					if("q".equals(command)){
						break;
					}
					try{
						if(Integer.parseInt(command) < stringList.size() && Integer.parseInt(command) >= 0){
								String name = stringList.get(Integer.parseInt(command));
								out.println(CardCli.print(name, ClientCardRegistry.getInstance()
										.getDetails(name)));
						}
						else
							out.println("type a valid index or type q to exit");
					}catch(NumberFormatException e){
						out.println("type a valid index or type q to exit");
					}
				}	
				break;
				
			case("quit"):
				return null;
			
			default:
				out.println("please, type a valid string");
				break;
			}
		return null;
	}	
		
}

