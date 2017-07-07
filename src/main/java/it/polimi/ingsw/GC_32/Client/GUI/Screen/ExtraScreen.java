package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ExtraScreen extends BorderPane {

	private BorderPane top;
	private BorderPane bot;
	
	//Chat
	private ArrayList<String> messageHistory;
	private ScrollPane chat;
	private HBox sendBar;
	
	//Profile
	private Label name;
	private GridPane profile;
	
	//Network
	private GameScreen game;
	
	public ExtraScreen(GameScreen game){
		super();
		this.game = game;
		
		this.top = new BorderPane();
		this.bot = new BorderPane();
		setupChat();
		setupProfile();
		
		this.messageHistory = new ArrayList<String>();
		this.setId("extra");
		this.getStylesheets().add(this.getClass().getResource("/css/extra.css").toExternalForm());
				
		this.setTop(top);
		this.setBottom(bot);
		
		this.top.setId("idle");
		this.bot.setId("idle");
	}

	public ExtraScreen getExtra(){
		return this;
	}

	public void setupProfile(){
		
		this.profile = new GridPane();
		this.name = new Label(this.game.getClient().getPlayerList().get(this.game.getClient().getPlayerUUID()).getName());
		
		TextField changeName = new TextField();
		Button confirm = new Button();
		HBox box = new HBox();
		
		confirm.setOnAction(click -> this.name.setText(changeName.getText()));
		box.getChildren().addAll(changeName, confirm);
		
		profile.add(name, 0, 0);
		profile.add(box, 0, 1);
		
	}

	public void setChat(){
		this.bot.setCenter(chat);
		this.bot.setBottom(sendBar);
	}

	public void setProfile(){
		this.top.setCenter(profile);
//		this.top.setTop(new ImageView(new Image(getClass().getResourceAsStream
	//			("/images/other/lorenzo_01.png"))));
		this.top.setPrefSize(200, 300);
	}
	
	public void setupChat(){
		this.chat = new ScrollPane();
		
		this.sendBar = new HBox();
		VBox scrollScreen= new VBox();
		scrollScreen.setId("chat");

		this.chat.setContent(scrollScreen);
		
		TextField text = new TextField();
		text.setId("chat_message");
		//text.getStylesheets().add(this.getClass().getResource("/css/game_buttons.css").toExternalForm());	
		Button button = new Button("send");
		button.getStylesheets().add(this.getClass().getResource("/css/game_buttons.css").toExternalForm());	
		button.setId("chat_button_idle");
		sendBar.getChildren().addAll(button, text);
		button.setOnMousePressed(click ->{
			
			button.setId("chat_button_pressed");
			((VBox) this.chat.getContent()).getChildren().add(new Text(text.getText()));
			text.clear();
		});
		button.setOnMouseReleased(click ->{
			
			button.setId("chat_button_idle");
		});
	}
}
