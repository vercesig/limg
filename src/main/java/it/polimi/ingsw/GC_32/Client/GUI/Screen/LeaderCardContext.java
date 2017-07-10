package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LeaderCardContext {
	
	public LeaderCardContext(){};
	
	public static void showLeaderCard(ArrayList <LeaderCardGUI> leaderList){ 
		
	       	Group family = new Group(); 
	       	leaderList.forEach(member-> family.getChildren().add(member));
	        
	       	AmbientLight light = new AmbientLight(Color.WHITE);
	        light.setTranslateX(230);
	        light.setTranslateY(130);
	        light.setTranslateZ(-180);

	        family.getChildren().add(light);
	        family.setTranslateX(140);
	        family.setTranslateY(130);
	        family.setTranslateZ(-150);
	       	
	       
			for(int i=0; i<leaderList.size(); i++){
				VBox vbox = new VBox();
				Label label = new Label(leaderList.get(i).getName());
				vbox.getChildren().add(leaderList.get(i));
				shift(vbox, 1, i*100);
				shift(label, 0.5, i);
				family.getChildren().addAll(vbox, label);
			}

	        Scene window = new Scene(family, 700, 300);
	        window.setFill(Color.GREY);
	        window.setCamera(new PerspectiveCamera());
	        
	        Stage stage = new Stage();
	        stage.setTitle("Leader Card view");
	        stage.setScene(window);
	        stage.setResizable(false);
	        stage.show();
	
		}
		
		private static void shift(VBox vbox, double scale, int factor){
			vbox.setScaleX(scale);
			vbox.setScaleY(scale);

			vbox.setTranslateY(-80);
			vbox.setTranslateX(factor);
			vbox.setTranslateZ(-40);

		}
		
		private static void shift(Label label, double scale, int factor){
			label.setScaleX(scale);
			label.setScaleY(scale);
			label.setTranslateY(100);
			label.setTranslateX(factor*100+30);
			label.setTranslateZ(-20);
		}
}
