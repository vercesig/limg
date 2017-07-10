package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FamilyMemberContext {
	 
	public FamilyMemberContext(){};
	
	public static void showFamily(ArrayList <FamilyMemberGUI> familiar){ 
		
	       	Group family = new Group(); 
	       	familiar.forEach(member-> family.getChildren().add(member));
	        
	        PointLight light = new PointLight(Color.WHITE);
	        light.setTranslateX(0);
	        light.setTranslateY(100);
	        light.setTranslateZ(-150);

	        family.getChildren().add(light);
	        family.setTranslateX(130);
	        family.setTranslateY(130);
	        family.setTranslateZ(-150);
	       	
	       
			for(int i=0; i<familiar.size(); i++){
				VBox vbox = new VBox();
				Button button = new Button("Move this!");
				vbox.getChildren().add(familiar.get(i).getInfo());
				shift(vbox, 0.5, i*100);
				shift(button, 0.5, i*70);
				family.getChildren().addAll(vbox, button);
			}

	        Scene window = new Scene(family, 700, 300);
	        window.setFill(Color.GREY);
	        window.setCamera(new PerspectiveCamera());
	        
	        Stage stage = new Stage();
	        stage.setTitle("Family Member view");
	        stage.setScene(window);
	        stage.setResizable(false);
	        stage.show();
	
		}
		
		private static void shift(VBox vbox, double scale, int factor){
			vbox.setScaleX(scale);
			vbox.setScaleY(scale);

			vbox.setTranslateY(-80);
			vbox.setTranslateX(factor - 50);
			vbox.setTranslateZ(0);

		}
		
		private static void shift(Button button, double scale, int factor){
			button.setScaleX(scale);
			button.setScaleY(scale);
			button.setTranslateY(100);
			button.setTranslateX(factor+100);
			button.setTranslateZ(0);

		}
			
}