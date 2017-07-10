package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ExtraScreen extends BorderPane {

	private BorderPane top;
	private BorderPane bot;
	
	//Player Ranking
	private GridPane playerRank;
	
	//Chat
	private GridPane chat;
	
	//Profile
	private GridPane profile;
	
	//Network
	private GameScreen game;
	
	public ExtraScreen(GameScreen game){
		super();
		this.game = game;
		
		this.top = new BorderPane();
		this.bot = new BorderPane();
		
		this.setId("extra");
		this.getStylesheets().add(this.getClass().getResource("/css/extra.css").toExternalForm());
				
		this.setTop(top);
		this.setBottom(bot);
		
		this.top.setId("idle");
		this.bot.setId("idle");
	
		setupPersonalProfile();
		setupPlayerRank();
		setupChat();
	}

	public ExtraScreen getExtra(){
		return this;
	}
	
	//PersonalProfie
	public void setupPersonalProfile(){
		this.profile = new GridPane();
		this.profile.setId("grid");
		this.getStylesheets().add(this.getClass().getResource("/css/personal_profile.css").toExternalForm());

		
		UserGUI player = this.game.getUsers().get(0);
		String name;
		if(this.game.getClient().getPlayerList()!=null){
			name = this.game.getClient().getPlayerList().get(this.game.getClient().getPlayerUUID()).getName();
		}
		else
			name = "Player";
		Label label = new Label("Player Profile");
		label.setId("label_Title");
		Label labelName = new Label("Name :");
		labelName.setId("label");
		TextField playerName = new TextField(name);
		playerName.setId("text");
		
		HBox nameBox = new HBox();
		nameBox.getChildren().addAll(labelName, playerName);
		
		ImageView gallery = new ImageView();
		gallery.setId("gallery");
		gallery.setImage(player.getUserImage());
	
		Button left = new Button();
		left.setId("prev_idle");
		left.setOnMouseClicked(click -> gallery.setImage(UserGUI.getRandom()));
		Button right = new Button();
		right.setId("next_idle");
		right.setOnMouseClicked(click -> gallery.setImage(UserGUI.getRandom()));
		HBox buttonSlide = new HBox();
	
		buttonSlide.getChildren().addAll(left, right);
		
		
		Label imageProfile = new Label("Profile Picture:");
		imageProfile.setId("label");
		Label color = new Label();
			
		VBox galleryElement = new VBox();
		VBox colorChoserElement = new VBox();
		HBox galleryColorBox = new HBox();
		galleryElement.getChildren().addAll(gallery, buttonSlide);
		colorChoserElement.getChildren().addAll(imageProfile, color);
		galleryColorBox.getChildren().addAll(galleryElement, colorChoserElement);
		Button button = new Button("rank");
		button.setOnMouseClicked(click -> setRank());
		HBox ender = new HBox();
		ender.getChildren().add(button);
		ender.setId("ender");
		this.profile.add(label, 0, 0);
		this.profile.add(nameBox, 0, 1);
		this.profile.add(galleryColorBox, 0, 2);
		this.profile.add(ender, 0, 3);
	}
	
	//Player Rank
	public void setupPlayerRank(){
		this.playerRank = new GridPane();
		playerRank.setId("idle");
		Label title = new Label("Players ranking");
		title.setId("label_Title");
		Label playerName = new Label("Name:");
		playerName.setId("labelRank");
	
		Label victoryPoints = new Label("Victory Points:");
		victoryPoints.setId("labelRank");
		HBox firstLine = new HBox();
		firstLine.setId("label");
		firstLine.getChildren().addAll(playerName, victoryPoints);
		
		ArrayList<HBox> lineSet = new ArrayList<HBox>();
		this.game.getUsers().forEach((key,value) -> {
			lineSet.add(playerLine(value));
		});
		
		this.playerRank.add(title, 0, 0);
		this.playerRank.add(firstLine, 0, 1);
		
		for(int i=0; i<lineSet.size(); i++){
			this.playerRank.add(lineSet.get(i), 0, i+2);
		}
	}
	
	public HBox playerLine(UserGUI p){
		Text name = new Text(p.getClientPlayer().getName());
		name.setId("label");
		Label color = new Label();
		color.setId("label_color");
		setColor(color, p.getUserColor());
		Text points = new Text(String.valueOf(p.getClientPlayer().getPlayerResources().getResource("VICTORY_POINTS")));
		points.setId("text");
		HBox line = new HBox();
		line.getChildren().addAll(name, color, points);
		return line;
	}

	public void setChat(){
		this.bot.setBottom(chat);
	}

	public void setRank(){
		this.top.setCenter(playerRank);
	}
	
	public void resetChat(){
		this.bot.getChildren().remove(0);
	}
	
	public void setProfile(){
		this.top.setCenter(profile);
	}
	
	 // Chat
	public void setupChat(){
		
		this.chat = new GridPane();
		chat.setId("idle");
		Label title = new Label("Chat Room");
		title.setId("label_Title");
		
		ScrollPane panel = new ScrollPane();
		panel.setId("chat");
		VBox scrollScreen= new VBox();
		scrollScreen.setId("idle");
		panel.setContent(scrollScreen);
		
		Button button = new Button("send");
		button.getStylesheets().add(this.getClass().getResource("/css/game_buttons.css").toExternalForm());	
		button.setId("chat_button_idle");
		TextField text = new TextField();
		text.setId("chat_message");
		HBox sendBar = new HBox();
		sendBar.getChildren().addAll(button, text);
		
		//text.getStylesheets().add(this.getClass().getResource("/css/game_buttons.css").toExternalForm());	
		button.setOnMousePressed(click ->{
			button.setId("chat_button_pressed");
		   ((VBox)panel.getContent()).getChildren().add(new Text(text.getText()));
			text.clear();
		});
		button.setOnMouseReleased(click ->{
			
			button.setId("chat_button_idle");
		});
		this.chat.add(title, 0, 0);
		this.chat.add(panel, 0, 1);
		this.chat.add(sendBar, 0, 2);
	}
	
	public void setColor(Label label, String color){
		label.setStyle("-fx-background-color: "+color +";");
	}

}
