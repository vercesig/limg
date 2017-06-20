package it.polimi.ingsw.GC_32.Client.CLI;

public class ZeroLevelContext extends Context implements Runnable{

	private ClientCLI client;
	
	public ZeroLevelContext(ClientCLI client){
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
			command = super.in.nextLine();
			switch(command){
			case "board":
				System.out.println(this.client.getBoard().toString());
				break;
			case "players":
				this.client.getPlayerList().forEach((UUID, client) -> System.out.println(client.toString()));
				break;
				
			}
		}
	}
	
}
