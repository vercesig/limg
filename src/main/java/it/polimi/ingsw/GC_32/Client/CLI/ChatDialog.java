package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;

public class ChatDialog extends Context {
	
	public ChatDialog(ClientCLI client){
		super(client);
	}
	
	public void openChat() throws InterruptedException{
		String name = null;
		String uuidReceiver = null;
		boolean all = true; //se false e' personale
		System.out.println("-------------------------------------------------");
		System.out.println("|	Welcome to ```Lorenzo il Magnifico's Chat``` \n"
				+ "| In this section you can send message to your friends and players online or to only one player.\n"
				+ "| Just select one of those options and have fun!\n|\n"
				+ "|	> personal: personal message. Only the player selected can display these message\n"
				+ "|	> all: send a message to the chat room. Everyone can see these message!\n"
				+ "|	> quit: to exit from ```Lorenzo il Magnifico's Chat```\n");
		
		System.out.println("> TYPE ENTER TO CONTINUE");
		
		runFlag = true;
		while(runFlag){
				command = in.nextLine();
				switch(command){
					case("personal"):
						System.out.println("| Personal message mode selected!");
						all = false;
						runFlag = false;
						break;
					case("all"):
						System.out.println(">| ChatRoom message mode selected!");
						all = true;
						runFlag = false;
						break;
					case("quit"):
						System.out.println("[!] Exiting from the chat.");
						return;
					default:
						System.out.println("------------------------------------");
						System.out.println("| Just select one of those options\n"
					+ "  > personal: personal message. Only the player selected can display these message\n"
								+ "  > all: send a message to the chat room. Everyone can see these message!\n"
					+ "  > quit: to exit from ```Lorenzo il Magnifico's Chat```\n");
						System.out.println("--------------------------------------");
						break;
					}
		}
		if(!all){
			System.out.println("| Select a player from this list:");
			ArrayList<String> onlinePlayers = new ArrayList<>();
			for(String uuid : client.getPlayerList().keySet()){
				if(!uuid.equals(client.getPlayerUUID())){
				System.out.println("> " + client.getPlayerList().get(uuid).getName());
				onlinePlayers.add(client.getPlayerList().get(uuid).getName());
				}
			}
			runFlag = true;
			while(runFlag){
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
					runFlag = false;
				}
				if("quit".equals(command)){
					System.out.println("[!] Exiting from the chat.\n\n");
					return;
				}
			}
		}
		System.out.println("------------------------------------");
		System.out.println("| Type the message:                 |");
		System.out.println("---------------------------------------------");
			
		runFlag = true;
		while(runFlag){
			command = in.nextLine();
			if(!command.isEmpty()){
				runFlag = false;
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
			client.getSendQueue().add(ClientMessageFactory.buildMSGmessage(client.getPlayerUUID(), message, uuidReceiver, false));
			client.displaySendMessage(client.getPlayerList().get(client.getPlayerUUID()).getName(), message);
		} else
			client.getSendQueue().add(ClientMessageFactory.buildMSGmessage(client.getPlayerUUID(), message, null, true));
		return;
	}
	
	public void openChangeName(){
		String name = null;
		System.out.println("-------------------------------------------------");
		System.out.println(" In this section you can change your Player name.\n"
				+ "Just type one of those options\n"
				+ "  > change: to change your Player's name\n"
				+ "  > quit: to return in the main scree");
		
		runFlag = true;
		while(runFlag){	
				command = in.nextLine();
				switch(command){
				case("change"):
					System.out.println("You decided to change your name.");
					runFlag = false;
					break;
				case("quit"):
					System.out.println("Exiting from this section");
					return;
				default:
					System.out.println("Please, type a valid option...\n");
					break;
				}
			}
		System.out.println("Type the new Name for your player");
		runFlag = true;
		while(runFlag){
			command = in.nextLine();
			name = command;
			if(!command.isEmpty()){
				runFlag = false;
			}
		}		
		runFlag = true;
		while(runFlag){
			System.out.println("Are you sure to change your current name " + client.getPlayerList()
					.get(client.getPlayerUUID()).getName() +" with the new one " + name);
			System.out.println(">type y or n");
			command = in.nextLine();
			switch(command){
			
			case("y"):
				System.out.println("Operation confermed.");
				runFlag = false;
				client.getSendQueue().add(ClientMessageFactory.buildCHGNAMEmessage(client.getPlayerUUID(), name));
				return;
			case("n"):
				System.out.println("Exiting from this section");	
				runFlag = false;
				break;
			default:
				System.out.println("Please, type a valid option");
				break;
			}	
		}		
	}			
	@Override
	public String open(Object object) {
		return null;
	}

}
