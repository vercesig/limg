package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.GC_32.Client.GUI.ClientGUI;
import it.polimi.ingsw.GC_32.Client.Game.ClientActionSpace;
import it.polimi.ingsw.GC_32.Client.Game.ClientCardRegistry;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;

public class CentralScreen{
	
//	private HashMap<String, Group> boardButtons;
	private BorderPane master;
	
	private TowerPane tower;
	private BoardPane board;
	private ResourcePane resource;
	
	//network
	private GameScreen game;
	
	public CentralScreen(GameScreen game){
		
		this.game = game;
		
		this.master= new BorderPane();
		this.tower = new TowerPane(this.master);
		this.board = new BoardPane(this.master);
		this.resource = new ResourcePane(this.master);
		
	}
	
	public void changeToTower(){
		this.board.show(false);
		this.resource.show(false);

	//	
		this.tower.show(true);
	}
	
	public void changeToBoard(){	
		this.tower.show(false);
		this.resource.show(false);

		this.board.show(true);
	}
	
	public void changeToPersonal(){
		this.board.show(false);
		this.tower.show(false);
		
		this.resource.show(true);
	}
	
	public BorderPane getMaster(){
		return this.master;
	}


	public TowerPane getTowerPane(){
		return this.tower;
	}
	
	public BoardPane getBoardPane(){
		return this.board;
	}

	public ResourcePane getPersonalPane(){
		return this.resource;
	}
}