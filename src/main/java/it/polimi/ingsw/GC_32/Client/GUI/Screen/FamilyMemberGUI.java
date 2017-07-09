package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import it.polimi.ingsw.GC_32.Client.Game.ClientFamilyMember;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;

public class FamilyMemberGUI extends Cylinder{
	
	private int index;
	private ClientFamilyMember family;
	private GridPane info;
	
	public FamilyMemberGUI(int radius, int height, Color color, int x, int y, int z, int factor){
		super(radius, height);
		PhongMaterial colored = new PhongMaterial(color);
		this.setMaterial(colored);
		this.setTranslateX(x + factor);
		this.setTranslateY(y);
		this.setTranslateZ(z);
		this.setDrawMode(DrawMode.FILL);
	}
	
	public void registerFamilyMember(ClientFamilyMember family){
		this.family = family;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public void setInfo(){
		this.info = new GridPane();
	//	info.setId("family_info");
		addInfo("Index:", index, 0);
		addInfo("Action Value:", family.getActionValue(), 1);
	}
	
	public void setColor(Color color){
		PhongMaterial colored = new PhongMaterial(color);
		this.setMaterial(colored);
	}

	public void addInfo(String string, Integer value, int x){
		HBox hbox = new HBox();
		Label label = new Label(string);
		TextField text = new TextField(value.toString());
		text.setEditable(false);
	//	label.setId("table_key");
	//	text.setId("table_value");
		hbox.getChildren().addAll(label, text);
		this.info.add(hbox, 0, x);
	}
	
	public ClientFamilyMember getClientFamily(){
		return this.family;
	}
	
	public GridPane getInfo(){
		return info;
	}
}
