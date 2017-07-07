
package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Client.GUI.ClientGUI;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameScreen extends BorderPane{
	
	//NetWork
	private ClientGUI client;
	
	private VBox vbox;
	private AnchorPane master;
	private CentralScreen board;
	private MenuScreen menuScreen;
	private ExtraScreen extraScreen;
	
	//private ArrayList <LeaderCardGUI>
	
	public GameScreen(ClientGUI client)  {
		super();
		System.out.println("prima assegmemto su game di client: " + client.getBoard());
		this.client = client;
		this.setId("master");
		this.getStylesheets().add(this.getClass().getResource("/css/scene.css").toExternalForm());
		System.out.println("Dopo assegmemto su game di client: "+ client.getBoard());

		
		MainBar menuBar = new MainBar();
		this.board = new CentralScreen(this);
		this.extraScreen = new ExtraScreen(this);
		this.menuScreen = new MenuScreen(this);
		
		this.setTop(menuBar);
		this.setCenter(board.getMaster());
		this.setRight(extraScreen);
		this.setLeft(menuScreen);
		GameUtils.initFamily(this);
		GameUtils.update(this);
		
	}
	public VBox getVbox(){
		return this.vbox;
	}
	
	public AnchorPane getMaster(){
		return this.master; 
	}
	
	public GameScreen getGameScreen(){
		return this;
	}
	
	public CentralScreen getBoard(){
		return this.board;
	}

	public ExtraScreen getExtraScreen(){
		return this.extraScreen;
	}
	
	public ClientGUI getClient(){
		return this.client;
	}
}