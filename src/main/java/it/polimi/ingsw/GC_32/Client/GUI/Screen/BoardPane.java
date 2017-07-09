package it.polimi.ingsw.GC_32.Client.GUI.Screen;


import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.layout.BorderPane;

public class BoardPane {
	
	private BorderPane master;
	private Group root;
	private ArrayList <BoardButton> boardButtons;
	private ArrayList <BoardButton> excomButtons;
	
	
	public BoardPane(BorderPane master){
		this.master = master;
		this.root = new Group();
		this.root.setManaged(false);

		this.boardButtons = new ArrayList <BoardButton> ();
		this.excomButtons = new ArrayList <BoardButton> ();
		this.master.setTop(root);
		
		this.master.setId("boardRegion"); 
		this.master.getStylesheets().add(this.getClass().getResource("/css/board.css").toExternalForm());
		loadButtons();
		set();
	}
	
	public void loadButtons(){
		for(int i=0; i<4; i++){
			loadButtons(i);
		}
		loadCard();
	}
	
	public void show(boolean flag){
		this.root.getChildren().forEach(node -> node.setVisible(flag));
		this.root.setVisible(flag);
		
		if(flag){
			this.master.setTop(root);
			this.master.setId("boardRegion"); 
		}
	}
	
	public void set(){
		this.boardButtons.forEach(item -> root.getChildren().add(item));
		this.excomButtons.forEach(item -> root.getChildren().add(item));
		this.master.setTop(root);
	}
	
	public void loadCard(){
		this.excomButtons.add(BoardButton.createSquareButton(120, 165, "excomunication_idle"));
		this.excomButtons.add(BoardButton.createSquareButton(175, 175, "excomunication_idle"));
		this.excomButtons.add(BoardButton.createSquareButton(230, 165, "excomunication_idle"));
	}
	
	public void loadButtons(int regionId){
		
		switch(regionId){
		case 0: //PRODUCTION
			this.boardButtons.add(BoardButton.createCircleButton(0, 0, 30, 380, 10, "production_idle"));
			this.boardButtons.add(BoardButton.createSquareButton(0, 1, 105, 370, "multi_production_idle"));
			break;
		case 1: //HARVEST
			this.boardButtons.add(BoardButton.createCircleButton(1, 0, 30, 460, 10, "harvest_idle"));
			this.boardButtons.add(BoardButton.createSquareButton(1, 1, 105, 460, "multi_harvest_idle"));
			break;
		case 2: //COUNCIL
			this.boardButtons.add(BoardButton.createSquareButton(2, 0, 320, 100,  "council_idle"));
			break;
		case 3:	//MARKET
			this.boardButtons.add(BoardButton.createCircleButton(3, 0, 375, 360, 15, "market_idle"));
			this.boardButtons.add(BoardButton.createCircleButton(3, 1, 440, 360, 15, "market_idle"));
			this.boardButtons.add(BoardButton.createCircleButton(3, 2, 505, 380, 15, "market_idle"));
			this.boardButtons.add(BoardButton.createCircleButton(3, 3, 550, 425, 15, "market_idle"));
			break;
		}
	}
	
	public BorderPane getBox(){
		return this.master;
	}
	
	public  ArrayList <BoardButton> getBoardButtons(){
		return this.boardButtons;
	}
}
