package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;

/**
 * ChatDialag manage a very simple chat. When a chat message must be sent a MSG message is generated and then sent to the server.  
 * 
 * @see Context
 *
 */

public class ChatDialog extends Context {
	
	public ChatDialog(ClientCLI client){
		super(client);
	}
	
	/**
	 * because chat doesn't return any message to the ClientCLI thread, open() can't be used. openChat() substitute open() method. It manage the chat interaction and
	 * is responsable of sending messages itself, putting the messsages directly into the client sendQueue.
	 * @throws InterruptedException
	 */
	public void openChat() throws InterruptedException{
		String name = null;
		String uuidReceiver = null;
		boolean all = true; //se false e' personale
		out.println("-------------------------------------------------");
		out.println("|	Welcome to ```Lorenzo il Magnifico's Chat``` \n"
				+ "| In this section you can send message to your friends and players online or to only one player.\n"
				+ "| Just select one of those options and have fun!\n|\n"
				+ "|	> personal: personal message. Only the player selected can display these message\n"
				+ "|	> all: send a message to the chat room. Everyone can see these message!\n"
				+ "|	> quit: to exit from ```Lorenzo il Magnifico's Chat```\n");
		
		out.println("> TYPE ENTER TO CONTINUE");
		
		runFlag = true;
		while(runFlag){
				command = in.nextLine();
				switch(command){
					case("personal"):
						out.println("| Personal message mode selected!");
						all = false;
						runFlag = false;
						break;
					case("all"):
						out.println(">| ChatRoom message mode selected!");
						all = true;
						runFlag = false;
						break;
					case("quit"):
						out.println("[!] Exiting from the chat.");
						return;
					default:
						out.println("--------------------------------------");
						out.println("| Just select one of those options\n"
					+ "|	> personal: personal message. Only the player selected can display these message\n"
					+ "|	> all: send a message to the chat room. Everyone can see these message!\n"
					+ "|	> quit: to exit from ```Lorenzo il Magnifico's Chat```\n");
						out.println("--------------------------------------");
						break;
					}
		}
		if(!all){
			out.println("| Select a player from this list:");
			ArrayList<String> onlinePlayers = new ArrayList<>();
			for(String uuid : client.getPlayerList().keySet()){
				if(!uuid.equals(client.getPlayerUUID())){
				out.println("> " + client.getPlayerList().get(uuid).getName());
				onlinePlayers.add(client.getPlayerList().get(uuid).getName());
				}
			}
			runFlag = true;
			while(runFlag){
				command = in.nextLine();
				out.println("| 	Player to send the message: " + command);
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
					out.println("[!] Exiting from the chat.\n\n");
					return;
				}
			}
		}
		out.println("---------------------------------------------");
		out.println("|	 Type the message:                 ");
		
			
		runFlag = true;
		while(runFlag){
			command = in.nextLine();
			if(!command.isEmpty()){
				runFlag = false;
			}
		}	
		String message = command;
		out.println("---------------------------------------------");
		if(!all){
			out.print("| [!] Sending the message to " + name);
			} else
				out.print("| [!] Sending the message");	
		
		if(!all){
			client.getSendQueue().add(ClientMessageFactory.buildMSGmessage(client.getPlayerUUID(), message, uuidReceiver, false));
			client.displaySendMessage(client.getPlayerList().get(client.getPlayerUUID()).getName(), message);
		} else
			client.getSendQueue().add(ClientMessageFactory.buildMSGmessage(client.getPlayerUUID(), message, null, true));
		return;
	}
	
	public void openChangeName(){
		String name = null;
		out.println("-------------------------------------------------");
		out.println(" In this section you can change your Player name.\n"
				+ "| Just type one of those options\n"
				+ "|	> change: to change your Player's name\n"
				+ "|	> quit: to return in the main scree");
		
		runFlag = true;
		while(runFlag){	
				command = in.nextLine();
				switch(command){
				case("change"):
					out.println("You decided to change your name.");
					runFlag = false;
					break;
				case("quit"):
					out.println("Exiting from this section");
					return;
				default:
					out.println("Please, type a valid option...\n");
					break;
				}
			}
		out.println("Type the new Name for your player");
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
			out.println("Are you sure to change your current name " + client.getPlayerList()
					.get(client.getPlayerUUID()).getName() +" with the new one " + name);
			out.println(">type y or n");
			command = in.nextLine();
			switch(command){
			
			case("y"):
				out.println("Operation confermed.");
				runFlag = false;
				client.getSendQueue().add(ClientMessageFactory.buildCHGNAMEmessage(client.getPlayerUUID(), name));
				return;
			case("n"):
				out.println("Exiting from this section");	
				runFlag = false;
				break;
			default:
				out.println("Please, type a valid option");
				break;
			}	
		}		
	}			
	@Override
	public String open(Object object) {
		return "";
	}

}
