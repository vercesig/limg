package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import javafx.scene.control.Button;
import javafx.scene.shape.Circle;

public class BoardButton extends Button {

	private int regionId;
	private	int spaceId;

	public BoardButton(int regionId, int spaceId, String layout){
		super();
		this.regionId = regionId;
		this.spaceId = spaceId;
		this.setId(layout);
		this.getStylesheets().add(this.getClass().getResource("/css/game_buttons.css").toExternalForm());
		
	}
	
	public static BoardButton createSquareButton(int x, int y, String layout){
		BoardButton button = new BoardButton(69, 69, layout); //numeri inifluenti
		
		button.setLayoutX(x);
		button.setLayoutY(y);
		return button;
	}
	
	public static BoardButton createCircleButton(int regionId, int spaceId, int x, int y, int dimension, String layout){
		BoardButton button = new BoardButton(regionId, spaceId, layout);
		
		button.setShape(new Circle(dimension));
		button.setLayoutX(x);
		button.setLayoutY(y);
		return button;	
	}
	
	public static BoardButton createSquareButton(int regionId, int spaceId, int x, int y, String layout){
		BoardButton button= new BoardButton(regionId, spaceId, layout);
		
		button.setLayoutX(x);
		button.setLayoutY(y);
		return button;
	}
	
	public int getRegionID(){
		return this.regionId;
	}
	
	public int getSpaceId(){
		return this.spaceId;
	}
}
