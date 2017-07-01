package it.polimi.ingsw.GC_32.Client.CLI;

public class ZeroLevelContext extends Context implements Runnable{

	private ClientCLI client;
	
	private ShowCardDialog showCard;
	private AskActDialog askAct;
	private ChatDialog chatRoom;
	private LeaderDialog leaderDialog;
	
	public ZeroLevelContext(ClientCLI client){
		super();
		this.client = client;
		this.showCard = new ShowCardDialog(this.client);
		this.askAct = new AskActDialog(this.client);
		this.chatRoom = new ChatDialog(this.client);
		this.leaderDialog = new LeaderDialog(this.client);
	}
		
	public void run(){
		open(null);
	}
	
	public AskActDialog getAskAct(){
		return this.askAct;
	}
	
	public LeaderDialog getLeaderDialog(){
		return this.leaderDialog;
	}
	
	public void open(Object object){
		
		askAct.registerActionRunningGameFlag(actionRunningGameFlag);
		
		runFlag = true;
		
		while(runFlag){
			
			if(client.isWaiting()){
				System.out.println("type a command:\n- board: display the board status\n- players: display players' status\n"
						+ "- show card: to show details of cards on the game\n"
						+ "- chat room: to chat with other players\n"
						+ "- change name: to change the name of your player\n"
						+ "- leader: performa a special action leader");
			}
			else
				System.out.println("type a command:\n- board: display the board status\n- players: display players' status\n"
						+ "- show card: to show details of cards on the game\n"
						+ "- chat room: to chat with other players\n"
						+ "- change name: to change the name of your playe\n"
						+ "- leader: performa a special action leader\n"
						+ "- action: make an action (if isn't your turn your requests won't be applied)");
				
			command = in.nextLine();
			switch(command){
			
			case "board":
				System.out.println(this.client.getBoard().toString());
				break;
			
			case "players":
				this.client.getPlayerList().forEach((UUID, client) -> System.out.println(client.toString()));
				break;
				
			case "show card":
					showCard.open(object);
					break;	
					
			case "chat room":
				try {
					chatRoom.openChat();
				} catch (InterruptedException e) {}
				break;
				
			case "leader":
				leaderDialog.open(object);
				break;	
				
			case "change name":
					chatRoom.openChangeName();
					break;
					
			case "action":		
				if(!client.isWaiting()){
					askAct.open(object);
				}	
				break;	
			}
		}
	}	
}
