package it.polimi.ingsw.GC_32.Client.CLI;

import it.polimi.ingsw.GC_32.Client.Game.ClientFamilyMember;
import it.polimi.ingsw.GC_32.Common.Network.ClientMessageFactory;

public class ZeroLevelContext extends Context implements Runnable{

	private ClientCLI client;
	
	public ZeroLevelContext(ClientCLI client){
		super();
		this.client = client;
	}
		
	public void run(){
		open(null);
	}
	
	public void open(Object object){
		
		runFlag = true;
		
		while(runFlag){
			System.out.println("type a command:\n- board: display the board status\n- players: display players' status"
					+ "\n- action: make an action (if isn't your turn your requests won't be applied)");
			command = in.nextLine();
			switch(command){
			case "board":
				System.out.println(this.client.getBoard().toString());
				break;
			case "players":
				this.client.getPlayerList().forEach((UUID, client) -> System.out.println(client.toString()));
				break;
			case "action":				
				int familyMemberIndex = 0;
				int regionID = 0;
				int spaceID = 0;
				String actionType = null;
				
				boolean endFlag = false;
				
				while(!endFlag){
					boolean actionFlag = true;
					while(actionFlag){						
						System.out.println("type the ID of the family member you want to place\nThe symbol '>' means that the family member is busy\n");
						int i = 0;
						for(ClientFamilyMember fm : client.getPlayerList().get(client.getUUID()).getFamilyMembers()){
							System.out.println("["+i+"] "+fm.toString());
							i++;
						}						
						command = in.nextLine();
						try{
						if(Integer.parseInt(command)>client.getPlayerList().get(client.getUUID()).getFamilyMembers().length
								||client.getPlayerList().get(client.getUUID()).getFamilyMembers()[Integer.parseInt(command)].isBusy()){
							System.out.println("the choosen family member is already busy or you have typed an invalid index, please enter a valid index");
						}else{
							familyMemberIndex = Integer.parseInt(command);
							actionFlag = false;
						}
						}catch(NumberFormatException e){
							System.out.println("type a valid number");
						}
					}
					actionFlag = true;
					System.out.println("type the regionID where you would place your pawn");
					while(actionFlag){
						command = in.nextLine();
						try{
							regionID = Integer.parseInt(command);
							actionFlag = false;
						}catch(NumberFormatException e){
							System.out.println("type a valid number");
						}
					}
					actionFlag = true;
					System.out.println("ok, now type the spaceID where you would place your pawn");
					while(actionFlag){
						command = in.nextLine();
						try{
							spaceID = Integer.parseInt(command);
							actionFlag = false;
						}catch(NumberFormatException e){
							System.out.println("type a valid number");
						}
					}
					
					System.out.println("action is ready to be sent to the server. Type 'y' if you want ask the server to apply your action, otherwise type 'n'");
					actionFlag = true;
					while(actionFlag){
						command = in.nextLine();
						switch(command){
						case "y":
							actionFlag = false;
							endFlag = true;
							break;
						case "n":
							actionFlag = false;
							endFlag = false;
							break;
						default:
							System.out.println("please, type a valid letter");
							break;
						}
					}
				}
				
				switch(regionID){
				case 0:
					actionType = "PRODUCTION";
					break;
				case 1:
					actionType = "HARVEST";
					break;
				case 2:
					actionType = "COUNCIL";
					break;
				case 3:
					actionType = "MARKET";
					break;
				default:
					actionType = "TOWER";
					break;
				}
				
				// sending ASKACT message
				sendQueue.add(ClientMessageFactory.buildASKACTmessage(actionType, familyMemberIndex, spaceID, regionID));
				
				System.out.println("action sent to the server... waiting for response");
				
				try{ // do tempo ad eventuali context di aprirsi
					Thread.sleep(200);
				}catch(Exception e){}
				break;
				
			}
		}
	}
	
}
