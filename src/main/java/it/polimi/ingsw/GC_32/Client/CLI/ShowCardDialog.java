package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Client.Game.CardCli;
import it.polimi.ingsw.GC_32.Server.Setup.JsonImporter;

public class ShowCardDialog extends Context{
	
	private ClientCLI client;
	private boolean flagCard;
	
	public ShowCardDialog(ClientCLI client){
		super();
		this.client = client;
		flagCard = true;
	}
	
	@Override
	public void open(Object object) {
		
		while(flagCard){	
			System.out.println("type a command:\n-region: show detail of a card on the board\n-player: show detail of a player's card"
					+ "\n-quit: to exit");
			command = in.nextLine();
			
			switch (command){
			
			case("region"):	{
				int regionID = 4;
				int spaceID = 0;
				System.out.println("type the regionID of the card yo want to see\n[4-7]");
				boolean regionFlag = true;
				
				while(regionFlag){
					command = in.nextLine();
					try{
						if(Integer.parseInt(command)<8 && Integer.parseInt(command) >3){
							regionID = Integer.parseInt(command);
							regionFlag = false;
						}
						else
							System.out.println("type a valid number");
					}catch(NumberFormatException e){
						System.out.println("type a valid number");
					}
				}
				
				regionFlag = true;
				while(regionFlag){
					System.out.println("ok, now type the spaceID of the card yo want to see\n[0-3]");
					command = in.nextLine();
					try{
						if(Integer.parseInt(command)<4 && Integer.parseInt(command) >=0){
							spaceID = Integer.parseInt(command);
							regionFlag = false;
						}
						else
							System.out.println("type a valid number");
					}catch(NumberFormatException e){
						System.out.println("type a valid number");
					}	
				}
				try{
					
				Reader json = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("cards.json"));
				JsonValue card = JsonImporter.importSingleCard(json, 
							client.getBoard().getRegionList().get(regionID)
							.getActionSpaceList().get(spaceID).getCardName());
				System.out.println("Showing details of " + this.client.getBoard().getRegionList().get(regionID)
						.getActionSpaceList().get(spaceID).getCardName());
				try{
					System.out.println(CardCli.print(card.asObject()));
				}
				catch(NullPointerException e){
					System.out.println("There are not any cards on this space");
					} 
				} catch(IOException e){}
				break;
			}	
			case("player"):
				System.out.println("NOT IMPLEMENTED YET");
				return;
			case("quit"):
				return;
			default:
				System.out.println("please, type a valid string");
				break;
			}
		}
	}	
		
}

