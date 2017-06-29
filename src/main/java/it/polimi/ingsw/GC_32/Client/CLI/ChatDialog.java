package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;

public class ChatDialog extends Context {

	private ClientCLI client;
	
	public ChatDialog(ClientCLI client){
		super();
		this.client = client;
		this.sendQueue = client.getSendQueue();
		this.actionRunningGameFlag= false;
	}
	
	public void openChat() throws InterruptedException{
		String name = null;
		String uuidReceiver = null;
		boolean all = true; //se false e' personale
		boolean optionSelected = false;
		System.out.println("-------------------------------------------------");
		System.out.println("|	Welcome to ```Lorenzo il Magnifico's Chat``` \n"
				+ "| In this section you can send message to your friends and players online or to only one player.\n"
				+ "| Just select one of those options and have fun!\n|\n"
				+ "|	> personal: personal message. Only the player selected can display these message\n"
				+ "|	> all: send a message to the chat room. Everyone can see these message!\n"
				+ "|	> quit: to exit from ```Lorenzo il Magnifico's Chat```\n");
		
		System.out.println("> TYPE ENTER TO CONTINUE");
		
		actionRunningGameFlag = true;
		while(true){
			while(actionRunningGameFlag){
				while(!optionSelected){
					System.out.println("------------------------------------");
					System.out.println("| Just select one of those options\n"
				+ "  > personal: personal message. Only the player selected can display these message\n"
							+ "  > all: send a message to the chat room. Everyone can see these message!\n"
				+ "  > quit: to exit from ```Lorenzo il Magnifico's Chat```\n");
					System.out.println("--------------------------------------");
					command = in.nextLine();
					switch(command){
					case("personal"):
						System.out.println("| Personal message mode selected!");
						all = false;
						optionSelected = true;
						break;
					case("all"):
						System.out.println(">| ChatRoom message mode selected!");
						all = true;
						optionSelected = true;
						break;
					case("quit"):
						System.out.println("[!] Exiting from the chat.");
						return;
					default:
						System.out.println("| Please, type a valid option...\n");
					}
				}
				optionSelected = false;
				if(!all){
					System.out.println("| Select a player from this list:");
					ArrayList <String> onlinePlayers = new <String> ArrayList();
					for(String uuid : client.getPlayerList().keySet()){
						if(!uuid.equals(client.getUUID())){
							System.out.println("> " + client.getPlayerList().get(uuid).getName());
							onlinePlayers.add(client.getPlayerList().get(uuid).getName());
						}
					}
					while(!optionSelected){
						command = in.nextLine();
						System.out.println("| Player to send the message: " + command);
						if(onlinePlayers.contains(command)){
							int index = onlinePlayers.lastIndexOf((command));
							name = onlinePlayers.get(index);
							for(String uuid: client.getPlayerList().keySet()){
								if(client.getPlayerList().get(uuid).getName().equals(name)){
									uuidReceiver = uuid;
								}
							}		
							break;
						}
						if(command.equals("quit")){
							System.out.println("[!] Exiting from the chat.\n\n");
							return;
						}
					}
				}
				System.out.println("------------------------------------");
				System.out.println("| Type the message:                 |");
				System.out.println("---------------------------------------------");
				while(!optionSelected){
					command = in.nextLine();
					if(!command.isEmpty()){
						break;
					}
				}	
				String message = command;
				System.out.println("---------------------------------------------");
				if(!all){
					System.out.print("| [!] Sending the message to " + name);
				} else
					System.out.print("| [!] Sending the message");
				
				//animazione . . . 
				
				for(int i=0; i<3;i++){
					Thread.sleep(500);
					System.out.print(".");
				}
				System.out.print("\n[PROGRESS]: [");
				
				for(int i=0; i<20; i++){
					Thread.sleep(100);
					System.out.print("#");	
				}
				System.out.println("]");
				if(!all){
					client.getSendQueue().add(ClientMessageFactory.buildMSGmessage(client.getUUID(), message, uuidReceiver, false));
					client.displaySendMessage(client.getPlayerList().get(client.getUUID()).getName(), message);
					break;
				} else
					client.getSendQueue().add(ClientMessageFactory.buildMSGmessage(client.getUUID(), message, null, true));
					break;
			}
		}	
	}
	
	public void openChangeName(){
		String name = null;
		boolean optionSelected = false;
		System.out.println("-------------------------------------------------");
		System.out.println(" In this section you can change your Player name.\n"
				+ "Just type one of those options\n"
				+ "  > change: to change your Player's name\n"
				+ "  > quit: to return in the main scree");
		
		actionRunningGameFlag = true;
		
		while(actionRunningGameFlag){
			while(!optionSelected){
				command = in.nextLine();
				switch(command){
				case("change"):
					System.out.println("You decided to change your name.");
					optionSelected = true;
					break;
				case("quit"):
					System.out.println("Exiting from this section");
					return;
				default:
					System.out.println("Please, type a valid option...\n");
				}
			}
			System.out.println("Type the new Name for your player");
			optionSelected = false;
			while(!optionSelected){
				command = in.nextLine();
				name = command;
				if(!command.isEmpty()){
					break;
				}
			}		
			while(!optionSelected){
				System.out.println("Are you sure to change your current name " + client.getPlayerList()
						.get(client.getUUID()).getName() +" with the new one " + name);
				System.out.println(">type y or n");
				command = in.nextLine();
				switch(command){
				
				case("y"):
					System.out.println("Operation confermed.");
					optionSelected = true;
					client.getSendQueue().add(ClientMessageFactory.buildCHGNAMEmessage(client.getUUID(), name));
					return;
				case("n"):
					System.out.println("Exiting from this section");	
					break;
				default:
					System.out.println("Please, type a valid option");
				}	
				break;
			}		
		}			
	}
		
	@Override
	public void open(Object object) {
	}

}
