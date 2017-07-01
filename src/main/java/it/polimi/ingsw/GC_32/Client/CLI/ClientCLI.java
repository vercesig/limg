package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import it.polimi.ingsw.GC_32.Common.Utils.KillableRunnable;

public class ClientCLI implements ClientInterface, KillableRunnable{

	// game management
	private ClientBoard boardReference;
	private HashMap<String, ClientPlayer> playerListReference;
	private String playerUUID;
	private String gameUUID;
	
	// network management
	private ConcurrentLinkedQueue<Object> contextQueue;
	private ConcurrentLinkedQueue<String> messageQueue;
	
	
	private ConcurrentLinkedQueue<String> sendQueue;
	private ConcurrentLinkedQueue<String> clientsendQueue;
	
	// context management
	private Context[] contextList;
	private Thread zeroLevelContextThread;
	
	private boolean idleRun = false; // if zeroLevel must run
	private boolean wait = true; // if player is waiting he can't display action menu;
	
	private boolean leaderStartPhase = true;
	
	private boolean stop = false;
	
	public ClientCLI(){		
		contextQueue = new ConcurrentLinkedQueue<Object>();
		messageQueue = new ConcurrentLinkedQueue<String>();
		
		this.contextList = new Context[6];
		contextList[0] = new ZeroLevelContext(this);		
		contextList[1] = new PrivilegeContext(this);
		contextList[2] = new ServantContext(this);
		contextList[3] = new ExcommunicationContext(this);
		contextList[4] = new ChangeEffectContext(this);
		//contextList[5] = new ActionEffectContext(this);
		contextList[5] = new LeaderSetContext(this);
		
		clientsendQueue = new ConcurrentLinkedQueue<String>();
	}
	
	public void run(){	
		
		while(!stop){
						
			while(!contextQueue.isEmpty()){
				contextList[0].close();
				JsonObject contextMessage = (JsonObject) contextQueue.poll();
				clientsendQueue.add(contextList[contextMessage.get("CONTEXTID").asInt()].open(contextMessage.get("PAYLOAD")));
				
				try{ //waiting for other context
					Thread.sleep(500);
				}catch(InterruptedException e){}
				
				if(contextQueue.isEmpty())
					idleRun=false;			
			}
			
			// show messages
			while(!messageQueue.isEmpty()){
				System.out.println(messageQueue.poll());
			}
			
			// spedisco messaggi
			if(!clientsendQueue.isEmpty()){
				clientsendQueue.forEach(message -> {
					sendQueue.add(message);
				});
				clientsendQueue.clear();		
			}
			
			if(!idleRun&&!leaderStartPhase){
				idleRun=true;
				zeroLevelContextThread = new Thread((Runnable) contextList[0]);
				zeroLevelContextThread.start();
			}
		}
	}
	
	public void registerBoard(ClientBoard board){
		this.boardReference = board;
	}
	
	public void registerPlayers(HashMap<String,ClientPlayer> playerList){
		this.playerListReference = playerList;
	}
	
	public void registerGameUUID(String UUID){
		this.gameUUID = UUID;
	}
	
	public void registerPlayerUUID(String UUID){
		this.playerUUID = UUID;
	}
		
	public ClientBoard getBoard(){
		return this.boardReference;
	}
	
	public String getPlayerUUID(){
		return this.playerUUID;
	}
	
	public String getGameUUID(){
		return this.gameUUID;
	}
	
	public boolean isWaiting(){
		return this.wait;
	}
	
	public void displayMessage(String message){
		this.messageQueue.add(message);
	}
	
	public ConcurrentLinkedQueue<String> getSendQueue(){
		return this.sendQueue;
	}
		
	public void leaderStartPhaseEnd(){
		this.leaderStartPhase=false;
	}
	
	public void openContext(JsonObject contextMessage){
		this.contextQueue.add(contextMessage);
	}
	
	public HashMap<String, ClientPlayer> getPlayerList(){
		return this.playerListReference;
	}
	
	public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue) {
		this.sendQueue = queue;		
	}
	
	public void displaySendMessage(String playerID, String message){
		System.out.println("---------------------------------------");
		System.out.println("|      YOU SENT A MESSAGE !!!      ");	
		System.out.println("|-------------------------------------------");
		System.out.println("| > "+ playerID+ ":");
		System.out.println("| "+ message +"\n|");
		System.out.println("| ===========================================");
	}
	
	public void receiveMessage(String playerID, String message) {
		System.out.println("---------------------------------------");
		System.out.println("|      YOU RECEIVED A MESSAGE !!!      ");	
		System.out.println("|-------------------------------------------");
		System.out.println("| > "+ playerID+ ":");
		System.out.println("| "+ message +"\n|");
		System.out.println("| ===========================================");		
	}
	
	@Override
	public void waitTurn(boolean flag) {
		this.wait = flag;
	}

    @Override
    public void kill() {
        this.stop = true;
    }
}
