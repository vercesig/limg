package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;
import java.util.Random;

import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import javafx.scene.image.Image;

public class UserGUI {

	private static String path = "images/avatars/";
	private Image image;
	private ClientPlayer player;
	private String color;

	//default
	public UserGUI(ClientPlayer player){
		this.player = player;
		this.color = "#FF0000";
		this.image = new Image(path + "stonemason.png");
	}
	
	//opponent user
	public UserGUI(ClientPlayer player, int index){
		this.player = player;
		switch(index){
		default: 
			this.color = "#FF0000"; //red
			break;
		case 1:
			this.color = "#0000FF"; //blue
			break;
		case 2:
			this.color = "#008000"; // green
			break;
		case 3:
			this.color = " #FFFF00"; // yellow
			break;
		}
		Random rand = new Random();
		int i = rand.nextInt(getAvatarList().size());
		this.image = new Image(path + getAvatarList().get(i));
	}
	
	public static ArrayList<String> getAvatarList(){
		ArrayList<String> path = new ArrayList<String>();
		path.add("abess.png");
		path.add("ambassador.png");
		path.add("captain.png");
		path.add("farmer.png");
		path.add("governor.png");
		path.add("paramour.png");
		path.add("stonemason.png");
		return path;
	}

	public static Image getRandom(){
		Random rand = new Random();
		int i = rand.nextInt(getAvatarList().size());
		return new Image(path + getAvatarList().get(i));
	}
	
	public void setUserGUI(String color, Image image){
		this.color = color;
		this.image = image;
	}
	
	public ClientPlayer getClientPlayer(){
		return this.player;
	}
	
	public Image getUserImage(){
		return this.image;
	}

	public String getUserColor(){
		return this.color;
	}
}
