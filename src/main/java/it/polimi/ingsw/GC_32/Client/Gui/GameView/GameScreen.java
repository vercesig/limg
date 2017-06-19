package it.polimi.ingsw.GC_32.Client.Gui.GameView;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameScreen {
	
	//BoardScreen board;
	VBox vbox;
	//SplitPane split;
	
	public GameScreen(){
		this.vbox = new VBox();
		vbox.setPrefSize(800.0, 600.0);
		MenuBar menuBar = new MenuBar();
		
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
		
		menuBar.getMenus().addAll(gameMenu, optionMenu, helpMenu);
		
		//SplitPane
		SplitPane split = new SplitPane();
		split.setPrefSize(800.0, 605.0);
		
		Pane pane1 = new Pane();
		pane1.setId("pane");
		pane1.getStylesheets().add(this.getClass().getResource("/css/board.css").toExternalForm());
		BoardScreen board = new BoardScreen();
		Pane pane2 = new Pane();
		split.getItems().addAll(pane1, board, pane2);
		
		vbox.getChildren().addAll(menuBar, split);
	}
	public VBox getVbox(){
		return this.vbox;
	}
}
