package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ResourcePane {

	GridPane root;
	BorderPane master;
	
	Pane top, center, bot;
	ArrayList<TextField> resourceSet;
	HashMap<String, ArrayList<BoardButton>> cards;
	
	//family
	private ArrayList <FamilyMemberGUI> family;

	
	public ResourcePane(BorderPane master){
		this.master = master;
		this.root = new GridPane();
		
		this.master.setTop(root);
		this.top = new Pane();
		this.center = new Pane();
		this.bot = new Pane();
		
		this.resourceSet = new ArrayList<TextField> ();
		this.cards = new HashMap<String, ArrayList<BoardButton>>();
		this.family = new ArrayList<FamilyMemberGUI>();

		root.add(top, 0, 0);
		root.add(center, 0, 1);
		root.add(bot, 0, 2);
				
		this.master.getStylesheets().add(this.getClass().getResource("/css/resource.css").toExternalForm());
		set();
		loadSet(this.top, "BUILDINGCARD", 100); //production card
		loadSet(this.center, "TERRITORYCARD", 100); //territory card
		loadSet(this.top, "VENTURECARD", 100); // venture card
		loadSet(this.center, "CHARACTERCARD", 100); // character card
			
		setupResource(10, "Coins: ");
		setupResource(105, "Wood: ");
		setupResource(200, "Stone: ");
		setupResource(295, "Servants: ");
		setupButton("production_set", "venture_set", "BUILDINGCARD", "VENTURECARD", 20, 120, this.top);
		setupButton("territory_set", "character_set", "TERRITORYCARD", "CHARACTERCARD", 400, 120, this.center);
		
		setupFamilyMemberButton("Family Member", 100, 100);
		setupLeaderButton("Leader Cards", 100, 200);

	}

	public void set(){
		
		this.root.setId("container");
		this.top.setId("production_set");
		this.center.setId("territory_set");
		this.bot.setId("resource_set");
		
		this.master.setTop(root);
	}
	
	
	public void show(boolean flag){
		hide(this.top, flag);
		hide(this.center, flag);
		hide(this.bot, flag);
		
		if(flag){
			set();
		}
	}
	
	public void show(String cardType, boolean flag){
		this.cards.get(cardType).forEach(cardSet ->cardSet.setVisible(flag));
	}
	
	public void hide (Pane node, boolean flag){
		node.getChildren().forEach(item -> item.setVisible(flag));
		node.setVisible(flag);
	}
	
	public void loadSet(Pane node, String cardType, int factor){
		
		 ArrayList<BoardButton> cardSet = new  ArrayList<BoardButton>();
		for (int i=0; i<6; i++){
			BoardButton item = BoardButton.createSquareButton(8+factor*i, 12, "neutral"); // posson non memorizzare regionId e spaceID
			node.getChildren().add(item);
			cardSet.add(item);
		}
		this.cards.put(cardType, cardSet);
	}	
	
	public void setupResource(int factor, String resource){
		
		Label string = new Label(resource);
		string.setId("banner_idle");
		string.setOnMouseEntered(mouse -> string.setId("banner_blink"));
		string.setOnMouseExited(mouse -> string.setId("banner_idle"));
		
		TextField value = new TextField();
		value.setEditable(false);
		value.setId("value");
		
		VBox set = new VBox();
		set.setAlignment(Pos.CENTER);
		set.getChildren().addAll(string, value);
		set.setLayoutX(10 + factor);
		set.setLayoutY(0);
		this.resourceSet.add(value);
		this.bot.getChildren().add(set);
	}

	public void setupLeaderButton(String text, int x, int y){
		
		MenuButton button = new MenuButton(text);
		button.setLayoutX(x);
		button.setLayoutY(y);
		this.bot.getChildren().add(button);
	}
	
	public void setupFamilyMemberButton(String text, int x, int y){
		
		MenuButton button = MenuButton.FamilyContextButton(text, this.family);
		button.setLayoutX(x);
		button.setLayoutY(y);
		this.bot.getChildren().add(button);
	}
	
	public void setupButton(String before, String after, String cardTypeBefore, String cardTypeAfter, int x, int y, Pane pane){
		HBox set = new HBox();
		Label next = new Label("Next page");
		Label prev = new Label("Prev page");
		next.getStylesheets().add(this.getClass().getResource("/css/game_buttons.css").toExternalForm());
		prev.getStylesheets().add(this.getClass().getResource("/css/game_buttons.css").toExternalForm());

		next.setId("next_idle");
		prev.setId("prev_idle");
		
		next.setOnMousePressed(click->{
			pane.setId(after);
			show(cardTypeAfter, true);
			show(cardTypeBefore, false);
			next.setId("next_pressed");
		});
		
		next.setOnMouseReleased(click->{
			next.setId("next_idle");
		});
		
		prev.setOnMousePressed(click->{
			pane.setId(before);
			show(cardTypeAfter, false);
			show(cardTypeBefore, true);
			prev.setId("prev_pressed");
		});
		
		prev.setOnMouseReleased(click->{
			prev.setId("prev_idle");
		});
		
		set.getChildren().addAll(prev, next);
		set.setLayoutX(x);
		set.setLayoutY(y);
		this.bot.getChildren().add(set);
	}
	
	public ArrayList<TextField> getResourceSet(){
		return this.resourceSet;
	}
	
	public HashMap<String, ArrayList<BoardButton>>  getCards(){
		return this.cards;
	}
	
	public ArrayList <FamilyMemberGUI> getFamily(){
		return this.family;
	}
}
