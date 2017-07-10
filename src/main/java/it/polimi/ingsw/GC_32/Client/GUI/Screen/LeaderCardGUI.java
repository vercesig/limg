package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Game.ClientCardRegistry;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class LeaderCardGUI extends Box {
	private String name;
	
	public LeaderCardGUI(int width, int height, int depth, int x, int y, int z, int factor){ //40 100 10
		super(width, height, depth);
		this.setTranslateX(x + factor);
		this.setTranslateY(y);
		this.setTranslateZ(z);
		this.setUknownMesh();
		this.setEmpty();
	}
	
	public void set(String cardName){
		//leader.setOnMouseDragEntered(click -> click);
		JsonObject path = ClientCardRegistry.getInstance().getDetails(cardName);
		this.setCardMesh(path.get("path").asString());
		this.setName(cardName);
	}

	public void setCardMesh(String path){
		PhongMaterial mesh = new PhongMaterial();
		mesh.setDiffuseMap(new Image("/images/cards/"+path));
		this.setMaterial(mesh);
	}

	public void setUknownMesh(){
		PhongMaterial mesh = new PhongMaterial();
		mesh.setDiffuseMap(new Image("/images/cards/leaders_b_c_00.jpg"));
		this.setMaterial(mesh);
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setEmpty(){
		this.setUknownMesh();
		this.setName("Empty");
	}
	
	public void setName(String name){
		this.name = name;
	}
}