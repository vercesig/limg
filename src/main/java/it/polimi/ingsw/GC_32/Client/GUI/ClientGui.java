/*package it.polimi.ingsw.GC_32.Client.GUI;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.CLI.ActionEffectContext;
import it.polimi.ingsw.GC_32.Client.CLI.ChangeEffectContext;
import it.polimi.ingsw.GC_32.Client.CLI.Context;
import it.polimi.ingsw.GC_32.Client.CLI.ExcommunicationContext;
import it.polimi.ingsw.GC_32.Client.CLI.LeaderSetContext;
import it.polimi.ingsw.GC_32.Client.CLI.PrivilegeContext;
import it.polimi.ingsw.GC_32.Client.CLI.ServantContext;
import it.polimi.ingsw.GC_32.Client.CLI.ZeroLevelContext;
import it.polimi.ingsw.GC_32.Client.GUI.Screen.GameScreen;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import it.polimi.ingsw.GC_32.Common.Utils.KillableRunnable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientGui extends Application  implements ClientInterface, KillableRunnable  {

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
		
		//Javafx
		private RunGui run;
		
		private boolean idleRun = false; // if zeroLevel must run
		private boolean wait = true; // if player is waiting he can't display action menu;
		
		private boolean leaderStartPhase = true;
		
		private boolean stop = false;
	
		public ClientGui(){		
			contextQueue = new ConcurrentLinkedQueue<Object>();
			messageQueue = new ConcurrentLinkedQueue<String>();
			
			// classi scene 
			this.contextList = new Context[7];
		//	contextList[0] = new ZeroLevelContext(this);		
			
			clientsendQueue = new ConcurrentLinkedQueue<String>();
			//Java fx	
			this.run = new RunGui(this);
		
		}
		
		
		public void run(){	
			
			while(!stop){
							
				while(!contextQueue.isEmpty()){
					contextList[0].close();
					JsonObject contextMessage = (JsonObject) contextQueue.poll();
					clientsendQueue.add(contextList[contextMessage.get("CONTEXTID").asInt()].open(contextMessage.get("PAYLOAD")));
					
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
			
		//getters
		
		public ClientBoard getBoard(){
			return this.boardReference;
		}
		
		public String getPlayerUUID(){
			return this.playerUUID;
		}
		
		public String getGameUUID(){
			return this.gameUUID;
		}
		
		
		public HashMap<String, ClientPlayer> getPlayerList(){
			return this.playerListReference;
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
		
		public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue) {
			this.sendQueue = queue;		
		}
		
		public void displaySendMessage(String playerID, String message){
		}
		
		public void receiveMessage(String playerID, String message) {
		
		}		
		
		@Override
		public void waitTurn(boolean flag) {
			this.wait = flag;
		}

	    @Override
	    public void kill() {
	        this.stop = true;
    }
		@Override
		public void start(Stage primaryStage) throws Exception {
			// TODO Auto-generated method stub
			this.run.start(primaryStage);
		}
}*/
