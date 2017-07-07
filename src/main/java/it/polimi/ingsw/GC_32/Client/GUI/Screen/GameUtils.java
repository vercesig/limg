package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Client.Game.ClientActionSpace;
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
	
	public static void updateCardButton(String cardName, BoardButton button){
		
		if(!cardName.equals("EMPTY")){
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
			ArrayList <String> cardNames = player.getCards().get(key);
			ArrayList <BoardButton> buttonSet = game.getBoard().getPersonalPane().getCards().get(key);
			for(int i=0; i<cardNames.size(); i++){
				if(ClientCardRegistry.getInstance().getDetails(cardNames.get(i))!= null){
					updateCardButton(cardNames.get(i), buttonSet.get(i));
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
					button.setId("button_busy");	
				BoardButton card = game.getBoard().getTowerPane().getCardButtons().get(game.getBoard().getTowerPane().getTowerButtons().indexOf(button));
				updateCardButton(space.getCardName(), card);
			
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
	
	public static void update(GameScreen game){
		System.out.println("STO CHIAMANDO UPDATE");
//		System.out.println(game.getClient().getBoard());
	//	System.out.println(game.getClient().getPlayerUUID());
		//System.out.println(game.getClient().getPlayerList().get(game.getClient().getPlayerUUID()));
		System.out.println(game.getClient().getSendQueue());


		updateTowerCard(game);
		updatePersonalBoard(game);
	}
}
