package it.polimi.ingsw.GC_32.Client.Gui.GameView;

import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class BoardScreen extends ScrollPane{
	
	public BoardScreen(){
		super();
		//Size
		this.setPrefSize(679, 525.0);	
		this.setMaxSize(679, 525.0);
		this.setMinSize(679, 525.0);
		
		
		AnchorPane anchor = new AnchorPane();
		anchor.setPrefSize(679, 936);
		anchor.setId("board");
		anchor.getStylesheets().add(this.getClass().getResource("/css/board.css").toExternalForm());
		
		new ActionSpaceButton(0, 1, "TowerGreen")		
		
		this.setContent(anchor);
	}
	
}
