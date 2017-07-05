package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
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
	protected ConcurrentLinkedQueue<Object> contextQueue;
	protected ConcurrentLinkedQueue<String> messageQueue;
	
	
	private ConcurrentLinkedQueue<String> sendQueue;
	protected ConcurrentLinkedQueue<String> clientsendQueue;
	
	// context management
	private Context[] contextList;
	private Thread zeroLevelContextThread;
	
	// input handling
	protected Scanner in;
	protected PrintWriter out;
	
	private boolean idleRun = false; // if zeroLevel must run
	private boolean wait = true; // if player is waiting he can't display action menu;
	
	protected boolean leaderStartPhase = true;
	
	private boolean stop = false;
	
	public ClientCLI(){		
		
		out = new PrintWriter(System.out, true);
		in = new Scanner(System.in);
		
		contextQueue = new ConcurrentLinkedQueue<Object>();
		messageQueue = new ConcurrentLinkedQueue<String>();
		
		this.contextList = new Context[7];
		contextList[0] = new ZeroLevelContext(this);		
		contextList[1] = new PrivilegeContext(this);
		contextList[2] = new ServantContext(this);
		contextList[3] = new ExcommunicationContext(this);
		contextList[4] = new ChangeEffectContext(this);
		contextList[5] = new LeaderSetContext(this);
		contextList[6] = new ActionEffectContext(this);
		
		clientsendQueue = new ConcurrentLinkedQueue<String>();
	}
	
	public void run(){	
		
		while(!stop){
						
			while(!contextQueue.isEmpty()){
				contextList[0].close();
				JsonObject contextMessage = (JsonObject) contextQueue.poll();
				String response = contextList[contextMessage.get("CONTEXTID").asInt()].open(contextMessage.get("PAYLOAD"));
				if(!"".equals(response)){
					System.out.println("inviando messaggio "+response);
					clientsendQueue.add(response);
				}
				try{ //waiting for other context
					Thread.sleep(500);
				}catch(InterruptedException e){
				    Thread.currentThread().interrupt();
				}
				
				if(contextQueue.isEmpty())
					idleRun=false;			
			}
			
			// show messages
			while(!messageQueue.isEmpty()){
				out.println(messageQueue.poll());
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
	
	public Scanner getIn(){
		return this.in;
	}
	
	public PrintWriter getOut(){
		return this.out;
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
	
	public Context[] getContextList(){
		return this.contextList;
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
	
	public void displayMessage(String message){
		this.messageQueue.add("[server message]-----------------------------\n"+message+""
									   + "\n---------------------------------------------");
	}
	
	public void displaySendMessage(String playerID, String message){
		out.println("| ===========================================\n"
				   +"|               YOU SENT A MESSAGE !!!       \n"
				   +"| ===========================================\n"
				   +"| > "+ playerID+ ":\n"
				   +"| "+ message +"\n|\n"
				   +"| ===========================================\n");
	}
	
	public void receiveMessage(String playerID, String message) {
		out.println("| ===========================================\n"
				   +"|           YOU RECEIVED A MESSAGE !!!       \n"
				   +"| ===========================================\n"
				   +"| > "+ playerID+ ":\n"
				   +"| "+ message +"\n|\n"
				   +"| ===========================================\n");	
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
