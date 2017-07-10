package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Client.Game.ClientActionSpace;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientCardRegistry;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class GameUtils {

	public GameUtils(){}
	

	private static void updateResource(ClientPlayer player, GameScreen game, String key, int index){
		TextField resourceSet = game.getBoard().getPersonalPane().getResourceSet().get(index);
		Integer value = player.getPlayerResources().getResource(key);
		resourceSet.setText(value.toString());
	}
	
	
	private static void setStyleFamiliar(String path, String color, BoardButton button){
		button.setStyle("-fx-graphic: url('"+path+"');" +
					  "-fx-background-size: auto;" +
					  "-fx-background-color: transparent;" +
					  "-fx-translate-x: -30;"+
					  "-fx-translate-y: -50;" +
					  "-fx-scale-x: 0.5;"+
					  "-fx-scale-y: 0.5;"+
					  "-fx-opacity: 1;" +
					  "-fx-effect: dropshadow(gaussian,"+color+", 10, 0.5, 0, 0);");
	}
	
	private static void updateButtonWithColor(BoardButton button, UserGUI value){
		String path;
		switch(value.getUserColor()){
		case("#FF0000"):
			path = "/images/pawns/red.png";
			setStyleFamiliar(path, "#FF0000", button);
			break;
		case("#0000FF"):
			path = "/images/pawns/blue.png";
			setStyleFamiliar(path, "#0000FF", button);
			break;
		case("#008000"):
			path = "/images/pawns/green.png";
			setStyleFamiliar(path, "#008000", button);
			break;
		case("#FFFF00"):
			path = "/images/pawns/yellow.png";
			setStyleFamiliar(path, "#FFFF00", button);
			break;
		default:
			path = "/images/pawns/red.png";
			setStyleFamiliar(path, "#FF0000", button);
		}
	}
	
	private static void updateOccupiedSpace(ClientActionSpace space, BoardButton button, GameScreen game){
		game.getUsers().forEach((key, value) ->{
			System.out.println(value.getClientPlayer().getName());
			for(int i=0; i<space.getOccupants().size(); i++){
				System.out.println(space.getOccupants().get(i));
				if(value.getClientPlayer().getName().equals(space.getOccupants().get(i).getOwner())){
					updateButtonWithColor(button, value);
				}		
			}		
		});
	}
	
	public static void updateCardButton(String cardName, BoardButton button){
		
		if(!"EMPTY".equals(cardName)){
			String path = "/images/cards/" + ClientCardRegistry.getInstance()
																.getDetails(cardName)
																.get("path").asString();		
			
			button.setStyle("-fx-background-image: url('"+path+"');" +
					  "-fx-background-size: contain;" +
					  "-fx-background-color: transparent;" +
					  "-fx-opacity: 1;" +
					  "-fx-effect: dropshadow(gaussian, white, 10, 0.5, 0, 0);");
			}
		else
			button.setStyle("-fx-background-color: white;" +
					  "-fx-opacity: 0.1;" +
					  "-fx-effect: dropshadow(gaussian, white, 10, 0.5, 0, 0);");
	}
	
	private static void updatePersonalCard(ClientPlayer player, GameScreen game){
		for(String key: player.getCards().keySet()){
			if(!"LEADER".equals(key)){
				ArrayList <String> cardNames = player.getCards().get(key);
				ArrayList <BoardButton> buttonSet = game.getBoard().getPersonalPane().getCards().get(key);
				for(int i=0; i<cardNames.size(); i++){
					if(ClientCardRegistry.getInstance().getDetails(cardNames.get(i))!= null){
						updateCardButton(cardNames.get(i), buttonSet.get(i));
					}
				}
			}	
		}
	}
	
	public static void updatePersonalBoard(GameScreen game){
		
		ClientPlayer player = game.getClient().getPlayerList().get(game.getClient().getPlayerUUID());
		updateResource(player, game, "COINS", 0);
		updateResource(player, game, "WOOD", 1);
		updateResource(player, game, "STONE", 2);
		updateResource(player, game, "SERVANTS", 3);
		//updateResource(player, game, "FAITH_POINTS", 1);
		//updateResource(player, game, "MILITARY_POINTS", 2);
		//updateResource(player, game, "VICTORY_POINTS", 3);
		updatePersonalCard(player, game);
	}
	
	public static void updateTowerCard(GameScreen game){
		game.getBoard().getTowerPane().getTowerButtons().forEach(button ->{
			
			if(game.getClient().getBoard().getRegionList()
					.get(button.getRegionID())
					.getActionSpaceList()
					.get(button.getSpaceId()) != null){
				ClientActionSpace space = game.getClient().getBoard().getRegionList()
						.get(button.getRegionID())
						.getActionSpaceList()
										.get(button.getSpaceId());
				if(space.isBusy())
					updateOccupiedSpace(space, button, game);	
				BoardButton card = game.getBoard().getTowerPane().getCardButtons().get(game.getBoard().getTowerPane().getTowerButtons().indexOf(button));
				updateCardButton(space.getCardName(), card);
			
			}
		});
	}
	
	public static void updateBoard(GameScreen game){
		game.getBoard().getBoardPane().getBoardButtons().forEach(button ->{
			if(game.getClient().getBoard().getRegionList()
					.get(button.getRegionID())
					.getActionSpaceList()
					.get(button.getSpaceId()) != null){
				ClientActionSpace space = game.getClient().getBoard().getRegionList()
						.get(button.getRegionID())
						.getActionSpaceList()
										.get(button.getSpaceId());
				if(space.isBusy()){
					updateOccupiedSpace(space, button, game);	
				}
			}
		});		
	}
	
	public static void createFamily(GameScreen game, ClientPlayer player, int index, Color color){
		FamilyMemberGUI family = new FamilyMemberGUI(40, 120, color, 0, 40, 0, 100*(index+1)); 
		family.registerFamilyMember(player.getFamilyMembers()[index]);
		family.setIndex(index);
		family.setInfo();
		game.getBoard().getPersonalPane().getFamily().add(index, family);
	}
	
	public static void initFamily(GameScreen game){
		ClientPlayer player = game.getClient().getPlayerList().get(game.getClient().getPlayerUUID());
		createFamily(game, player, 0, Color.PERU);
		for(int i=1; i<player.getFamilyMembers().length; i++){
			createFamily(game, player, i, Color.RED);
		}
	}
	
	public static void updateExcomunicationCard(GameScreen game){
		ClientBoard board = game.getClient().getBoard();
		for(int i=0; i < board.getExcommunicationCards().size(); i++){
			String cardName = board.getExcommunicationCards().get(i);
			updateCardButton(cardName, game.getBoard().getBoardPane().getEcomButtons().get(i));
		}
	}
	
	public static void updateLeaderCard(GameScreen game){
		ClientPlayer player = game.getClient().getPlayerList().get(game.getClient().getPlayerUUID());
		ArrayList<LeaderCardGUI> leaderBoxName =  game.getBoard().getPersonalPane().getLeaderCards();
		if(player.getCards().get("LEADER") == null){
			return;
		}
		ArrayList<String> leaderList = player.getCards().get("LEADER");
		int index = 0;
		for(; index<leaderList.size(); index++){
				leaderBoxName.get(index).set(leaderList.get(index));
			}	
		if(leaderBoxName.size()> leaderList.size()){
			for(;index<leaderBoxName.size(); index++){
				leaderBoxName.get(index).setEmpty();
			}
		}
	}
	
	public static void initUserList(GameScreen game){
		
		int index = 1;
		for(String key: game.getClient().getPlayerList().keySet()){
			if(key.equals(game.getClient().getPlayerUUID())){
				game.getUsers().put(0, new UserGUI(game.getClient().getPlayerList().get(game.getClient().getPlayerUUID())));
				continue;
			}
			game.getUsers().put(index, new UserGUI(game.getClient().getPlayerList().get(key), index));
			index ++;
		}
	}
	
	public static void update(GameScreen game){

		updateTowerCard(game);
		updateBoard(game);
		updatePersonalBoard(game);
		updateLeaderCard(game);
		updateExcomunicationCard(game);
	}
}
