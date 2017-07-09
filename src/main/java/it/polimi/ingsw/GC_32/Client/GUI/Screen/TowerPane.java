package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class TowerPane {

	private ArrayList <BoardButton> towerButtons;
	private ArrayList <BoardButton> cardButtons;
	private BorderPane master;
	private GridPane grid;
	private Group root;
	
	public TowerPane(BorderPane master){
		this.master = master;
		this.grid = new GridPane();
		this.root = new Group();
		this.root.setManaged(false);
		this.master.setTop(root);
							//RegionID SpaceID
		this.towerButtons = new ArrayList <BoardButton>();
		this.cardButtons = new  ArrayList <BoardButton>();
		
		this.master.setId("TowerRegion");
		this.master.getStylesheets().add(this.getClass().getResource("/css/tower.css").toExternalForm());
		
		int factor = 140;
		this.loadButtons("territoryButton", "territoryCard", 4, 0);
		this.loadButtons("characterButton", "characterCard", 5, factor);
		this.loadButtons("buildingButton", "buildingCard", 6, factor*2);
		this.loadButtons("ventureButton", "ventureCard", 7, factor*3);
		this.set();
		
		this.show(false);
	}
	
	public void loadButtons(String button, String card, int regionId, int factor){	
		
		for(int i=0; i<4; i++){
			this.towerButtons.add(
					BoardButton.createCircleButton(regionId, 3-i, 110+factor, 90 + i*130, 15, button));
			
			this.cardButtons.add( 
					BoardButton.createSquareButton(regionId, 3-i, 25+factor, 45+ i*(130), card));
		}
	}

	public void set(){
		for(int j=4; j<8; j++){ 		// REGION ID
			for(int i=0; i<4; i++){		// SPACE ID
				setColumn(j, i);
			}
		}
		this.show(false);
	}
	
	public void show(boolean flag){
		this.towerButtons.forEach((item) -> 
					item.setVisible(flag));
		this.cardButtons.forEach((item) -> 
					item.setVisible(flag));
		this.root.setVisible(flag);
		
		if(flag){
			this.master.setTop(root);
			this.master.setId("TowerRegion");
		}
	}
	
	public void setColumn(int regionId, int spaceId){
		//	HBox set = new HBox();
			BoardButton button = null;
			BoardButton card = null;
			
			for(BoardButton b: this.towerButtons){
				if(b.getSpaceId() == spaceId && b.getRegionID() == regionId){
					button = b;
				}
			}
			
			for(BoardButton c : this.cardButtons){
				if(c.getSpaceId() == spaceId && c.getRegionID() == regionId){
					card = c;
				}
			}
			if(button!=null && card != null){
			//	set.getChildren().addAll(card, button);
				this.root.getChildren().addAll(button, card);
			}
	}

	public ArrayList  <BoardButton> getTowerButtons(){
		return this.towerButtons;
	}
	public ArrayList <BoardButton> getCardButtons(){
		return this.cardButtons;
	}
}
