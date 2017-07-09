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
import it.polimi.ingsw.GC_32.Common.Utils.Utils;

/**
 * the main thread which handle all the command line interface interaction. It's main scope is to open context when a CONTEXT message has been received, to send message
 * coming from contexts and to offer methods (ClientCLI implements ClientInterface interface) avaiable from external classes to interact with the player. It also has
 * all the references to the client-model classes, in order to print them on the screen.
 *
 * <ul>
 * <li>{@link ClientCLI#boardReference}: the ClientBoard</li>
 * <li>{@link ClientCLI#playerListReference}: the player list connected to the game</li>
 * <li>{@link ClientCLI#playerUUID}: the UUID of this player</li>
 * <li>{@link ClientCLI#gameUUID}: the UUID of the game the player is playing on</li>
 * <li>{@link ClientCLI#contextList}: the list of all available contexts</li>
 * <li>{@link ClientCLI#zeroLevelContextThread}: reference to the ZeroLevelContext thread</li>
 * <li>{@link ClientCLI#contextQueue}: the queue containing all the CONTEXT messages sent to the client. Every message in this queue will open a single context</li>
 * <li>{@link ClientCLI#messageQueue}: the queue containing all the messages which must be displayed</li>
 * <li>{@link ClientCLI#sendQueue}: the queue used to send message from the CLI threads</li>
 * <li>{@link ClientCLI#clientSendQueue}: the queue used from contexts to send messages to the server</li>
 * <li>{@link ClientCLI#in}: scanner handling the input stream</li>
 * <li>{@link ClientCLI#out}: printWriter handling the output stream</li>
 * <li>{@link ClientCLI#idleRun}: flag which indicates if ZeroLevelContext must be activated</li>
 * <li>{@link ClientCLI#wait}: flag which indicates if player must wait his turn. If so, the action menu can't be activated</li>
 * <li>{@link ClientCLI#leaderStartPhase}: flag which indicates if the leader card distributing phase is ended or not</li>
 * <li>{@link ClientCLI#stop}: use this flag to stop this thread</li>
 * </ul>
 * 
 * @see ClientBoard, ClientPlayer, Context, ZeroLevelContext, PrivilegeContext, ServantContext, ExcommunicationContext, ChangeEffectContext, LeaderSetContext, 
 * ActionEffectContext
 *
 */

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
	
	/**
	 * initialize the CLI, loading all the available context
	 */
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
	
	/**
	 * run() method continuosly check if there are context to open or message to display or to send. When no of this action can be performed ZeroLevelContextThread is
	 * launched and stopped when one of the previous condition is satisfied.
	 */
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
				
				Utils.safeSleep(1000); //waiting for others context
				
				if(contextQueue.isEmpty())
					idleRun=false;			
			}
			
			// show messages
			while(!messageQueue.isEmpty()){
				out.println(messageQueue.poll());
			}
			
			// send messages
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
	
	/**
	 * allows to retrive the Scanner 
	 * @return the Scanner to work on the system.in input stream
	 */
	public Scanner getIn(){
		return this.in;
	}
	
	/**
	 * allows to retrive the PrintWriter 
	 * @return the PrintWriter to work on the system.out output stream
	 */
	public PrintWriter getOut(){
		return this.out;
	}
	
	/**
	 * register the board
	 * @param board the board which must be registered
	 */
	public void registerBoard(ClientBoard board){
		this.boardReference = board;
	}
	
	/**
	 * register the player list
	 * @param HashMap containing all the information about the player connected to the game
	 */
	public void registerPlayers(HashMap<String,ClientPlayer> playerList){
		this.playerListReference = playerList;
	}
	
	/**
	 * register the UUID of the game to which the client has been connected
	 * @param the game UUID
	 */
	public void registerGameUUID(String UUID){
		this.gameUUID = UUID;
	}
	
	/**
	 * register the player UUID
	 * @param the UUID of the player
	 */
	public void registerPlayerUUID(String UUID){
		this.playerUUID = UUID;
	}
	
	/**
	 * allows to get all the available contexts
	 * @return an array of all available contexts
	 */
	public Context[] getContextList(){
		return this.contextList;
	}
		
	/**
	 * allows to get the board
	 * @return the ClientBoard instance
	 */
	public ClientBoard getBoard(){
		return this.boardReference;
	}
	
	/**
	 * allows to get the player UUID
	 * @return the UUID of the player handled by the ClientCLI thread
	 */
	public String getPlayerUUID(){
		return this.playerUUID;
	}
	
	/**
	 * allows to get the game UUID
	 * @return the UUID of the game to which the player has been connected
	 */
	public String getGameUUID(){
		return this.gameUUID;
	}
	
	/**
	 * tells if the player must wait is turn
	 * @return true if isn't the turn of this player, false otherwise
	 */
	public boolean isWaiting(){
		return this.wait;
	}	
	
	/**
	 * allows to retrive the queue used to send message to the server 
	 * @return the sendQueue of this class
	 */
	public ConcurrentLinkedQueue<String> getSendQueue(){
		return this.sendQueue;
	}
	
	/**
	 * set false the leaderStartPhase flag
	 */
	public void leaderStartPhaseEnd(){
		this.leaderStartPhase=false;
	}
	
	/**
	 * open a context using the information specified into the JsonObject message passed as argument
	 * @param contextMessage the received CONTEXT message used to customize the context 
	 */
	public void openContext(JsonObject contextMessage){
		this.contextQueue.add(contextMessage);
	}
	
	/**
	 * allows to retrive the list of players connected 
	 * @return an HashMap which map every player name to its ClientPlayer instance
	 */
	public HashMap<String, ClientPlayer> getPlayerList(){
		return this.playerListReference;
	}
	
	/**
	 * register the send message queue
	 * @param the queue which must be registered
	 */
	public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue) {
		this.sendQueue = queue;		
	}
	
	/**
	 * display the given message
	 * @param message the message to be displayed
	 */
	public void displayMessage(String message){
		this.messageQueue.add("[server message]-----------------------------\n"+message+""
									   + "\n---------------------------------------------");
	}
	
	/**
	 * change the value of the idleRunFlag, to stop or reactivate the ZeroLevelContextThread
	 * @param idleRunFlag the value of the idleRun flag
	 */
	public void setIdleRun(boolean idleRunFlag){
		this.idleRun = idleRunFlag;
	}
	
	/**
	 * print in a nice way the chat message which has been just sent
	 * @param playerID the name of the player who has sent the message
	 * @param message the sent message
	 */
	public void displaySendMessage(String playerID, String message){
		out.println("| ===========================================\n"
				   +"|               YOU SENT A MESSAGE !!!       \n"
				   +"| ===========================================\n"
				   +"| > "+ playerID+ ":\n"
				   +"| "+ message +"\n|\n"
				   +"| ===========================================\n");
	}
	
	/**
	 * print in a nice way the chat message which has been just received
	 * @param playerID the name of the player who has sent the message
	 * @param message the sent message
	 */
	public void receiveMessage(String playerID, String message) {
		out.println("| ===========================================\n"
				   +"|           YOU RECEIVED A MESSAGE !!!       \n"
				   +"| ===========================================\n"
				   +"| > "+ playerID+ ":\n"
				   +"| "+ message +"\n|\n"
				   +"| ===========================================\n");	
	}
	
	public boolean getLeaderStartPhase(){
		return this.leaderStartPhase;
	}
	
	/**
	 * change the value to the wait boolean flag
	 * @param flag the value of the wait flag
	 */
	@Override
	public void waitTurn(boolean flag) {
		this.wait = flag;
	}

	/**
	 * stop this thread
	 */
    @Override
    public void kill() {
        this.stop = true;
    }
}
