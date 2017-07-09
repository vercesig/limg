package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class MainBar extends MenuBar{

	public MainBar (){
		super();
		
		//Game Menu
		 Menu gameMenu = new Menu("Game");
		MenuItem newItem = new MenuItem("New");
		MenuItem openItem = new MenuItem("Open..");
		MenuItem quitItem = new MenuItem("Quit");
		quitItem.setOnAction(actionEvent -> Platform.exit()); 

		gameMenu.getItems().addAll(newItem, openItem, new SeparatorMenuItem(), quitItem);
	
		//Option Menu
		 Menu optionMenu = new Menu("Option");
		MenuItem soundItem = new MenuItem("Sound");
		MenuItem graphicItem = new MenuItem("Graphics");
		MenuItem socketItem = new MenuItem("Socket");
		MenuItem RMIItem = new MenuItem("RMI");
		
		optionMenu.getItems().addAll(soundItem, graphicItem, new SeparatorMenuItem(), socketItem, RMIItem);
		
		// Help Menu
		 Menu helpMenu = new Menu("Help");
		MenuItem welcomeItem = new MenuItem("Welcome");
		MenuItem rulesItem = new MenuItem("Game Rules");
		MenuItem aboutItem = new MenuItem("About the Team");
		
		helpMenu.getItems().addAll(welcomeItem, rulesItem, new SeparatorMenuItem(), aboutItem);
		
		this.getMenus().addAll(gameMenu, optionMenu, helpMenu);
	}
}
