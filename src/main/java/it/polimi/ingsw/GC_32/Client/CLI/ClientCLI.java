package it.polimi.ingsw.GC_32.Client.CLI;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.ClientInterface;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

public class ClientCLI implements ClientInterface{

	// game management
	private ClientBoard boardReference;
	private HashMap<String, ClientPlayer> playerListReference;
	private String UUID;
	private String gameUUID;
	
	// network management
	private ConcurrentLinkedQueue<Object> contextQueue;
	private ConcurrentLinkedQueue<String> sendQueue;
	private ConcurrentLinkedQueue<String> clientsendQueue;
	
	// context management
	private Context[] contextList;
	private Thread zeroLevelContextThread;
	
	private boolean idleRun = false;
	private boolean wait = true; // if player is waiting he can't display action menu;
	private boolean actionRunningGameFlag;
	
	public ClientCLI(){		
		contextQueue = new ConcurrentLinkedQueue<Object>();
		
		this.contextList = new Context[6];
		contextList[0] = new ZeroLevelContext(this);
		
		contextList[1] = new PrivilegeContext();
		contextList[2] = new ServantContext();
		contextList[3] = new ExcommunicationContext();
		contextList[4] = new ChangeEffectContext();
		//contextList[5] = new ActionEffectContext(this);
		contextList[5] = new LeaderSetContext(this);
		
		clientsendQueue = new ConcurrentLinkedQueue<String>();
		
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
	
	public void registerUUID(String UUID){
		this.UUID = UUID;
	}
	
	public void registerActionRunningGameFlag(boolean flag){
		this.actionRunningGameFlag = flag;
	}
	
	public ClientBoard getBoard(){
		return this.boardReference;
	}
	
	public String getUUID(){
		return this.UUID;
	}
	
	public String getGameUUID(){
		return this.gameUUID;
	}
	
	public boolean isWaiting(){
		return this.wait;
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
	
	public void displayMessage(String message){
		System.out.println(message);
	}
	
	public ConcurrentLinkedQueue<String> getSendQueue(){
		return this.sendQueue;
	}
	
	public void run(){	
		
		// inizialize zeroLevelContext
		contextList[0].registerSendQueue(sendQueue);
		contextList[0].registerActionRunningGameFlag(actionRunningGameFlag);
		
		//inizialize excomunnicationContext
		contextList[3].registerGameUUID(gameUUID);
		contextList[3].registerPlayerUUID(UUID);
		
		//inizialize changeEffectContext
		contextList[4].registerClientPlayer(playerListReference.get(UUID));
		
		//inizialize leaderSetContext
		contextList[5].registerGameUUID(gameUUID);
		contextList[5].registerPlayerUUID(UUID);
		
		//inizialize privilegeContext
		contextList[1].registerClientPlayer(playerListReference.get(UUID));
		
		
		while(true){
			
			while(!contextQueue.isEmpty()){
				contextList[0].close();
				JsonObject contextMessage = (JsonObject) contextQueue.poll();
				contextList[contextMessage.get("CONTEXTID").asInt()].registerSendQueue(sendQueue);
				contextList[contextMessage.get("CONTEXTID").asInt()].open(contextMessage.get("PAYLOAD"));
				
				try{ //waiting for other context
					Thread.sleep(200);
				}catch(InterruptedException e){}
				
				if(contextQueue.isEmpty())
					idleRun=false;
			
			}
			
			if(!idleRun){
				idleRun=true;
				zeroLevelContextThread = new Thread((Runnable) contextList[0]);
				zeroLevelContextThread.start();
			}
			
			// spedisco messaggi
			if(!clientsendQueue.isEmpty())
				clientsendQueue.forEach(message -> {
					System.out.println("Client CLI"+message);
					
					sendQueue.add(message);
				});
			clientsendQueue.clear();
			
		}
		
	}
	
	@Override
	public void openContext(JsonObject contextMessage){
		this.contextQueue.add(contextMessage);
	}
	
	@Override
	public int getContextId() {
		// TODO Auto-generated method stub
		return 0;
	}

	// change context
	@Override
	public void openContext(int screenId, String additionalData) {
		
	}	
	
	@Override
	public void registerContextPayloadQueue(ConcurrentLinkedQueue<Object> queue) {
		
	}

	@Override
	public void receiveMessage(String playerID, String message) {
		System.out.println("---------------------------------------");
		System.out.println("|      YOU RECEIVED A MESSAGE !!!      ");	
		System.out.println("|-------------------------------------------");
		System.out.println("| > "+ playerID+ ":");
		System.out.println("| "+ message +"\n|");
		System.out.println("| ===========================================");		
	}

	@Override
	public void setTowerCards(int regionID, int spaceID, String cardName) {
		this.boardReference.getRegionList().get(regionID).getActionSpaceList().get(spaceID).setCard(cardName);
		
	}

	@Override
	public void updateTurnOrder(String[] playerIDs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDiceValue(int blackDice, int whiteDice, int orangeDice) {
		this.getBoard().setDiceValue(blackDice, whiteDice, orangeDice);
		this.playerListReference.forEach((UUID,player)->{
			player.getFamilyMembers()[1].setActionValue(blackDice); 
			player.getFamilyMembers()[2].setActionValue(whiteDice);
			player.getFamilyMembers()[3].setActionValue(orangeDice);
		});
		
	}

	@Override
	public void enableSpace(int regionID, int spaceID) {
		this.boardReference.getRegionList().get(regionID).getActionSpaceList().get(spaceID).Unlock();
	}

	@Override
	public void disableSpace(int regionID, int spaceID) {
		this.boardReference.getRegionList().get(regionID).getActionSpaceList().get(spaceID).Lock();	
	}

	@Override
	public void moveFamiliar(int familiar, int regionID, int spaceID) {
		/* ClientPlayer player = this.playerListReference.get(UUID);
		 ClientFamilyMember familyMember =  player.getFamilyMembers()[familiar];
		 familyMember.setBusyFlag(true);
		 this.boardReference.getRegionList().get(regionID).getActionSpaceList().get(spaceID)
		 												  .addFamilyMember(familyMember);*/
		
	}

	@Override
	public void moveCardToPlayer(String playerID, int regionID, int spaceID) {
		/*ClientPlayer player = this.playerListReference.get(playerID);
		String cardName = this.boardReference.getRegionList().get(regionID)
											 .getActionSpaceList().get(spaceID)
											 .getCardName();
		if(cardName!=null){
			Reader json = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("cards.json"));
			try {
				JsonValue card = JsonImporter.importSingleCard(json, cardName);
				player.addCard(card.asObject().get("cardType").asString(), cardName);	
				this.setTowerCards(regionID, spaceID, cardName);
			} 
			catch (IOException e) {}
			this.boardReference.getRegionList().get(regionID).getActionSpaceList()
															 .get(spaceID)
															 .setCard("empty");
		}*/
	}

	@Override
	public void setTrackValue(String playerID, int trackID) {
		ClientPlayer player = this.playerListReference.get(playerID);
		player.getTrack()[trackID].addScore(player.getPlayerResources());
	}

	@Override
	public void setCurrentPlayer(String playerID) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void unlockZone(int playerNumber) {
		if(playerNumber <3){
			disableSpace(0,1);//production
			disableSpace(1,1);//harvest
			disableSpace(3,2);//market
			disableSpace(3,3);//market
		}
		if(playerNumber <4){
			disableSpace(3,2);//market
			disableSpace(3,3);//market
		}
	}
	
	@Override
	public void waitTurn(boolean flag) {
		this.wait = flag;
	}
}
