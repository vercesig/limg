package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Client.Network.ClientMessageFactory;
import it.polimi.ingsw.GC_32.Common.Utils.Utils;

/**
 * ZeroLevelContext is the context which is open every time the client hasn't an action to perform or there isn't any active context. It is implemented as a Thread to
 * stop when a CONTEXT message has been received, and then to reactivate when the context has ended its functionality.
 * 
 * The main action can be performed from the ZeroLevelContext are
 * 
 * open the show card dialog
 * open the chat room
 * open the context which allows the client to change his name
 * open the leader action handler context
 * open the action context
 * allows to end the turn without perform any action
 * 
 * @see Context
 *
 */

public class ZeroLevelContext extends Context implements Runnable{
    private Logger LOGGER = Logger.getLogger(this.getClass().toString());

	private ShowCardDialog showCard;
	private AskActDialog askAct;
	private ChatDialog chatRoom;
	private LeaderDialog leaderDialog;
	
	public ZeroLevelContext(ClientCLI client){
		super(client);
		this.showCard = new ShowCardDialog(client);
		this.askAct = new AskActDialog(client);
		this.chatRoom = new ChatDialog(client);
		this.leaderDialog = new LeaderDialog(client);
	}
		
	public void run(){
		open(null);
	}
		
	public String open(Object object){
		
		runFlag = true;
		
		while(runFlag){

			out.println("type a command:\n- board: display the board status\n- players: display players' status\n"
					+ "- show card: to show details of cards on the game\n"
					+ "- chat room: to chat with other players\n"
					+ "- change name: to change the name of your playe\n"
					+ "- leader: perform a special action leader\n"
					+ "- action: make an action\n"
					+ "- end turn: to pass your turn");
				
			try{ // consente di interrompere lo zeroLevel sull'inputstream in seguito ad interrupt dovuti all'apertura di nuovi context
				while(!reader.ready()){
					Thread.sleep(200);
				}
				command = reader.readLine();
			}catch(Exception e){
			    LOGGER.log(Level.FINEST, "Cambio di contesto", e);
			}
			
			switch(command){
    			case "board":
    				out.println(this.client.getBoard().toString());
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
    				} catch (InterruptedException e) {
    				    Thread.currentThread().interrupt();
    				}
    				break;
    				
    			case "leader":
    				if(!client.isWaiting()){
	    				String leaderResponse = leaderDialog.open(object);
	    				if(leaderResponse!=null&&!"".equals(leaderResponse))
	    					client.getSendQueue().add(leaderResponse);
						Utils.safeSleep(300);
    				}else{
    					out.println("isn't your turn");
    				}
    				break;	
    				
    			case "change name":
    				chatRoom.openChangeName();
    				break;
    					
    			case "action":		
    				if(!client.isWaiting()){
    					String response = askAct.open(object);
    					if(response!=null&&!"".equals(response))
    						client.getSendQueue().add(response);
    					Utils.safeSleep(300);
    				}else{
    					out.println("isn't your turn");
    				}
    				break;
    			case "end turn":
    				client.getSendQueue().add(ClientMessageFactory.buildTRNENDmessage(client.getGameUUID(), client.getPlayerList().get(client.getPlayerUUID()).getName()));	
    				break;
			}
		}
		return null;
	}	
}
